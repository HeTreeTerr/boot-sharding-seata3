# boot-sharding-seata3
## 1.简介
使用开源框架seate，解决sharding-jdbc分库分表下，多数据库见分布式事务问题。

## 2.环境准备
### 2.1 seata1.3安装
安装seata-1.3.0  
注册中心类型》》registry.type=file  
配置中心类型》》registry.config=file  
持久化类型》》》store.mode=db

### 2.2 mysql数据库
业务数据库和表关系如下所示，sql脚本见项目resources\sql下。  
db1:store  
|_ goods  
|_ undo_log  
  
db2:saleorder01    
|_ t_order_1  
|_ t_order_2  
|_ undo_log  

db3:saleorder01  
|_ t_order_1  
|_ t_order_2  
|_ undo_log  

### 2.3 sharding-jdbc分库分表策略
默认数据源：store》》goods(商品表)  
订单数据源：saleorder01,saleorder02》》t_order(订单表)  

订单表分库策略
1. t_order.orderId字段值使用雪花算法生成
2. 将orderId作为字符串哈希后取余2，来分库
3. 将orderId取余2，来分表

## 2.4 seata保证分布式事务
案例使用seata AT模式，演示了单体项目和rest远程调用下，下单
业务的分布式事务保证

## 3.效果演示
### 3.1 打开订单列表页面  
http://localhost:8080/home/orderlist/?currentPage=1  

### 3.2 下单（单体项目）  
正常下单（事务提交测试）  
http://localhost:8080/home/addorder?goodsId=1&goodsNum=1&isfail=0
异常下单（事务回滚测试）  
http://localhost:8080/home/addorder?goodsId=1&goodsNum=1&isfail=1

### 3.3 下单（rest远程调用）  
正常下单（事务提交测试）  
http://localhost:8080/home/addorderrest?goodsId=1&goodsNum=2&isfail=0
异常下单（事务回滚测试）  
http://localhost:8080/home/addorderrest?goodsId=1&goodsNum=2&isfail=1