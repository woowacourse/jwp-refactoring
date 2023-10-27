package kitchenpos.prodcut;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import kitchenpos.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @Nested
    class 상품_생성시 {

        @ParameterizedTest
        @ValueSource(longs = {10, 50, 100, 1000, 10000})
        void 성공(Long price) {
            // when && then
            assertThatNoException().isThrownBy(() -> new Product("상품", BigDecimal.valueOf(price)));
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -1})
        void 가격이_음수면_예외(int price) {
            // when && then
            Assertions.assertThatThrownBy(() -> new Product("상품", BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격이_널이면_예외() {
            // when && then
            Assertions.assertThatThrownBy(() -> new Product("상품", (BigDecimal) null))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
