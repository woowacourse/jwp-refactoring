package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.common.Name;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.exception.InvalidMenuPriceException;
import kitchenpos.domain.exception.InvalidMenuProductException;

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

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public static Menu of(
            final String name,
            final BigDecimal price,
            final List<MenuProduct> menuProducts,
            final MenuGroup menuGroup
    ) {
        final Price menuPrice = new Price(price);

        validateMenuProduct(menuProducts);
        validateMenuProductPrice(menuPrice, menuProducts);

        return new Menu(name, menuPrice, menuGroup, menuProducts);
    }

    private static void validateMenuProductPrice(final Price menuPrice, final List<MenuProduct> menuProducts) {
        final Price totalMenuProductPrice = calculateTotalMenuProductPrice(menuProducts);

        if (menuPrice.compareTo(totalMenuProductPrice) > 0) {
            throw new InvalidMenuPriceException();
        }
    }

    private static Price calculateTotalMenuProductPrice(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                           .map(menuProduct -> menuProduct.productPrice().times(menuProduct.getQuantity()))
                           .reduce(Price.ZERO, Price::plus);
    }

    private static void validateMenuProduct(final List<MenuProduct> menuProducts) {
        if (menuProducts == null || menuProducts.isEmpty()) {
            throw new InvalidMenuProductException();
        }
    }

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
        this.menuProducts = menuProducts;

        initMenuForMenuProduct(menuProducts);
    }

    private void initMenuForMenuProduct(final List<MenuProduct> menuProducts) {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.initMenu(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
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
