package com.shardingseata3.demo.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Collection;

@Component
public class OrderTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        logger.info("--------==================select table");
        String logicTableName = shardingValue.getLogicTableName();
        Long curValue = shardingValue.getValue();
        BigInteger shardingValueB = BigInteger.valueOf(curValue);
        BigInteger resB = (shardingValueB.mod(new BigInteger("2"))).add(new BigInteger("1"));
        String curTable = logicTableName + "_" + resB;
        if(availableTargetNames.contains(curTable)){
            return curTable;
        }
        throw new UnsupportedOperationException("route "+ curTable +" is not supported ,please check your config");
    }

}