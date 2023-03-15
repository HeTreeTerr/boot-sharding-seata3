package com.shardingseata3.demo.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Collection;

@Component
public class DatabasePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        logger.info("------------------select database name");
        Long curValue = shardingValue.getValue();
        BigInteger shardingValueB = new BigInteger(new Integer(curValue.toString().hashCode()).toString());
        BigInteger resB = (shardingValueB.mod(new BigInteger("2"))).add(new BigInteger("1"));
        String curBase = "saleorder0"+resB;
        if(availableTargetNames.contains(curBase)){
            return curBase;
        }
        //couse_1, course_2
        throw new UnsupportedOperationException("route "+ curBase +" is not supported ,please check your config");
    }
}