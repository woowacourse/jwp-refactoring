package kitchenpos.integration;

import static io.restassured.RestAssured.given;
import static kitchenpos.utils.TestObjectUtils.createOrder;
import static kitchenpos.utils.TestObjectUtils.createOrderLineItem;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.application.OrderService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderRestControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @DisplayName("주문 생성")
    @Test
    void create() {
        OrderTable orderTable = tableService.create(new OrderTable());

        Map<String, Object> data = new HashMap<>();
        Map<String, String> orderLineItems = new HashMap<>();
        orderLineItems.put("menuId", "1");
        orderLineItems.put("quantity", "1");
        data.put("orderTableId", orderTable.getId());
        data.put("orderLineItems", Collections.singletonList(orderLineItems));

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            post("/api/orders").
        then().
            assertThat().
            statusCode(HttpStatus.CREATED.value()).
            header("Location", containsString("/api/orders/")).
            body("id", any(Integer.class)).
            body("orderTableId", equalTo(Math.toIntExact(orderTable.getId()))).
            body("orderStatus", equalTo("COOKING")).
            body("orderedTime", any(String.class)).
            body("orderLineItems", hasSize(1));
        // @formatter:on
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        OrderTable orderTable = tableService.create(new OrderTable());
        List<OrderLineItem> orderLineItems = Collections.singletonList(createOrderLineItem(1L, 1L));
        orderService.create(createOrder(orderTable.getId(), null, orderLineItems));

        // @formatter:off
        given().
        when().
            get("/api/orders").
        then().
            assertThat().
            statusCode(HttpStatus.OK.value()).
            body("$", hasSize(greaterThan(0)));
        // @formatter:on
    }

    @DisplayName("주문 상태 수정")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = tableService.create(new OrderTable());
        List<OrderLineItem> orderLineItems = Collections.singletonList(createOrderLineItem(1L, 1L));
        Order order = orderService.create(createOrder(orderTable.getId(), null, orderLineItems));

        Map<String, String> data = new HashMap<>();
        data.put("orderStatus", "MEAL");

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            put("/api/orders/" + order.getId() + "/order-status").
        then().
            assertThat().
            statusCode(HttpStatus.OK.value()).
            body("id", any(Integer.class)).
            body("orderTableId", any(Integer.class)).
            body("orderStatus", equalTo("MEAL")).
            body("orderedTime", any(String.class)).
            body("orderLineItems", hasSize(1));
        // @formatter:on
    }
}
