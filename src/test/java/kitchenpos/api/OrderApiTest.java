package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.generator.MenuGenerator;
import kitchenpos.generator.MenuGroupGenerator;
import kitchenpos.generator.OrderGenerator;
import kitchenpos.generator.TableGenerator;
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
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @Autowired
    private JdbcTemplateMenuDao menuDao;

    @Autowired
    private JdbcTemplateOrderTableDao orderTableDao;

    @Autowired
    private JdbcTemplateOrderDao orderDao;

    @Autowired
    private JdbcTemplateOrderLineItemDao orderLineItemDao;

    private MenuGroup menuGroup;
    private Menu menu;
    private OrderTable orderTable;
    private Order order;
    private OrderLineItem orderLineItem;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();

        menuGroup = menuGroupDao.save(MenuGroupGenerator.newInstance("두마리메뉴"));
        menu = menuDao.save(MenuGenerator.newInstance("후라이드치킨", 16000, menuGroup.getId()));
        orderTable = orderTableDao.save(TableGenerator.newInstance(0, false));
        order = orderDao.save(OrderGenerator.newInstance(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));
        orderLineItem = orderLineItemDao.save(OrderGenerator.newOrderLineItem(order.getId(), menu.getId(), 1));
    }

    @DisplayName("주문 등록")
    @Test
    void postOrder() {
        OrderLineItem orderLineItemRequest = OrderGenerator.newOrderLineItem(menu.getId(), 1);
        Order request = OrderGenerator.newInstance(orderTable.getId(), Collections.singletonList(orderLineItemRequest));

        ResponseEntity<Order> responseEntity = testRestTemplate.postForEntity(BASE_URL, request, Order.class);
        Order response = responseEntity.getBody();

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
        ResponseEntity<Order[]> responseEntity = testRestTemplate.getForEntity(BASE_URL, Order[].class);
        Order[] response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).hasSize(1);
        Order actualOrderLineItem = response[0];
        assertThat(actualOrderLineItem).usingRecursiveComparison()
            .ignoringFields("orderLineItems")
            .isEqualTo(order);
        assertThat(actualOrderLineItem.getOrderLineItems()).hasSize(1);
        assertThat(actualOrderLineItem.getOrderLineItems().get(0)).usingRecursiveComparison()
            .isEqualTo(orderLineItem);
    }

    @DisplayName("주문 상태 수정")
    @Test
    void putOrderOrderStatus() {
        Order request = OrderGenerator.newInstance(OrderStatus.MEAL.name());
        ResponseEntity<Order> responseEntity = testRestTemplate.exchange(
            BASE_URL + "/" + order.getId() + "/order-status",
            HttpMethod.PUT,
            new HttpEntity<>(request, new HttpHeaders()),
            Order.class
        );
        Order response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getOrderStatus()).isEqualTo(request.getOrderStatus());
        assertThat(response).usingRecursiveComparison()
            .ignoringFields("orderStatus", "orderLineItems")
            .isEqualTo(order);
        assertThat(response.getOrderLineItems()).hasSize(1);
        assertThat(response.getOrderLineItems().get(0)).usingRecursiveComparison()
            .isEqualTo(orderLineItem);
    }
}
