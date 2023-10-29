import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.Price;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Nested
    class 생성 {

        @Test
        void 가격을_생성할_수_있다() {
            final var price = new Price(BigDecimal.valueOf(300));

            assertThat(price.getValue()).isEqualTo(BigDecimal.valueOf(300));
        }

        @Test
        void 가격은_존재해야_한다() {
            assertThatThrownBy(() -> new Price(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 존재해야 합니다.");
        }

        @Test
        void 가격은_0이상이어야_한다() {
            assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 0이상이어야 합니다.");
        }
    }

    @Nested
    class add {

        @Test
        void 가격을_더할_수_있다() {
            final var price1 = new Price(BigDecimal.valueOf(300));
            final var price2 = new Price(BigDecimal.valueOf(500));

            Price result = price1.add(price2);
            assertThat(result).isEqualTo(new Price(BigDecimal.valueOf(800)));
        }
    }

    @Nested
    class multiply {

        @Test
        void 가격을_곱할_수_있다() {
            final var price = new Price(BigDecimal.valueOf(300));
            final var amount = 30;

            final var result = price.multiply(amount);
            assertThat(result).isEqualTo(new Price(BigDecimal.valueOf(9000)));
        }
    }

    @Nested
    class isBiggerThan {

        @Test
        void 가격이_더_큰지_비교할_수_있다() {
            final var price1 = new Price(BigDecimal.valueOf(300));
            final var price2 = new Price(BigDecimal.valueOf(500));

            final var result = price1.isBiggerThan(price2);

            assertThat(result).isFalse();
        }
    }
}
