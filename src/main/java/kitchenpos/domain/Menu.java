package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    @ManyToOne(fetch = LAZY)
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = REMOVE)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(
            final Long id,
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final String name,
                          final BigDecimal price,
                          final MenuGroup menuGroup,
                          final Map<Product, Integer> productWithQuantity
    ) {
        validatePrice(price);
        final List<MenuProduct> menuProducts = validateTotalPrice(price, productWithQuantity);

        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    private static List<MenuProduct> validateTotalPrice(final BigDecimal price, final Map<Product, Integer> productWithQuantity) {
        BigDecimal calculatedTotalPrice = productWithQuantity.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(calculatedTotalPrice) > 0) {
            throw new IllegalArgumentException();
        }

        return productWithQuantity.keySet().stream()
                .map(product -> MenuProduct.of(product, productWithQuantity.get(product)))
                .collect(Collectors.toList());
    }


    private static void validatePrice(final BigDecimal price) {
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
