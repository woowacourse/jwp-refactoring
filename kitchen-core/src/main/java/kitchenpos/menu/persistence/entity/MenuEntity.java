package kitchenpos.menu.persistence.entity;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;

import java.math.BigDecimal;
import java.util.List;

public class MenuEntity {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    public MenuEntity(final Long id, final String name, final BigDecimal price,
                      final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public MenuEntity() {
    }

    public static MenuEntity from(final Menu menu) {
        return new MenuEntity(menu.getId(), menu.getName(), menu.getPrice().getValue(),
                menu.getMenuGroupId());
    }

    public Menu toMenu() {
        return new Menu(id, name, new Price(price), menuGroupId);
    }

    public Menu toMenu(final List<MenuProduct> menuProducts) {
        final Price validatedPrice = new Price(price);
        return new Menu(id, name, validatedPrice, menuGroupId, MenuProducts.of(validatedPrice, menuProducts));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

}
