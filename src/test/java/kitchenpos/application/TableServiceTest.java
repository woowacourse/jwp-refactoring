package kitchenpos.application;

import kitchenpos.application.request.table.ChangeEmptyRequest;
import kitchenpos.application.request.table.ChangeNumberOfGuestsRequest;
import kitchenpos.application.request.table.OrderTableRequest;
import kitchenpos.application.response.tablegroup.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.newOrder;
import static kitchenpos.fixture.OrderTableFixture.newOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class TableServiceTest {

    private final TableService tableService;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    @Autowired
    public TableServiceTest(final TableService tableService, final OrderDao orderDao,
                            final OrderTableDao orderTableDao) {
        this.tableService = tableService;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @DisplayName("주문 테이블을 추가한다")
    @Test
    void create() {
        final var request = new OrderTableRequest(1, false);
        final var actual = tableService.create(request);

        assertThat(actual.getId()).isPositive();
    }

    @Nested
    @ServiceTest
    class ChangeEmptyTest {

        @DisplayName("주문 테이블을 비운다")
        @Test
        void changeEmpty() {
            final var orderTableId = saveOrderTable(null, 1, false).getId();

            final var request = new ChangeEmptyRequest(true);

            final var actual = tableService.changeEmpty(orderTableId, request);
            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("존재하는 주문 테이블이어야만 한다")
        @Test
        void changeEmptyWithNonExistOrderTable() {
            final var nonExistOrderTableId = 0L;

            final var request = new ChangeEmptyRequest(true);

            assertThatThrownBy(() -> tableService.changeEmpty(nonExistOrderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @DisplayName("단체 지정되지 않은 주문 테이블이어야 한다")
        @Test
        void changeEmptyWithAssignedTableGroup() {
            final var tableGroupId = 1L;
            final var orderTableId = saveOrderTable(tableGroupId, 1, false).getId();

            final var request = new ChangeEmptyRequest(true);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정된 테이블입니다.");
        }

        @DisplayName("계산이 완료된 테이블이어야 한다")
        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void changeEmptyWithNotCompletedOrderTable(final OrderStatus orderStatus) {
            final var orderTableId = saveOrderTable(null, 1, false).getId();
            orderDao.save(newOrder(orderTableId, orderStatus, LocalDateTime.now()));

            final var request = new ChangeEmptyRequest(true);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("계산이 완료되지 않은 테이블입니다.");
        }
    }

    @Nested
    @ServiceTest
    class ChangeNumberOfGuestsTest {

        @DisplayName("주문 테이블의 손님 수를 변경한다")
        @ParameterizedTest
        @ValueSource(ints = {0, 10})
        void changeNumberOfGuests(final int expectedNumberOfGuests) {
            final var orderTable = saveOrderTable(null, 1, false);

            final var request = new ChangeNumberOfGuestsRequest(expectedNumberOfGuests);
            final var actual = tableService.changeNumberOfGuests(orderTable.getId(), request);

            assertThat(actual.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
        }

        @DisplayName("주문 테이블의 손님 수는 음수가 될 수 없다")
        @Test
        void changeNumberOfGuestsWithNegative() {
            final var negativeNumberOfGuests = -1;

            final var orderTableId = saveOrderTable(null, 1, false).getId();
            final var request = new ChangeNumberOfGuestsRequest(negativeNumberOfGuests);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("손님 수는 음수가 될 수 없습니다.");
        }

        @DisplayName("존재하는 주문 테이블이어야 한다")
        @Test
        void changeNumberOfGuestsWithNonExistOrderTable() {
            final var nonExistOrderTableId = 0L;

            final var request = new ChangeNumberOfGuestsRequest(2);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(nonExistOrderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블을 찾을 수 없습니다.");
        }

        @DisplayName("비어있는 주문 테이블이어야 한다")
        @Test
        void changeNumberOfGuestsWithEmptyOrderTable() {
            final var isEmpty = true;

            final var orderTableId = saveOrderTable(null, 1, isEmpty).getId();

            final var request = new ChangeNumberOfGuestsRequest(2);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 테이블이 비어있습니다.");
        }
    }

    @DisplayName("주문 테이블을 전체 조회한다")
    @Test
    void list() {
        final var expectedSize = 4;
        for (int i = 0; i < expectedSize; i++) {
            saveOrderTable(null, 1, false);
        }

        final List<OrderTableResponse> actual = tableService.list();

        assertThat(actual).hasSize(expectedSize);
    }

    private OrderTable saveOrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        final var orderTable = newOrderTable(tableGroupId, numberOfGuests, empty);
        return orderTableDao.save(orderTable);
    }
}
