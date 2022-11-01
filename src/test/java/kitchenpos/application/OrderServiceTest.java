package kitchenpos.application;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.order.application.request.OrderLineItemRequest;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.request.OrderTableRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderServiceTest {

    private static final long QUANTITY = 1L;
    private static final long MENU_ID = 1L;
    private static final long ORDER_ID = 1L;
    private static final long SEQUENCE = 1L;

    private OrderService sut;
    private TableService tableService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        sut = new OrderService(menuRepository, orderRepository, orderTableRepository);
        tableService = new TableService(orderTableRepository);
    }

    @DisplayName("주문을 등록할 수 있다. (주문을 하면 조리 상태가 된다.)")
    @Test
    void create() {
        // given
        final OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest();
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1, false);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        final OrderRequest request = new OrderRequest(orderTableResponse.getId(), null, LocalDateTime.now(),
                List.of(orderLineItemRequest));

        // when
        final OrderResponse response = sut.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderStatus()).isEqualTo(COOKING.name());
        final Order foundOrder = orderRepository.findById(response.getId()).get();
        assertThat(foundOrder.getId()).isNotNull();
    }

    @DisplayName("주문한 메뉴 항목 개수와 실제 메뉴의 수가 일치해야한다.")
    @Test
    void orderLineItemSizeEqualToMenuSize() {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1, false);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        final OrderRequest request = new OrderRequest(orderTableResponse.getId(), null, LocalDateTime.now(),
                invalidQuantityOrderLineItemRequest());

        // when & then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 안된다.")
    @Test
    void createWithNonEmptyOrderTable() {
        // given
        final OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest();
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1, true);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        final OrderRequest request = new OrderRequest(orderTableResponse.getId(), null, LocalDateTime.now(),
                List.of(orderLineItemRequest));

        // when & then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태는 변경될 수 있다.")
    @Test
    void canChangeOrderStatus() {
        // given
        final OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest();
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1, false);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        final OrderRequest request = new OrderRequest(orderTableResponse.getId(), null, LocalDateTime.now(),
                List.of(orderLineItemRequest));
        final OrderResponse response = sut.create(request);

        // when
        final OrderRequest changeRequest = new OrderRequest(orderTableResponse.getId(), "COMPLETION",
                LocalDateTime.now(),
                List.of(orderLineItemRequest));
        final OrderResponse changedOrderResponse = sut.changeOrderStatus(response.getId(), changeRequest);

        // then
        assertThat(changedOrderResponse.getOrderStatus()).isEqualTo(COMPLETION.name());
    }

    @DisplayName("주문의 조회결과가 없는 경우 주문의 상태를 변경할 수 없다.")
    @Test
    void canNotChangeOrderStatusWhenEmptyOrder() {
        // given
        final long notExistOrderId = -1L;
        final OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest();
        final OrderTableRequest orderTableRequest = new OrderTableRequest(null, 1, false);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        final OrderLineItem orderLineItem = toOrderLineItem(orderLineItemRequest);
        final Order order = new Order(notExistOrderId, orderTableResponse.getId(), COOKING, LocalDateTime.now(), List.of(orderLineItem));
        final OrderRequest changeRequest = new OrderRequest(orderTableResponse.getId(), "COMPLETION",
                LocalDateTime.now(),
                List.of(orderLineItemRequest));

        // when & then
        assertThatThrownBy(() -> sut.changeOrderStatus(order.getId(), changeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 전체 정보를 조회할 수 있다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable = OrderTable.of(1, false);
        final OrderTable anotherOrderTable = OrderTable.of(1, false);

        final OrderRequest orderRequest1 = createdOrderRequest(orderTable, createOrderLineItemRequest());
        final OrderRequest orderRequest2 = createdOrderRequest(anotherOrderTable, createOrderLineItemRequest());

        final OrderResponse orderResponse1 = sut.create(orderRequest1);
        final OrderResponse orderResponse2 = sut.create(orderRequest2);

        // when
        final List<OrderResponse> orders = sut.list();

        // then
        assertThat(orders)
                .hasSize(2)
                .extracting(OrderResponse::getId, OrderResponse::getOrderTableId, OrderResponse::getOrderStatus)
                .containsExactlyInAnyOrder(
                        tuple(orderResponse1.getId(), orderResponse1.getOrderTableId(),
                                orderResponse1.getOrderStatus()),
                        tuple(orderResponse2.getId(), orderResponse2.getOrderTableId(), orderResponse2.getOrderStatus())
                );
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest orderLineItemRequest) {
        final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId()).get();
        return new OrderLineItem(menu.getId(), orderLineItemRequest.getQuantity());
    }

    private OrderLineItemRequest createOrderLineItemRequest() {
        return new OrderLineItemRequest(SEQUENCE, ORDER_ID, MENU_ID, QUANTITY);
    }

    private OrderRequest createdOrderRequest(final OrderTable orderTable,
                                             final OrderLineItemRequest orderLineItemRequest) {
        final OrderTableRequest orderTableRequest = new OrderTableRequest(orderTable);
        final OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        return new OrderRequest(orderTableResponse.getId(), null, LocalDateTime.now(), List.of(orderLineItemRequest));
    }

    private static List<OrderLineItemRequest> invalidQuantityOrderLineItemRequest() {
        final OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1L, 1L, 1L);
        final OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1L, 1L, 1L);
        return List.of(orderLineItemRequest1, orderLineItemRequest2);
    }
}
