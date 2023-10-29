package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.common.exception.ExceptionType;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import kitchenpos.product.domain.Product;

class ProductTest {

    @ParameterizedTest(name = "[{index}] price={0}")
    @DisplayName("Product 객체 정상 생성")
    @ValueSource(strings = {"16000", "0"})
    void create(int priceValue) {
        // given
        String name = "후라이드";

        // when
        BigDecimal price = BigDecimal.valueOf(priceValue);
        Product product = new Product.Builder()
            .name(name)
            .price(price)
            .build();

        // then
        assertAll(
            () -> assertThat(product.getName()).isEqualTo(name),
            () -> assertThat(product.getPrice().longValue()).isEqualTo(price.longValue())
        );
    }

    @ParameterizedTest(name = "[{index}] price={0}")
    @DisplayName("Product 객체 생성 실패")
    @ValueSource(ints = {-1})
    void create_fail(int priceVal) {
        // given
        String name = "후라이드";

        // when
        BigDecimal price = BigDecimal.valueOf(priceVal);

        // then
        assertThatThrownBy(() -> new Product.Builder()
            .name(name)
            .price(price)
            .build()).hasMessageContaining(ExceptionType.PRICE_RANGE.getMessage());
    }
}
