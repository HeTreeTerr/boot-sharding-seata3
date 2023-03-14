package com.shardingseata3.demo.mapper;

import com.shardingseata3.demo.pojo.OrderSharding;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.List;

@SpringBootTest
public class OrderShardingMapperTest {

    @Autowired
    private OrderShardingMapper orderShardingMapper;

    @Test
    public void insertOneOrder() {
        for (int i = 0; i < 20; i++) {
            OrderSharding orderSharding = new OrderSharding();
            orderSharding.setGoodsName("芭比芭比");
            int amount = 1 + (int)(Math.random() * (10-1+1));
            orderSharding.setChangeAmount(amount);
            int res = orderShardingMapper.insertOneOrder(orderSharding);
            System.out.println("orderSharding=" + orderSharding);
        }
    }

    @Test
    public void selectAllOrder() {
        List<OrderSharding> orderShardings = orderShardingMapper.selectAllOrder();
        System.out.println("orderShardings=" + orderShardings);
    }

}