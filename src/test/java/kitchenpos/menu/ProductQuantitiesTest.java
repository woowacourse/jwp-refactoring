package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.ProductQuantities;
import kitchenpos.menu.domain.ProductQuantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductQuantitiesTest {

    private BigDecimal expectedTotalPrice;
    private ProductQuantities productQuantities;

    @BeforeEach
    void setUp() {
        BigDecimal priceOfChicken = BigDecimal.valueOf(10_000);
        long quantityOfChicken = 10L;

        BigDecimal priceOfPizza = BigDecimal.valueOf(15_000);
        long quantityOfPizza = 5L;

        expectedTotalPrice = BigDecimal.ZERO;
        expectedTotalPrice = expectedTotalPrice.add(priceOfChicken.multiply(BigDecimal.valueOf(quantityOfChicken)))
                .add(priceOfPizza.multiply(BigDecimal.valueOf(quantityOfPizza)));


        List<ProductQuantity> productQuantityList = Arrays.asList(
                new ProductQuantity(
                        new Product(1L, "대왕치킨", priceOfChicken),
                        quantityOfChicken
                ),
                new ProductQuantity(
                        new Product(1L, "대왕피자", BigDecimal.valueOf(15_000)),
                        quantityOfPizza
                )
        );

        productQuantities = new ProductQuantities(productQuantityList);
    }

    @DisplayName("포함된 제품에 수량을 곱한 전체 가격을 계산한다.")
    @Test
    void totalPrice() {
        // when - then
        assertThat(productQuantities.totalPrice()).isEqualTo(expectedTotalPrice);
    }

    @DisplayName("MenuProduct로 그룹화한다.")
    @Test
    void groupToMenuProduct() {
        // given
        Menu menu = new Menu(1L, "대왕메뉴", BigDecimal.valueOf(175_000), new MenuGroup("대왕그룹"));
        List<MenuProduct> menuProducts
                = productQuantities.groupToMenuProduct(menu);

        // when - then
        assertThat(menuProducts).hasSize(2);
    }
}
