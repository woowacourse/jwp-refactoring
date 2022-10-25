package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {

    @Nested
    class 주문_등록_메소드는 {

        @Test
        void 입력받은_주문을_저장한다() {
            // given
            OrderTable orderTable = 테이블을_저장한다(4);
            Menu menu = 메뉴를_저장한다("메뉴");
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(3L);

            Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            // when
            final Order savedOrder = orderService.create(order);

            // then
            List<Order> orders = orderService.list();
            assertThat(orders).extracting(Order::getId, Order::getOrderTableId, Order::getOrderStatus)
                    .contains(tuple(savedOrder.getId(), orderTable.getId(), OrderStatus.COOKING.name()));
        }

        @Test
        void 메뉴_목록이_비어있다면_예외가_발생한다() {
            // given
            OrderTable orderTable = 테이블을_저장한다(4);

            Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴를_주문하면_예외가_발생한다() {
            // given
            OrderTable orderTable = 테이블을_저장한다(4);
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(20L);
            orderLineItem.setQuantity(3L);

            Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문하는_테이블이_존재하지_않는다면_예외가_발생한다() {
            // given
            Menu menu = 메뉴를_저장한다("메뉴");
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(3L);

            Order order = new Order();
            order.setOrderTableId(20L);
            order.setOrderLineItems(List.of(orderLineItem));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문하는_테이블이_비어_있다면_예외가_발생한다() {
            // given
            OrderTable orderTable = 빈_테이블을_저장한다();
            Menu menu = 메뉴를_저장한다("메뉴");
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(3L);

            Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 주문_목록_조회_메소드는_모든_주문을_조회한다() {
        // given
        Order savedOrder1 = 주문을_저장한다();
        Order savedOrder2 = 주문을_저장한다();

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).extracting(Order::getId, Order::getOrderStatus)
                .contains(tuple(savedOrder1.getId(), OrderStatus.COOKING.name()),
                        tuple(savedOrder2.getId(), OrderStatus.COOKING.name()));
    }

    @Nested
    class 주문_상태_변경_메소드는 {

        @Test
        void 주문_상태를_변경한다() {
            // given
            Order savedOrder = 주문을_저장한다();

            Order order = new Order();
            order.setOrderStatus(OrderStatus.MEAL.name());

            // when
            orderService.changeOrderStatus(savedOrder.getId(), order);

            // then
            final Order findOrder = orderService.list()
                    .stream()
                    .filter(it -> it.getId().equals(savedOrder.getId()))
                    .findFirst()
                    .orElseThrow();

            assertThat(findOrder).extracting(Order::getOrderStatus)
                    .isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 주문_완료_상태인_주문을_변경하려고_하면_예외가_발생한다() {
            // given
            Order savedOrder = 주문을_저장한다();

            Order order1 = new Order();
            order1.setOrderStatus(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(savedOrder.getId(), order1);

            // when & then
            Order order2 = new Order();
            order2.setOrderStatus(OrderStatus.MEAL.name());
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), order2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}