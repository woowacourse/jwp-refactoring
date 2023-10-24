package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.common.Name;
import kitchenpos.domain.common.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(
            final String name,
            final Price price,
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts
    ) {
        this.name = new Name(name);
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.of(this, menuProducts);
    }

    public static Menu of(
            final String name,
            final BigDecimal price,
            final List<MenuProduct> menuProducts,
            final MenuGroup menuGroup
    ) {
        final Price menuPrice = new Price(price);

        return new Menu(name, menuPrice, menuGroup, menuProducts);
    }

    public Price price() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getValues();
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final Menu targetMenu = (Menu) target;
        return Objects.equals(getId(), targetMenu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
