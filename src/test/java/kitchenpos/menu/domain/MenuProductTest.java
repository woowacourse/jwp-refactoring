package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductTest {
    @Test
    @DisplayName("MenuProduct를 생성한다.")
    void createMenuProduct() {
        // given
        final Long productId = 1L;
        final Integer quantity = 5;

        // when
        final MenuProduct menuProduct = MenuProduct.of(productId, quantity);

        // then
        assertThat(menuProduct.getProductId()).isEqualTo(productId);
        assertThat(menuProduct.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("productId가 null인 경우 예외를 던진다.")
    void throwExceptionIfProductIdIsNull() {
        // given
        final Integer quantity = 5;

        // then
        assertThatThrownBy(
                () -> MenuProduct.of(null, quantity)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("quantity가 null인 경우 예외를 던진다.")
    void throwExceptionIfQuantityIsNull() {
        // given
        final Long productId = 1L;

        // then
        assertThatThrownBy(
                () -> MenuProduct.of(productId, null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("quantity가 음수인 경우 예외를 던진다.")
    void throwExceptionIfQuantityIsNegative() {
        // given
        final Long productId = 1L;
        final Integer quantity = -5;

        // then
        assertThatThrownBy(
                () -> MenuProduct.of(productId, quantity)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("quantity가 0 이상인 경우 MenuProduct를 정상적으로 생성한다.")
    void createMenuProductIfQuantityIsNonNegative() {
        // given
        final Long productId = 1L;
        final Integer quantity = 0;

        // when
        final MenuProduct menuProduct = MenuProduct.of(productId, quantity);

        // then
        assertThat(menuProduct.getQuantity()).isEqualTo(0);
    }
}
