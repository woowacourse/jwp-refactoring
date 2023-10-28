package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.menugroup.MenuGroup;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup, null);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this(id, name, price, menuGroup, null);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup,
            List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        validatePrice(price);
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
