package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.vo.Price;

@Embeddable
public class OrderedItem {

    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "item_name")
    private String itemName;

    @Embedded
    private Price price;

    protected OrderedItem() {}

    public OrderedItem(final Long menuId, final String itemName, final Price price) {
        this.menuId = menuId;
        this.itemName = itemName;
        this.price = price;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getItemName() {
        return itemName;
    }

    public Price getPrice() {
        return price;
    }
}
