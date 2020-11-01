package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {

    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    private Menu() {
    }

    public Menu(Long id, String name, Price price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, Price price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public boolean isPriceBiggerThen(Price price) {
        return this.price.isBiggerThen(price.getValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
