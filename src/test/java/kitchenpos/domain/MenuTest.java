package kitchenpos.domain;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("menu 가격이 상품들의 가격합보다 더 크면 예외를 발생한다.")
    @Test
    void validate_price_isBigger_than_products_price() {
        // given
        final BigDecimal productsPrice = new BigDecimal(20000);

        final Menu menu = Menu.forSave("메뉴", 30000, 1L);

        // when
        // then
        assertThatThrownBy(() -> menu.validateOverPrice(productsPrice))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
