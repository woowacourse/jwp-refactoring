package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuProduct 클래스의")
class MenuProductTest {

    @Test
    @DisplayName("calculateAmount 메서드는 메뉴 상품의 (수량 * 가격)의 값을 계산한다.")
    void calculateAmount() {
        MenuProduct menuProduct = new MenuProduct(1L, 3, BigDecimal.valueOf(1000L));
        assertThat(menuProduct.calculateAmount()).isEqualTo(BigDecimal.valueOf(3000L));
    }
}
