package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.global.domain.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long menuGroupId;
    @Embedded
    private Price price;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts);
        validatePriceUnderThanSumOfProductPrice();
    }

    private void validatePriceUnderThanSumOfProductPrice() {
        BigDecimal sumOfPrice = menuProducts.calculateSumOfPrice();

        if (price.isExpensiveThan(sumOfPrice)) {
            throw new IllegalArgumentException("메뉴 가격은 상품 가격의 합 보다 작아야 합니다.");
        }
    }

    public void validate(final Consumer menuValidator) {
        menuValidator.accept(this);
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
        return menuProducts.getMenuProducts();
    }
}
