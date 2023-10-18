package kitchenpos.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.MenuExceptionType.NEGATIVE_PRICE_EXCEPTION;
import static kitchenpos.exception.MenuExceptionType.NULL_PRICE_EXCEPTION;
import static kitchenpos.exception.MenuExceptionType.PRICE_IS_UNDER_TOTAL_PRODUCT_AMOUNT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.exception.MenuException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new MenuException(NULL_PRICE_EXCEPTION);
        }
        if (price.compareTo(BigDecimal.ZERO) < 0)
            throw new MenuException(NEGATIVE_PRICE_EXCEPTION);
    }

    public void add(MenuProduct menuProduct) {
        validatePrice(menuProduct);
        menuProducts.add(menuProduct);
    }

    private void validatePrice(MenuProduct menuProduct) {
        List<MenuProduct> tmp = new ArrayList<>(menuProducts);
        tmp.add(menuProduct);
        BigDecimal sumOfAmount = tmp.stream()
                .map(MenuProduct::amount)
                .reduce(BigDecimal::add)
                .get();
        if (price.compareTo(sumOfAmount) > 0) {
            throw new MenuException(PRICE_IS_UNDER_TOTAL_PRODUCT_AMOUNT);
        }
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    public MenuGroup menuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts;
    }
}
