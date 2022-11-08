package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.order.MenuInfo;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.product.Product;
import kitchenpos.support.RequestBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문 관련 api")
public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // given
        final OrderTable savedTable = 손님이_있는_테이블_등록();
        final MenuInfo menuInfo = 메뉴_등록();

        // when
        final OrderRequest request = RequestBuilder.ofOrder(menuInfo, savedTable);
        final ValidatableResponse response = post("/api/orders", request);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @DisplayName("주문의 목록을 조회한다.")
    @Test
    void list() {
        // given, when
        final ValidatableResponse response = get("/api/orders");

        // then
        response.statusCode(HttpStatus.OK.value());
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        final OrderTable savedTable = 손님이_있는_테이블_등록();
        final MenuInfo menuInfo = 메뉴_등록();

        final OrderLineItem orderLineItem = OrderLineItem.ofUnsaved(null, menuInfo, 1);
        final Order savedOrder = dataSupport.saveOrder(savedTable.getId(), OrderStatus.COOKING, orderLineItem);

        // when
        final OrderStatusRequest request = RequestBuilder.ofOrderStatus(OrderStatus.MEAL);
        final ValidatableResponse response = put("/api/orders/" + savedOrder.getId() + "/order-status", request);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("orderStatus", Matchers.is(OrderStatus.MEAL.name()));
    }

    private OrderTable 손님이_있는_테이블_등록() {
        return dataSupport.saveOrderTable(2, false);
    }

    private MenuInfo 메뉴_등록() {
        final int price = 3500;
        final Product savedProduct = dataSupport.saveProduct("치킨마요", price);
        final MenuGroup savedMenuGroup = dataSupport.saveMenuGroup("추천 메뉴");

        final MenuProduct menuProduct = MenuProduct.ofUnsaved(null, savedProduct, 1L);
        return dataSupport.saveMenuAndGetInfo("치킨마요", price, savedMenuGroup.getId(), menuProduct);
    }
}
