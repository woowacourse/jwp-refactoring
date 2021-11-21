package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.factory.MenuProductFactory;
import kitchenpos.factory.ProductFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductsTest {

    private Products products;

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        Product product1 = ProductFactory.builder()
            .id(1L)
            .name("강정치킨")
            .price(new BigDecimal(10000))
            .build();

        Product product2 = ProductFactory.builder()
            .id(2L)
            .name("뼈치킨")
            .price(new BigDecimal(20000))
            .build();

        products = new Products(product1, product2);

        menuProduct = MenuProductFactory.builder()
            .seq(1L)
            .menuId(1L)
            .productId(1L)
            .quantity(1L)
            .build();
    }

    @DisplayName("주어진 MenuProduct 와 관련된 객체의 총 금액을 계산한다")
    @Test
    void totalPrice() {
        // when
        final BigDecimal totalPrice = products.totalPrice(menuProduct);

        // then
        assertThat(totalPrice).isEqualTo(new BigDecimal(10000));
    }

    @DisplayName("주어진 MenuProduct 와 관련된 객체의 총 금액 계산 실패 - 가지고 있지 않는 Product 의 id")
    @Test
    void totalPriceFail_whenGivenNonExistingMenuProduct() {
        // given
        MenuProduct nonExistingMenuProduct = MenuProductFactory.builder()
            .seq(2L)
            .menuId(1L)
            .productId(3L)
            .quantity(2L)
            .build();

        // when
        ThrowingCallable throwingCallable = () -> products.totalPrice(nonExistingMenuProduct);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
