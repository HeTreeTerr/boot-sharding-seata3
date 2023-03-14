package com.shardingseata3.demo.mapper;

import com.shardingseata3.demo.pojo.Goods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoodsMapperTest {

    @Autowired
    private GoodsMapper goodsMapper;

    @Test
    public void selectOneGoods() {
        Goods goods = goodsMapper.selectOneGoods(1L);
        System.out.println("goods=" + goods);
    }

    @Test
    public void updateGoodsStock() {
        int i = goodsMapper.updateGoodsStock(1L, 5);
        if(i > 0){
            System.out.println("修改成功");
        }else {
            System.out.println("修改失败");
        }
    }
}