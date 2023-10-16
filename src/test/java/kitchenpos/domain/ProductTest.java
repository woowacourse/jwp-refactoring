package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.KitchenPosException;
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
    class 생성 {

        @Test
        void 가격이_null_이면_예외() {
            // given
            Money price = null;

            // when & then
            assertThatThrownBy(() -> new Product(1L, "맥주", price))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("상품의 가격은 null이 될 수 없습니다.");
        }

        @Test
        void 가격이_0보다_작으면_예외() {
            // given
            Money price = Money.from(-1);

            // when & then
            assertThatThrownBy(() -> new Product(1L, "맥주", price))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("상품의 가격은 0보다 작을 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 1, 1000})
        void 가격이_0_이상이면_성공(long value) {
            // given
            Money price = Money.from(value);

            // when
            Product product = new Product(1L, "맥주", price);

            // then
            assertThat(product.getPrice().getValue()).isNotNegative();
        }
    }
}
