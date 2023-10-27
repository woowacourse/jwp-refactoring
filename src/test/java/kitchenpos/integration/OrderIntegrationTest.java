package kitchenpos.integration;

import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.application.dto.OrderLineItemDto;
import kitchenpos.application.dto.OrderStatusDto;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
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
        final Menu menu = createMenu("오마카세", 900);
        final OrderTable orderTable = createTable();

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(new OrderLineItemDto(menu.getId(), 1L)));
        final HttpEntity<OrderCreateRequest> request = new HttpEntity<>(orderCreateRequest);

        // when
        final ResponseEntity<Order> response = testRestTemplate
                .postForEntity("/api/orders", request, Order.class);
        final Order createdOrder = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/orders/" + createdOrder.getId())
        );
    }

    @Test
    void 모든_주문_목록을_조회한다() {
        // given
        final Menu menu = createMenu("오마카세", 1000);
        final OrderTable orderTable = createTable();

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(new OrderLineItemDto(menu.getId(), 1L)));
        final HttpEntity<OrderCreateRequest> request = new HttpEntity<>(orderCreateRequest);

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
        final Menu menu = createMenu("오마카세", 900);
        final OrderTable orderTable = createTable();

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                List.of(new OrderLineItemDto(menu.getId(), 1L)));
        final HttpEntity<OrderCreateRequest> request = new HttpEntity<>(orderCreateRequest);

        final Long orderId = testRestTemplate
                .postForEntity("/api/orders", request, Order.class)
                .getBody()
                .getId();

        // 수정 주문 요청
        final OrderStatusDto statusRequest = new OrderStatusDto("COOKING");
        final HttpEntity<OrderStatusDto> updateRequest = new HttpEntity<>(statusRequest);

        // when
        final ResponseEntity<Order> response = testRestTemplate
                .exchange("/api/orders/" + orderId + "/order-status", HttpMethod.PUT, updateRequest, Order.class);
        final Order updatedOrder = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
        );
    }

    private Menu createMenu(final String name, final int price) {
        final Product chicken = createProduct("chicken", 500);
        final Product pizza = createProduct("pizza", 500);
        final MenuGroup menuGroup = createMenuGroup("외식");

        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(name, BigDecimal.valueOf(price), menuGroup.getId(),
                List.of(new MenuProductDto(chicken.getId(), 1L), new MenuProductDto(pizza.getId(), 1L)));
        final HttpEntity<MenuCreateRequest> request = new HttpEntity<>(menuCreateRequest);

        final MenuResponse response = testRestTemplate.postForEntity("/api/menus", request, MenuResponse.class)
                .getBody();

        return new Menu(response.getId(), response.getName(), new Price(response.getPrice()), null);
    }

    private Product createProduct(final String name, final int value) {
        final ProductCreateRequest product = new ProductCreateRequest(name, BigDecimal.valueOf(value));
        final HttpEntity<ProductCreateRequest> request = new HttpEntity<>(product);

        final ProductResponse response = testRestTemplate
                .postForEntity("/api/products", request, ProductResponse.class)
                .getBody();

        return new Product(response.getId(), response.getName(), new Price(response.getPrice()));
    }

    private MenuGroup createMenuGroup(final String name) {
        final MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest(name);
        final HttpEntity<MenuGroupCreateRequest> request = new HttpEntity<>(menuGroup);

        final MenuGroupResponse response = testRestTemplate
                .postForEntity("/api/menu-groups", request, MenuGroupResponse.class)
                .getBody();

        return new MenuGroup(response.getId(), response.getName());
    }

    private OrderTable createTable() {
        final OrderTableCreateRequest orderTable = new OrderTableCreateRequest(3, false);
        final HttpEntity<OrderTableCreateRequest> request = new HttpEntity<>(orderTable);

        final OrderTableResponse response = testRestTemplate
                .postForEntity("/api/tables", request, OrderTableResponse.class)
                .getBody();

        return new OrderTable(response.getId(), null, response.getNumberOfGuests(), response.isEmpty());
    }
}
