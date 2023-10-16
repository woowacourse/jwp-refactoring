package kitchenpos.domain;

import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.vo.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 메뉴의_가격이_0원_미만인_경우_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> new Menu("치즈피자", Money.valueOf(-1), 1L, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 메뉴의_가격이_메뉴_상품들의_금액의_합보다_큰_경우_예외를_던진다() {
        // given
        Product product = 상품("치즈 피자", 8900L);
        MenuProduct menuProduct = 메뉴_상품(product, 1L);

        // expect
        assertThatThrownBy(() -> new Menu("치즈피자", Money.valueOf(8901L), 1L, List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품들의 금액의 합보다 클 수 없습니다.");
    }
}
