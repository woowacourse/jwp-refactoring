package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("OrderService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("Order에 속한 OrderLineItem 컬렉션이 비어있는 경우")
        @Nested
        class Context_orderLineItem_empty {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Order order = new Order();
                order.setOrderLineItems(Collections.emptyList());

                // when. then
                assertThatCode(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문에 포함된 주문 항목이 없습니다.");
            }
        }

        @DisplayName("Order에 속한 OrderLineItem이 속한 Menu들이 DB에 저장되지 않은 경우")
        @Nested
        class Context_menu_not_persisted {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Order order = new Order();
                OrderLineItem orderLineItem = new OrderLineItem();
                OrderLineItem orderLineItem2 = new OrderLineItem();
                orderLineItem.setMenuId(1L);
                orderLineItem2.setMenuId(2L);
                order.setOrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));
                given(menuDao.countByIdIn(anyList())).willReturn(1L);

                // when. then
                assertThatCode(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목이 속한 Menu가 DB에 존재하지 않습니다.");

                verify(menuDao, times(1)).countByIdIn(anyList());
            }
        }

        @DisplayName("Order가 속한 OrderTable을 조회할 수 없는 경우")
        @Nested
        class Context_order_table_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Order order = new Order();
                OrderLineItem orderLineItem = new OrderLineItem();
                OrderLineItem orderLineItem2 = new OrderLineItem();
                orderLineItem.setMenuId(1L);
                orderLineItem2.setMenuId(2L);
                order.setOrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));
                order.setOrderTableId(1L);
                given(menuDao.countByIdIn(anyList())).willReturn(2L);
                given(orderTableDao.findById(1L)).willReturn(Optional.empty());

                // when. then
                assertThatCode(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 OrderTable ID입니다.");

                verify(menuDao, times(1)).countByIdIn(anyList());
                verify(orderTableDao, times(1)).findById(1L);
            }
        }

        @DisplayName("Order가 속한 OrderTable이 비어있는 경우")
        @Nested
        class Context_order_table_empty {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Order order = new Order();
                OrderLineItem orderLineItem = new OrderLineItem();
                OrderLineItem orderLineItem2 = new OrderLineItem();
                OrderTable orderTable = new OrderTable();
                orderLineItem.setMenuId(1L);
                orderLineItem2.setMenuId(2L);
                order.setOrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));
                order.setOrderTableId(1L);
                orderTable.setEmpty(true);
                given(menuDao.countByIdIn(anyList())).willReturn(2L);
                given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

                // when. then
                assertThatCode(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 비어있는 상태입니다.");

                verify(menuDao, times(1)).countByIdIn(anyList());
                verify(orderTableDao, times(1)).findById(1L);
            }
        }

        @DisplayName("그 외 정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("Order를 정상 생성 및 반환한다.")
            @Test
            void it_saves_and_returns_order() {
                // given
                Order order = new Order();
                OrderLineItem orderLineItem = new OrderLineItem();
                OrderLineItem orderLineItem2 = new OrderLineItem();
                OrderTable orderTable = new OrderTable();
                orderLineItem.setMenuId(1L);
                orderLineItem2.setMenuId(2L);
                order.setOrderLineItems(Arrays.asList(orderLineItem, orderLineItem2));
                order.setOrderTableId(1L);
                orderTable.setId(1L);
                orderTable.setEmpty(false);
                given(menuDao.countByIdIn(anyList())).willReturn(2L);
                given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
                given(orderDao.save(order)).willReturn(order);

                // when
                Order response = orderService.create(order);

                // then
                assertThat(response).extracting("orderTableId", "orderStatus")
                    .contains(1L, OrderStatus.COOKING.name());

                verify(menuDao, times(1)).countByIdIn(anyList());
                verify(orderTableDao, times(1)).findById(1L);
                verify(orderDao, times(1)).save(order);
                verify(orderLineItemDao, times(2)).save(any(OrderLineItem.class));
            }
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("Order 목록을 조회한다.")
        @Test
        void it_returns_order_list() {
            // given
            Order order = new Order();
            order.setId(1L);
            Order order2 = new Order();
            order2.setId(2L);
            List<Order> orders = Arrays.asList(order, order2);
            given(orderDao.findAll()).willReturn(orders);

            // when
            List<Order> response = orderService.list();

            // then
            assertThat(response).hasSize(2);

            verify(orderDao, times(1)).findAll();
            verify(orderLineItemDao, times(2)).findAllByOrderId(anyLong());
        }
    }

    @DisplayName("changeOrderStatus 메서드는")
    @Nested
    class Describe_changeOrderStatus {

        @DisplayName("Order를 조회할 수 없으면")
        @Nested
        class Context_order_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Order order = new Order();
                given(orderDao.findById(1L)).willReturn(Optional.empty());

                // when, then
                assertThatCode(() -> orderService.changeOrderStatus(1L, order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 Order입니다.");
            }
        }

        @DisplayName("조회된 Order의 상태가 COMPLETION인 경우")
        @Nested
        class Context_order_status_completion {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Order order = new Order();
                Order savedOrder = new Order();
                savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
                given(orderDao.findById(1L)).willReturn(Optional.of(savedOrder));

                // when, then
                assertThatCode(() -> orderService.changeOrderStatus(1L, order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Order 상태를 변경할 수 없는 상황입니다.");
            }
        }

        @DisplayName("그 외 정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("Order 상태가 변경된다.")
            @Test
            void it_changes_order_status() {
                // given
                Order order = new Order();
                order.setOrderStatus(OrderStatus.MEAL.name());
                Order savedOrder = new Order();
                savedOrder.setOrderStatus(OrderStatus.COOKING.name());
                given(orderDao.findById(1L)).willReturn(Optional.of(savedOrder));

                // when
                Order response = orderService.changeOrderStatus(1L, order);

                // then
                assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

                verify(orderDao, times(1)).findById(1L);
                verify(orderDao, times(1)).save(savedOrder);
                verify(orderLineItemDao, times(1)).findAllByOrderId(1L);
            }
        }
    }
}
