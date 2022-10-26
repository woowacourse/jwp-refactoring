package kitchenpos.acceptance;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        Order order = createOrderFixture();

        // when, then
        _주문생성_Id반환(order);
    }

    @Test
    @DisplayName("전체 주문을 조회한다.")
    void list() {
        // given
        Order order = createOrderFixture();
        _주문생성_Id반환(order);

        // when, then
        get("/api/orders").assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("주문 상태를 바꾼다.")
    void changeOrderStatus() {
        // given
        Order order = createOrderFixture();
        long orderId = _주문생성_Id반환(order);
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when, then
        put("/api/orders/" + orderId + "/order-status", order).assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    private Order createOrderFixture() {
        MenuGroup menuGroup = new MenuGroup("세마리메뉴");
        long menuGroupId = _메뉴그룹등록_Id반환(menuGroup);

        Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        long productId = _상품등록_Id반환(product);

        Menu menu = new Menu("후라이드+후라이드+후라이드", new BigDecimal(48000), menuGroupId,
            List.of(new MenuProduct(productId, 3)));
        long menuId = _메뉴등록_Id반환(menu);

        OrderTable orderTable = new OrderTable(1, false);
        long tableId = _테이블생성_Id반환(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem(1L, menuId, 1);
        return new Order(tableId, List.of(orderLineItem));
    }
}
