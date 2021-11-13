package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuProduct 단위 테스트")
class MenuProductTest {

    @DisplayName("총 금액은 quantity가 곱해진 값이 반환된다.")
    @Test
    void totalPrice() {
        // given
        Product product1 = new Product("좋은 상품1", BigDecimal.valueOf(3_000));
        Product product2 = new Product("좋은 상품2", BigDecimal.valueOf(5_000));
        MenuProduct menuProduct1 = new MenuProduct(product1, 3);
        MenuProduct menuProduct2 = new MenuProduct(product2, 4);

        // when
        BigDecimal totalPrice1 = menuProduct1.totalPrice();
        BigDecimal totalPrice2 = menuProduct2.totalPrice();

        // then
        assertThat(totalPrice1).isEqualTo(BigDecimal.valueOf(9_000));
        assertThat(totalPrice2).isEqualTo(BigDecimal.valueOf(20_000));
    }
}