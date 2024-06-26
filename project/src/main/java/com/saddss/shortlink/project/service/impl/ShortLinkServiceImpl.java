package com.saddss.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saddss.shortlink.project.common.constant.RedisKeyConstant;
import com.saddss.shortlink.project.common.convention.exception.ClientException;
import com.saddss.shortlink.project.common.convention.exception.ServiceException;
import com.saddss.shortlink.project.common.enums.VailDateTypeEnum;
import com.saddss.shortlink.project.dao.entity.ShortLinkDO;
import com.saddss.shortlink.project.dao.entity.ShortLinkGotoDO;
import com.saddss.shortlink.project.dao.mapper.ShortLinkGotoMapper;
import com.saddss.shortlink.project.dao.mapper.ShortLinkMapper;
import com.saddss.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.saddss.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.saddss.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import com.saddss.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.saddss.shortlink.project.service.ShortLinkService;
import com.saddss.shortlink.project.util.HashUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;
    private final ShortLinkGotoMapper shortLinkGotoMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String shortLinkSuffix = genSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(requestParam.getDomain())
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(requestParam.getDomain())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createdType(requestParam.getCreatedType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .totalPv(0)
                .totalUv(0)
                .totalUip(0)
                .delTime(0L)
                .fullShortUrl(fullShortUrl)
                .build();
        ShortLinkGotoDO shortLinkGotoDO = ShortLinkGotoDO.builder()
                .fullShortUrl(fullShortUrl)
                .gid(requestParam.getGid())
                .build();
        try {
            baseMapper.insert(shortLinkDO);
            shortLinkGotoMapper.insert(shortLinkGotoDO);
        } catch (DuplicateKeyException ex) {
            log.warn("短链接: {} 重复入库", fullShortUrl);
        }
        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
        return ShortLinkCreateRespDTO.builder()
                .gid(requestParam.getGid())
                .fullShortUrl("http" + fullShortUrl)
                .originUrl(requestParam.getOriginUrl())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getOriginGid())
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0);
        ShortLinkDO hasShortLinkDO = baseMapper.selectOne(queryWrapper);
        if (hasShortLinkDO == null){
            throw new ClientException("短链接记录不存在");
        }
        if (Objects.equals(requestParam.getGid(), requestParam.getOriginGid())){
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, requestParam.getGid())
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .set(Objects.equals(requestParam.getValidDateType(), VailDateTypeEnum.PERMANENT.getType()), ShortLinkDO::getValidDate, null);
            ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                    .originUrl(requestParam.getOriginUrl())
                    .describe(requestParam.getDescribe())
                    .validDateType(requestParam.getValidDateType())
                    .validDate(requestParam.getValidDate()).build();
            baseMapper.update(shortLinkDO, updateWrapper);
        }else{
            LambdaUpdateWrapper<ShortLinkDO> deleteWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, requestParam.getOriginGid())
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getEnableStatus, 0);
            ShortLinkDO delLink = ShortLinkDO.builder()
                    .delTime(System.currentTimeMillis())
                    .build();
            delLink.setDelFlag(1);
            baseMapper.update(delLink, deleteWrapper);
            ShortLinkDO newShortLink = ShortLinkDO.builder()
                    .domain(hasShortLinkDO.getDomain())
                    .originUrl(requestParam.getOriginUrl())
                    .gid(requestParam.getGid())
                    .createdType(hasShortLinkDO.getCreatedType())
                    .validDateType(requestParam.getValidDateType())
                    .validDate(requestParam.getValidDate())
                    .describe(requestParam.getDescribe())
                    .shortUri(hasShortLinkDO.getShortUri())
                    .enableStatus(hasShortLinkDO.getEnableStatus())
                    .totalPv(hasShortLinkDO.getTotalPv())
                    .totalUv(hasShortLinkDO.getTotalUv())
                    .totalUip(hasShortLinkDO.getTotalUip())
                    .fullShortUrl(hasShortLinkDO.getFullShortUrl())
                    .favicon(hasShortLinkDO.getFavicon())
                    .delTime(0L)
                    .build();
            baseMapper.insert(newShortLink);
        }
    }

    @Override
    public void jumpToOriginUrl(String shortUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String serverName = request.getServerName();
        String fullShortUrl = serverName + "/" + shortUrl;
        String originUrl = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstant.GOTO_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(originUrl)){
            response.sendRedirect(originUrl);
            return;
        }
        boolean contains = shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl);
        if (!contains){
            return;
        }
        String gotoIsNullShortLink = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(gotoIsNullShortLink)){
            return;
        }
        RLock lock = redissonClient.getLock(String.format(RedisKeyConstant.LOCK_GOTO_SHORT_LINK_KEY, fullShortUrl));
        lock.lock();
        try{
            //双重检查
            originUrl = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstant.GOTO_SHORT_LINK_KEY, fullShortUrl));
            if (StrUtil.isNotBlank(originUrl)){
                response.sendRedirect(originUrl);
                return;
            }
            LambdaQueryWrapper<ShortLinkGotoDO> shortLinkGotoQueryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                    .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
            ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(shortLinkGotoQueryWrapper);
            if (shortLinkGotoDO == null){
                stringRedisTemplate.opsForValue().set(String.format(RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl), "-", 30, TimeUnit.MINUTES);
                return;
            }
            LambdaQueryWrapper<ShortLinkDO> shortLinkQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, shortLinkGotoDO.getGid());
            ShortLinkDO shortLinkDO = baseMapper.selectOne(shortLinkQueryWrapper);
            if (shortLinkDO != null){
                stringRedisTemplate.opsForValue().set(String.format(RedisKeyConstant.GOTO_SHORT_LINK_KEY, fullShortUrl), shortLinkDO.getOriginUrl());
                response.sendRedirect(shortLinkDO.getOriginUrl());
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public IPage<ShortLinkPageRespDTO> getPage(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getDelFlag, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);
        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> {
            ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
            result.setDomain("http://" + each.getDomain());
            return result;
        });
    }

    @Override
    public List<ShortLinkGroupCountQueryRespDTO> getShortLinkCountInGroup(List<String> requestParam) {
        QueryWrapper<ShortLinkDO> queryWrapper = Wrappers.query(new ShortLinkDO())
                .select("gid, count(*) as shortLinkCount")
                .eq("enable_status", 0)
                .in("gid", requestParam)
                .groupBy("gid");
        List<Map<String, Object>> resultList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(resultList, ShortLinkGroupCountQueryRespDTO.class);
    }



    private String genSuffix(ShortLinkCreateReqDTO requestParam){
        int tryCount = 0;
        String shortLinkSuffix;
        while (true){
            if (tryCount > 10){
                throw new ServiceException("短链接频繁生成，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();
            originUrl += UUID.randomUUID().toString();
            shortLinkSuffix = HashUtil.hashToBase62(originUrl);
            if (!shortUriCreateCachePenetrationBloomFilter.contains(requestParam.getDomain() + "/" + shortLinkSuffix)){
                break;
            }
            tryCount++;
        }
        return shortLinkSuffix;
    }
}
