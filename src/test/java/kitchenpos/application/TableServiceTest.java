package kitchenpos.application;

import kitchenpos.application.exceptions.NotExistedOrderTableException;
import kitchenpos.application.exceptions.OrderStatusNotCompletionException;
import kitchenpos.config.IsolatedTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exceptions.InvalidForChangingNumberOfGuestsException;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.ui.dto.ordertable.OrderTableRequest;
import kitchenpos.ui.dto.ordertable.OrderTableResponse;
import kitchenpos.ui.dto.ordertable.OrderTableResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends IsolatedTest {

    @Autowired
    private TableService service;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문 테이블 생성")
    @Test
    public void createTable() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    public void readTables() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        service.create(request);

        final OrderTableResponses responses = service.list();

        assertThat(responses.getOrderTableResponses()).hasSize(1);
        assertThat(responses.getOrderTableResponses().get(0).getNumberOfGuests()).isEqualTo(4);
        assertThat(responses.getOrderTableResponses().get(0).isEmpty()).isFalse();
    }

    @DisplayName("Empty 상태 변경 불가 - 주문 테이블을 찾지 못할 때")
    @Test
    public void changeEmptyFailNotExistedTable() {
        OrderTableRequest changeRequest = new OrderTableRequest(4, true);

        assertThatThrownBy(() -> service.changeEmpty(10L, changeRequest))
                .isInstanceOf(NotExistedOrderTableException.class);
    }

    @DisplayName("Empty 상태 변경 불가 - 조리중")
    @Test
    public void changeEmptyFailInvalidOrderStatus_Cooking() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);
        createOrderWithOrderStatusOf(OrderStatus.COOKING);

        OrderTableRequest changeRequest = new OrderTableRequest(4, true);

        assertThatThrownBy(() -> service.changeEmpty(response.getId(), changeRequest))
            .isInstanceOf(OrderStatusNotCompletionException.class);
    }

    @DisplayName("Empty 상태 변경 불가 - 식사중")
    @Test
    public void changeEmptyFailInvalidOrderStatus_Meal() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);
        createOrderWithOrderStatusOf(OrderStatus.MEAL);

        OrderTableRequest changeRequest = new OrderTableRequest(4, true);

        assertThatThrownBy(() -> service.changeEmpty(response.getId(), changeRequest))
                .isInstanceOf(OrderStatusNotCompletionException.class);
    }

    @DisplayName("Empty 상태 변경")
    @Test
    public void changeEmpty() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);
        createOrderWithOrderStatusOf(OrderStatus.COMPLETION);

        OrderTableRequest changeRequest = new OrderTableRequest(4, true);

        final OrderTableResponse changedTable = service.changeEmpty(response.getId(), changeRequest);

        assertThat(changedTable.isEmpty()).isTrue();
    }

    @DisplayName("인원 수 변경 실패 - 주문 테이블을 찾지 못할 때")
    @Test
    public void changeNumberOfGuestsFailNotExistedTable() {
        OrderTableRequest changeRequest = new OrderTableRequest(3, false);

        assertThatThrownBy(() -> service.changeNumberOfGuests(10L, changeRequest))
                .isInstanceOf(NotExistedOrderTableException.class);
    }

    @DisplayName("인원 수 변경 실패 - 변경할 인원 수가 음수일 때")
    @Test
    public void changeNumberOfGuestsFailNumberMinus() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);

        OrderTableRequest changeRequest = new OrderTableRequest(-3, false);

        assertThatThrownBy(() -> service.changeNumberOfGuests(response.getId(), changeRequest))
                .isInstanceOf(InvalidForChangingNumberOfGuestsException.class);
    }

    @DisplayName("인원 수 변경")
    @Test
    public void changeNumberOfGuests() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);

        OrderTableRequest changeRequest = new OrderTableRequest(3, false);

        final OrderTableResponse changedTable = service.changeNumberOfGuests(response.getId(), changeRequest);

        assertThat(changedTable.getNumberOfGuests()).isEqualTo(3);
    }

    private void createOrderWithOrderStatusOf(OrderStatus orderStatus) {
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        Order order = new Order(orderTable, orderStatus, LocalDateTime.now());
        orderRepository.save(order);
    }
}
