package kitchenpos.domain.product;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @ValueSource(ints = {-1, -100})
    @ParameterizedTest
    void 가격은_0보다_작을_수_없다(int price) {
        assertThatThrownBy(() -> new Product(1L, "상품", new BigDecimal(price)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ValueSource(ints = {0, 1})
    @ParameterizedTest
    void 가격이_0_이상이면_정상_생성된다(int price) {
        assertThatCode(() -> new Product(1L, "상품", new BigDecimal(price)))
                .doesNotThrowAnyException();
    }
}
