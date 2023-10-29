package kitchenpos.menu.domain;

import kitchenpos.menu.domain.validator.MenuValidator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(final String name,
                 final BigDecimal price,
                 final Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    private Menu(final Long id,
                 final String name,
                 final BigDecimal price,
                 final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu create(final String name,
                              final BigDecimal price,
                              final Long menuGroupId,
                              final List<MenuProduct> menuProducts,
                              final MenuValidator menuValidator) {
        final Menu menu = new Menu(name, price, menuGroupId);
        menu.addMenuProducts(menuProducts);
        menuValidator.validateCreate(price, menuGroupId, menuProducts);
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
