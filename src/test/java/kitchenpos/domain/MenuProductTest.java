package kitchenpos.domain;

import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductTest {

    @Test
    void 금액을_계산하여_반환한다() {
        // given
        Product product = 상품("치즈피자", 8900L);
        MenuProduct menuProduct = new MenuProduct(product, 2L);

        // when
        BigDecimal result = menuProduct.calculateAmount();

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(17800L));
    }
}
