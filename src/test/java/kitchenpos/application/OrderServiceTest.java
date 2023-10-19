package kitchenpos.application;

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

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenu(menu);
            orderLineItem.setQuantity(1L);

            Order order = new Order();
            order.setOrderTable(orderTable);
            order.setOrderLineItems(List.of(orderLineItem));

            // when
            Order result = orderService.create(order);

            // then
            assertThat(result.getId()).isNotNull();
        }

        @Test
        void 주문항목이_비어있는_경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
            Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
            Product product = fixtures.상품_저장("왕햄버거", 10_000L);
            fixtures.메뉴_상품_저장(menu, product, 1L);
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenu(menu);
            orderLineItem.setQuantity(1L);

            Order order = new Order();
            order.setOrderTable(orderTable);
            order.setOrderLineItems(Collections.emptyList());

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문항목에_메뉴가_없는경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
            Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
            Product product = fixtures.상품_저장("왕햄버거", 10_000L);
            fixtures.메뉴_상품_저장(menu, product, 1L);
            OrderTable orderTable = fixtures.주문_테이블_저장();

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenu(null);
            orderLineItem.setQuantity(1L);

            Order order = new Order();
            order.setOrderTable(orderTable);
            order.setOrderLineItems(List.of(orderLineItem));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블인_경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
            Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
            Product product = fixtures.상품_저장("왕햄버거", 10_000L);
            fixtures.메뉴_상품_저장(menu, product, 1L);
            OrderTable orderTable = fixtures.빈_테이블_저장();

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenu(menu);
            orderLineItem.setQuantity(1L);

            Order order = new Order();
            order.setOrderTable(orderTable);
            order.setOrderLineItems(List.of(orderLineItem));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않는_경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("세트메뉴");
            Menu menu = fixtures.메뉴_저장(menuGroup, "햄버거세트", 10_000L);
            Product product = fixtures.상품_저장("왕햄버거", 10_000L);
            fixtures.메뉴_상품_저장(menu, product, 1L);

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenu(menu);
            orderLineItem.setQuantity(1L);

            Order order = new Order();
            order.setOrderTable(null);
            order.setOrderLineItems(List.of(orderLineItem));

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
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

        Order order = fixtures.주문_저장(orderTable);
        OrderLineItem orderLineItem = fixtures.주문_항목_저장(order, menu, 1L, 1L);

        // when
        List<Order> results = orderService.list();

        // then
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getOrderTable().getId()).isEqualTo(orderTable.getId());
        assertThat(results.get(0).getOrderLineItems().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderLineItem);
    }

}
