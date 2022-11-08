package kitchenpos.domain;

import static kitchenpos.support.DomainFixture.뿌링클;
import static kitchenpos.support.DomainFixture.치즈볼;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴_가격이_메뉴_상품들의_가격의_합보다_크면_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> new Menu(
                        "뿌링클+치즈볼",
                        30_000,
                        0L,
                        List.of(new MenuProduct(1L, 뿌링클.getPrice(), 1),
                                new MenuProduct(2L, 치즈볼.getPrice(), 1))
                )
        ).isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.MENU_PRICE_ERROR);
    }
}
