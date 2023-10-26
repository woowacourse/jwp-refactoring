package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuPrice;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private MenuName name;


    @Embedded
    private MenuPrice price;

    @NotNull
    private Long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(String name, BigDecimal price, Long quantity) {
        this.name = MenuName.from(name);
        this.price = MenuPrice.from(price);
        this.quantity = quantity;
    }

    public static OrderLineItem create(String name, BigDecimal price, Long quantity) {
        return new OrderLineItem(name, price, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public MenuName getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }

}
