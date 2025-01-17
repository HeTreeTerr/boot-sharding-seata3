package com.shardingseata3.demo.controller;

import com.shardingseata3.demo.mapper.GoodsMapper;
import com.shardingseata3.demo.pojo.Goods;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";

    @Resource
    private GoodsMapper goodsMapper;

    //更新商品库存 参数:商品id
    @RequestMapping("/goodsstock/{goodsId}/{count}")
    @ResponseBody
    public String goodsStock(@PathVariable Long goodsId,
                            @PathVariable int count) {

        TransactionTypeHolder.set(TransactionType.BASE);
         int res = goodsMapper.updateGoodsStock(goodsId,count);
         logger.info("res:{}",res);

         if (res>0) {
             return SUCCESS;
         } else {
             return FAIL;
         }
    }

    //商品详情 参数:商品id
    @GetMapping("/goodsinfo")
    @ResponseBody
    public Goods goodsInfo(@RequestParam(value="goodsid",required = true,defaultValue = "0") Long goodsId) {
        Goods goods = goodsMapper.selectOneGoods(goodsId);
        return goods;
    }

}
