package com.shardingseata3.demo.mapper;

import com.shardingseata3.demo.pojo.Goods;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoodsMapperTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GoodsMapper goodsMapper;

    @Test
    public void selectOneGoods() {
        Goods goods = goodsMapper.selectOneGoods(1L);
        logger.info("=============>goods={}",goods);
    }

    @Test
    public void updateGoodsStock() {
        int i = goodsMapper.updateGoodsStock(1L, 5);
        if(i > 0){
            logger.info("修改成功");
        }else {
            logger.info("修改失败");
        }
    }
}