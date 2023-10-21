package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductTest {

    @Test
    void 상품을_정상적으로_생성한다() {
        // given
        String productName = "productName";
        BigDecimal price = BigDecimal.ONE;

        // expect
        assertThatNoException().isThrownBy(() -> Product.of(productName, price));
    }

    @Test
    void 상품을_생성할_때_가격이_0_미만이면_예외를_던진다() {
        // given
        String productName = "productName";
        BigDecimal invalidPrice = BigDecimal.valueOf(-1L);

        // expect
        assertThatThrownBy(() -> Product.of(productName, invalidPrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 0 미만일 수 없습니다.");
    }
}
