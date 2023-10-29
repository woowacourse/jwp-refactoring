package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.vo.OrderMenuName;
import kitchenpos.domain.vo.OrderMenuPrice;
import kitchenpos.domain.vo.OrderQuantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private OrderMenuName orderMenuName;

    @Embedded
    private OrderMenuPrice orderMenuPrice;

    @Embedded
    private OrderQuantity orderQuantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(
            Long menuId,
            String orderMenuName,
            BigDecimal orderMenuPrice,
            long orderQuantity
    ) {
        this.menuId = menuId;
        this.orderMenuName = new OrderMenuName(orderMenuName);
        this.orderMenuPrice = new OrderMenuPrice(orderMenuPrice);
        this.orderQuantity = new OrderQuantity(orderQuantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getOrderMenuName() {
        return orderMenuName.getName();
    }

    public BigDecimal getOrderMenuPrice() {
        return orderMenuPrice.getPrice();
    }

    public long getOrderQuantity() {
        return orderQuantity.getQuantity();
    }
}
