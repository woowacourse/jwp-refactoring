package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<MenuProduct> menuProducts
    ) {
        validate(name, price);
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private static void validate(final String name, final BigDecimal price) {
        validateName(name);
        validatePrice(price);
    }

    private static void validateName(final String name) {
        if (name.isEmpty() || name.length() > 64) {
            throw new IllegalArgumentException();
        }
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
}
