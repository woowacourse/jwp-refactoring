package kitchenpos.domain;

import static java.math.BigDecimal.ZERO;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.vo.Money;

@Entity
public class Menu {

    private static final int MINIMUM_VALUE = 0;

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Money price;

    @Column(nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    public Menu(Long id, String name, Money price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, Money price, Long menuGroupId, MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    protected Menu() {
    }

    public static Menu of(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProduct> products
    ) {
        validatePrice(price);
        Money priceAmount = Money.valueOf(price);
        MenuProducts menuProducts = MenuProducts.from(products);
        validateMenuProductTotalPrice(priceAmount, menuProducts.calculateMenuProductsTotalPrice());
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
        return menuProducts.getMenuProducts();
    }
}
