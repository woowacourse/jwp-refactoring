package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuProductTest {

    @ParameterizedTest(name = "메뉴에 속해있는 상품의 개수는 1개 미만일 수 없다.")
    @ValueSource(longs = {-100, 0})
    void createMenuProductFailTest_ByQuantityIsLessThanOne(Long quantity) {
        //when then
        assertThatThrownBy(() -> MenuProduct.create(quantity, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 개수는 1개 이상이어야 합니다.");
    }

    @DisplayName("메뉴에 속해있는 상품 수량은 null일 수 없다.")
    @Test
    void createMenuProductFailTest_ByQuantityIsNull() {
        //when then
        assertThatThrownBy(() -> MenuProduct.create(null, 1L))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("상품 수량은 null일 수 없습니다.");
    }
}
