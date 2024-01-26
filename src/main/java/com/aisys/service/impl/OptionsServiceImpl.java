package com.aisys.service.impl;

import com.aisys.entity.Options;
import com.aisys.mapper.OptionsMapper;
import com.aisys.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jack
 * @date 2017/9/7
 */
@Service
public class OptionsServiceImpl implements OptionsService {


    @Autowired
    private OptionsMapper optionsMapper;

    @Override
    @Cacheable(value = "default", key = "'options'")
    public Options getOptions() {
        return optionsMapper.getOptions();
    }

    @Override
    @CacheEvict(value = "default", key = "'options'")
    public void insertOptions(Options options) {
        optionsMapper.insert(options);
    }

    @Override
    @CacheEvict(value = "default", key = "'options'")
    public void updateOptions(Options options) {
        optionsMapper.update(options);
    }
}
