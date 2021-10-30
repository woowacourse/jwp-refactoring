package kitchenpos.integration;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.factory.MenuProductFactory;
import kitchenpos.factory.OrderLineItemFactory;
import kitchenpos.integration.annotation.IntegrationTest;
import kitchenpos.integration.templates.MenuGroupTemplate;
import kitchenpos.integration.templates.MenuTemplate;
import kitchenpos.integration.templates.OrderTableTemplate;
import kitchenpos.integration.templates.OrderTemplate;
import kitchenpos.integration.templates.ProductTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class OrderIntegrationTest {

    private static final String ORDER_URL = "/api/orders";

    @Autowired
    private MenuGroupTemplate menuGroupTemplate;

    @Autowired
    private ProductTemplate productTemplate;

    @Autowired
    private MenuTemplate menuTemplate;

    @Autowired
    private OrderTemplate orderTemplate;

    @Autowired
    private OrderTableTemplate orderTableTemplate;

    private Long orderTableId;

    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = orderTableTemplate.create(
                3,
                false
        ).getBody();
        assertThat(orderTable).isNotNull();
        orderTableId = orderTable.getId();

        MenuGroup menuGroup = menuGroupTemplate
                .create("추천메뉴")
                .getBody();
        assertThat(menuGroup).isNotNull();
        Long menuGroupId = menuGroup.getId();

        Product product = productTemplate.create(
                "강정치킨",
                new BigDecimal(17000)
        ).getBody();
        assertThat(product).isNotNull();
        Long productId = product.getId();

        MenuProduct menuProduct = MenuProductFactory.builder()
                .productId(productId)
                .quantity(2L)
                .build();

        Menu menu = menuTemplate.create(
                "후라이드+후라이드",
                new BigDecimal(19000),
                menuGroupId,
                Collections.singletonList(menuProduct)
        ).getBody();
        assertThat(menu).isNotNull();
        Long menuId = menu.getId();

        orderLineItem = OrderLineItemFactory.builder()
                .menuId(menuId)
                .quantity(1L)
                .build();
    }

    @DisplayName("Order 를 생성한다")
    @Test
    void create() {
        // given // when
        ResponseEntity<Order> orderResponseEntity = orderTemplate
                .create(
                        orderTableId,
                        Collections.singletonList(orderLineItem)
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
        Order order = orderTemplate.create(
                orderTableId,
                Collections.singletonList(orderLineItem)
        ).getBody();
        assertThat(order).isNotNull();
        Long orderId = order.getId();

        // when
        ResponseEntity<Order[]> orderResponseEntity = orderTemplate.list();
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
        Order order = orderTemplate.create(
                orderTableId,
                Collections.singletonList(orderLineItem)
        ).getBody();
        assertThat(order).isNotNull();
        Long orderId = order.getId();

        // when
        order.setOrderStatus(OrderStatus.MEAL.name());

        ResponseEntity<Order> orderResponseEntity = orderTemplate.changeOrderStatus(
                orderId,
                order
        );
        HttpStatus statusCode = orderResponseEntity.getStatusCode();
        Order body = orderResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
