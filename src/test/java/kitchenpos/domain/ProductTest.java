package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.factory.ProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = ProductFactory.builder()
            .id(1L)
            .name("강정치킨")
            .price(new BigDecimal(10000))
            .build();
    }

    @DisplayName("상품의 개수를 받아 총 금액을 개산한다")
    @Test
    void totalPrice() {
        // given
        long quantity = 2L;

        // when
        BigDecimal totalPrice = product.totalPrice(quantity);

        // then
        assertThat(totalPrice).isEqualTo(new BigDecimal(20000));
    }
}
