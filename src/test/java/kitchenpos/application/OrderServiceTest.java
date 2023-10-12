package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderServiceTest extends ServiceTestConfig {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final TableGroup tableGroup = saveTableGroup();
            final OrderTable orderTable = saveOrderTable(tableGroup);

            final Order orderInput = new Order();
            orderInput.setOrderTableId(orderTable.getId());

            final Menu menu = saveMenu(saveMenuGroup());
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(1L);

            orderInput.setOrderLineItems(List.of(orderLineItem));

            // when
            final Order actual = orderService.create(orderInput);

            // then
            // FIXME: equals&hashcode 적용
            assertSoftly(softly -> {
                softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                softly.assertThat(actual.getOrderTableId()).isEqualTo(orderInput.getOrderTableId());
//                softly.assertThat(actual.getOrderLineItems()).isEqualTo(orderInput.getOrderLineItems());
            });
        }

        @DisplayName("OrderLineItems 가 비어있으면 실패한다.")
        @Test
        void fail_if_OrderLineItems_is_empty() {
            // given
            final TableGroup tableGroup = saveTableGroup();
            final OrderTable orderTable = saveOrderTable(tableGroup);

            final Order orderInput = new Order();
            orderInput.setOrderTableId(orderTable.getId());
            orderInput.setOrderLineItems(Collections.emptyList());

            // then
            assertThatThrownBy(() -> orderService.create(orderInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("orderLineItems 에 존재하지 않는 메뉴가 있으면 실패한다.")
        @Test
        void fail_if_not_exist_menu_in_orderLineItems() {
            // given
            final TableGroup tableGroup = saveTableGroup();
            final OrderTable orderTable = saveOrderTable(tableGroup);

            final Order orderInput = new Order();
            orderInput.setOrderTableId(orderTable.getId());

            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(1111L);
            orderInput.setOrderLineItems(List.of(orderLineItem));

            // then
            assertThatThrownBy(() -> orderService.create(orderInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 OrderTableId 이면 실패한다.")
        @Test
        void fail_if_invalid_orderTableId() {
            // given
            final Order orderInput = new Order();
            orderInput.setOrderTableId(1111L);

            final Menu menu = saveMenu(saveMenuGroup());
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(1L);

            orderInput.setOrderLineItems(List.of(orderLineItem));

            // then
            assertThatThrownBy(() -> orderService.create(orderInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("OrderTable 이 주문을 등록할 수 없는 상태면 실패한다.")
        @Test
        void fail_if_orderTable_is_empty() {
            // given
            final TableGroup tableGroup = saveTableGroup();
            final OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(2);
            orderTable.setEmpty(true);
            orderTable.setTableGroupId(tableGroup.getId());

            final Order orderInput = new Order();
            orderInput.setOrderTableId(orderTable.getId());

            final Menu menu = saveMenu(saveMenuGroup());
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(1L);

            orderInput.setOrderLineItems(List.of(orderLineItem));

            // then
            assertThatThrownBy(() -> orderService.create(orderInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
