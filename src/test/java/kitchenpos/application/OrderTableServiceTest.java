package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.exception.NumberOfGuestsNegativeException;
import kitchenpos.exception.OrderTableEmptyException;
import kitchenpos.exception.OrderTableNotEmptyException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.request.OrderTableEmptyRequest;
import kitchenpos.ui.request.OrderTableGuestsRequest;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Table Service 테스트")
@SpringBootTest
class OrderTableServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("OrderTable을 저장할 때 TableGroupId가 Null인 상태로 저장된다.")
    @Test
    void createWithTableGroupIdNull() {
        // given
        OrderTableRequest request = new OrderTableRequest(5, true);

        // when
        OrderTableResponse response = orderTableService.create(request);

        // then
        assertThat(orderTableRepository.findById(response.getId())).isPresent();
        assertThat(response.getTableGroup()).isNull();
        assertThat(response).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(response);
    }

    @DisplayName("모든 OrderTable을 조회한다.")
    @Test
    void findAll() {
        // given
        List<OrderTableResponse> beforeSavedOrderTable = orderTableService.list();

        beforeSavedOrderTable.add(orderTableService.create(new OrderTableRequest(5, true)));
        beforeSavedOrderTable.add(orderTableService.create(new OrderTableRequest(6, true)));
        beforeSavedOrderTable.add(orderTableService.create(new OrderTableRequest(7, true)));

        // when
        List<OrderTableResponse> afterSavedOrderTable = orderTableService.list();

        // then
        assertThat(afterSavedOrderTable).hasSize(beforeSavedOrderTable.size());
        assertThat(afterSavedOrderTable).usingRecursiveComparison()
            .isEqualTo(beforeSavedOrderTable);
    }

    @DisplayName("OrderTable의 상태를 Empty로 변경할 때")
    @Nested
    class ChangeStatusEmpty {

        @DisplayName("OrderTableId와 일치하는 OrderTable이 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void orderTableNotFoundException() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(TableGroup을_생성한다());
            OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다(5, false));
            OrderTableEmptyRequest request = new OrderTableEmptyRequest(true);

            // when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(-1L, request))
                .isExactlyInstanceOf(OrderTableNotFoundException.class);
        }

        @DisplayName("OrderTable에 TableGroupId가 등록되어 있는 경우 예외가 발생한다.")
        @Test
        void tableGroupIdNonNullException() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(TableGroup을_생성한다());
            OrderTable orderTable = OrderTable을_생성한다(5, false);
            orderTable.groupBy(tableGroup);
            orderTableRepository.save(orderTable);

            OrderTableEmptyRequest request = new OrderTableEmptyRequest(true);

            // when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                .isExactlyInstanceOf(OrderTableNotEmptyException.class);
        }

        @DisplayName("OrderTableId가 일치하고, COOKING 상태인 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existOrderTableIdMatchAndCookingStatusOrder() {
            // given
            OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다());
            Order order = orderRepository.save(Order를_생성한다(orderTable, COOKING));
            OrderTableEmptyRequest request = new OrderTableEmptyRequest(true);

            // when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                .isExactlyInstanceOf(InvalidOrderStatusException.class);
        }

        @DisplayName("OrderTableId가 일치하고, MEAL 상태인 Order가 존재하는 경우 예외가 발생한다.")
        @Test
        void existOrderTableIdMatchAndMealStatusOrder() {
            // given
            OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다());
            Order order = orderRepository.save(Order를_생성한다(orderTable, MEAL));
            OrderTableEmptyRequest request = new OrderTableEmptyRequest(true);

            // when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                .isExactlyInstanceOf(InvalidOrderStatusException.class);
        }

        @DisplayName("OrderTableId가 일치하고, COMPLETION 상태인 Order가 존재하는 경우 상태가 변경된다.")
        @Test
        void existOrderTableIdMatchAndCompletionStatusOrder() {
            // given
            OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다());
            Order order = orderRepository.save(Order를_생성한다(orderTable, COMPLETION));

            OrderTableEmptyRequest request = new OrderTableEmptyRequest(true);

            // when
            OrderTableResponse response = orderTableService.changeEmpty(orderTable.getId(), request);

            // then
            assertThat(response.isEmpty()).isTrue();
        }
    }

    @DisplayName("손님 수를 변경할 때")
    @Nested
    class ChangeNumberOfGuests {

        @DisplayName("손님 수가 음수면 예외가 발생한다.")
        @Test
        void numberOfGuestsNegativeException() {
            // given
            OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다());
            OrderTableGuestsRequest request = new OrderTableGuestsRequest(-1);

            // when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), request))
                .isExactlyInstanceOf(NumberOfGuestsNegativeException.class);
        }

        @DisplayName("orderTableId와 일치하는 OrderTable이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistOrderTableByIdException() {
            // given
            OrderTable newOrderTable = OrderTable을_생성한다();
            OrderTableGuestsRequest request = new OrderTableGuestsRequest(3);

            // when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(-1L, request))
                .isExactlyInstanceOf(OrderTableNotFoundException.class);
        }

        @DisplayName("OrderTable이 이미 비어있는 상태일 경우 예외가 발생한다.")
        @Test
        void alreadyEmptyStatusOrderTableException() {
            // given
            OrderTable orderTable = OrderTable을_생성한다();
            orderTable.changeEmpty(true);
            OrderTable savedOrderTable = orderTableRepository.save(orderTable);

            OrderTableGuestsRequest request = new OrderTableGuestsRequest(5);

            // when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                .isExactlyInstanceOf(OrderTableEmptyException.class);
        }

        @DisplayName("OrderTable이 비어있는 상태가 아니고, ID를 통해 조회할 수 있으며 손님 수가 음수가 아닌 경우 변경에 성공한다.")
        @Test
        void success() {
            // given
            OrderTable orderTable = orderTableRepository.save(OrderTable을_생성한다());
            OrderTableGuestsRequest request = new OrderTableGuestsRequest(5);

            // when
            OrderTableResponse response = orderTableService.changeNumberOfGuests(orderTable.getId(), request);

            assertThat(response.isEmpty()).isFalse();
            assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        }
    }

    private OrderTable OrderTable을_생성한다() {
        return OrderTable을_생성한다(10, false);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, boolean isEmpty) {
        return new OrderTable(numberOfGuests, isEmpty);
    }

    private TableGroup TableGroup을_생성한다() {
        return TableGroup.create();
    }

    private Order Order를_생성한다(OrderTable orderTable, OrderStatus orderStatus) {
        Order order = new Order(orderTable);
        order.changeStatus(orderStatus);

        return order;
    }
}