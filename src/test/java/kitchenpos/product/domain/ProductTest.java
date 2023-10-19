package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.vo.PriceIsNegativeException;
import kitchenpos.common.vo.PriceIsNotProvidedException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ProductTest {

    @Test
    void 상품_가격이_제공되지_않으면_예외를_던진다() {
        // given, when, then
        assertThatThrownBy(() -> new Product(null, null))
                .isInstanceOf(PriceIsNotProvidedException.class);
    }

    @Test
    void 상품_가격이_음수라면_예외를_던진다() {
        // given, when, then
        assertThatThrownBy(() -> new Product(null, BigDecimal.valueOf(-1L)))
                .isInstanceOf(PriceIsNegativeException.class);
    }

    @Test
    void 상품을_정상_생성할_수_있다() {
        // given
        Product product = new Product(null, BigDecimal.valueOf(1000L));

        // when, then
        assertThat(product.getName()).isNull();
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(1000L));
    }
}
