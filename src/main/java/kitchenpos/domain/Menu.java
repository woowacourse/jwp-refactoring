package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.exception.MenuException.MenuOverPriceException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(final String name, final BigDecimal price, final MenuGroup menuGroup,
        final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final String name, final BigDecimal price, final MenuGroup menuGroup,
        final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu(name, price, menuGroup, menuProducts);
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(menu);
        }

        if (menu.checkPriceCondition()) {
            throw new MenuOverPriceException();
        }
        return menu;
    }

    private boolean checkPriceCondition() {
        final long sum = menuProducts.stream()
            .map(MenuProduct::calculateTotalPrice)
            .mapToLong(BigDecimal::longValue)
            .sum();
        final BigDecimal totalPrice = BigDecimal.valueOf(sum);

        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0 ||
            totalPrice.compareTo(price) < 0;
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public BigDecimal getTotalPrice() {
        final long sum = menuProducts.stream()
            .map(MenuProduct::calculateTotalPrice)
            .mapToLong(BigDecimal::longValue)
            .sum();
        return BigDecimal.valueOf(sum);
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }
}
