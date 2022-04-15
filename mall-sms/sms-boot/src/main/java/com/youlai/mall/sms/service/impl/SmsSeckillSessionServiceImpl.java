package com.youlai.mall.sms.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youlai.mall.sms.mapper.SmsSeckillSessionMapper;
import com.youlai.mall.sms.pojo.domain.SmsSeckillSession;
import com.youlai.mall.sms.service.ISmsSeckillSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huawei
 * @desc 秒杀活动场次管理业务实现类
 * @email huawei_code@163.com
 * @date 2021/3/5
 */
@Service
@Slf4j
public class SmsSeckillSessionServiceImpl extends ServiceImpl<SmsSeckillSessionMapper, SmsSeckillSession> implements ISmsSeckillSessionService {
    @Override
    public List<SmsSeckillSession> selectByTime(DateTime startTime, DateTime endTime) {
        log.info("根据起始时间和结束时间查询秒杀活动列表, startTime={}, endTime={}", startTime, endTime);
        QueryWrapper<SmsSeckillSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1).between("start_time", startTime, endTime).orderByAsc("start_time");

        return this.list(queryWrapper);
    }
    @Override
    public List<SmsSeckillSession> selectCurrentSeckillSession() {
        DateTime now = DateUtil.date();
        log.info("获取正在进行的秒杀活动信息");
        QueryWrapper<SmsSeckillSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1).lt("start_time", now).gt("end_time", now).orderByAsc("start_time");
        return this.list(queryWrapper);
    }
}
