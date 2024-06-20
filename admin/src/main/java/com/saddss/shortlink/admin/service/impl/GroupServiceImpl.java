package com.saddss.shortlink.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saddss.shortlink.admin.dao.entity.GroupDO;
import com.saddss.shortlink.admin.dao.mapper.GroupMapper;
import com.saddss.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.saddss.shortlink.admin.service.GroupService;
import com.saddss.shortlink.admin.util.RandomGenerator;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    @Override
    public void saveGroup(ShortLinkGroupSaveReqDTO requestParam) {
        String gid = RandomGenerator.generateRandom();
        do{
          gid = RandomGenerator.generateRandom();
        } while(!hasGid(gid));
        GroupDO groupDO = GroupDO.builder()
                .name(requestParam.getName())
                .gid(gid)
                .sortOrder(0)
                .build();
        baseMapper.insert(groupDO);
    }

    private boolean hasGid(String gid){
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, gid)
                //todo 从上下文获取用户信息
                .eq(GroupDO::getUsername, null);
        GroupDO groupDO = baseMapper.selectOne(queryWrapper);
        return groupDO == null;
    }
}
