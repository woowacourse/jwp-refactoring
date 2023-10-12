package kitchenpos.application;

import static kitchenpos.common.OrderFixtures.ORDER1_CREATE_REQUEST;
import static kitchenpos.common.OrderFixtures.ORDER1_ORDER_LINE_ITEMS;
import static kitchenpos.common.OrderTableFixtures.ORDER_TABLE1_CREATE_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.OrderException;
import kitchenpos.exception.OrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private MenuDao menuDao;

    @MockBean
    private OrderTableDao orderTableDao;

    @MockBean
    private OrderDao orderDao;

    @MockBean
    private OrderLineItemDao orderLineItemDao;

    @Nested
    @DisplayName("주문 생성 시")
    class CreateOrder {

        @Test
        @DisplayName("생성에 성공한다.")
        void success() {
            // given
            Order orderRequest = ORDER1_CREATE_REQUEST();
            List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems();
            List<Long> ids = orderLineItems.stream()
                    .map(OrderLineItem::getMenuId)
                    .collect(Collectors.toList());

            BDDMockito.given(menuDao.countByIdIn(ids))
                    .willReturn((long) orderLineItems.size());

            OrderTable orderTable = ORDER_TABLE1_CREATE_REQUEST();
            orderTable.setId(1L);
            orderTable.setEmpty(false);
            BDDMockito.given(orderTableDao.findById(orderRequest.getOrderTableId()))
                    .willReturn(Optional.of(orderTable));

            Order order = ORDER1_CREATE_REQUEST();
            order.setOrderTableId(orderTable.getId());
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setId(1L);
            BDDMockito.given(orderDao.save(any(Order.class)))
                    .willReturn(order);

            OrderLineItem orderLineItem = orderLineItems.get(0);
            orderLineItem.setOrderId(order.getId());
            orderLineItem.setSeq(1L);
            BDDMockito.given(orderLineItemDao.save(orderLineItem))
                    .willReturn(orderLineItem);

            // when
            Order savedOrder = orderService.create(orderRequest);

            // then
            assertSoftly(softly -> {
                softly.assertThat(savedOrder.getId()).isNotNull();
                softly.assertThat(savedOrder.getOrderTableId()).isNotNull();
                softly.assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                softly.assertThat(savedOrder.getOrderedTime()).isNotNull();
                softly.assertThat(savedOrder.getOrderLineItems()).isNotEmpty();
            });
        }

        @Test
        @DisplayName("주문 항목 목록이 비어있으면 예외가 발생한다.")
        void throws_OrderLineItemsIsEmpty() {
            // given
            Order order = new Order();
            order.setOrderTableId(1L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 항목 목록이 비어있습니다.");
        }

        @Test
        @DisplayName("요청한 주문 항목 목록의 개수와 메뉴 아이디로 조회한 메뉴의 개수가 다르면 예외가 발생한다.")
        void throws_notSameOrderLineItemsSize() {
            // given
            Order order = ORDER1_CREATE_REQUEST();
            List<OrderLineItem> orderLineItems = order.getOrderLineItems();
            List<Long> ids = orderLineItems.stream()
                    .map(OrderLineItem::getMenuId)
                    .collect(Collectors.toList());

            long notSameOrderLineItemsSize = (long) orderLineItems.size() - 1;
            BDDMockito.given(menuDao.countByIdIn(ids))
                    .willReturn(notSameOrderLineItemsSize);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(OrderException.NotFoundOrderLineItemMenuExistException.class)
                    .hasMessage("[ERROR] 주문 항목 목록에 메뉴가 누락된 주문 항목이 존재합니다.");
        }
    }

    @Test
    @DisplayName("주문 테이블 ID에 해당하는 주문 테이블이 존재하지 않으면 예외가 발생한다.")
    void throws_notFoundOrderTable() {
        // given
        final Long notExistOrderTableId = -1L;
        Order order = ORDER1_CREATE_REQUEST();
        order.setOrderTableId(notExistOrderTableId);
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        List<Long> ids = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        BDDMockito.given(menuDao.countByIdIn(ids))
                .willReturn((long) orderLineItems.size());

        BDDMockito.given(orderTableDao.findById(notExistOrderTableId))
                .willThrow(new OrderTableException.NotFoundOrderTableException());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
    }
    
    @Test
    @DisplayName("주문 테이블이 비어있는 상태면 예외가 발생한다")
    void throws_OrderTableIsEmpty() {
        // given
        Order order = ORDER1_CREATE_REQUEST();
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        List<Long> ids = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        BDDMockito.given(menuDao.countByIdIn(ids))
                .willReturn((long) orderLineItems.size());

        OrderTable orderTable = ORDER_TABLE1_CREATE_REQUEST();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        BDDMockito.given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(OrderException.CannotOrderStateByOrderTableEmptyException.class)
                .hasMessage("[ERROR] 주문 테이블이 비어있는 상태일 때 주문할 수 없습니다.");
    }

    @Nested
    @DisplayName("주문 조회 시")
    class FindAll {

        @Test
        @DisplayName("조회에 성공한다.")
        void success() {
            // given
            Order order = ORDER1_CREATE_REQUEST();
            order.setOrderTableId(1L);
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setId(1L);

            BDDMockito.given(orderDao.findAll())
                    .willReturn(List.of(order));

            List<OrderLineItem> orderLineItems = ORDER1_ORDER_LINE_ITEMS();
            BDDMockito.given(orderLineItemDao.findAllByOrderId(order.getId()))
                    .willReturn(orderLineItems);

            // when
            List<Order> orders = orderService.list();

            // then
            assertThat(orders.get(0).getOrderLineItems())
                    .usingRecursiveFieldByFieldElementComparator()
                    .isEqualTo(orderLineItems);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 시")
    class ChangeOrderStatus {

        @Test
        @DisplayName("변경에 성공한다.")
        void success() {
            // given
            Order order = ORDER1_CREATE_REQUEST();
            order.setOrderTableId(1L);
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setId(1L);

            BDDMockito.given(orderDao.findById(order.getId()))
                    .willReturn(Optional.of(order));

            String orderStatusToChange = OrderStatus.MEAL.name();
            order.setOrderStatus(orderStatusToChange);
            BDDMockito.given(orderDao.save(any(Order.class)))
                    .willReturn(order);

            List<OrderLineItem> orderLineItems = ORDER1_ORDER_LINE_ITEMS();
            BDDMockito.given(orderLineItemDao.findAllByOrderId(order.getId()))
                    .willReturn(orderLineItems);

            // when
            Order savedOrder = orderService.changeOrderStatus(order.getId(), order);

            // then
            assertSoftly(softly -> {
                softly.assertThat(savedOrder.getOrderLineItems()).isEqualTo(orderLineItems);
                softly.assertThat(savedOrder.getOrderStatus()).isEqualTo(orderStatusToChange);
            });
        }
        
        @Test
        @DisplayName("주문 ID에 해당하는 주문이 없으면 예외가 발생한다.")
        void throws_NotFoundOrder() {
            // given
            final Long notExistOrderId = -1L;
            Order order = ORDER1_CREATE_REQUEST();
            order.setOrderStatus(OrderStatus.MEAL.name());

            BDDMockito.given(orderDao.findById(notExistOrderId))
                            .willThrow(new OrderException.NotFoundOrderException());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, order))
                    .isInstanceOf(OrderException.NotFoundOrderException.class)
                    .hasMessage("[ERROR] 주문을 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("기존 주문 상태가 계산 완료이면 예외가 발생한다.")
        void throws_OrderStatusIsCompletion() {
            // given
            Order order = ORDER1_CREATE_REQUEST();
            order.setOrderTableId(1L);
            order.setOrderStatus(OrderStatus.COMPLETION.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setId(1L);

            BDDMockito.given(orderDao.findById(order.getId()))
                    .willReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                    .isInstanceOf(OrderException.CannotChangeOrderStatusByCurrentOrderStatusException.class)
                    .hasMessage("[ERROR] 기존 주문의 주문 상태가 계산 완료인 주문은 상태를 변경할 수 없습니다.");
        }
    }

}
