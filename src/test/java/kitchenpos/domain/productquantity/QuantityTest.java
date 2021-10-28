package kitchenpos.domain.productquantity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.config.CustomParameterizedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Quantity 도메인 단위테스트")
class QuantityTest {

    @DisplayName("생성 - 성공 - value가 null 또는 음수가 아닐 때")
    @CustomParameterizedTest
    @ValueSource(longs = {0, 1, 100, 1_000, 1_000_000_000})
    void create_Success_When_ValueIsValid(long value) {
        // given
        // when
        // then
        assertThatCode(() -> new Quantity(value))
            .doesNotThrowAnyException();
    }

    @DisplayName("생성 - 실패 - value가 음수 또는 null일 때")
    @CustomParameterizedTest
    @ValueSource(longs = {-1_000_000, -1_000, -1})
    @NullSource
    void create_Fail_When_ValueIsNegativeOrNull(Long value) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Quantity(value))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
