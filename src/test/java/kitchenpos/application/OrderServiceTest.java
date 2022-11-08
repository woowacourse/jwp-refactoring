package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.updatedOrderStatusRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderService 클래스의")
class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("list 메서드는 모든 order를 조회한다.")
    void list() {
        // given
        MenuGroup menuGroup = saveMenuGroup("반마리치킨");
        Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
        Menu menu1 = saveMenu("크림치킨", menuGroup, product);
        Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
        OrderTable orderTable1 = saveOrderTable(2, false);
        OrderTable orderTable2 = saveOrderTable(4, false);
        OrderTable orderTable3 = saveOrderTable(1, false);

        saveOrder(orderTable1, menu1, menu2);
        saveOrder(orderTable2, menu1, menu2);
        saveOrder(orderTable3, menu1, menu2);
        entityManager.flush();

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(3);
    }

    @Nested
    @DisplayName("changeOrderStatus 메서드는")
    class ChangeOrderStatus {

        @Test
        @DisplayName("order 상태를 업데이트한다.")
        void success() {
            // given
            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderTable orderTable = saveOrderTable(2, false);
            Order savedOrder = saveOrder(orderTable, menu1, menu2);
            OrderStatusRequest request = updatedOrderStatusRequest("MEAL");
            entityManager.flush();

            // when
            Order actual = orderService.changeOrderStatus(savedOrder.getId(), request);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
        }

        @Test
        @DisplayName("orderId에 해당하는 order가 존재하지 않는 경우 예외를 던진다.")
        void orderId_NotExist_ExceptionThrown() {
            // given
            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            saveMenu("크림치킨", menuGroup, product);
            saveMenu("크림어니언치킨", menuGroup, product);
            saveOrderTable(2, false);
            OrderStatusRequest request = updatedOrderStatusRequest("MEAL");

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(2L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("order의 상태가 COMPLETION인 경우 예외를 던진다.")
        void orderStatus_IsCompleted_ExceptionThrown() {
            // given
            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderTable orderTable = saveOrderTable(2, false);
            Order savedOrder = saveOrder(orderTable, menu1, menu2);
            OrderStatusRequest request = updatedOrderStatusRequest("COMPLETION");
            entityManager.flush();
            orderService.changeOrderStatus(savedOrder.getId(), request);

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
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
            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderTable orderTable = saveOrderTable(2, false);
            OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu1.getId(), 1);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu2.getId(), 2);
            OrderRequest request = new OrderRequest(orderTable.getId(),
                    List.of(orderLineItemRequest1, orderLineItemRequest2));

            // when
            Order savedOrder = orderService.create(request);
            entityManager.flush();

            // then
            Optional<Order> actual = orderRepository.findById(savedOrder.getId());
            assertThat(actual).isPresent();
        }

        @Test
        @DisplayName("OrderLineItem 리스트가 빈 리스트인 경우 예외를 던진다.")
        void orderLineItems_IsEmpty_ExceptionThrown() {
            // given
            OrderTable orderTable = saveOrderTable(2, false);
            OrderRequest request = new OrderRequest(orderTable.getId(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("OrderLineItem의 개수와 OrderLineItem에 해당하는 메뉴의 개수가 다른 경우 예외를 던진다.")
        void orderLineItem_MenuNotExist_ExceptionThrown() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu = saveMenu("크림치킨", menuGroup, product);
            OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu.getId(), 1);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu.getId(), 2);
            OrderTable orderTable = saveOrderTable(2, false);
            OrderRequest request = new OrderRequest(orderTable.getId(),
                    List.of(orderLineItemRequest1, orderLineItemRequest2));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 존재하지 않는 경우 예외를 던진다.")
        void orderTable_NotExist_ExceptionThrown() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu1.getId(), 1);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu2.getId(), 2);
            OrderRequest request = new OrderRequest(1L, List.of(orderLineItemRequest1, orderLineItemRequest2));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 empty인 경우 예외를 던진다.")
        void orderTable_IsEmpty_ExceptionThrown() {
            // given
            MenuGroup menuGroup = menuGroupService.create(saveMenuGroup("반마리치킨"));
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
            Menu menu1 = saveMenu("크림치킨", menuGroup, product);
            Menu menu2 = saveMenu("크림어니언치킨", menuGroup, product);
            OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu1.getId(), 1);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu2.getId(), 2);
            OrderTable orderTable = saveOrderTable(2, true);
            OrderRequest request = new OrderRequest(orderTable.getId(),
                    List.of(orderLineItemRequest1, orderLineItemRequest2));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
