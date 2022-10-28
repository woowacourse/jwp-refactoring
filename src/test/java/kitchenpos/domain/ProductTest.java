package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @DisplayName("메뉴 가격이 상품 수량 * 상품 가격보다 크면 예외를 반환한다.")
    @Test
    void validatePrice() {
        // given
        Menu menu = new Menu(1L, "빅맥 세트", new Price(BigDecimal.valueOf(6500)), 1L);

        // when & then
        assertThatThrownBy(() -> menu.validatePrice(BigDecimal.valueOf(6400)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 상품 수량 * 상품 가격의 합보다 클 수 없습니다.");
    }
}
