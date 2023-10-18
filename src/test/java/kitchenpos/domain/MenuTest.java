package kitchenpos.domain;

import static kitchenpos.exception.MenuExceptionType.PRICE_IS_UNDER_TOTAL_PRODUCT_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import kitchenpos.exception.MenuException;
import kitchenpos.exception.MenuExceptionType;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴_가격이_메뉴_상품의_총합_이상이면_예외_발생() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "치킨");
        Menu menu = new Menu(1L, "후라이드 치킨", BigDecimal.valueOf(1001), menuGroup);

        Product product = new Product(1L, "후라이드 치킨", BigDecimal.valueOf(1000));

        // when
        MenuExceptionType exceptionType = assertThrows(MenuException.class,
                () -> new MenuProduct(menu, product, 1)
        ).exceptionType();


        // then
        assertThat(exceptionType).isEqualTo(PRICE_IS_UNDER_TOTAL_PRODUCT_AMOUNT);
    }
}
