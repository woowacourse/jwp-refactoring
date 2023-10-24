package kitchenpos.domain;

import static java.math.BigDecimal.ZERO;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.vo.Money;

@Entity
public class Menu {

    private static final int MINIMUM_VALUE = 0;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Money price;

    @Column(nullable = false)
    private Long menuGroupId;

    @OneToMany(cascade = PERSIST, fetch = EAGER)
    @JoinColumn(name = "menu_id", updatable = false, nullable = false)
    private List<MenuProduct> menuProducts;

    public Menu(Long id, String name, Money price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, Money price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    protected Menu() {
    }

    public static Menu of(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProduct> menuProducts
    ) {
        validatePrice(price);
        Money priceAmount = Money.valueOf(price);

        Money menuProductTotalPrice = menuProducts.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(Money.ZERO, Money::add);

        validateMenuProductTotalPrice(priceAmount, menuProductTotalPrice);
        return new Menu(name, priceAmount, menuGroupId, menuProducts);
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(ZERO) < MINIMUM_VALUE) {
            throw new IllegalArgumentException("메뉴 가격은 " + MINIMUM_VALUE + " 미만일 수 없습니다.");
        }
    }

    private static void validateMenuProductTotalPrice(Money price, Money menuProductTotalPrice) {
        if (price.isGreaterThan(menuProductTotalPrice)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 총액을 초과할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public BigDecimal getPriceValue() {
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
