package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.support.RequestBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // given
        final OrderTable savedTable = 손님이_있는_테이블_등록();
        final Menu savedMenu = 메뉴_등록();

        // when
        final Order request = RequestBuilder.ofOrder(savedMenu, savedTable);
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

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1);
        final Order savedOrder = dataSupport.saveOrder(savedTable.getId(), OrderStatus.COOKING.name(), orderLineItem);

        // when
        final Order request = RequestBuilder.ofOrder(OrderStatus.MEAL);
        final ValidatableResponse response = put("/api/orders/" + savedOrder.getId() + "/order-status", request);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("orderStatus", Matchers.is(OrderStatus.MEAL.name()));
    }

    private OrderTable 손님이_있는_테이블_등록() {
        return dataSupport.saveOrderTable(2, false);
    }

    private Menu 메뉴_등록() {
        final Product savedProduct = dataSupport.saveProduct("치킨마요", new BigDecimal(3500));
        final MenuGroup savedMenuGroup = dataSupport.saveMenuGroup("추천 메뉴");

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        final Menu savedMenu =
                dataSupport.saveMenu("치킨마요", new BigDecimal(3500), savedMenuGroup.getId(), menuProduct);
        return savedMenu;
    }
}
