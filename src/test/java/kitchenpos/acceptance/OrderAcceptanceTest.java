package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

// Todo 여기 할 차례
class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTable tableA;
    private Menu 치킨_세트;
    private Menu 맥주;

    @BeforeEach
    void setUp() {
        // 영업준비
        tableA = createTable(0, true);
        OrderTable tableB = createTable(0, true);
        OrderTable tableC = createTable(0, true);

        MenuGroup 세트메뉴_그룹 = createMenuGroup("세트 메뉴");
        MenuGroup 음료수_그룹 = createMenuGroup("음료수");

        List<Product> 치킨세트_구성상품들 = new ArrayList<>();

        치킨세트_구성상품들.add(createProduct("후라이드 치킨", 10_000));
        치킨세트_구성상품들.add(createProduct("감자 튀김", 4_000));
        치킨세트_구성상품들.add(createProduct("매운 치즈 떡볶이", 5_000));

        치킨_세트 = createMenu("치킨 세트", 치킨세트_구성상품들, 16_000L, 세트메뉴_그룹.getId());

        Product beerProduct = createProduct("맥주 500CC", 4_000);
        맥주 = createMenu("맥주 500cc", Collections.singletonList(beerProduct),
            beerProduct.getPrice().longValue(), 음료수_그룹.getId());

        // tableA 에 손님 입장
        changeEmptyToFalse(tableA);
    }

    /**
     * Feature: 한 테이블의 주문을 관리한다.
     * <p>
     * Given 테이블, 메뉴가 존재한다. When 테이블에서 메뉴들을 주문한다. Then 메뉴들이 주문된다.
     */
    @Test
    @DisplayName("한 테이블의 주문을 관리한다.")
    void manageOrderOfOneTable() {
        // 주문한다
        List<OrderLineItemForTest> orderLineItems = new ArrayList<>();

        orderLineItems.add(new OrderLineItemForTest(치킨_세트.getId(), 1));
        orderLineItems.add(new OrderLineItemForTest(맥주.getId(), 4));

        Order orderResult = order(tableA, orderLineItems);

        assertThat(orderResult.getId()).isNotNull();
        assertThat(orderResult.getOrderStatus()).isEqualTo("COOKING");
        assertThat(orderResult.getOrderTableId()).isEqualTo(tableA.getId());

        for (OrderLineItemForTest orderLineItem : orderLineItems) {
            assertThatOrderLineItemIsIncludedInOrder(orderResult, orderLineItem);
        }
    }

    private Order order(OrderTable orderTable, List<OrderLineItemForTest> orderLineItems) {
        Map<String, Object> body = new HashMap<>();

        body.put("orderTableId", orderTable.getId());

        List<Map> orderLineItemsForRequest = new ArrayList<>();

        orderLineItems.forEach(orderLineItem -> {
            Map<String, Object> orderLineItemForRequest = new HashMap<>();

            orderLineItemForRequest.put("menuId", orderLineItem.getMenuId());
            orderLineItemForRequest.put("quantity", orderLineItem.getQuantity());

            orderLineItemsForRequest.add(Collections.unmodifiableMap(orderLineItemForRequest));
        });

        body.put("orderLineItems", orderLineItemsForRequest);

        return
            given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/api/orders")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(Order.class);
    }

    private void assertThatOrderLineItemIsIncludedInOrder(Order order,
        OrderLineItemForTest orderLineItem) {
        List<OrderLineItem> collect = order.getOrderLineItems().stream()
            .filter(responseOrderLineItem ->
                responseOrderLineItem.getMenuId().equals(orderLineItem.getMenuId()))
            .collect(Collectors.toList());

        assertThat(collect).hasSize(1);
        assertThat(collect.get(0).getOrderId()).isNotNull();
        assertThat(collect.get(0).getMenuId()).isEqualTo(orderLineItem.getMenuId());
        assertThat(collect.get(0).getQuantity()).isEqualTo(orderLineItem.getQuantity());
    }

    /**
     * Feature: 한 테이블 그룹의 주문을 관리한다.
     * <p>
     * Given 테이블, 메뉴가 존재한다. When 테이블에서 메뉴들을 주문한다. Then 메뉴들이 주문된다.
     */
    @Test
    @DisplayName("한 테이블 그룹의 주문을 관리한다.")
    void manageOrderOfGroupedTable() {

    }

    static class OrderLineItemForTest {

        private Long menuId;
        private int quantity;

        OrderLineItemForTest(Long menuId, int quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        Long getMenuId() {
            return menuId;
        }

        int getQuantity() {
            return quantity;
        }
    }
}
