package kitchenpos.menu.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.vo.MenuQuantity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuQuantityTest {

    @Nested
    class 수량_생성_시 {

        @Test
        void 정상적인_수량이라면_생성에_성공한다() {
            //given
            long amount = 10;

            //when
            MenuQuantity menuQuantity = new MenuQuantity(amount);

            //then
            assertThat(menuQuantity.getQuantity()).isEqualTo(amount);
        }

        @Test
        void 수량이_1보다_작으면_예외를_던진다() {
            //given
            long amount = 0;

            //when, then
            assertThatThrownBy(() -> new MenuQuantity(amount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("최소 1개의 수량은 필요합니다.");
        }
    }
}
