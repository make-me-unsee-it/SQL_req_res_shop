package com.step.hryshkin.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ORDER_GOODS")
public class OrderGood implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;

    @Column(name = "GOOD_ID", nullable = false)
    private Long goodId;

    public OrderGood(Long orderId, Long goodId) {
        this.orderId = orderId;
        this.goodId = goodId;
    }

    public OrderGood() {
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
