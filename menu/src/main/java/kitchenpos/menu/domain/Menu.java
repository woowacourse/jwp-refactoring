package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProductGroup menuProducts;

    protected Menu() {
    }

    public static Menu createSingleId(Long menuId) {
        final Menu menu = new Menu();
        menu.id = menuId;
        return menu;
    }

    public void updateInfo(String name, BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (price.compareTo(menuProducts.totalSum(price)) > 0) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.price = price;
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

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProductsList() {
        return menuProducts.value();
    }

    public MenuProductGroup getMenuProducts() {
        return menuProducts;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private String name;
        private BigDecimal price;
        private MenuGroup menuGroup;
        private MenuProductGroup menuProducts;

        private Builder() {
        }

        public Builder menu(Menu menu) {
            this.name = menu.name;
            this.price = menu.price;
            this.menuGroup = menu.menuGroup;
            this.menuProducts = menu.menuProducts;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder menuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public Builder menuProducts(MenuProductGroup menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Builder menuProducts(List<MenuProduct> menuProducts) {
            return menuProducts(MenuProductGroup.of(menuProducts));
        }


        public Menu build() {
            final Menu menu = new Menu();
            menu.id = this.id;
            menu.name = this.name;
            menu.price = this.price;
            menu.menuGroup = this.menuGroup;
            menu.menuProducts = this.menuProducts;
            if(menuProducts != null) {
                menuProducts.addMenu(menu);
            }
            return menu;
        }
    }
}
