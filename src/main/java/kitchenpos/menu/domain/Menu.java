package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private MenuGroup menuGroup;

    @OneToMany(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "menu_id", updatable = false, nullable = false)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(
            final String name,
            final Price price,
            final MenuGroup menuGroup
    ) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    private void addMenuProduct(final MenuProduct menuProduct) {
        if (Objects.isNull(menuProducts)) {
            menuProducts = new ArrayList<>();
        }
        menuProducts.add(menuProduct);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public BigDecimal getPriceValue() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuFactory {

        private Menu menu;

        public MenuFactory(final String name, final Price price, final MenuGroup menuGroup) {
            this.menu = new Menu(name, price, menuGroup);
        }

        public MenuFactory addProduct(final Product product, final long quantity) {
            menu.addMenuProduct(new MenuProduct(quantity, product));
            return this;
        }

        public Menu create() {
            if (Objects.isNull(menu)) {
                throw new IllegalStateException("Menu already created from this factory " + this);
            }
            validatePrice();

            final Menu result = menu;
            this.menu = null;

            return result;
        }

        private void validatePrice() {
            Price sum = new Price(BigDecimal.ZERO);
            for (final MenuProduct menuProduct : menu.menuProducts) {
                final Price productPrice = menuProduct.getProduct().getPrice();
                final Price menuProductPrice = productPrice.multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
                sum = sum.add(menuProductPrice);
            }
            if (menu.price.isLargerThan(sum)) {
                throw new IllegalArgumentException();
            }
        }
    }
}
