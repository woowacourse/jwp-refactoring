package kitchenpos.domain;

import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @Test
    void 가격이_0원_보다_작다면_예외를_발생시킨다() {
        // given
        BigDecimal invalidPrice = BigDecimal.valueOf(-100);
        Product product = ProductFixture.상품_생성("상품", invalidPrice);

        // when & then
        assertThatThrownBy(product::validatePriceIsEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
