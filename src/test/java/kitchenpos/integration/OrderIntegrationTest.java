package kitchenpos.integration;

import kitchenpos.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderIntegrationTest extends IntegrationTest {

    @Test
    void 주문_생성을_요청한다() {
        // given
        final Menu menu = createMenu("오마카세", 1000);
        final OrderTable orderTable = createTable();

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);

        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        final HttpEntity<Order> request = new HttpEntity<>(order);

        // when
        final ResponseEntity<Order> response = testRestTemplate
                .postForEntity("/api/orders", request, Order.class);
        final Order createdOrder = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/orders/" + createdOrder.getId()),
                () -> assertThat(createdOrder.getOrderLineItems()).hasSize(1)
        );
    }

    @Test
    void 모든_주문_목록을_조회한다() {
        // given
        final Menu menu = createMenu("오마카세", 1000);
        final OrderTable orderTable = createTable();

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);

        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        final HttpEntity<Order> request = new HttpEntity<>(order);

        testRestTemplate.postForEntity("/api/orders", request, Order.class);

        // when
        final ResponseEntity<Order[]> response = testRestTemplate
                .getForEntity("/api/orders", Order[].class);
        final List<Order> orders = Arrays.asList(response.getBody());

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(orders).hasSize(1)
        );
    }

    @Test
    void 주문_상태_변경을_요청한다() {
        // given
        // 최초 주문 요청
        final Menu menu = createMenu("오마카세", 1000);
        final OrderTable orderTable = createTable();

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);

        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        final HttpEntity<Order> request = new HttpEntity<>(order);

        final Long orderId = testRestTemplate
                .postForEntity("/api/orders", request, Order.class)
                .getBody()
                .getId();

        // 수정 주문 요청
        final Order toUpdateOrder = new Order();
        toUpdateOrder.setOrderStatus(OrderStatus.COOKING.name());
        final HttpEntity<Order> updateRequset = new HttpEntity<>(toUpdateOrder);

        // when
        final ResponseEntity<Order> response = testRestTemplate
                .exchange("/api/orders/" + orderId + "/order-status", HttpMethod.PUT, updateRequset, Order.class);
        final Order updatedOrder = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    private Menu createMenu(final String name, final int price) {
        final Product chicken = createProduct("chicken", 500);
        final Product pizza = createProduct("pizza", 500);
        final MenuGroup menuGroup = createMenuGroup("외식");

        final MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(chicken.getId());
        menuProduct1.setQuantity(1);

        final MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(pizza.getId());
        menuProduct2.setQuantity(1);

        final Menu menu = new Menu();
        menu.setName(name);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2));
        final HttpEntity<Menu> request = new HttpEntity<>(menu);

        return testRestTemplate
                .postForEntity("/api/menus", request, Menu.class)
                .getBody();
    }

    private Product createProduct(final String name, final int price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        final HttpEntity<Product> request = new HttpEntity<>(product);

        return testRestTemplate
                .postForEntity("/api/products", request, Product.class)
                .getBody();
    }

    private MenuGroup createMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        final HttpEntity<MenuGroup> request = new HttpEntity<>(menuGroup);

        return testRestTemplate
                .postForEntity("/api/menu-groups", request, MenuGroup.class)
                .getBody();
    }

    private OrderTable createTable() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(false);
        final HttpEntity<OrderTable> request = new HttpEntity<>(orderTable);

        return testRestTemplate
                .postForEntity("/api/tables", request, OrderTable.class)
                .getBody();
    }
}
