package kitchenpos.application;

import kitchenpos.config.IsolatedTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.ordertable.OrderTableRequest;
import kitchenpos.ui.dto.ordertable.OrderTableResponse;
import kitchenpos.ui.dto.ordertable.OrderTableResponses;
import org.assertj.core.util.Lists;
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
    private OrderDao orderDao;

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
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty 상태 변경 불가 - 조리중")
    @Test
    public void changeEmptyFailInvalidOrderStatus_Cooking() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);
        createOrderWithOrderStatusOf("COOKING", response.getId());

        OrderTableRequest changeRequest = new OrderTableRequest(4, true);

        assertThatThrownBy(() -> service.changeEmpty(response.getId(), changeRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty 상태 변경 불가 - 식사중")
    @Test
    public void changeEmptyFailInvalidOrderStatus_Meal() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);
        createOrderWithOrderStatusOf("MEAL", response.getId());

        OrderTableRequest changeRequest = new OrderTableRequest(4, true);

        assertThatThrownBy(() -> service.changeEmpty(response.getId(), changeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Empty 상태 변경")
    @Test
    public void changeEmpty() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);
        createOrderWithOrderStatusOf("COMPLETION", response.getId());

        OrderTableRequest changeRequest = new OrderTableRequest(4, true);

        final OrderTableResponse changedTable = service.changeEmpty(response.getId(), changeRequest);

        assertThat(changedTable.isEmpty()).isTrue();
    }

    @DisplayName("인원 수 변경 실패 - 주문 테이블을 찾지 못할 때")
    @Test
    public void changeNumberOfGuestsFailNotExistedTable() {
        OrderTableRequest changeRequest = new OrderTableRequest(3, false);

        assertThatThrownBy(() -> service.changeNumberOfGuests(10L, changeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원 수 변경 실패 - 변경할 인원 수가 음수일 때")
    @Test
    public void changeNumberOfGuestsFailNumberMinus() {
        OrderTableRequest request = new OrderTableRequest(4, false);
        final OrderTableResponse response = service.create(request);

        OrderTableRequest changeRequest = new OrderTableRequest(-3, false);

        assertThatThrownBy(() -> service.changeNumberOfGuests(response.getId(), changeRequest))
                .isInstanceOf(IllegalArgumentException.class);
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

    private void createOrderWithOrderStatusOf(String orderStatus, Long orderTableId) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(Lists.newArrayList());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);
    }
}
