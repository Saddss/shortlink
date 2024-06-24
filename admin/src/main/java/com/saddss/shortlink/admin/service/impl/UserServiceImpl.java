package com.saddss.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saddss.shortlink.admin.common.biz.user.UserContext;
import com.saddss.shortlink.admin.common.constant.RedisCacheConstant;
import com.saddss.shortlink.admin.common.convention.exception.ClientException;
import com.saddss.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.saddss.shortlink.admin.dao.entity.UserDO;
import com.saddss.shortlink.admin.dao.mapper.UserMapper;
import com.saddss.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.saddss.shortlink.admin.dto.req.UserLoginReqDTO;
import com.saddss.shortlink.admin.dto.req.UserRegisterReqDto;
import com.saddss.shortlink.admin.dto.req.UserUpdateReqDto;
import com.saddss.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.saddss.shortlink.admin.dto.resp.UserRespDto;
import com.saddss.shortlink.admin.service.GroupService;
import com.saddss.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    private final RedissonClient redissonClient;

    private final StringRedisTemplate stringRedisTemplate;
    private final GroupService groupService;
    @Override
    public UserRespDto getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null){
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDto result = new UserRespDto();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUsername(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDto requestParam) {
        if (hasUsername(requestParam.getUsername())){
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        try {
            if (lock.tryLock()){
                try {
                    baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                } catch (DuplicateKeyException ex) {
                    throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
                groupService.saveGroup(requestParam.getUsername(), new ShortLinkGroupSaveReqDTO("默认分组"));
                return;
            }
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        } finally{
            lock.unlock();
        }
    }

    @Override
    public void update(UserUpdateReqDto requestParam) {
        String loginUserName = UserContext.getUsername();
        if (!loginUserName.equals(requestParam.getUsername())){
            throw new ClientException("不可修改其他用户信息");
        }
        LambdaQueryWrapper<UserDO> updateWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null){
            throw new ClientException(UserErrorCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        if (stringRedisTemplate.hasKey(RedisCacheConstant.USER_LOGIN_KEY + requestParam.getUsername())){
            throw new ClientException(UserErrorCodeEnum.USER_HAS_LOGGED);
        }
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(RedisCacheConstant.USER_LOGIN_KEY + requestParam.getUsername(), uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(RedisCacheConstant.USER_LOGIN_KEY + requestParam.getUsername(), 30L, TimeUnit.DAYS);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get(RedisCacheConstant.USER_LOGIN_KEY + username, token) != null;
    }

    @Override
    public void logout(String username, String token) {
        if (checkLogin(username, token)){
            stringRedisTemplate.delete(RedisCacheConstant.USER_LOGIN_KEY + username);
            return;
        }else{
            throw new ClientException(UserErrorCodeEnum.USER_UNLOGIN_OR_TOKEN_IS_NOT_VALID);
        }
    }
}
