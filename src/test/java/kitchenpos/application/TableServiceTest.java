package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.request.table.OrderTableEmptyRequest;
import kitchenpos.application.dto.request.table.OrderTableGuestsRequest;
import kitchenpos.application.dto.request.table.OrderTableRequest;
import kitchenpos.application.dto.response.table.OrderTableResponse;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItem.OrderLineItemBuilder;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.InvalidNumberOfGuestsException;
import kitchenpos.exception.NotChangeOrderTableException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private static OrderTable orderTable;

    @BeforeEach
    void setUp() {
        OrderLineItem orderLineItem = new OrderLineItemBuilder()
                .setQuantity(4)
                .build();

        Order order = new Order.OrderBuilder()
                .setOrderStatus(OrderStatus.COMPLETION.name())
                .setOrderLineItems(List.of(orderLineItem))
                .build();

        orderTable = new OrderTable.OrderTableBuilder()
                .setNumberOfGuests(5)
                .setOrders(List.of(order))
                .setEmpty(false)
                .build();
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, true);
        //when
        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        //then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(4);
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    @DisplayName("테이블 목록을 가져올 수 있다.")
    @Test
    void list() {
        //given
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(2, true);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(4, true);

        OrderTableResponse orderTableResponse1 = tableService.create(orderTableRequest1);
        OrderTableResponse orderTableResponse2 = tableService.create(orderTableRequest2);

        //when
        List<OrderTableResponse> orderTableResponses = tableService.list();
        //then
        assertThat(orderTableResponses).hasSize(2);
        assertThat(orderTableResponses.get(0).getNumberOfGuests()).isEqualTo(orderTableResponse1.getNumberOfGuests());
        assertThat(orderTableResponses.get(1).getNumberOfGuests()).isEqualTo(orderTableResponse2.getNumberOfGuests());
    }

    @DisplayName("테이블 상태를 빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        //given
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        OrderTableEmptyRequest changeOrderTableEmptyRequest = new OrderTableEmptyRequest(true);

        //when
        OrderTableResponse response = tableService
                .changeEmpty(savedOrderTable.getId(), changeOrderTableEmptyRequest);
        //then
        assertThat(response.isEmpty()).isTrue();
    }


    @DisplayName("테이블 상태 변경 실패 - 주문 상태가 요리 또는 식사 상태인 경우")
    @Test
    void changeEmptyFailInvalidOrderStatus() {
        //given
        OrderLineItem orderLineItem = new OrderLineItemBuilder()
                .setQuantity(4)
                .build();

        Order order = new Order.OrderBuilder()
                .setOrderStatus(OrderStatus.MEAL.name())
                .setOrderLineItems(List.of(orderLineItem))
                .build();

        orderTable = new OrderTable.OrderTableBuilder()
                .setNumberOfGuests(5)
                .setOrders(List.of(order))
                .setEmpty(false)
                .build();

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTableEmptyRequest(true)))
                .isInstanceOf(OrderStatusException.class)
                .hasMessage("주문 상태가 요리 또는 식사 상태입니다.");
    }

    @DisplayName("테이블의 인원 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        //when
        OrderTableResponse orderTableResponse = tableService
                .changeNumberOfGuests(savedOrderTable.getId(), new OrderTableGuestsRequest(2));
        //then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(2);
    }
//
    @DisplayName("테이블의 인원 수 변경 실패 - 손님의 수가 음수일 경우")
    @Test
    void changeNumberOfGuestsFailInvalidGuestNumber() {
        //given
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        //when
        //then
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(savedOrderTable.getId(), new OrderTableGuestsRequest(-2)))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @DisplayName("테이블의 인원 수 변경 실패 - 변경할 테이블 번호가 없을 경우")
    @Test
    void changeNumberOfGuestsFailInvalidTableNumber() {
        //given
        //when
        //then
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(1000L, new OrderTableGuestsRequest(2)))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @DisplayName("테이블의 인원 수 변경 실패 - 변경할 테이블이 빈 테이블일 경우")
    @Test
    void changeNumberOfGuestsFailEmptyTable() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, true);
        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);
        //when
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.getId(), new OrderTableGuestsRequest(2)))
                .isInstanceOf(NotChangeOrderTableException.class);
    }
}
