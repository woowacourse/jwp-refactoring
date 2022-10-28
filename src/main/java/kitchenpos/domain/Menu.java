package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.MenuProductAmountException;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validatePrice(menuProducts, price);
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private static void validatePrice(final List<MenuProduct> menuProducts, final BigDecimal price) {
        if (menuPriceIsNullOrNegative(price)  || menuPriceIsExpansiveThanMenuProducts(menuProducts, price)) {
            throw new InvalidMenuPriceException();
        }
    }

    private static boolean menuPriceIsNullOrNegative(final BigDecimal price) {
        return price == null || price.compareTo(BigDecimal.ZERO) < 0;
    }
    private static boolean menuPriceIsExpansiveThanMenuProducts(final List<MenuProduct> menuProducts,
                                                                final BigDecimal price) {
        final BigDecimal amountSum = sumAllAmount(menuProducts);
        return price.compareTo(amountSum) > 0;
    }

    private static BigDecimal sumAllAmount(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal::add)
                .orElseThrow(MenuProductAmountException::new);
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
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
        return Collections.unmodifiableList(menuProducts);
    }
}
