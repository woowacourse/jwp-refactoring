package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long orderId;
    @Column(nullable = false)
    private Long menuId;
    @Column(nullable = false)
    private Long quantity;
    @Column(nullable = false)
    private String menuName;
    @Column(nullable = false)
    private BigDecimal menuPrice;

    public OrderLineItem() {
    }

    private OrderLineItem(Builder builder) {
        this.id = builder.id;
        this.orderId = builder.orderId;
        this.menuId = builder.menuId;
        this.quantity = builder.quantity;
        this.menuName = builder.menuName;
        this.menuPrice = builder.menuPrice;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long orderId;
        private Long menuId;
        private Long quantity;
        private String menuName;
        private BigDecimal menuPrice;

        private Builder() {
        }

        public Builder of(OrderLineItem orderLineItem) {
            this.id = orderLineItem.id;
            this.orderId = orderLineItem.orderId;
            this.menuId = orderLineItem.menuId;
            this.quantity = orderLineItem.quantity;
            this.menuName = orderLineItem.menuName;
            this.menuPrice = orderLineItem.menuPrice;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder order(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(this);
        }

        public Builder menuName(String menuName) {
            this.menuName =  menuName;
            return this;
        }

        public Builder menuPrice(BigDecimal menuPrice){
            this.menuPrice = menuPrice;
            return this;
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void updateOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
