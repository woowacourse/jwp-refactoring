package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.product.domain.InvalidProductPriceException;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("상품(Product) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ProductTest {

    @Nested
    class 생성_시 {

        @ParameterizedTest(name = "price: {0}")
        @ValueSource(ints = {0, 1, 1000})
        void 가격이_0원_이상이면_생성된다(int price) {
            // given
            BigDecimal bigDecimalPrice = BigDecimal.valueOf(price);

            // when & then
            assertDoesNotThrow(() ->
                    new Product("말랑", bigDecimalPrice)
            );
        }

        @Test
        void 가격이_0원_미만인_경우_예외() {
            // given
            BigDecimal price = BigDecimal.valueOf(-1);

            // when & then
            assertThatThrownBy(() ->
                    new Product("말랑", price)
            ).isInstanceOf(InvalidProductPriceException.class)
                    .hasMessage("상품의 가격은 0원 이상이어야 합니다.");
        }
    }
}
