package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusRequest;
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
        final Menu savedMenu = 메뉴_등록();

        // when
        final OrderRequest request = RequestBuilder.ofOrder(savedMenu, savedTable);
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
        final Menu savedMenu = 메뉴_등록();

        final OrderLineItem orderLineItem = OrderLineItem.ofNew(savedMenu.getId(), 1);
        final Order savedOrder = dataSupport.saveOrder(savedTable.getId(), OrderStatus.COOKING.name(), orderLineItem);

        // when
        final OrderStatusRequest request = RequestBuilder.ofOrder(OrderStatus.MEAL);
        final ValidatableResponse response = put("/api/orders/" + savedOrder.getId() + "/order-status", request);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("orderStatus", Matchers.is(OrderStatus.MEAL.name()));
    }

    private OrderTable 손님이_있는_테이블_등록() {
        return dataSupport.saveOrderTable(2, false);
    }

    private Menu 메뉴_등록() {
        final int price = 3500;
        final Product savedProduct = dataSupport.saveProduct("치킨마요", price);
        final MenuGroup savedMenuGroup = dataSupport.saveMenuGroup("추천 메뉴");

        final MenuProduct menuProduct = MenuProduct.ofNew(null, savedProduct.getId(), 1L);
        return dataSupport.saveMenu("치킨마요", price, savedMenuGroup.getId(), menuProduct);
    }
}
