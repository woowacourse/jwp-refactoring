package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Order;
import kitchenpos.menu.domain.OrderLineItem;
import kitchenpos.menu.domain.OrderLineItemRepository;
import kitchenpos.menu.domain.OrderMenu;
import kitchenpos.menu.domain.OrderRepository;
import kitchenpos.menu.domain.OrderStatus;
import kitchenpos.menu.domain.OrderTable;
import kitchenpos.menu.domain.OrderTableRepository;
import kitchenpos.menu.dto.OrderRequest;
import kitchenpos.menu.dto.OrderRequest.OrderLineItemRequest;
import kitchenpos.menu.dto.OrderResponse;
import kitchenpos.menu.dto.OrderResponse.OrderLineItemResponse;
import kitchenpos.menu.dto.OrderStatusRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
    private ProductRepository productRepository;

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
    private List<OrderLineItem> orderLineItems;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        orderLineItems = new ArrayList<>();

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
        Product product = productRepository.save(new Product("후라이드치킨", BigDecimal.valueOf(16000)));
        menu = menuRepository.save(
            new Menu("후라이드치킨", BigDecimal.valueOf(16000), menuGroup.getId(),
                new MenuProducts(Collections.singletonList(new MenuProduct(product.getId(), 2L)))
            )
        );
        orderTable = orderTableRepository.save(new OrderTable(0, false));
        order = new Order(orderTable.getId());
        order = orderRepository.save(order);
        orderLineItems.add(orderLineItemRepository.save(
            new OrderLineItem(order, new OrderMenu(menu.getId(), menu.getName(), menu.getPrice()),
                1L)));
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
            .isEqualTo(OrderResponse.from(order, orderLineItems));
        assertThat(actualOrderLineItem.getOrderLineItems()).hasSize(1);
        assertThat(actualOrderLineItem.getOrderLineItems().get(0)).usingRecursiveComparison()
            .isEqualTo(OrderLineItemResponse.from(orderLineItems.get(0)));
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
        assertThat(response.getOrderLineItems()).hasSameSizeAs(orderLineItems);
        assertThat(response.getOrderLineItems().get(0)).usingRecursiveComparison()
            .isEqualTo(OrderLineItemResponse.from(orderLineItems.get(0)));
    }
}
