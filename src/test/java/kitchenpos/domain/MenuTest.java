package kitchenpos.domain;

import static kitchenpos.common.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.common.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Nested
    class 메뉴를_생성할_때 {

        @Test
        void 메뉴를_정상적으로_생성한다() {
            // given
            String menuName = "menuName";
            BigDecimal price = BigDecimal.ONE;
            Long menuGroupId = 1L;
            List<MenuProduct> menuProducts = List.of(메뉴_상품(상품()));
            BigDecimal menuProductTotalPrice = BigDecimal.ONE;

            // expect
            assertThatNoException().isThrownBy(() -> Menu.of(
                    menuName,
                    price,
                    menuGroupId,
                    menuProducts
            ));
        }

        @Test
        void 메뉴_가격이_0_미만이면_예외를_던진다() {
            // given
            BigDecimal invalidPrice = BigDecimal.valueOf(-1L);

            // expect
            assertThatThrownBy(() -> Menu.of(
                            "menuName",
                            invalidPrice,
                            1L,
                            List.of(메뉴_상품(상품()))
                    )
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 0 미만일 수 없습니다.");
        }

        @Test
        void 메뉴_가격이_메뉴_상품_총액을_초과하면_예외를_던진다() {
            // given
            BigDecimal invalidPrice = BigDecimal.ONE;

            // expect
            assertThatThrownBy(() -> Menu.of(
                            "menuName",
                            invalidPrice,
                            1L,
                            List.of(메뉴_상품(상품(BigDecimal.ZERO)))
                    )
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격은 메뉴 상품 총액을 초과할 수 없습니다.");
        }
    }
}
