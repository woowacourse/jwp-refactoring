package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.common.vo.Price;

@Embeddable
public class MenuDetails {

    @Column(nullable = false)
    private long menuId;
    @Column(nullable = false)
    private String name;
    @Embedded
    private Price price;

    protected MenuDetails() {
    }

    public MenuDetails(final long menuId) {
        this.menuId = menuId;
    }

    public long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public void updateName(final String name) {
        this.name = name;
    }

    public void updatePrice(final Price price) {
        this.price = price;
    }
}
