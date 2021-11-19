package kitchenpos.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.exception.InvalidStateException;
import kitchenpos.fixture.CustomParameterizedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("OrderTable 도메인 단위테스트")
class OrderTableTest {

    static Stream<Arguments> create_Success() {
        return Stream.of(
            Arguments.of(0, true),
            Arguments.of(0, false),
            Arguments.of(1, true),
            Arguments.of(1, false),
            Arguments.of(1_000, true),
            Arguments.of(1_000, false)
        );
    }

    static Stream<Arguments> create_Fail_When_NumberOfGuestsIsNegative() {
        return Stream.of(
            Arguments.of(-1, true),
            Arguments.of(-1, false),
            Arguments.of(-1_000, true),
            Arguments.of(-1_000, false)
        );
    }

    static Stream<Arguments> changeEmpty_Success() {
        return Stream.of(
            Arguments.of(true, false),
            Arguments.of(false, true),
            Arguments.of(false, false),
            Arguments.of(true, true)
        );
    }

    @DisplayName("생성 - 성공")
    @CustomParameterizedTest
    @MethodSource
    void create_Success(Integer numberOfGuests, Boolean empty) {
        // given
        // when
        // then
        assertThatCode(() -> new OrderTable(numberOfGuests, empty))
            .doesNotThrowAnyException();
    }

    @DisplayName("생성 - 실패 - numberOfGuests가 음수일 때")
    @CustomParameterizedTest
    @MethodSource
    void create_Fail_When_NumberOfGuestsIsNegative(Integer numberOfGuests, Boolean empty) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new OrderTable(numberOfGuests, empty))
            .isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("생성 - 실패 - numberOfGuests가 null일 때")
    @CustomParameterizedTest
    @ValueSource(booleans = {true, false})
    void create_Fail_When_NumberOfGuestsIsNull(Boolean empty) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new OrderTable(null, empty))
            .isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("생성 - 실패 - empty가 null일 때")
    @CustomParameterizedTest
    @ValueSource(ints = {0, 1, 1_000})
    void create_Fail_When_EmptyIsNull(Integer numberOfGuests) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new OrderTable(numberOfGuests, null))
            .isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("비어있지 않음 검증 - 예외 발생 - 비어있을 때")
    @Test
    void validateNotEmpty_ThrowsException_When_Empty() {
        // given
        final OrderTable orderTable = new OrderTable(1, true);

        // when
        // then
        assertThatThrownBy(orderTable::validateNotEmpty)
            .isInstanceOf(InvalidStateException.class);
    }

    @DisplayName("비어있지 않음 검증 - 예외 발생하지 않음 - 비어있지 않을 때")
    @Test
    void validateNotEmpty_NotThrowsException_When_NotEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);

        // when
        // then
        assertThatCode(orderTable::validateNotEmpty)
            .doesNotThrowAnyException();
    }

    @DisplayName("numberOfGuests 변경 - 성공 - 음수, null이 아니고 empty가 false일 때")
    @CustomParameterizedTest
    @ValueSource(ints = {0, 1, 100})
    void changeNumberOfGuests(Integer newNumberOfGuests) {
        // given
        final OrderTable orderTable = new OrderTable(2, false);

        // when
        // then
        assertThatCode(() -> orderTable.changeNumberOfGuests(newNumberOfGuests))
            .doesNotThrowAnyException();

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(newNumberOfGuests);
    }

    @DisplayName("numberOfGuests 변경 - 실 - 음수, null이 아니고 empty가 true일 때")
    @CustomParameterizedTest
    @ValueSource(ints = {0, 1, 100})
    void changeNumberOfGuests_Fail_WhenEmptyIsTrue(Integer newNumberOfGuests) {
        // given
        final int oldNumberOfGuests = 2;
        final OrderTable orderTable = new OrderTable(oldNumberOfGuests, true);

        // when
        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(newNumberOfGuests))
            .isInstanceOf(InvalidStateException.class);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(oldNumberOfGuests);
    }

    @DisplayName("numberOfGuests 변경 - 실패 - 음수 또는 null일 때")
    @CustomParameterizedTest
    @ValueSource(ints = {-1, -100})
    @NullSource
    void changeNumberOfGuests_Fail_When_NewNumberOfGuestsIsNegativeOrNull(Integer newNumberOfGuests) {
        // given
        final int oldNumberOfGuests = 0;
        final OrderTable orderTable = new OrderTable(oldNumberOfGuests, true);

        // when
        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(newNumberOfGuests))
            .isInstanceOf(InvalidArgumentException.class);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(oldNumberOfGuests);
    }

    @DisplayName("empty값 변경 - 성공 - newEmpty값이 null이 아닐 때")
    @CustomParameterizedTest
    @MethodSource
    void changeEmpty_Success(boolean oldEmpty, boolean newEmpty) {
        // given
        final OrderTable orderTable = new OrderTable(10, oldEmpty);

        // when
        orderTable.changeEmpty(newEmpty);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(newEmpty);
    }

    @DisplayName("empty값 변경 - 실패 - newEmpty값이 null일 때")
    @CustomParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty_Fail_When_NewEmptyIsNull(boolean oldEmpty) {
        // given
        final OrderTable orderTable = new OrderTable(10, oldEmpty);

        // when
        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(null))
            .isInstanceOf(InvalidArgumentException.class);
    }
}
