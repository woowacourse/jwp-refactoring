package kitchenpos.acceptance.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OrderAcceptanceTest extends AcceptanceTest {
    private OrderTable savedOrderTable;
    private Menu savedMenu;

    @BeforeEach
    void setUp() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(false);
        savedOrderTable = orderTableDao.save(table);

        MenuGroup recommendation = new MenuGroup();
        recommendation.setName("추천메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(recommendation);

        Product chicken = new Product();
        chicken.setName("강정치킨");
        chicken.setPrice(BigDecimal.valueOf(17000));
        Product savedChicken = productDao.save(chicken);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedChicken.getId());
        menuProduct.setQuantity(2);

        Menu halfHalf = new Menu();
        halfHalf.setName("후라이드+후라이드");
        halfHalf.setPrice(BigDecimal.valueOf(19000));
        halfHalf.setMenuGroupId(savedMenuGroup.getId());
        halfHalf.setMenuProducts(Arrays.asList(menuProduct));
        savedMenu = menuDao.save(halfHalf);
    }

    @DisplayName("주문 등록 성공")
    @Test
    void create() {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        ResponseEntity<Order> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                order,
                Order.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @DisplayName("주문 등록 실패 - 메뉴가 1개 미만")
    @Test
    void createByMenuLessThanOne() {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(Arrays.asList());

        ResponseEntity<Order> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                order,
                Order.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 등록 실패 - 잘못된 메뉴 아이디")
    @Test
    void createByIncorrectMenuId() {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(100L);
        orderLineItem.setQuantity(2);
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        ResponseEntity<Order> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                order,
                Order.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 등록 실패 - 잘못된 테이블 아이디")
    @Test
    void createByIncorrectTableId() {
        Order order = new Order();
        order.setOrderTableId(100L);
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        ResponseEntity<Order> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                order,
                Order.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 등록 실패 - 잘못된 테이블 상태")
    @Test
    void createByIncorrectTableState() {
        Order order = new Order();

        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        savedOrderTable = orderTableDao.save(table);
        order.setOrderTableId(savedOrderTable.getId());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        ResponseEntity<Order> responseEntity = testRestTemplate.postForEntity(
                "/api/orders",
                order,
                Order.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        ResponseEntity<List> responseEntity = testRestTemplate.getForEntity(
                "/api/orders",
                List.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(1);
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void changeOrderStatus() {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        Order savedOrder = orderDao.save(order);
        Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.MEAL.name());

        testRestTemplate.put("/api/orders/" + savedOrder.getId() + "/order-status", order2);

        savedOrder = orderDao.findById(savedOrder.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태 변경 실패 - 잘못된 주문 아이디")
    @Test
    void changeOrderStatusByIncorrectOrderId() {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);
        Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.MEAL.name());

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/orders/" + 100 + "/order-status",
                HttpMethod.PUT,
                new HttpEntity<>(order2),
                Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 상태 변경 실패 - 잘못된 주문 상태")
    @Test
    void changeOrderStatusByIncorrectOrderState() {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(2);
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        Order savedOrder = orderDao.save(order);
        Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.MEAL.name());

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/orders/" + savedOrder.getId() + "/order-status",
                HttpMethod.PUT,
                new HttpEntity<>(order2),
                Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
