package com.ml.elasticjobdemo.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("------Thread ID: {}, 任务总片数: {}, 当前分片项: {}, 分片参数：{}", Thread.currentThread().getId(),
                shardingContext.getShardingTotalCount(), shardingContext.getShardingItem(), shardingContext.getShardingParameter());
    }
}
