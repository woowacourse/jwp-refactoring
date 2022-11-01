package kitchenpos.menu.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Menu {

    private static final int ZERO_PRICE = 0;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final String name, final BigDecimal price, final Long menuGroupId, final MenuProducts menuProducts) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("올바르지 않은 메뉴의 가격입니다.");
        }

        final BigDecimal productSumPrice = calculateProductSum(menuProducts);
        if (price.compareTo(productSumPrice) > ZERO_PRICE) {
            throw new IllegalArgumentException("메뉴의 가격이 상품(product)의 금액 총합보다 크면 안됩니다.");
        }

        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private static BigDecimal calculateProductSum(final MenuProducts menuProducts) {
        return menuProducts.getProductsSumPrice();
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
