package kitchenpos.integration;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class OrderIntegrationTest extends IntegrationTest {

    private static final String ORDER_URL = "/api/orders";

    private Long orderTableId;

    private Long menuId;

    private OrderLineItem orderLineItem;


    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTable();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        orderTableId = savedOrderTable.getId();

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        Long menuGroupId = savedMenuGroup.getId();

        Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(new BigDecimal(19000));
        menu.setMenuGroupId(menuGroupId);
        Menu savedMenu = menuDao.save(menu);
        menuId = savedMenu.getId();

        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(1);
    }

    @DisplayName("Order 를 생성한다")
    @Test
    void create() {
        // given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(orderTableId);

        // when
        ResponseEntity<Order> orderResponseEntity = testRestTemplate.postForEntity(
                ORDER_URL,
                order,
                Order.class
        );
        HttpStatus statusCode = orderResponseEntity.getStatusCode();
        URI location = orderResponseEntity.getHeaders().getLocation();
        Order body = orderResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getOrderTableId()).isEqualTo(orderTableId);
        assertThat(body.getOrderLineItems().get(0))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(orderLineItem);
        assertThat(location).isEqualTo(URI.create(ORDER_URL + "/" + body.getId()));
    }

    @DisplayName("모든 Order 를 조회한다")
    @Test
    void list() {
        // given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        Long orderId = orderDao.save(order)
                .getId();

        // when
        ResponseEntity<Order[]> orderResponseEntity = testRestTemplate.getForEntity(
                ORDER_URL,
                Order[].class
        );
        HttpStatus statusCode = orderResponseEntity.getStatusCode();
        Order[] body = orderResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body)
                .hasSize(1)
                .extracting("id")
                .contains(orderId);
    }

    @DisplayName("Order 의 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        Long orderId = orderDao.save(order)
                .getId();

        // when
        order.setOrderStatus(OrderStatus.MEAL.name());

        ResponseEntity<Order> orderResponseEntity = testRestTemplate.exchange(
                ORDER_URL + "/{orderId}/order-status",
                HttpMethod.PUT,
                new HttpEntity<>(order),
                Order.class,
                orderId
        );
        HttpStatus statusCode = orderResponseEntity.getStatusCode();
        Order body = orderResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
