package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.order.OrderService;
import kitchenpos.table.OrderTable;
import kitchenpos.product.Product;
import kitchenpos.fixtures.Fixtures;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
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
