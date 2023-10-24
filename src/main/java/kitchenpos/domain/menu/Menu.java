package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.exception.InvalidPriceException;

@Entity
public class Menu {

    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    private MenuName name;

    @Embedded
    private MenuPrice price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final List<MenuProduct> menuProducts, final MenuGroup menuGroup) {
        this(null, name, price, menuProducts, menuGroup);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final List<MenuProduct> menuProducts, final MenuGroup menuGroup) {
        this.id = id;
        this.name = new MenuName(name);
        this.menuProducts = menuProducts;
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
        this.price = new MenuPrice(price);
        this.menuGroup = menuGroup;
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

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(final String name) {
        this.name = new MenuName(name);
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public void setPrice(final BigDecimal price) {
        this.price = new MenuPrice(price);
    }
}
