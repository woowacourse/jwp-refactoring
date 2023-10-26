package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    @JoinColumn(name = "menu_group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(final String name,
                 final BigDecimal price,
                 final MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    private Menu(final Long id,
                 final String name,
                 final BigDecimal price,
                 final MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public static Menu create(final String name,
                              final BigDecimal price,
                              final MenuGroup menuGroup,
                              final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu(name, price, menuGroup);
        menu.addMenuProducts(menuProducts);
        menu.validatePriceOverZero();
        menu.validateTotalPrice();
        return menu;
    }

    private void addMenuProducts(final List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            addMenuProduct(menuProduct);
        }
    }

    private void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.setMenu(this);
        this.menuProducts.add(menuProduct);
    }

    private void validatePriceOverZero() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTotalPrice() {
        final BigDecimal totalPrice = calculateAllProductPrice();
        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateAllProductPrice() {
        return menuProducts.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
