package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class OrderLineItem {

    @Id
    private Long seq;
    private String name;
    @Embedded.Empty
    private Price price;
    private long quantity;

    private OrderLineItem() {
    }

    public OrderLineItem(String name, Price price, long quantity) {
        this(null, name, price, quantity);
    }

    private OrderLineItem(Long seq, String name, Price price, long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Menu menu, long quantity) {
        return new OrderLineItem(menu.getName(), menu.getPrice(), quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
