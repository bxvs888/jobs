package com.baomidou.jobs.starter.mybatisplus.service;

import com.baomidou.jobs.starter.entity.JobsRegistry;
import com.baomidou.jobs.starter.mybatisplus.mapper.JobsRegistryMapper;
import com.baomidou.jobs.starter.service.IJobsRegistryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobsRegistryServiceImpl implements IJobsRegistryService<IPage> {
    @Resource
    private JobsRegistryMapper jobRegistryMapper;

    @Override
    public IPage page(HttpServletRequest request, JobsRegistry jobRegistry) {
        return jobRegistryMapper.selectPage(JobsPageHelper.getPage(request),
                Wrappers.<JobsRegistry>lambdaQuery().setEntity(jobRegistry));
    }

    @Override
    public int removeTimeOut(int timeout) {
        return jobRegistryMapper.deleteTimeOut(timeout);
    }

    @Override
    public List<String> listAddress(String app) {
        List<JobsRegistry> jobsRegistryList = jobRegistryMapper.selectList(Wrappers.<JobsRegistry>lambdaQuery()
                .eq(JobsRegistry::getApp, app));
        return CollectionUtils.isEmpty(jobsRegistryList) ? null : jobsRegistryList.stream()
                .map(j -> j.getIp() + ":" + j.getPort()).collect(Collectors.toList());
    }

    @Override
    public int update(String app, String ip, String port) {
        return jobRegistryMapper.update(new JobsRegistry().setUpdateTime(new Date()),
                Wrappers.<JobsRegistry>lambdaQuery().eq(JobsRegistry::getApp, app)
                        .eq(JobsRegistry::getIp, ip)
                        .eq(JobsRegistry::getPort, port));
    }

    @Override
    public int save(String app, String ip, String port) {
        return jobRegistryMapper.insert(new JobsRegistry().setApp(app)
                .setIp(ip).setPort(port).setUpdateTime(new Date()));
    }

    @Override
    public int remove(String app, String ip, String port) {
        return jobRegistryMapper.delete(Wrappers.<JobsRegistry>lambdaQuery()
                .eq(JobsRegistry::getApp, app)
                .eq(JobsRegistry::getIp, ip)
                .eq(JobsRegistry::getPort, port));
    }

    @Override
    public List<JobsRegistry> listTimeout(int timeout) {
        return jobRegistryMapper.selectTimeout(timeout);
    }
}
