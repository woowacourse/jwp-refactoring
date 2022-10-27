package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

public class Menu {
    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    @Deprecated
    public Menu() {
    }

    public Menu(String name, long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(final String name) {
        this.name = name;
    }

    public long getPrice() {
        return price.getValue();
    }

    @Deprecated
    public void setPrice(final long price) {
        this.price = new Price(price);
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    @Deprecated
    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Deprecated
    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(name, menu.name) && Objects.equals(price, menu.price)
                && Objects.equals(menuGroupId, menu.menuGroupId) && Objects.equals(menuProducts,
                menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProducts);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
