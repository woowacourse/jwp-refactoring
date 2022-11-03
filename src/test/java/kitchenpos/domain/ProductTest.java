package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.vo.Price;
import kitchenpos.exception.badrequest.PriceInvalidException;
import kitchenpos.exception.badrequest.ProductNameInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ProductTest {
    @DisplayName("Product는")
    @Nested
    class ProductConstructor {
        @DisplayName("name과 price로 생성할 수 있다")
        @Test
        void success_to_create() {
            // given
            final var name = "골뱅이 소면";
            final var price = new BigDecimal("20000.00");

            // when
            final var actual = new Product(name, Price.from(price));

            // then
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(new Product(null, name, Price.from(price)));
        }

        @DisplayName("name이 null이거나 비어있을 경우 예외가 발생한다")
        @NullAndEmptySource
        @ParameterizedTest
        void creating_product_with_null_or_empty_name_should_fail(final String invalidName) {
            // given
            final var name = invalidName;
            final var price = new BigDecimal("20000.00");

            // when & then
            assertThatThrownBy(() -> new Product(name, Price.from(price)))
                    .isInstanceOf(ProductNameInvalidException.class);
        }

        @DisplayName("price가 null일 경우 예외가 발생한다")
        @Test
        void creating_product_with_null_price_should_fail() {
            // given
            final var name = "팟타이";
            final BigDecimal price = null;

            // when & then
            assertThatThrownBy(() -> new Product(name, Price.from(price)))
                    .isInstanceOf(PriceInvalidException.class);
        }

        @DisplayName("price가 0 보다 작을 경우 예외가 발생한다")
        @Test
        void creating_product_with_price_less_than_zero_should_fail() {
            // given
            final var name = "팟타이";
            final var price = new BigDecimal("-1.00");

            // when & then
            assertThatThrownBy(() -> new Product(name, Price.from(price)))
                    .isInstanceOf(PriceInvalidException.class);
        }
    }
}
