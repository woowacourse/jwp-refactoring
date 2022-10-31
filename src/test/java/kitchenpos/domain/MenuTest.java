package kitchenpos.domain;

import static kitchenpos.support.DomainFixture.뿌링클;
import static kitchenpos.support.DomainFixture.뿌링클_치즈볼;
import static kitchenpos.support.DomainFixture.치즈볼;
import static kitchenpos.support.DomainFixture.한개;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴_가격이_메뉴_상품들의_가격의_합보다_크면_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> new Menu(
                        뿌링클_치즈볼,
                        Price.valueOf(30_000),
                        0L,
                        List.of(new MenuProduct(뿌링클, 한개), new MenuProduct(치즈볼, 한개))
                )
        ).isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.MENU_PRICE_ERROR);
    }
}
