package com.shardingseata3.demo.pojo;

public class OrderSharding {

    private Long orderId;
    public Long getOrderId() {
        return this.orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    private String goodsName;
    public String getGoodsName() {
        return this.goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    private Integer changeAmount;
    public Integer getChangeAmount() {
        return changeAmount;
    }
    public void setChangeAmount(Integer changeAmount) {
        this.changeAmount = changeAmount;
    }
}
