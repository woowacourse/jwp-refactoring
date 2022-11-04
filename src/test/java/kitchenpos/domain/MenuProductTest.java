package kitchenpos.domain;

import kitchenpos.domain.menu.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuProductTest {

    @DisplayName("상품 id가 null인 메뉴 상품은 생성할 수 없다")
    @Test
    void create_nameNull() {
        assertThatThrownBy(() -> new MenuProduct(null, 1L)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("수량이 양의 정수가 아닌 메뉴 상품은 생성할 수 없다")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L})
    void create_quantityNotPositive(Long invalidQuantity) {
        assertThatThrownBy(() -> new MenuProduct(1L, invalidQuantity)).isInstanceOf(IllegalArgumentException.class);
    }
}
