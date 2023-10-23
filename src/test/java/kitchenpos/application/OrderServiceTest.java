package kitchenpos.application;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.Fixtures;
import kitchenpos.ui.dto.request.OrderLineRequest;
import kitchenpos.ui.dto.request.OrderRequest;
import kitchenpos.ui.dto.response.OrderResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    Fixtures fixtures;

    @Nested
    class 주문 {
        @Test
        void 주문을_한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
            Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
            Product product = fixtures.상품_저장("왕햄버거", 10_000L);
            fixtures.메뉴_상품_저장(menu, product, 1L);
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderLineRequest orderLineRequest = new OrderLineRequest(menu.getId(), 1L);
            OrderRequest request = new OrderRequest(orderTable.getId(), List.of(orderLineRequest));

            // when
            OrderResponse result = orderService.create(request);

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result.getOrderedTime()).isNotNull();
            assertThat(result.getOrderStatus()).isEqualTo("COOKING");
        }

        @Test
        void 주문항목에_메뉴가_없는경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
            Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
            Product product = fixtures.상품_저장("왕햄버거", 10_000L);
            fixtures.메뉴_상품_저장(menu, product, 1L);
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderLineRequest orderLineRequest = new OrderLineRequest(-1L, 1L);
            OrderRequest request = new OrderRequest(orderTable.getId(), List.of(orderLineRequest));

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않는_경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
            Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
            Product product = fixtures.상품_저장("왕햄버거", 10_000L);
            fixtures.메뉴_상품_저장(menu, product, 1L);
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderLineRequest orderLineRequest = new OrderLineRequest(menu.getId(), 1L);
            OrderRequest request = new OrderRequest(-1L, List.of(orderLineRequest));

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_변경한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
            Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
            Product product = fixtures.상품_저장("왕햄버거", 10_000L);
            fixtures.메뉴_상품_저장(menu, product, 1L);
            OrderTable orderTable = fixtures.주문_테이블_저장();

            Order order = fixtures.주문_저장(orderTable);
            fixtures.주문_항목_저장(order, menu, 1L, 1L);

            Order newOrder = new Order();
            newOrder.setOrderStatus(OrderStatus.MEAL);

            // when
            Order result = orderService.changeOrderStatus(order.getId(), newOrder);

            // then
            assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        }

        @Test
        void 계산완료_상태인_경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
            Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
            Product product = fixtures.상품_저장("왕햄버거", 10_000L);
            fixtures.메뉴_상품_저장(menu, product, 1L);
            OrderTable orderTable = fixtures.주문_테이블_저장();

            Order order = fixtures.주문_저장(orderTable, OrderStatus.COMPLETION);
            fixtures.주문_항목_저장(order, menu, 1L, 1L);

            Order newOrder = new Order();
            newOrder.setOrderStatus(OrderStatus.MEAL);

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), newOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_주문목록을_불러온다() {
        // given
        MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
        Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
        Product product = fixtures.상품_저장("왕햄버거", 10_000L);
        fixtures.메뉴_상품_저장(menu, product, 1L);
        OrderTable orderTable = fixtures.주문_테이블_저장();

        OrderLineRequest orderLineRequest = new OrderLineRequest(menu.getId(), 1L);
        OrderRequest request = new OrderRequest(orderTable.getId(), List.of(orderLineRequest));
        orderService.create(request);

        // when
        List<OrderResponse> results = orderService.list();

        // then
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getId()).isNotNull();
        assertThat(results.get(0).getOrderStatus()).isEqualTo("COOKING");
        assertThat(results.get(0).getOrderLineItemIds()).isNotNull();
    }
}
