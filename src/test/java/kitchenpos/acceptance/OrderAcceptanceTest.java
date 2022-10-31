package kitchenpos.acceptance;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.application.request.MenuGroupRequest;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.request.MenuRequest;
import kitchenpos.application.request.OrderLineItemRequest;
import kitchenpos.application.request.OrderRequest;
import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.application.request.ProductCreateRequest;
import kitchenpos.domain.OrderStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        OrderRequest request = createOrderRequest();

        // when, then
        _주문생성_Id반환(request);
    }

    @Test
    @DisplayName("전체 주문을 조회한다.")
    void list() {
        // given
        OrderRequest request = createOrderRequest();
        _주문생성_Id반환(request);

        // when, then
        get("/api/orders").assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("주문 상태를 바꾼다.")
    void changeOrderStatus() {
        // given
        OrderRequest request = createOrderRequest();
        long orderId = _주문생성_Id반환(request);
        OrderRequest changeRequest = createChangeRequest(orderId);

        // when, then
        put("/api/orders/" + orderId + "/order-status", changeRequest).assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    private OrderRequest createOrderRequest() {
        MenuGroupRequest menuGroup = new MenuGroupRequest(NO_ID, "세마리메뉴");
        long menuGroupId = _메뉴그룹등록_Id반환(menuGroup);

        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        long productId = _상품등록_Id반환(product);

        MenuRequest menu = new MenuRequest(NO_ID, "후라이드+후라이드+후라이드", new BigDecimal(48000), menuGroupId,
            List.of(new MenuProductRequest(NO_ID, NO_ID, productId, 3)));
        long menuId = _메뉴등록_Id반환(menu);

        OrderTableRequest orderTable = new OrderTableRequest(NO_ID, NO_ID, 1, false);
        long tableId = _테이블생성_Id반환(orderTable);

        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L, NO_ID, menuId, 1);
        return new OrderRequest(null, tableId, OrderStatus.COOKING.name(), List.of(orderLineItem));
    }

    private OrderRequest createChangeRequest(final long orderId) {
        return new OrderRequest(orderId, NO_ID, OrderStatus.MEAL.name(), List.of());
    }
}
