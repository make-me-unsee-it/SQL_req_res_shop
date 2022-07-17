package com.step.hryshkin.model;

import java.util.Objects;

public class OrderGood {
    private Long id;
    private Long orderId;
    private Long goodId;

    public OrderGood(Long id, Long orderId, Long goodId) {
        this.id = id;
        this.orderId = orderId;
        this.goodId = goodId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderGood)) return false;
        OrderGood orderGood = (OrderGood) o;
        return Objects.equals(id, orderGood.id) && Objects.equals(orderId, orderGood.orderId) && Objects.equals(goodId, orderGood.goodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, goodId);
    }

    @Override
    public String toString() {
        return "OrderGood{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", goodId=" + goodId +
                '}';
    }
}
