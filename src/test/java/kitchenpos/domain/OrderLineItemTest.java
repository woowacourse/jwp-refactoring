package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrderLineItemTest {

    @Test
    @DisplayName("constructor")
    void constructor() {
        assertThat(new OrderLineItem(1L, 1L, 1L))
            .isInstanceOf(OrderLineItem.class);
    }

    @ParameterizedTest
    @MethodSource("wrongParameterForConstructor")
    @DisplayName("constructor - 잘못된 파라미터가 들어올 경우 예외처리")
    void constructor_IfAnyParameterIsWrong_ThrowException(
            Long orderId, Long menuId, Long quantity) {
        assertThatThrownBy(() -> new OrderLineItem(orderId, menuId, quantity))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> wrongParameterForConstructor() {
        return Stream.of(
            Arguments.of(null, 1L, 10L),
            Arguments.of(1L, null, 10L),
            Arguments.of(1L, 1L, null),
            Arguments.of(1L, 1L, 0L),
            Arguments.of(1L, 1L, -1L)
        );
    }
}