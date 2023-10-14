package kitchenpos.domain;

import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.vo.Money;
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
        Money result = menuProduct.calculateAmount();

        // then
        assertThat(result).isEqualTo(Money.valueOf(17800L));
    }

    @Test
    void 상품이_존재하지_않으면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> new MenuProduct(null, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }
}
