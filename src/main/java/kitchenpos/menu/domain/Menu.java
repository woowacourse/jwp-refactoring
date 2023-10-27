package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.common.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Menu() {
    }

    Menu(
            final Long menuGroupId,
            final List<MenuProduct> menuProducts,
            final String name,
            final BigDecimal price
    ) {
        this.menuGroupId = menuGroupId;
        this.name = name;
        this.price = new Price(price);

        for (final MenuProduct menuProduct : menuProducts) {
            addMenuProduct(menuProduct);
        }
    }

    public static Menu of(
            final Long menuGroupId,
            final List<MenuProduct> menuProducts,
            final String name,
            final BigDecimal price,
            final MenuValidator menuValidator
    ) {
        menuValidator.validate(menuProducts, price);
        return new Menu(menuGroupId, menuProducts, name, price);
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.setMenu(this);
        this.menuProducts.add(menuProduct);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
