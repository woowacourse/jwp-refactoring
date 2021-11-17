package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Menu {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    private Menu(MenuBuilder menuBuilder) {
        this.id = menuBuilder.id;
        this.name = menuBuilder.name;
        this.price = menuBuilder.price;
        this.menuGroupId = menuBuilder.menuGroupId;
        this.menuProducts = menuBuilder.menuProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static class MenuBuilder {

        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public MenuBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public MenuBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public MenuBuilder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuBuilder setMenuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuBuilder setMenuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}
