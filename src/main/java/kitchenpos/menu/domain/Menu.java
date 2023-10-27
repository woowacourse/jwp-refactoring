package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menuproduct.domain.MenuProduct;
import kitchenpos.menuproduct.domain.MenuProducts;
import kitchenpos.product.domain.Product;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    @ManyToOne(fetch = LAZY)
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(
            final Long id,
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup,
            final MenuProducts menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public static Menu of(
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup,
            final Map<Product, Integer> productWithQuantity
    ) {
        validatePrice(price);
        final Menu menu = new Menu(name, price, menuGroup);
        menu.menuProducts = createMenuProducts(menu, price, productWithQuantity);
        return menu;
    }

    private static MenuProducts createMenuProducts(final Menu menu, final BigDecimal price, final Map<Product, Integer> productWithQuantity) {
        final MenuProducts menuProducts = new MenuProducts(productWithQuantity.keySet().stream()
                .map(product -> new MenuProduct(null, menu, product, productWithQuantity.get(product)))
                .collect(Collectors.toList()));
        menuProducts.validateTotalPrice(price);
        return menuProducts;
    }

    private static void validatePrice(final BigDecimal price) {
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
