package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    private Menu(MenuBuilder menuBuilder) {
        this.id = menuBuilder.id;
        this.name = menuBuilder.name;
        this.price = Price.create(menuBuilder.price);
        this.menuGroup = menuBuilder.menuGroup;
        this.menuProducts = MenuProducts.create(menuBuilder.menuProducts, price);
    }

    public void changeMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = MenuProducts.create(menuProducts, this.price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public static class MenuBuilder {

        private Long id;
        private String name;
        private int price;
        private MenuGroup menuGroup;
        private List<MenuProduct> menuProducts;

        public MenuBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public MenuBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public MenuBuilder setPrice(int price) {
            this.price = price;
            return this;
        }

        public MenuBuilder setMenuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
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
