package com.shardingseata3.demo.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shardingseata3.demo.mapper.GoodsMapper;
import com.shardingseata3.demo.mapper.OrderShardingMapper;
import com.shardingseata3.demo.pojo.Goods;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.shardingseata3.demo.pojo.OrderSharding;
//import io.seata.spring.annotation.GlobalTransactional;
import com.shardingseata3.demo.mapper.OrderShardingMapper;
import com.shardingseata3.demo.pojo.OrderSharding;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

@Controller
@RequestMapping("/home")
//@MapperScan("com.shardingseata3.demo.mapper.sharding")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";

    @Resource
    private OrderShardingMapper orderShardingMapper;

    @Resource
    private GoodsMapper goodsMapper;

    //订单列表，列出分库分表的数据
    @GetMapping("/orderlist")
    public String list(Model model, @RequestParam(value="currentPage",required = false,defaultValue = "1") Integer currentPage){

        PageHelper.startPage(currentPage, 5);
        List<OrderSharding> orderList = orderShardingMapper.selectAllOrder();
        model.addAttribute("orderlist",orderList);
        PageInfo<OrderSharding> pageInfo = new PageInfo<>(orderList);
        model.addAttribute("pageInfo", pageInfo);
        logger.info("------------------------size:{}",orderList.size());
        return "order/list";
    }

    //添加一个订单,访问一个数据库和分库分表的两个数据库
    @GetMapping("/addorder")
    @ResponseBody
    @GlobalTransactional(timeoutMills = 300000,rollbackFor = Exception.class)
    public String addOrder(@RequestParam(value="goodsId",required = true,defaultValue = "0") Long goodsId,
                           @RequestParam(value="goodsNum",required = true,defaultValue = "0") Integer goodsNum,
            @RequestParam(value="isfail",required = true,defaultValue = "0") int isFail
                           )  throws SQLException, IOException {

        TransactionTypeHolder.set(TransactionType.BASE);
        Goods goodsQuery = goodsMapper.selectOneGoods(goodsId);
        String goodsName = goodsQuery.getGoodsName() + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        OrderSharding orderOne = new OrderSharding();
        //名称
        orderOne.setGoodsName(goodsName);
        //数量
        orderOne.setChangeAmount(goodsNum);

        TransactionTypeHolder.set(TransactionType.BASE);
        int resIns = orderShardingMapper.insertOneOrder(orderOne);
        logger.info("orderId:{}",orderOne.getOrderId());


        TransactionTypeHolder.set(TransactionType.BASE);
        int res = goodsMapper.updateGoodsStock(goodsId, -goodsNum);
        logger.info("res:{}",res);

        if (isFail == 1) {
            int divide = 0;
            int resul = 100 / divide;
        }
        if (res>0) {
            return SUCCESS;
        } else {
            return FAIL;
        }
    }


    //添加一个订单,访问一个数据库和分库分表的两个数据库,rest方式
    @GetMapping("/addorderrest")
    @ResponseBody
    @GlobalTransactional(timeoutMills = 300000,rollbackFor = Exception.class)
    public String addOrderrest(@RequestParam(value="goodsId",required = true,defaultValue = "0") Long goodsId,
                               @RequestParam(value="goodsNum",required = true,defaultValue = "0") Integer goodsNum,
                           @RequestParam(value="isfail",required = true,defaultValue = "0") int isFail
    )  throws SQLException, IOException {

        TransactionTypeHolder.set(TransactionType.BASE);
        Goods goodsQuery = goodsMapper.selectOneGoods(goodsId);
        String goodsName = goodsQuery.getGoodsName() + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        OrderSharding orderOne = new OrderSharding();
        orderOne.setGoodsName(goodsName);
        orderOne.setChangeAmount(goodsNum);

        TransactionTypeHolder.set(TransactionType.BASE);
        int resIns = orderShardingMapper.insertOneOrder(orderOne);
        logger.info("orderId:{}",orderOne.getOrderId());

        RestTemplate restTemplate = new RestTemplate();

        String xid = RootContext.getXID();
        logger.info("xid before send:{}",xid);

        HttpHeaders headers = new HttpHeaders();
        headers.add(RootContext.KEY_XID, xid);

        String goodsUPNum = new Integer(-goodsNum).toString();
        String urlUpStock = "http://127.0.0.1:8080/goods/goodsstock/"+goodsId+"/"+goodsUPNum+"/";
        String resultUp = restTemplate.postForObject(urlUpStock,new HttpEntity<String>(headers),String.class);
        if (!SUCCESS.equals(resultUp)) {
            throw new RuntimeException();
        }
        if (isFail == 1) {
            int divide = 0;
            int resul = 100 / divide;
        }
        return SUCCESS;
    }

}

