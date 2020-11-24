package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MenuProductTest {

    @Test
    @DisplayName("constructor")
    void constructor() {
        assertThat(new MenuProduct(1L, 1L, 1))
            .isInstanceOf(MenuProduct.class);
    }

    @ParameterizedTest
    @MethodSource("wrongParametersForConstructor")
    @DisplayName("constructor - 파라미터가 잘못된 경우 예외처리")
    void constructor_IfAnyParameterIsWrong_ThrowException(
            Long menuId, Long productId, long quantity) {
        assertThatThrownBy(() -> new MenuProduct(menuId, productId, quantity))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> wrongParametersForConstructor() {
        return Stream.of(
            Arguments.of(null, 1L, 1),
            Arguments.of(1L, null, 1),
            Arguments.of(1L, 1L, 0),
            Arguments.of(1L, 1L, -1)
        );
    }
}