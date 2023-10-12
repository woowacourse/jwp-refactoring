package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("테이블 테스트")
class TableServiceTest extends ServiceTest {

    @DisplayName("테이블을 생성한다")
    @Test
    void create() {
        // given & when
        final OrderTable actual = tableService.create(createOrderTable(null, 2, null));

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("테이블 목록을 가져온다")
    @Test
    void list() {
        // then
        assertThat(tableService.list()).isNotNull();
    }

    @DisplayName("테이블의 상태를 변경한다")
    @ParameterizedTest(name = "테이블을 {0} 상태로 변경한다")
    @MethodSource("tableProvider")
    void changeEmpty(String name, boolean empty) {
        // when
        final OrderTable actual = tableService.changeEmpty(1L, createOrderTable(null, null, empty));

        // then
        assertThat(actual.isEmpty()).isEqualTo(empty);
    }

    private static Stream<Arguments> tableProvider() {
        return Stream.of(
                Arguments.of("빈", true),
                Arguments.of("체운", false)
        );
    }

    @DisplayName("없는 테이블 상태 변경시 실패한다")
    @Test
    void changeEmpty_FailWithNonExistTable() {
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, createOrderTable(-1L, null, true)))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("조리중 혹은 먹는 중의 테이블 상태 변경시 실패한다")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_FailWithCookingOrEating(final String state) {
        // given
        final OrderTable orderTable = createOrderTable(null, 1, false);
        final Order order = orderService.create(createOrder(orderTable, List.of(1L, 2L)));
        order.setOrderStatus(state);
        orderService.changeOrderStatus(order.getId(), order);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(order.getOrderTableId(), createOrderTable(1L, null, true)))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("테이블의 손님 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = createOrderTable(null, 3, false);

        // when
        tableService.changeEmpty(1L, orderTable);
        final OrderTable actual = tableService.changeNumberOfGuests(1L, orderTable);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수 변경 실패한다")
    @ParameterizedTest(name = "테이블의 손님 수를 {0} 변경하면 실패한다")
    @MethodSource("idAndEmptyAndGuestsProvider")
    void changeNumberOfGuests_Failed(
            final String name,
            final long orderTableId,
            final int numberOfGuests
    ) {
        // given
        final OrderTable orderTable = createOrderTable(null, numberOfGuests, null);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTableId, orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> idAndEmptyAndGuestsProvider() {
        return Stream.of(
                Arguments.of("0이하로", 1L, -1),
                Arguments.of("없는 테이블에서", -1L, 4)
        );
    }

    @DisplayName("빈 상태의 테이블의 손님 변경은 실패한다")
    @Test
    void changeNumberOfGuests_FailedEmpty() {
        // given
        final OrderTable orderTable = createOrderTable(null, 3, true);
        tableService.changeEmpty(1L, orderTable);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(1L, orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
