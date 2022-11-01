package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "menu")
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        validatePrice(price);
        validateMenuProductsPrice(price, menuProducts);

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validateMenuProductsPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        BigDecimal sum = calculateSumOfMenuProductsPrice(menuProducts);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateSumOfMenuProductsPrice(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            BigDecimal productPrice = menuProduct.getProduct().getPrice();
            BigDecimal menuProductQuantity = BigDecimal.valueOf(menuProduct.getQuantity());
            sum = sum.add(productPrice.multiply(menuProductQuantity));
        }
        return sum;
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
