package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품을 생성한다.")
    void createMenuProduct() {
        final MenuProduct menuProduct = new MenuProduct(1L, 2L, 10);

        assertAll(
                () -> assertThat(menuProduct.getMenuId()).isEqualTo(1L),
                () -> assertThat(menuProduct.getProductId()).isEqualTo(2L),
                () -> assertThat(menuProduct.getQuantity()).isEqualTo(10)
        );
    }
}