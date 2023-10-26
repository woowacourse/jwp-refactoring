package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.OrderChangeRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends ServiceTest {

    private OrderCreateRequest orderCreateRequest;
    private OrderChangeRequest orderChangeRequest;

    @BeforeEach
    void setUp() {
        orderCreateRequest = makeOrderCreateRequest();
        orderChangeRequest = makeOrderChangeRequest();
    }

    @Test
    void 주문을_생성한다() {
        tableService.changeEmpty(1L,new OrderTableChangeEmptyRequest(false));

        OrderResponse saved = orderService.create(orderCreateRequest);

        assertAll(
                () -> assertThat(saved.getOrderTableId()).isEqualTo(orderCreateRequest.getOrderTableId()),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString()),
                () -> assertThat(saved.getOrderLineItems().size()).isEqualTo(1L)
        );
    }

    @Test
    void 주문테이블_불가상태에서_주문생성시_예외발생() {
        assertThatThrownBy(
                () -> orderService.create(orderCreateRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_주문테이블_조회시_예외발생() {
        OrderCreateRequest request = makeWrongIdOrderCreateRequest();
        assertThatThrownBy(
                () -> orderService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_메뉴ID로_조회시_예외발생() {
        OrderCreateRequest request = makeWrongMenuOrderCreateRequest();
        assertThatThrownBy(
                () -> orderService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_주문을_조회한다() {
        tableService.changeEmpty(1L,new OrderTableChangeEmptyRequest(false));
        orderService.create(orderCreateRequest);
        orderService.create(orderCreateRequest);
        List<OrderResponse> responses = orderService.list();
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void 주문_상태를_변경한다() {
        tableService.changeEmpty(1L,new OrderTableChangeEmptyRequest(false));
        OrderResponse orderResponse = orderService.create(orderCreateRequest);
        OrderResponse response = orderService.changeOrderStatus(orderResponse.getId(), orderChangeRequest);
        assertThat(response.getOrderStatus().toString()).isEqualTo(orderChangeRequest.getOrderStatus());
    }

    private OrderCreateRequest makeOrderCreateRequest() {
        return new OrderCreateRequest(1L, List.of(new OrderLineItemCreateRequest(1L, 1L)));
    }

    private OrderCreateRequest makeWrongIdOrderCreateRequest() {
        return new OrderCreateRequest(10L, List.of(new OrderLineItemCreateRequest(1L, 1L)));
    }

    private OrderCreateRequest makeWrongMenuOrderCreateRequest() {
        return new OrderCreateRequest(1L, List.of(new OrderLineItemCreateRequest(10L, 1L)));
    }

    private OrderChangeRequest makeOrderChangeRequest() {
        return new OrderChangeRequest("MEAL");
    }
}
