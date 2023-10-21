package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.exception.ProductException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ProductPriceTest {

    @Test
    void 상품_가격은_null일_수_없다() {
        // given
        final BigDecimal price = null;

        // expected
        assertThatThrownBy(() -> new ProductPrice(price))
                .isInstanceOf(ProductException.NoPriceException.class);
    }

    @Test
    void 상품_가격은_음수일_수_없다() {
        // given
        final BigDecimal price = BigDecimal.valueOf(-1L);

        // expected
        assertThatThrownBy(() -> new ProductPrice(price))
                .isInstanceOf(ProductException.NegativePriceException.class);
    }

    @Test
    void 상품_가격은_0원_일_수_있다() {
        // given
        final BigDecimal price = BigDecimal.valueOf(0L);

        // expected
        assertDoesNotThrow(() -> new ProductPrice(price));
    }

    @Test
    void 상품_가격은_양수일_수_있다() {
        // given
        final BigDecimal price = BigDecimal.valueOf(10L);

        // expected
        assertDoesNotThrow(() -> new ProductPrice(price));
    }

    @Test
    void 상품_가격은_소숫점_두번째_자리까지_사용한다() {
        // given
        final BigDecimal price = BigDecimal.valueOf(1000.001);

        // when
        final ProductPrice productPrice = new ProductPrice(price);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(productPrice.getPrice()).hasScaleOf(2);
            softly.assertThat(productPrice.getPrice()).isEqualTo(BigDecimal.valueOf(1000.00).setScale(2));
        });
    }

    @Test
    void 상품_가격은_소숫점_두번째_자리까지_반올림된다() {
        // given
        final BigDecimal price = BigDecimal.valueOf(1000.005);

        // when
        final ProductPrice productPrice = new ProductPrice(price);

        // then
        assertThat(productPrice.getPrice()).isEqualTo(BigDecimal.valueOf(1000.01));
    }
}
