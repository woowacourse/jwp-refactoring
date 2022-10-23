package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderService 클래스의")
class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("list 메서드는 모든 order를 조회한다.")
    void list() {
        // given
        MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
        Product product = productService.create(saveProduct("크림치킨", BigDecimal.valueOf(15000.00)));
        Menu menu1 = saveMenu("크림치킨", menuGroup, product);
        Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
        OrderTable orderTable1 = tableService.create(createOrderTable(2, false));
        OrderTable orderTable2 = tableService.create(createOrderTable(4, false));
        OrderTable orderTable3 = tableService.create(createOrderTable(1, false));

        saveOrder(orderTable1, menu1, menu2);
        saveOrder(orderTable2, menu1, menu2);
        saveOrder(orderTable3, menu1, menu2);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(3);
    }

    private Order saveOrder(OrderTable orderTable, Menu menu1, Menu menu2) {
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(menu1.getId());
        orderLineItem1.setQuantity(1);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(menu2.getId());
        orderLineItem2.setQuantity(2);

        Order order = new Order();
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        order.setOrderTableId(orderTable.getId());

        return orderService.create(order);
    }

    @Nested
    @DisplayName("changeOrderStatus 메서드는")
    class ChangeOrderStatus {

        @Test
        @DisplayName("order 상태를 업데이트한다.")
        void success() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = productService.create(saveProduct("크림치킨", BigDecimal.valueOf(15000.00)));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderTable orderTable = tableService.create(createOrderTable(2, false));
            Order savedOrder = saveOrder(orderTable, menu1, menu2);
            Order updateOrder = new Order();
            updateOrder.setOrderStatus("MEAL");
            orderService.changeOrderStatus(savedOrder.getId(), updateOrder);

            // when
            Order actual = orderService.changeOrderStatus(savedOrder.getId(), updateOrder);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
        }

        @Test
        @DisplayName("orderId에 해당하는 order가 존재하지 않는 경우 예외를 던진다.")
        void orderId_NotExist_ExceptionThrown() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = productService.create(saveProduct("크림치킨", BigDecimal.valueOf(15000.00)));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderTable orderTable = tableService.create(createOrderTable(2, false));
            Order order = saveOrder(orderTable, menu1, menu2);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(2L, order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("order의 상태가 COMPLETION인 경우 예외를 던진다.")
        void orderStatus_IsCompleted_ExceptionThrown() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = productService.create(saveProduct("크림치킨", BigDecimal.valueOf(15000.00)));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderTable orderTable = tableService.create(createOrderTable(2, false));
            Order savedOrder = saveOrder(orderTable, menu1, menu2);
            Order updateOrder = new Order();
            updateOrder.setOrderStatus("COMPLETION");
            orderService.changeOrderStatus(savedOrder.getId(), updateOrder);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), updateOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("order를 생성한다.")
        void success() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = productService.create(saveProduct("크림치킨", BigDecimal.valueOf(15000.00)));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderLineItem orderLineItem1 = new OrderLineItem();
            orderLineItem1.setMenuId(menu1.getId());
            orderLineItem1.setQuantity(1);
            OrderLineItem orderLineItem2 = new OrderLineItem();
            orderLineItem2.setMenuId(menu2.getId());
            orderLineItem2.setQuantity(2);
            OrderTable orderTable = tableService.create(createOrderTable(2, false));

            Order order = new Order();
            order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
            order.setOrderTableId(orderTable.getId());

            // when
            Order savedOrder = orderService.create(order);

            // then
            Optional<Order> actual = orderDao.findById(savedOrder.getId());
            assertThat(actual).isPresent();
        }

        @Test
        @DisplayName("OrderLineItem 리스트가 빈 리스트인 경우 예외를 던진다.")
        void orderLineItems_IsEmpty_ExceptionThrown() {
            // given
            Order order = new Order();
            order.setOrderLineItems(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("OrderLineItem의 개수와 OrderLineItem에 해당하는 메뉴의 개수가 다른 경우 예외를 던진다.")
        void orderLineItem_MenuNotExist_ExceptionThrown() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = productService.create(saveProduct("크림치킨", BigDecimal.valueOf(15000.00)));
            Menu menu = saveMenu("크림치킨", menuGroup, product);
            OrderLineItem orderLineItem1 = new OrderLineItem();
            orderLineItem1.setMenuId(menu.getId());
            orderLineItem1.setQuantity(1);
            OrderLineItem orderLineItem2 = new OrderLineItem();
            orderLineItem2.setMenuId(menu.getId());
            orderLineItem2.setQuantity(2);

            Order order = new Order();
            order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 존재하지 않는 경우 예외를 던진다.")
        void orderTable_NotExist_ExceptionThrown() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = productService.create(saveProduct("크림치킨", BigDecimal.valueOf(15000.00)));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderLineItem orderLineItem1 = new OrderLineItem();
            orderLineItem1.setMenuId(menu1.getId());
            orderLineItem1.setQuantity(1);
            OrderLineItem orderLineItem2 = new OrderLineItem();
            orderLineItem2.setMenuId(menu2.getId());
            orderLineItem2.setQuantity(2);

            Order order = new Order();
            order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
            order.setOrderTableId(1L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 empty인 경우 예외를 던진다.")
        void orderTable_IsEmpty_ExceptionThrown() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = productService.create(saveProduct("크림치킨", BigDecimal.valueOf(15000.00)));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderLineItem orderLineItem1 = new OrderLineItem();
            orderLineItem1.setMenuId(menu1.getId());
            orderLineItem1.setQuantity(1);
            OrderLineItem orderLineItem2 = new OrderLineItem();
            orderLineItem2.setMenuId(menu2.getId());
            orderLineItem2.setQuantity(2);
            OrderTable orderTable = tableService.create(createOrderTable(2, true));

            Order order = new Order();
            order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
            order.setOrderTableId(orderTable.getId());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
