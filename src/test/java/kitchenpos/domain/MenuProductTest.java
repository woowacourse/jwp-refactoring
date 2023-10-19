package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MenuProductTest {

    @DisplayName("메뉴 상품 생성 시, 수량이 0개 이하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -0L})
    void menuProduct_FailWithInvalidQuantity(Long invalidQuantity) {
        // when & then
        assertThatThrownBy(() -> new MenuProduct(1L, 1L, invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 상품의 수량은 1개 이상이어야 합니다.");
    }

    @DisplayName("메뉴 상품을 생성할 수 있다.")
    @Test
    void menuProduct() {
        // then
        assertDoesNotThrow(() -> new MenuProduct(1L, 1L, 1L));
    }

}
