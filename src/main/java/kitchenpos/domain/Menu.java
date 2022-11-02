package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.MenuProductAmountException;
import kitchenpos.exception.MenuProductRemoveFailException;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(final Long id, final String name, final Price price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validatePrice(menuProducts, price);
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final String name, final Price price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    private static void validatePrice(final List<MenuProduct> menuProducts, final Price menuPrice) {
        if (menuPriceIsExpansiveThanMenuProducts(menuProducts, menuPrice)) {
            throw new InvalidMenuPriceException();
        }
    }

    private static boolean menuPriceIsExpansiveThanMenuProducts(final List<MenuProduct> menuProducts,
                                                                final Price menuPrice) {
        final Price allAmount  = sumAllAmount(menuProducts);
        return menuPrice.isExpansiveThan(allAmount) ;
    }

    private static Price sumAllAmount(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(Price::add)
                .orElseThrow(MenuProductAmountException::new);
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public void removeMenuProduct(final MenuProduct menuProduct) {
        final boolean removeSuccess = menuProducts.remove(menuProduct);
        if (!removeSuccess) {
            throw new MenuProductRemoveFailException();
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
        return Collections.unmodifiableList(menuProducts);
    }
}
