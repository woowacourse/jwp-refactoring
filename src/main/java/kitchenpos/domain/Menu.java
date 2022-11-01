package kitchenpos.domain;

import static kitchenpos.application.exception.ExceptionType.INVALID_MENU_PRODUCT_EXCEPTION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.application.exception.CustomIllegalArgumentException;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Enumerated
    private Price price;
    @Column
    private Long menuGroupId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this(id, name, price, menuGroupId, new ArrayList<>());
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId,
                List<MenuProduct> menuProducts) {

        validOverMenuProductsSum(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validOverMenuProductsSum(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = menuProducts.stream().map(MenuProduct::calculateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sum) > 0) {
            throw new CustomIllegalArgumentException(INVALID_MENU_PRODUCT_EXCEPTION);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
