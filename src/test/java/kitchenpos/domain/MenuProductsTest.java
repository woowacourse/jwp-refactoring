package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.factory.MenuProductFactory;
import kitchenpos.factory.ProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    private MenuProducts menuProducts;

    @BeforeEach
    void setUp() {
        MenuProduct menuProduct1 = MenuProductFactory.builder()
            .seq(1L)
            .menuId(1L)
            .productId(1L)
            .quantity(2L)
            .build();

        MenuProduct menuProduct2 = MenuProductFactory.builder()
            .seq(2L)
            .menuId(1L)
            .productId(2L)
            .quantity(3L)
            .build();

        menuProducts = new MenuProducts(menuProduct1, menuProduct2);
    }

    @DisplayName("주어진 Products 에 있는 상품 금액을 활용하여 총 금액을 구한다")
    @Test
    void calculateTotalPrice() {
        // given
        Product product1 = ProductFactory.builder()
            .id(1L)
            .name("강정치킨")
            .price(new BigDecimal(10000))
            .build();
        Product product2 = ProductFactory.builder()
            .id(2L)
            .name("뼈치킨")
            .price(new BigDecimal(11000))
            .build();
        Products products = new Products(product1, product2);

        // when
        BigDecimal totalPrice = menuProducts.calculateTotalPrice(products);

        // then
        assertThat(totalPrice).isEqualTo(new BigDecimal(53000));
    }
}
