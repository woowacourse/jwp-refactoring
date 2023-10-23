package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.orderException.IllegalOrderStatusException;
import kitchenpos.exception.orderTableException.IllegalOrderTableGuestNumberException;
import kitchenpos.exception.orderTableException.OrderTableNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableServiceTest extends ServiceBaseTest {

    @Autowired
    protected TableService tableService;
    @Autowired
    protected TableGroupService tableGroupService;

    @Test
    @DisplayName("비어있는 주문 테이블을 생성할 수 있다.")
    void create() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);

        //when
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        //then
        assertAll(
                () -> assertThat(orderTableResponse).isNotNull(),
                () -> assertThat(orderTableResponse.isEmpty()).isTrue(),
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuest())
        );
    }

    @Test
    @DisplayName("주문 테이블을 조회할 수 있다.")
    void list() {
        //given
        tableService.create(new OrderTableRequest(3, false));
        tableService.create(new OrderTableRequest(3, false));

        //when
        final List<OrderTableResponse> orderTableResponses = tableService.list();

        //then
        assertThat(orderTableResponses).hasSize(2);
    }

    @Test
    @DisplayName("주문 테이블을 빈 상태로 변경할 수 있다.")
    void changeEmpty() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        final OrderTableRequest orderTableRequest2 = new OrderTableRequest(3, true);

        //when
        final OrderTableResponse response = tableService.changeEmpty(orderTableResponse.getId(), orderTableRequest2);

        //then
        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블은 존재해야한다.")
    void changeEmptyValidOrderTable() {
        //when&then
        assertThatThrownBy(() -> tableService.changeEmpty(999L, null))
                .isInstanceOf(OrderTableNotFoundException.class)
                .hasMessage("OrderTable을 찾을 수 없습니다.");
    }

    @ParameterizedTest(name = "주문 테이블이 요리, 식사 상태이면 안된다.")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmptyValidStatus(final OrderStatus orderStatus) {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 2, false));
        final Order order = new Order(orderTable, null);
        order.changeOrderStatus(orderStatus);
        orderRepository.save(order);

        OrderTableRequest request = new OrderTableRequest(3, false);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(IllegalOrderStatusException.class)
                .hasMessage("잘못된 주문 상태입니다.");
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(10, true);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        final OrderTableRequest changedOrderTableRequest = new OrderTableRequest(1, true);

        //when
        final OrderTableResponse changedOrderTableResponse = tableService.changeNumberOfGuests(orderTableResponse.getId(), changedOrderTableRequest);

        //then
        assertThat(changedOrderTableResponse.getNumberOfGuests()).isEqualTo(changedOrderTableRequest.getNumberOfGuest());
    }

    @Test
    @DisplayName("주문 테이블의 손님 수는 0 이상이어야 한다.")
    void changeNumberOfGuestsOverZero() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(10, true);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        final OrderTableRequest changedOrderTableRequest = new OrderTableRequest(-1, true);

        //when&then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.getId(), changedOrderTableRequest))
                .isInstanceOf(IllegalOrderTableGuestNumberException.class)
                .hasMessage("잘못된 주문 테이블 게스트 숫자 설정입니다.");
    }

    @Test
    @DisplayName("주문 테이블은 존재해야 한다.")
    void changeNumberOfGuestValidOrderTable() {
        //given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(10, true);

        //when&then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(999L, orderTableRequest))
                .isInstanceOf(OrderTableNotFoundException.class)
                .hasMessage("OrderTable을 찾을 수 없습니다.");
    }
}
