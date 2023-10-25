package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        validatePrice(price);
        validateTotalMenuProductPrice(price, menuProducts);

        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    protected Menu() {
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTotalMenuProductPrice(final BigDecimal price, final MenuProducts menuProducts) {
        if (price.compareTo(menuProducts.calculateTotalPrice()) > 0) {
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
