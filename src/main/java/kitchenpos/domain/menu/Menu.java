package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Price;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;
    private final List<MenuProduct> menuProducts;

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this(id, name, price, menuGroupId, new ArrayList<>());
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getAmount();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts);
    }
}
