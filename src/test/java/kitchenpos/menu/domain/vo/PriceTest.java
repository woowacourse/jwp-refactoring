package kitchenpos.menu.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.domain.vo.Price;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Nested
    class 가격_생성_시 {

        @Test
        void 정상적인_가격이라면_생성에_성공한다() {
            //given
            BigDecimal originPrice = BigDecimal.valueOf(11000);

            //when
            Price price = new Price(originPrice);

            //then
            assertThat(price.getPrice()).isEqualByComparingTo(originPrice);
        }

        @Test
        void 가격이_존재하지_않으면_예외를_던진다() {
            //given
            BigDecimal price = null;

            //when, then
            assertThatThrownBy(() -> new Price(price))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품 가격이 존재하지 않습니다.");
        }

        @Test
        void 가격이_0보다_작으면_예외를_던진다() {
            //given
            BigDecimal price = BigDecimal.valueOf(-1);

            //when, then
            assertThatThrownBy(() -> new Price(price))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품 가격이 0보다 작을 수 없습니다.");
        }
    }
}
