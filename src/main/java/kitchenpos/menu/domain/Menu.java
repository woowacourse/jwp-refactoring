package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> menuProducts;

    protected Menu() {}

    public Menu(final String name, final BigDecimal price,
                final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        validateMenu(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validateMenu(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal totalMenuProductPrice = getTotalMenuProductPrice(menuProducts);
        if (price.compareTo(totalMenuProductPrice) > 0) {
            throw new IllegalArgumentException("[ERROR] 메뉴의 가격이 메뉴 상품 가격의 합보다 클 수 없습니다.");
        }
    }

    private BigDecimal getTotalMenuProductPrice(final List<MenuProduct> menuProducts) {
        final int menuProductPrice = menuProducts.stream()
                .mapToInt(menuProduct -> {
                    final Product product = menuProduct.getProduct();
                    return product.calculateTotalPrice(menuProduct.getQuantity()).intValue();
                }).reduce(0, Integer::sum);
        return BigDecimal.valueOf(menuProductPrice);
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

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
