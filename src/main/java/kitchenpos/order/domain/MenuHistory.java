package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Price;

import javax.persistence.*;

@Entity
public class MenuHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @Column(name = "price", nullable = false)
    private Price price;

    protected MenuHistory() {
    }

    private MenuHistory(final Long menuId, final String name, final Price price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static MenuHistory copyOf(final Menu menu) {
        return new MenuHistory(menu.getId(), menu.getName(), menu.getPrice());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
