package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.domain.common.Price;
import kitchenpos.exception.badrequest.ExpensiveMenuPriceException;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Embedded
    private Price price;
    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, new Price(price), menuGroupId, menuProducts);
    }

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validatePrice(menuProducts, price);
        mapMenuProduct(menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    private void mapMenuProduct(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.designateMenu(this);
        }
    }

    private void validatePrice(final List<MenuProduct> menuProducts, final Price price) {
        Price sum = menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(new Price(BigDecimal.ZERO), Price::add);
        if (price.isExpensiveThan(sum)) {
            throw new ExpensiveMenuPriceException();
        }
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
