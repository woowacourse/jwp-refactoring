package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderRequest.OrderLineItemRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderResponse.OrderLineItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OrderApiTest extends ApiTest {

    private static final String BASE_URL = "/api/orders";

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    private Menu menu;
    private OrderTable orderTable;
    private Order order;
    private OrderLineItem orderLineItem;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        menu = menuRepository.save(new Menu("후라이드치킨", BigDecimal.valueOf(16000), menuGroup));
        orderTable = orderTableRepository.save(new OrderTable(0, false));
        order = orderRepository.save(
            new Order(orderTable, Collections.singletonList(new OrderLineItem(menu, 1L)))
        );
        orderLineItem = orderLineItemRepository.save(order.getOrderLineItems().get(0));
    }

    @DisplayName("주문 등록")
    @Test
    void postOrder() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
        OrderRequest request = new OrderRequest(orderTable.getId(),
            Collections.singletonList(orderLineItemRequest));

        ResponseEntity<OrderResponse> responseEntity = testRestTemplate.postForEntity(
            BASE_URL, request, OrderResponse.class
        );
        OrderResponse response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(response.getOrderedTime()).isNotNull();
        assertThat(response.getOrderLineItems().get(0).getSeq()).isNotNull();
        assertThat(response.getOrderLineItems().get(0).getOrderId()).isEqualTo(response.getId());
        assertThat(response.getOrderLineItems().get(0)).usingRecursiveComparison()
            .ignoringFields("seq", "orderId")
            .isEqualTo(orderLineItemRequest);
    }

    @DisplayName("주문 조회")
    @Test
    void getOrder() {
        ResponseEntity<OrderResponse[]> responseEntity = testRestTemplate.getForEntity(
            BASE_URL, OrderResponse[].class
        );
        OrderResponse[] response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).hasSize(1);
        OrderResponse actualOrderLineItem = response[0];
        assertThat(actualOrderLineItem).usingRecursiveComparison()
            .ignoringFields("orderLineItems")
            .isEqualTo(OrderResponse.from(order));
        assertThat(actualOrderLineItem.getOrderLineItems()).hasSize(1);
        assertThat(actualOrderLineItem.getOrderLineItems().get(0)).usingRecursiveComparison()
            .isEqualTo(OrderLineItemResponse.from(orderLineItem));
    }

    @DisplayName("주문 상태 수정")
    @Test
    void putOrderOrderStatus() {
        OrderStatusRequest request = new OrderStatusRequest(OrderStatus.MEAL.name());
        ResponseEntity<OrderResponse> responseEntity = testRestTemplate.exchange(
            BASE_URL + "/" + order.getId() + "/order-status", HttpMethod.PUT,
            new HttpEntity<>(request, new HttpHeaders()), OrderResponse.class
        );
        OrderResponse response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getOrderStatus()).isEqualTo(request.getOrderStatus());
        assertThat(response).usingRecursiveComparison()
            .ignoringFields("orderStatus", "orderLineItems")
            .isEqualTo(order);
        assertThat(response.getOrderLineItems()).hasSize(1);
        assertThat(response.getOrderLineItems().get(0)).usingRecursiveComparison()
            .isEqualTo(OrderLineItemResponse.from(orderLineItem));
    }
}
