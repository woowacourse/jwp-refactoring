package kitchenpos.newdomain;

import static kitchenpos.newdomain.DomainFixture.뿌링클;
import static kitchenpos.newdomain.DomainFixture.치즈볼;
import static kitchenpos.newdomain.DomainFixture.한개;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import kitchenpos.newdomain.vo.Name;
import kitchenpos.newdomain.vo.Price;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴_가격이_메뉴_상품들의_가격의_합보다_크면_예외를_던진다() {
        // when & then
        assertThatThrownBy(() -> new Menu(
                        new Name("뿌링클+치즈볼"),
                        Price.valueOf(30_000),
                        0L,
                        List.of(new MenuProduct(뿌링클, 한개), new MenuProduct(치즈볼, 한개))
                )
        ).isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.MENU_PRICE_ERROR);
    }
}
