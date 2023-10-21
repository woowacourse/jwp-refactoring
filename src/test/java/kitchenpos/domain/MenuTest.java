package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 메뉴를_정상적으로_생성한다() {
        // given
        String menuName = "menuName";
        BigDecimal price = BigDecimal.ONE;
        Long menuGroupId = 1L;

        // expect
        assertThatNoException().isThrownBy(() -> Menu.of(menuName, price, menuGroupId));
    }

    @Test
    void 메뉴를_생성할_때_메뉴_가격이_0미만이면_예외를_던진다() {
        // given
        BigDecimal invalidPrice = BigDecimal.valueOf(-1L);

        // expect
        assertThatThrownBy(() -> Menu.of("menuName", invalidPrice, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 0 미만일 수 없습니다.");
    }

    @Nested
    class 메뉴_상품_총액을_검증할_때 {

        @Test
        void 메뉴_가격이_메뉴_상품_총액을_초과하면_예외를_던진다() {
            // given
            Menu menu = Menu.of("menuName", BigDecimal.ONE, 1L);
            BigDecimal menuProductTotalPrice = BigDecimal.ZERO;

            // expect
            assertThatThrownBy(() -> menu.validateMenuProductTotalPrice(menuProductTotalPrice))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격은 메뉴 상품 총액을 초과할 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 0L})
        void 메뉴_가격이_메뉴_상품_총액_이하이면_예외를_던진다(long price) {
            // given
            Menu menu = Menu.of("menuName", BigDecimal.ZERO, 1L);
            BigDecimal menuProductTotalPrice = BigDecimal.valueOf(price);

            // expect
            assertThatNoException().isThrownBy(() -> menu.validateMenuProductTotalPrice(menuProductTotalPrice));
        }
    }
}
