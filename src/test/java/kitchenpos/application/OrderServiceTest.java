package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.util.Pair;

@DisplayName("OrderService의")
class OrderServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("주문을 생성할 수 있다.")
        void create_validOrder_success() {
            // given
            final List<OrderLineItem> orderLineItems = getOrderLineItems();

            final Long orderTableId = saveOrderTable(10, false).getId();
            final Order expected = new Order();
            expected.setOrderTableId(orderTableId);
            expected.setOrderLineItems(orderLineItems);

            // when
            final Order actual = orderService.create(expected);

            // then
            softly.assertThat(actual.getOrderTableId()).isEqualTo(orderTableId);
            softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            softly.assertThat(actual.getOrderLineItems()).extracting("orderId", "menuId", "quantity")
                    .containsExactly(tuple(actual.getId(), orderLineItems.get(0).getMenuId(), 2L));
            softly.assertAll();
        }

        @Test
        @DisplayName("주문 항목은 비어있거나 0개일 수 없다.")
        void create_orderLineItemLessThenOne_exception() {
            // given
            final Long orderTableId = saveOrderTable(10, false).getId();
            final Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderLineItems(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("각각의 주문 항목의 메뉴 ID는 서로 중복되지 않아야 한다.")
        void create_duplicateMenu_exception() {
            // given
            final List<OrderLineItem> orderLineItems = getOrderLineItems();

            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(orderLineItems.get(0).getMenuId());
            orderLineItem.setQuantity(5L);
            orderLineItems.add(orderLineItem);

            final Long orderTableId = saveOrderTable(10, false).getId();
            final Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderLineItems(orderLineItems);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("모든 메뉴는 시스템에 등록된 상태여야 한다.")
        void create_notExistMenu_exception() {
            // given
            final List<OrderLineItem> orderLineItems = new ArrayList<>();
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(999L);
            orderLineItem.setQuantity(2L);
            orderLineItems.add(orderLineItem);

            final Long orderTableId = saveOrderTable(10, false).getId();
            final Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderLineItems(orderLineItems);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블은 시스템에 등록된 상태여야 한다.")
        void create_notExistOrderTable_exception() {
            // given
            final List<OrderLineItem> orderLineItems = getOrderLineItems();

            final Order order = new Order();
            order.setOrderTableId(999L);
            order.setOrderLineItems(orderLineItems);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블은 빈 테이블일 수 없다.")
        void create_emptyOrderTable_exception() {
            // given
            final List<OrderLineItem> orderLineItems = getOrderLineItems();

            final Long orderTableId = saveOrderTable(10, true).getId();
            final Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderLineItems(orderLineItems);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private List<OrderLineItem> getOrderLineItems() {
            final MenuGroup menuGroup = saveMenuGroup("치킨");
            final Product product1 = saveProduct("후라이드치킨");
            final Product product2 = saveProduct("양념치킨");
            final Menu menu = saveMenu("치킨세트", BigDecimal.ONE, menuGroup, Pair.of(product1, 1L), Pair.of(product2, 2L));

            final List<OrderLineItem> orderLineItems = new ArrayList<>();
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(2L);
            orderLineItems.add(orderLineItem);

            return orderLineItems;
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("주문 목록을 조회한다.")
        void list_savedOrders_success() {
            // given
            final Product chicken1 = saveProduct("간장치킨");
            final Product chicken2 = saveProduct("앙념치킨");
            final MenuGroup chickenMenuGroup = saveMenuGroup("치킨");
            final Menu chickenMenu = saveMenu("반반치킨", BigDecimal.valueOf(10_000), chickenMenuGroup,
                    Pair.of(chicken1, 2L), Pair.of(chicken2, 4L));

            final OrderTable orderTable1 = saveOrderTable(10, false);
            saveOrder(orderTable1, "COOKING", Pair.of(chickenMenu, 2L));

            final Product sushi1 = saveProduct("연어초밥");
            final Product sushi2 = saveProduct("광어초밥");
            final Product sushi3 = saveProduct("참치초밥");
            final MenuGroup sushiMenuGroup = saveMenuGroup("초밥");
            final Menu sushiMenu = saveMenu("모둠초밥", BigDecimal.valueOf(15_000), sushiMenuGroup,
                    Pair.of(sushi1, 3L), Pair.of(sushi2, 2L), Pair.of(sushi3, 1L));

            final OrderTable orderTable2 = saveOrderTable(2, false);
            saveOrder(orderTable2, "MEAL", Pair.of(sushiMenu, 3L));

            // when
            final List<Order> actual = orderService.list();

            // then
            assertThat(actual).extracting(Order::getOrderTableId, Order::getOrderStatus)
                    .contains(
                            tuple(orderTable1.getId(), OrderStatus.COOKING.name()),
                            tuple(orderTable2.getId(), OrderStatus.MEAL.name())
                    );
        }
    }

    @Nested
    @DisplayName("changeOrderStatus 메서드는")
    class ChangeOrderStatus {

        @ParameterizedTest(name = "{0} -> {1}")
        @DisplayName("주문 상태를 변경할 수 있다.")
        @CsvSource(value = {"COOKING,COMPLETION", "MEAL,COMPLETION", "MEAL,COOKING"})
        void changeOrderStatus_validOrderStatus_success(final String sourceOrderStatus,
                                                        final String targetOrderStatus) {
            // given
            final Menu chickenMenu = getMenu();
            final OrderTable orderTable = saveOrderTable(10, false);
            final Long orderId = saveOrder(orderTable, sourceOrderStatus, Pair.of(chickenMenu, 2L)).getId();

            final Order orderToUpdate = new Order();
            orderToUpdate.setOrderStatus(targetOrderStatus);

            // when
            final Order actual = orderService.changeOrderStatus(orderId, orderToUpdate);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(targetOrderStatus);
        }

        @Test
        @DisplayName("주문이 존재하지 않으면 주문 상태를 변경할 수 없다.")
        void changeOrderStatus_notExistOrder_exception() {
            // given
            final Order orderToUpdate = new Order();
            orderToUpdate.setOrderStatus("COMPLETION");

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(999L, orderToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("계산 완료인 주문은 주문 상태를 변경할 수 없다.")
        void changeOrderStatus_orderStatusIsCompletion_exception() {
            // given
            final Menu chickenMenu = getMenu();
            final OrderTable orderTable = saveOrderTable(10, false);
            final Long orderId = saveOrder(orderTable, OrderStatus.COMPLETION.name(), Pair.of(chickenMenu, 2L)).getId();

            final Order orderToUpdate = new Order();
            orderToUpdate.setOrderStatus(OrderStatus.MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, orderToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Menu getMenu() {
            final Product chicken1 = saveProduct("간장치킨");
            final Product chicken2 = saveProduct("앙념치킨");
            final MenuGroup chickenMenuGroup = saveMenuGroup("치킨");
            return saveMenu("반반치킨", BigDecimal.valueOf(10_000), chickenMenuGroup,
                    Pair.of(chicken1, 2L), Pair.of(chicken2, 4L));
        }
    }
}
