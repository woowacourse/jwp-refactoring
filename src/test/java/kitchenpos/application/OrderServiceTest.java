package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @DisplayName("주문을 추가하면 주문 목록에 추가된다.")
    @Test
    void create() {
        init();
        OrderResponse 주문 = orderService.create(주문요청_변환(주문_테이블1()));

        검증_필드비교_값포함(assertThat(orderService.list()), 주문);
    }

    @DisplayName("주문들을 추가하면 주문 목록에 추가된다.")
    @Test
    void list() {
        init();
        OrderResponse 주문 = orderService.create(주문요청_변환(주문_테이블1()));
        OrderResponse 주문2 = orderService.create(주문요청_변환(주문_테이블1()));

        검증_필드비교_동일_목록(orderService.list(), List.of(주문, 주문2));
    }

    @DisplayName("하나 이상의 메뉴를 주문해야 한다.")
    @Test
    void create_noMenu() {
        init();
        List<OrderLineItem> 빈_메뉴들 = new ArrayList<>();
        Order 주문 = 주문_테이블1(빈_메뉴들);

        assertThatThrownBy(() -> orderService.create(주문요청_변환(주문)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("하나 이상의 메뉴를 주문해야 한다.");
    }

    @DisplayName("주문한 메뉴들은 모두 DB에 등록되어야 한다.")
    @Test
    void create_invalidMenu() {
        init();
        long 잘못된_메뉴_ID = 500L;
        Order 주문 = new Order(1L, 1L, MEAL.name(),
                LocalDateTime.now(), List.of(주문아이템(잘못된_메뉴_ID)));

        assertThatThrownBy(() -> orderService.create(주문요청_변환(주문)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문한 메뉴들은 모두 DB에 등록되어야 한다.");
    }

    @DisplayName("주문 테이블은 DB에 등록되어야 한다.")
    @Test
    void create_invalidTable() {
        init();
        long 잘못된_테이블_ID = 500L;
        Order 주문 = new Order(1L, 잘못된_테이블_ID, MEAL.name(),
                LocalDateTime.now(), List.of(주문아이템_후라이드()));

        assertThatThrownBy(() -> orderService.create(주문요청_변환(주문)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 DB에 등록되어야 한다.");
    }

    @DisplayName("주문 테이블은 손님이 존재해야 한다.")
    @Test
    void create_noCustomer() {
        init();
        OrderTable 빈테이블_1 = orderTableDao.save(빈테이블_1());
        Order 주문 = new Order(1L, 빈테이블_1.getId(), MEAL.name(),
                LocalDateTime.now(), List.of(주문아이템_후라이드()));

        assertThatThrownBy(() -> orderService.create((주문요청_변환(주문))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 손님이 존재해야 한다.");
    }

    @DisplayName("주문 상태를 변경하면 변경된 주문 상태가 반영된다.")
    @Test
    void changeOrderStatus() {
        init();
        OrderResponse 주문 = orderService.create((주문요청_변환(주문_테이블1())));
        OrderResponse 변경된_주문 = 주문_상태를_변경했다(주문.getId(), MEAL);

        OrderResponse 주문_목록 = orderService.list().get(0);

        assertThat(주문_목록)
                .usingRecursiveComparison()
                .isEqualTo(변경된_주문);
    }

    @DisplayName("존재하는 주문의 상태만 수정할 수 있다.")
    @Test
    void changeOrderStatus_noOrder() {
        init();
        long 존재하지않는_주문_ID = 100L;
        assertThatThrownBy(() -> 주문_상태를_변경했다(존재하지않는_주문_ID, MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 아이디의 주문은 존재하지 않는다.");
    }

    @DisplayName("주문 상태가 COMPLETION일 시 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_noComplete() {
        init();
        OrderResponse 주문 = orderService.create((주문요청_변환(주문_테이블1())));
        OrderResponse 변경된_주문 = 주문_상태를_변경했다(주문.getId(), OrderStatus.COMPLETION);

        assertThatThrownBy(() -> 주문_상태를_변경했다(변경된_주문.getId(), MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 COMPLETION일 시 주문 상태를 변경할 수 없다.");
    }

    private List<OrderLineItemRequest> toOrderLineItemRequest(Order savedOrder) {
        return savedOrder.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(), orderLineItem.getQuantity())).collect(
                        Collectors.toList());
    }

    private OrderResponse 주문_상태를_변경했다(long id, OrderStatus orderStatus) {
        return orderService.changeOrderStatus(id, new ChangeOrderStatusRequest(orderStatus));
    }

    private OrderRequest 주문상태_요청_변경(Order order) {
        return new OrderRequest(order.getOrderTableId(), order.getOrderLineItems().stream()
                .map(item -> 변환(item, OrderLineItemRequest.class))
                .collect(Collectors.toList()));
    }


    private OrderResponse 주문상태_응답_변경(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                order.getOrderedTime(), order.getOrderLineItems().stream()
                .map(item -> 변환(item, OrderLineItemResponse.class))
                .collect(Collectors.toList()));
    }

    private void init() {
        menuGroupDao.save(new MenuGroup(null, "한마리메뉴"));
        productDao.save(new Product(null, "후라이드", BigDecimal.valueOf(16000)));
        menuDao.save(new Menu(null, "후라이드치킨", BigDecimal.valueOf(16000),
                1L,
                List.of(메뉴상품_후라이드())));
        orderTableDao.save(new OrderTable(null, null, 0, false));
        menuProductDao.save(new MenuProduct(null, 1L, 1L, 1));
    }
}
