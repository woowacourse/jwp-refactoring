package kitchenpos.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductTest {

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void 수량이_0이하이면_예외가_발생한다(long 수량) {
        //expect
        assertThatThrownBy(() -> new MenuProduct(null, null, 수량))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴에 등록된 상품의 수량은 0 이하일 수 없습니다.");
    }

}
