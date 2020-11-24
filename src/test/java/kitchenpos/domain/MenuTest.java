package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MenuTest {

    @Test
    @DisplayName("Menu 생성자")
    void constructor() {
        assertThat(
            new Menu("치킨세트", new Price(BigDecimal.valueOf(19_000)), 1L)
        ).isInstanceOf(Menu.class);
    }

    @ParameterizedTest
    @MethodSource("wrongMenuConstructorParameters")
    @DisplayName("Menu 생성자 - 잘못된 인자 전달시 예외처리")
    void constructor_IfAnyParameterIsWrong_ThrowException(String name, Price price,
            Long menuGroupId) {
        assertThatThrownBy(() -> new Menu(name, price, menuGroupId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> wrongMenuConstructorParameters() {
        return Stream.of(
            Arguments.of(null, new Price(BigDecimal.valueOf(19_000)), 1L),
            Arguments.of("", new Price(BigDecimal.valueOf(19_000)), 1L),
            Arguments.of("치킨세트", null, 1L),
            Arguments.of("치킨세트", new Price(BigDecimal.valueOf(19_000)), null)
        );
    }
}
