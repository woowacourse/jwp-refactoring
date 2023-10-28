package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menu.domain.vo.MenuPrice;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private MenuPrice price;
    
    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    protected Menu(final Long id, final String name, final MenuPrice price, final Long menuGroupId,
                   final MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        addMenuProducts(menuProducts);
    }

    public static Menu of(final String name, final MenuPrice price, final Long menuGroupId,
                          final MenuProducts menuProducts, final MenuValidator validator) {
        validator.validate(menuGroupId, price, menuProducts);
        return new Menu(
                null,
                name,
                price,
                menuGroupId,
                menuProducts
        );
    }

    private void addMenuProducts(final MenuProducts menuProducts) {
        if (menuProducts.isEmpty()) {
            return;
        }
        for (final MenuProduct menuProduct : menuProducts.getValues()) {
            menuProduct.updateMenu(this);
        }
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
        return menuProducts.getValues();
    }
}
