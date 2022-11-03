package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @DisplayName("메뉴의 이름을 빈값이거나 null로 생성하면 예외가 발생한다.")
    @ValueSource(strings = "")
    @NullSource
    @ParameterizedTest
    void creatFailureWhenEmptyName(String name) {

        assertThatThrownBy(
                () -> new Menu(name, BigDecimal.ONE, 1L, Collections.emptyList()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 공백일 수 없습니다.");
    }

    @DisplayName("메뉴의 가격이 없거나 음수로 생성하면 예외가 발생한다")
    @MethodSource
    @ParameterizedTest
    void createFailureWhenNotPositive(BigDecimal price) {

        assertThatThrownBy(
                () -> new Menu("제육 볶음", price, 1L, Collections.emptyList()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 가격은 없거나 음수일 수가 없습니다.");
    }

    private static Stream<BigDecimal> createFailureWhenNotPositive() {
        return Stream.of(null, BigDecimal.valueOf(-1L));
    }

    @DisplayName("메뉴의 가격이 매개변수 값보다 크면 true를 반환한다.")
    @Test
    void lessThanPrice() {
        Menu menu = new Menu("제육 볶음", BigDecimal.TEN, 1L, Collections.emptyList());

        assertThat(menu.isBiggerPrice(BigDecimal.ONE)).isTrue();
    }
}
