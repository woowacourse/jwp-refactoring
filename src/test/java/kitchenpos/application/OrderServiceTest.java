package kitchenpos.application;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 테스트")
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("OrderLineItem 리스트가 비어있다면")
        class Context_with_orderLineItems_empty {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                // given
                Order order = new Order();
                order.setOrderLineItems(Collections.emptyList());

                // when, then
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
                then(menuDao)
                        .should(never())
                        .countByIdIn(any());
                then(orderTableDao)
                        .should(never())
                        .findById(anyLong());
                then(orderDao)
                        .should(never())
                        .save(any());
                then(orderLineItemDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("OrderLineItem 리스트의 Menu Id가 실제 db에 저장된 Menu Id가 아니라면")
        class Context_with_not_exist_menu {

            @Test
            @DisplayName("예외가 발생한다.")
            void it_throws_exception() {
                // given
                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setSeq(1L);
                orderLineItem.setMenuId(1L);
                OrderLineItem anotherOrderLineItem = new OrderLineItem();
                anotherOrderLineItem.setSeq(2L);
                anotherOrderLineItem.setMenuId(2L);
                Order order = new Order();
                order.setOrderLineItems(Arrays.asList(orderLineItem, anotherOrderLineItem));
                given(menuDao.countByIdIn(Arrays.asList(orderLineItem.getMenuId(), anotherOrderLineItem.getMenuId())))
                        .willReturn(0L);

                // when, then
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
                then(menuDao)
                        .should()
                        .countByIdIn(Arrays.asList(orderLineItem.getMenuId(), anotherOrderLineItem.getMenuId()));
                then(orderTableDao)
                        .should(never())
                        .findById(anyLong());
                then(orderDao)
                        .should(never())
                        .save(any());
                then(orderLineItemDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("OrderTable이 빈 테이블이라면")
        class Context_with_orderTable_empty {

            @Test
            @DisplayName("예외가 발생한다.")
            void it_throws_exception() {
                // given
                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setSeq(1L);
                orderLineItem.setMenuId(1L);
                OrderLineItem anotherOrderLineItem = new OrderLineItem();
                anotherOrderLineItem.setSeq(2L);
                anotherOrderLineItem.setMenuId(2L);
                List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem, anotherOrderLineItem);

                Order order = new Order();
                order.setOrderTableId(1L);
                order.setOrderLineItems(orderLineItems);
                OrderTable orderTable = new OrderTable();
                orderTable.setEmpty(true);
                given(menuDao.countByIdIn(Arrays.asList(orderLineItem.getMenuId(), anotherOrderLineItem.getMenuId())))
                        .willReturn((long) orderLineItems.size());
                given(orderTableDao.findById(order.getOrderTableId()))
                        .willReturn(Optional.of(orderTable));

                // when, then
                assertThatThrownBy(() -> orderService.create(order))
                        .isInstanceOf(IllegalArgumentException.class);
                then(menuDao)
                        .should()
                        .countByIdIn(Arrays.asList(orderLineItem.getMenuId(), anotherOrderLineItem.getMenuId()));
                then(orderTableDao)
                        .should()
                        .findById(order.getOrderTableId());
                then(orderDao)
                        .should(never())
                        .save(any());
                then(orderLineItemDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("정상적인 경우라면")
        class Context_with_correct_case {

            @Test
            @DisplayName("Order를 반환한다")
            void it_return_order() {
                // given
                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setSeq(1L);
                orderLineItem.setMenuId(1L);
                OrderLineItem anotherOrderLineItem = new OrderLineItem();
                anotherOrderLineItem.setSeq(2L);
                anotherOrderLineItem.setMenuId(2L);
                List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem, anotherOrderLineItem);

                Order order = new Order();
                order.setId(1L);
                order.setOrderTableId(1L);
                order.setOrderLineItems(orderLineItems);

                OrderTable orderTable = new OrderTable();
                orderTable.setId(1L);
                orderTable.setEmpty(false);

                given(menuDao.countByIdIn(Arrays.asList(orderLineItem.getMenuId(), anotherOrderLineItem.getMenuId())))
                        .willReturn((long) orderLineItems.size());
                given(orderTableDao.findById(order.getOrderTableId()))
                        .willReturn(Optional.of(orderTable));
                given(orderDao.save(order)).willReturn(order);
                given(orderLineItemDao.save(any())).willReturn(orderLineItem, anotherOrderLineItem);

                // when
                Order createdOrder = orderService.create(order);

                // then
                assertThat(order).isEqualTo(createdOrder);
                then(menuDao)
                        .should()
                        .countByIdIn(Arrays.asList(orderLineItem.getMenuId(), anotherOrderLineItem.getMenuId()));
                then(orderTableDao)
                        .should()
                        .findById(order.getOrderTableId());
                then(orderDao)
                        .should()
                        .save(order);
                then(orderLineItemDao)
                        .should(times(2))
                        .save(any());
            }
        }
    }


    @Nested
    @DisplayName("changeOrderStatus 메소드는")
    class Describe_changeOrderStatus {
        @Nested
        @DisplayName("조회한 Order의 OrderStatus가 Completion이라면")
        class Context_with_orderStatus_completion {

            @Test
            @DisplayName("예외가 발생한다.")
            void it_throws_exception() {
                // given
                Long orderId = 1L;
                Order order = new Order();
                order.setId(orderId);
                order.setOrderStatus(OrderStatus.COMPLETION.name());
                given(orderDao.findById(orderId)).willReturn(Optional.of(order));

                // when, then
                assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                        .isInstanceOf(IllegalArgumentException.class);
                then(orderDao)
                        .should()
                        .findById(orderId);
                then(orderDao)
                        .should(never())
                        .save(any());
                then(orderLineItemDao)
                        .should(never())
                        .findAllByOrderId(orderId);
            }
        }

        @Nested
        @DisplayName("정상적인 경우라면")
        class Context_with_correct_case {

            @ParameterizedTest
            @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
            @DisplayName("Order를 반환한다")
            void it_return_order(OrderStatus orderStatus) {
                // given
                Long orderId = 1L;
                Order savedOrder = new Order();
                savedOrder.setId(orderId);
                savedOrder.setOrderStatus(orderStatus.name());

                Order order = new Order();
                order.setOrderStatus(OrderStatus.COMPLETION.name());

                OrderLineItem orderLineItem = new OrderLineItem();
                orderLineItem.setSeq(1L);
                orderLineItem.setOrderId(orderId);
                orderLineItem.setMenuId(1L);

                given(orderDao.findById(orderId)).willReturn(Optional.of(savedOrder));
                given(orderDao.save(savedOrder)).willReturn(any());
                given(orderLineItemDao.findAllByOrderId(orderId)).willReturn(Collections.singletonList(orderLineItem));

                // when
                Order changedOrderService = orderService.changeOrderStatus(orderId, order);

                // then
                assertThat(changedOrderService).isEqualTo(savedOrder);
                then(orderDao)
                        .should()
                        .findById(orderId);
                then(orderDao)
                        .should()
                        .save(savedOrder);
                then(orderLineItemDao)
                        .should()
                        .findAllByOrderId(orderId);

            }
        }
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Test
        @DisplayName("Order 리스트를 반환한다")
        void it_return_order_list() {
            // given
            Order order = new Order();
            order.setId(1L);
            Order anotherOrder = new Order();
            anotherOrder.setId(2L);

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrderId(order.getId());
            OrderLineItem anotherOrderLineItem = new OrderLineItem();
            anotherOrderLineItem.setOrderId(anotherOrder.getId());

            given(orderDao.findAll()).willReturn(Arrays.asList(order, anotherOrder));
            given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(
                    Collections.singletonList(orderLineItem),
                    Collections.singletonList(anotherOrderLineItem));

            // when
            List<Order> list = orderService.list();

            // then
            assertThat(list).containsExactly(order, anotherOrder);
            assertThat(list).hasSize(2);
            then(orderDao)
                    .should()
                    .findAll();
            then(orderLineItemDao)
                    .should(times(2))
                    .findAllByOrderId(anyLong());

        }

    }

}