package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class OrderMenu {

    @Column(nullable = false)
    private Long menuId;

    private String menuName;

    @Embedded
    private Price price;

    protected OrderMenu() {
    }

    private OrderMenu(final Long menuId, final String menuName, final Price price) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
    }

    public static OrderMenu from(Menu menu) {
        return new OrderMenu(menu.getId(), menu.getName(), menu.getPrice());
    }

    public Long getMenuId() {
        return menuId;
    }
}
