package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {
    private void init() {
        menuGroupDao.save(new MenuGroup("한마리메뉴"));
        productDao.save(new Product(null, "후라이드", new Price(16000)));
        menuDao.save(메뉴_후라이드치킨());
        orderTableDao.save(new OrderTable(null, null, 0, false));
        menuProductDao.save(new MenuProduct(null, 1L, 1L, 1));
    }

    @DisplayName("주문을 추가하면 주문 목록에 추가된다.")
    @Test
    void create() {
        init();
        OrderResponse 주문 = orderService.create(주문요청_테이블1());

        검증_필드비교_값포함(assertThat(orderService.list()), 주문);
    }

    @DisplayName("주문들을 추가하면 주문 목록에 추가된다.")
    @Test
    void list() {
        init();
        OrderResponse 주문 = orderService.create(주문요청_테이블1());
        OrderResponse 주문2 = orderService.create(주문요청_테이블1());

        검증_필드비교_동일_목록(orderService.list(), List.of(주문, 주문2));
    }

    @DisplayName("하나 이상의 메뉴를 주문해야 한다.")
    @Test
    void create_noMenu() {
        init();
        List<OrderLineItemRequest> 빈_메뉴들 = new ArrayList<>();
        OrderRequest orderRequest = new OrderRequest(1L, 빈_메뉴들);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("하나 이상의 메뉴를 주문해야 한다.");
    }

    @DisplayName("주문한 메뉴들은 모두 DB에 등록되어야 한다.")
    @Test
    void create_invalidMenu() {
        init();
        long 잘못된_메뉴_ID = 500L;
        OrderRequest 주문 = new OrderRequest(1L, List.of(주문아이템요청(잘못된_메뉴_ID)));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문한 메뉴들은 모두 DB에 등록되어야 한다.");
    }

    @DisplayName("주문 테이블은 DB에 등록되어야 한다.")
    @Test
    void create_invalidTable() {
        init();
        long 잘못된_테이블_ID = 500L;
        OrderRequest 주문 = new OrderRequest(잘못된_테이블_ID, List.of(주문아이템요청_후라이드()));

        assertThatThrownBy(() -> orderService.create(주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 DB에 등록되어야 한다.");
    }

    @DisplayName("주문 테이블은 손님이 존재해야 한다.")
    @Test
    void create_noCustomer() {
        init();
        OrderTableResponse 테이블2 = tableService.create(빈테이블생성요청());

        assertThatThrownBy(() -> orderService.create(new OrderRequest(테이블2.getId(), List.of(주문아이템요청_후라이드()))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 손님이 존재해야 한다.");
    }

    @DisplayName("주문 상태를 변경하면 변경된 주문 상태가 반영된다.")
    @Test
    void changeOrderStatus() {
        init();
        OrderResponse 주문 = orderService.create(주문요청_테이블1());
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
        OrderResponse 주문 = orderService.create(주문요청_테이블1());
        OrderResponse 변경된_주문 = 주문_상태를_변경했다(주문.getId(), OrderStatus.COMPLETION);

        assertThatThrownBy(() -> 주문_상태를_변경했다(변경된_주문.getId(), MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 COMPLETION일 시 주문 상태를 변경할 수 없다.");
    }

    private OrderResponse 주문_상태를_변경했다(long id, OrderStatus orderStatus) {
        return orderService.changeOrderStatus(id, new ChangeOrderStatusRequest(orderStatus));
    }

    private OrderTableResponse 테이블_빈_여부_변경(Long id, boolean empty) {
        return tableService.changeEmpty(id, new OrderTableChangeEmptyRequest(empty));

    }
}
