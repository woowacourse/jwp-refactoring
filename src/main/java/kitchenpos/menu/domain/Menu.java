package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.menu.domain.vo.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Embedded
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup
    ) {
        this(name, new Price(price), menuGroup);
    }

    public Menu(
            final String name,
            final Price price,
            final MenuGroup menuGroup
    ) {
        this(null, name, price, menuGroup);
    }

    public Menu(
            final Long id,
            final String name,
            final Price price,
            final MenuGroup menuGroup
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
        this.menuProducts.addAll(menuProducts);
        validateMenuPrice();
    }

    public void validateMenuPrice() {
        if (this.price.compareTo(this.menuProducts.totalPrice()) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long id() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Price price() {
        return price;
    }

    public void setPrice(final Price price) {
        this.price = price;
    }

    public MenuGroup menuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public MenuProducts menuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
    }
}
