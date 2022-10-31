package kitchenpos.domain;

import static kitchenpos.support.MenuProductFixture.MENU_PRODUCT_1;
import static kitchenpos.support.ProductFixture.PRODUCT_PRICE_10000;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void 메뉴상품의_총_가격을_계산한다() {
        // given
        final MenuProduct menuProduct = MENU_PRODUCT_1.생성(PRODUCT_PRICE_10000.생성());

        // when
        final Price price = menuProduct.calculateProductTotalPrice();

        // then
        assertThat(price).isEqualTo(new Price(new BigDecimal(10_000)));
    }
}
