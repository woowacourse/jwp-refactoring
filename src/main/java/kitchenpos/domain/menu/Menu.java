package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private MenuPrice menuPrice;

    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(final Long id, final String name, final BigDecimal menuPrice, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.menuPrice = new MenuPrice(menuPrice);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts, this);
    }

    public Menu(final String name, final BigDecimal menuPrice, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, menuPrice, menuGroupId, menuProducts);
    }

    public static Menu create(final String name, final BigDecimal menuPrice, final Long menuGroupId,
                              final List<MenuProduct> menuProducts, final MenuValidator menuValidator) {
        menuValidator.validate(menuGroupId, menuProducts, menuPrice);
        return new Menu(null, name, menuPrice, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getMenuPrice() {
        return menuPrice;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
