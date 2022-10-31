package kitchenpos.application.order;

import kitchenpos.application.ServiceTest;
import kitchenpos.application.order.dto.request.table.ChangeEmptyRequest;
import kitchenpos.application.order.dto.request.table.ChangeNumberOfGuestsRequest;
import kitchenpos.application.order.dto.request.table.OrderTableRequest;
import kitchenpos.application.order.dto.response.OrderTableResponse;
import kitchenpos.domain.order.GuestCount;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import kitchenpos.domain.order.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class TableServiceTest {

    private final TableService tableService;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    @Autowired
    public TableServiceTest(final TableService tableService,
                            final OrderRepository orderRepository,
                            final OrderTableRepository orderTableRepository,
                            final TableGroupRepository tableGroupRepository
    ) {
        this.tableService = tableService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
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
            final var orderTableId = saveOrderTable(1, false).getId();

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
            final var orderTable = saveEmptyOrderTable();
            tableGroupRepository.save(new TableGroup(List.of(orderTable)));

            final var request = new ChangeEmptyRequest(true);
            final var orderTableId = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정된 테이블입니다.");
        }

        @DisplayName("계산이 완료된 테이블이어야 한다")
        @Test
        void changeEmptyWithNotCompletedOrderTable() {
            final var orderTableId = saveNonEmptyOrderTable(1).getId();
            saveOrder(orderTableId);

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
            final var orderTable = saveNonEmptyOrderTable(1);

            final var request = new ChangeNumberOfGuestsRequest(expectedNumberOfGuests);
            final var actual = tableService.changeNumberOfGuests(orderTable.getId(), request);

            assertThat(actual.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
        }

        @DisplayName("주문 테이블의 손님 수는 음수가 될 수 없다")
        @Test
        void changeNumberOfGuestsWithNegative() {
            final var negativeNumberOfGuests = -1;

            final var orderTableId = saveNonEmptyOrderTable(1).getId();
            final var request = new ChangeNumberOfGuestsRequest(negativeNumberOfGuests);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("손님 수는 음수가 아니어야 합니다.");
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
            final var orderTableId = saveEmptyOrderTable().getId();

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
        saveOrderTableAsTimes(expectedSize);

        final List<OrderTableResponse> actual = tableService.list();
        assertThat(actual).hasSize(expectedSize);
    }

    private void saveOrderTableAsTimes(final int times) {
        for (int i = 0; i < times; i++) {
            saveEmptyOrderTable();
        }
    }

    private OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final var orderTable = new OrderTable(new GuestCount(numberOfGuests), empty);
        return orderTableRepository.save(orderTable);
    }

    private OrderTable saveEmptyOrderTable() {
        return orderTableRepository.save(makeEmptyOrderTable());
    }

    private OrderTable saveNonEmptyOrderTable(final int numberOfGuests) {
        return orderTableRepository.save(makeNonEmptyOrderTable(numberOfGuests));
    }

    private Order saveOrder(final Long orderTableId) {
        final var order = new Order(orderTableId, makeSingleOrderLineItems());
        return orderRepository.save(order);
    }
}
