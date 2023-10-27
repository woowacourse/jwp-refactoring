package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.common.Price;
import kitchenpos.common.exception.InvalidPriceException;

@Entity
public class Menu {

    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    private MenuName name;

    @Embedded
    private Price price;


    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final List<MenuProduct> menuProducts, final Long menuGroupId) {
        this(null, name, price, menuProducts, menuGroupId);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final List<MenuProduct> menuProducts, final Long menuGroupId) {
        this.id = id;
        this.name = new MenuName(name);
        this.menuProducts.addAll(menuProducts);
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        validateValueLessThanProductTotalPrice(price, calculateMenuProductTotalPrice());
    }

    private void validateValueLessThanProductTotalPrice(final BigDecimal value, final BigDecimal productTotalPrice) {
        if (value.compareTo(productTotalPrice) > 0) {
            throw new InvalidPriceException("메뉴의 가격은 메뉴 상품의 가격 합보다 작거나 같아야 합니다.");
        }
    }

    private BigDecimal calculateMenuProductTotalPrice() {
        return menuProducts.stream()
                           .map(MenuProduct::getPrice)
                           .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
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
}
