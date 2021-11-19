package kitchenpos.acceptance.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OrderAcceptanceTest extends AcceptanceTest {
    private OrderTable savedOrderTable;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        OrderTable table = new OrderTable(null, 0, false);
        savedOrderTable = orderTableRepository.save(table);

        MenuGroup recommendation = new MenuGroup("추천메뉴");
        MenuGroup savedMenuGroup = menuGroupRepository.save(recommendation);

        Product chicken = new Product("강정치킨", BigDecimal.valueOf(17000));
        Product savedChicken = productRepository.save(chicken);

        Menu halfHalf = new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), savedMenuGroup);
        savedMenu = menuRepository.save(halfHalf);
    }

    @DisplayName("주문 등록 성공")
    @Test
    void create() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), Arrays.asList(orderLineItemRequest));

        ResponseEntity<OrderResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                orderRequest,
                OrderResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderResponse response = responseEntity.getBody();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getOrderTableId()).isEqualTo(savedOrderTable.getId());
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(response.getOrderLineItemResponses()).hasSize(1);
    }

    @DisplayName("주문 등록 실패 - 메뉴가 1개 미만")
    @Test
    void createByMenuLessThanOne() {
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), Arrays.asList());

        ResponseEntity<OrderResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                orderRequest,
                OrderResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 등록 실패 - 잘못된 메뉴 아이디")
    @Test
    void createByIncorrectMenuId() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(100L, 2);
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), Arrays.asList(orderLineItemRequest));

        ResponseEntity<OrderResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                orderRequest,
                OrderResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 등록 실패 - 잘못된 테이블 아이디")
    @Test
    void createByIncorrectTableId() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);
        OrderRequest orderRequest = new OrderRequest(100L, Arrays.asList(orderLineItemRequest));

        ResponseEntity<OrderResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                orderRequest,
                OrderResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 등록 실패 - 잘못된 테이블 상태")
    @Test
    void createByIncorrectTableState() {
        OrderTable table = new OrderTable(null, 0, true);
        savedOrderTable = orderTableRepository.save(table);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenu.getId(), 2);
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), Arrays.asList(orderLineItemRequest));

        ResponseEntity<OrderResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                orderRequest,
                OrderResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        Order order = new Order(savedOrderTable, OrderStatus.COOKING.name());
        orderRepository.save(order);

        ResponseEntity<List<Order>> responseEntity = testRestTemplate.exchange(
                "/api/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Order>>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Order> response = responseEntity.getBody();
        assertThat(response).hasSize(1);
        assertThat(response)
                .extracting(Order::getId)
                .containsExactlyInAnyOrder(1L);
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void changeOrderStatus() {
        Order order = new Order(savedOrderTable, OrderStatus.COOKING.name());
        orderRepository.save(order);
        Order savedOrder = orderRepository.save(order);
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());

        testRestTemplate.put("/api/orders/" + savedOrder.getId() + "/order-status", orderRequest);

        savedOrder = orderRepository.findById(savedOrder.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태 변경 실패 - 잘못된 주문 아이디")
    @Test
    void changeOrderStatusByIncorrectOrderId() {
        Order order = new Order(savedOrderTable, OrderStatus.COOKING.name());
        orderRepository.save(order);
        Order savedOrder = orderRepository.save(order);
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/orders/" + 100 + "/order-status",
                HttpMethod.PUT,
                new HttpEntity<>(orderRequest),
                Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 상태 변경 실패 - 잘못된 주문 상태")
    @Test
    void changeOrderStatusByIncorrectOrderState() {
        Order order = new Order(savedOrderTable, OrderStatus.COMPLETION.name());
        orderRepository.save(order);
        Order savedOrder = orderRepository.save(order);
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/orders/" + savedOrder.getId() + "/order-status",
                HttpMethod.PUT,
                new HttpEntity<>(orderRequest),
                Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
