package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductTest {
    @DisplayName("메뉴 상품을 생성할 수 있다.")
    @Test
    void constructor() {
        MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 1);

        assertAll(
            () -> assertThat(menuProduct.getSeq()).isEqualTo(1L),
            () -> assertThat(menuProduct.getMenuId()).isEqualTo(1L),
            () -> assertThat(menuProduct.getProductId()).isEqualTo(1L),
            () -> assertThat(menuProduct.getQuantity()).isEqualTo(1)
        );
    }

    @DisplayName("메뉴 상품의 메뉴 번호를 변경할 수 있다.")
    @Test
    void changeOrderId() {
        MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 1);

        menuProduct.changeMenuId(2L);

        assertThat(menuProduct.getMenuId()).isEqualTo(2L);
    }
}
