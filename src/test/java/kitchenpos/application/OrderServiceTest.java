package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @DisplayName("create 메서드는")
    @Nested
    class CreateTest {

        @Test
        void 주문을_생성하고_반환한다() {
            Long orderTableId = tableService.create(new OrderTable()).getId();
            Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderLineItems(List.of(generateOrderLineItem(1L, 1)));

            Order actual = orderService.create(order);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTableId),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(actual.getOrderedTime()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }

        @Test
        void 주문하는_메뉴_정보가_없는_경우_예외가_발생한다() {
            Long orderTableId = tableService.create(new OrderTable()).getId();
            Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderLineItems(List.of());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴가_포함된_경우_예외가_발생한다() {
            Long orderTableId = tableService.create(new OrderTable()).getId();
            Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderLineItems(List.of(generateOrderLineItem(9999999L, 1)));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블에서_주문한_경우_예외가_발생한다() {
            Order order = new Order();
            order.setOrderTableId(99999999L);
            order.setOrderLineItems(List.of(generateOrderLineItem(1L, 1)));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블에서_주문한_경우_예외가_발생한다() {
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            Long orderTableId = tableService.create(orderTable).getId();
            Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderLineItems(List.of(generateOrderLineItem(1L, 1)));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void list_메서드는_주문_목록을_조회한다() {
        Order order = new Order();
        order.setOrderTableId(tableService.create(new OrderTable()).getId());
        order.setOrderLineItems(List.of(generateOrderLineItem(1L, 1)));
        orderService.create(order);
        orderService.create(order);

        List<Order> orders = orderService.list();

        assertThat(orders).hasSizeGreaterThan(1);
    }

    @DisplayName("changeOrderStatus 메서드는")
    @Nested
    class ChangeOrderStatusTest {

        @Test
        void 주문의_상태를_수정하고_반환한다() {
            Long orderTableId = tableService.create(new OrderTable()).getId();
            Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderLineItems(List.of(generateOrderLineItem(1L, 1)));
            Long orderId = orderService.create(order).getId();

            Order orderDto = new Order();
            orderDto.setOrderStatus(OrderStatus.MEAL.name());
            Order actual = orderService.changeOrderStatus(orderId, orderDto);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTableId),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name()),
                    () -> assertThat(actual.getOrderedTime()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }

        @Test
        void 존재하지_않는_주문인_경우_예외를_발생시킨다() {
            assertThatThrownBy(() -> orderService.changeOrderStatus(99999L, new Order()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void COMPLETION_상태인_주문의_상태롤_변경하려는_경우_예외를_발생시킨다() {
            Order order = new Order();
            order.setOrderTableId(tableService.create(new OrderTable()).getId());
            order.setOrderLineItems(List.of(generateOrderLineItem(1L, 1)));
            Long orderId = orderService.create(order).getId();

            Order orderDto = new Order();
            orderDto.setOrderStatus(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(orderId, orderDto);

            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, orderDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderLineItem generateOrderLineItem(Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
