package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import kitchenpos.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Embedded
    @NotNull
    private Price price;

    @NotNull
    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final Price price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        validatePrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(final Price price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!menuProducts.isEmpty() && price.isBiggerThan(sum)) {
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
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
