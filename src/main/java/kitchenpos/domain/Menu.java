package kitchenpos.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", cascade = PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validate(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        for (final MenuProduct menuProduct : menuProducts) {
            addMenuProduct(menuProduct);
        }
    }

    private void validate(final BigDecimal price, final List<MenuProduct> menuProducts) {
        validatePrice(price);
        validateSum(price, menuProducts);
    }

    private void validateSum(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = menuProducts.stream()
                .map(it -> it.getProduct().getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        if (menuProduct.getMenu() != this) {
            menuProduct.setMenu(this);
        }
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
        return menuProducts;
    }
}
