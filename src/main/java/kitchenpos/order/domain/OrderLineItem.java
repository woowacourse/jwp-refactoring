package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Price;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Embedded;

public class OrderLineItem {
    @Id
    private Long seq;
    private AggregateReference<Menu, Long> menuId;
    @Embedded.Empty
    private Price price;
    private String name;
    private long quantity;

    @PersistenceCreator
    public OrderLineItem(final Long menuId, final Price price, final String name,
                         final long quantity) {
        this.menuId = AggregateReference.to(menuId);
        this.price = price;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId.getId();
    }

    public long getQuantity() {
        return quantity;
    }
}
