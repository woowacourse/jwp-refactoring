package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, new MenuProducts(new ArrayList<>()));
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId, final MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final Long menuGroupId,
                final MenuProducts menuProducts) {
        validatePrice(price);
        validateMenuGroup(menuGroupId);
        validateMenuProducts(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuGroup(final Long menuGroupId) {
        if (menuGroupId == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuProducts(final BigDecimal price, final MenuProducts menuProducts) {
        if (price.compareTo(menuProducts.calculateTotalAmount()) > 0) {
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
