package kitchenpos.refactoring.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuProductPriceCalculatorTest {

    public static final List<Product> PRODUCTS = List.of(
            new Product(
                    1L,
                    "레오가 사랑하는 치킨",
                    new Price(BigDecimal.valueOf(18000))
            ),
            new Product(
                    2L,
                    "준팍이 사랑하는 치킨",
                    new Price(BigDecimal.valueOf(16000))
            )
    );
    public static final List<MenuProduct> MENU_PRODUCTS = List.of(
            new MenuProduct(1L, 3L),
            new MenuProduct(2L, 1L)
    );

    private Price expectedPrice;

    @BeforeEach
    void setUp() {
        Price product1Price = PRODUCTS.get(0).getPrice()
                .multiply(MENU_PRODUCTS.get(0).getQuantity());
        Price product2Price = PRODUCTS.get(1).getPrice()
                .multiply(MENU_PRODUCTS.get(1).getQuantity());

        expectedPrice = product1Price.add(product2Price);
    }

    @Nested
    class calculateTotalPrice {

        @Test
        void success() {
            // when
            Price actualTotalPrice = MenuProductPriceCalculator.calculateTotalPrice(MENU_PRODUCTS, PRODUCTS);

            // then
            assertThat(actualTotalPrice).isEqualTo(expectedPrice);

        }
    }
}
