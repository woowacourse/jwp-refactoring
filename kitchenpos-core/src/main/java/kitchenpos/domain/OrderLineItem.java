package kitchenpos.domain;

import kitchenpos.domain.vo.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long menuId;

    private String menuName;

    @Embedded
    private Price menuPrice;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final String menuName, final Price menuPrice, final long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItem of(final Menu menu, final int quantity) {
        return new OrderLineItem(
                menu.getId(),
                menu.getName(),
                new Price(menu.getPrice()),
                quantity
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
