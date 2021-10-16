package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        Order order = new Order();

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setSeq(1L);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setQuantity(1L);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setSeq(2L);
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setOrderId(1L);
        orderLineItem2.setQuantity(1L);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem1, orderLineItem2);
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(order.getOrderTableId());

        given(menuDao.countByIdIn(List.of(orderLineItem1.getMenuId(), orderLineItem2.getMenuId())))
                .willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.save(order))
                .willReturn(order);
        given(orderLineItemDao.save(orderLineItem1))
                .willReturn(orderLineItem1);
        given(orderLineItemDao.save(orderLineItem2))
                .willReturn(orderLineItem2);

        //when
        Order actual = orderService.create(order);
        //then
        assertThat(actual).isEqualTo(order);

        verify(menuDao, times(1)).countByIdIn(anyList());
        verify(orderTableDao, times(1)).findById(anyLong());
        verify(orderDao, times(1)).save(order);
        verify(orderLineItemDao, times(2)).save(any());
    }

    @DisplayName("주문 등록 실패 - 비어 있는 주문 항목")
    @Test
    void createFailEmptyOrderLineItem() {
        //given
        Order order = new Order();
        order.setOrderLineItems(Collections.emptyList());
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 비어있을 수 없습니다.");
    }

    @DisplayName("주문 등록 실패 - 존재하지 않는 메뉴가 주문에 포함되어 있을 경우")
    @Test
    void createFailNotExistMenu() {
        //given
        Order order = new Order();

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setSeq(1L);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setQuantity(1L);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setSeq(2L);
        orderLineItem2.setMenuId(3L);
        orderLineItem2.setOrderId(1L);
        orderLineItem2.setQuantity(1L);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem1, orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        given(menuDao.countByIdIn(List.of(1L, 3L)))
                .willReturn(1L);
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴입니다.");
    }

    @DisplayName("주문 등록 실패 - 존재하지 않는 주문 테이블")
    @Test
    void createFailNotExistOrderTable() {
        //given
        Order order = new Order();

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setSeq(1L);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setQuantity(1L);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setSeq(2L);
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setOrderId(1L);
        orderLineItem2.setQuantity(1L);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem1, orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        order.setOrderTableId(1L);
        given(menuDao.countByIdIn(List.of(1L, 2L)))
                .willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
    }

    @DisplayName("주문 등록 실패 - 주문 테이블이 비어있을 경우")
    @Test
    void createFailEmptyOrderTable() {
        //given
        Order order = new Order();

        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setSeq(1L);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setQuantity(1L);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setSeq(2L);
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setOrderId(1L);
        orderLineItem2.setQuantity(1L);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem1, orderLineItem2);
        order.setOrderLineItems(orderLineItems);

        order.setOrderTableId(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        given(menuDao.countByIdIn(List.of(1L, 2L)))
                .willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어있습니다.");
    }

    @DisplayName("주문 목록을 불러온다.")
    @Test
    void list() {
        //given
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);

        List<Order> expected = List.of(order1, order2);

        given(orderDao.findAll())
                .willReturn(expected);
        //when
        List<Order> actual = orderService.list();
        //then
        assertThat(actual).isEqualTo(expected);

        verify(orderDao, times(1)).findAll();
        verify(orderLineItemDao, times(2)).findAllByOrderId(anyLong());
    }

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(String status) {
        //given
        Long orderId = 1L;
        Order savedOrder = new Order();
        Order changeOrder = new Order();
        changeOrder.setOrderStatus(status);

        OrderLineItem orderLineItem = new OrderLineItem();
        List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        savedOrder.setOrderLineItems(orderLineItems);

        given(orderDao.findById(orderId))
                .willReturn(Optional.of(savedOrder));
        given(orderLineItemDao.findAllByOrderId(orderId))
                .willReturn(orderLineItems);
        //when
        Order actual = orderService.changeOrderStatus(orderId, changeOrder);
        //then
        assertThat(actual).isEqualTo(savedOrder);

        verify(orderDao, times(1)).findById(orderId);
        verify(orderDao, times(1)).save(savedOrder);
        verify(orderLineItemDao, times(1)).findAllByOrderId(orderId);
    }

    @DisplayName("주문 상태를 변경 실패 - 주문 상태가 이미 계산 완료일 경우")
    @Test
    void changeOrderStatusFailAlreadyCompletionStats() {
        //given
        Long orderId = 1L;
        Order savedOrder = new Order();
        savedOrder.setOrderStatus("COMPLETION");
        Order changeOrder = new Order();
        changeOrder.setOrderStatus("COMPLETION");

        given(orderDao.findById(orderId))
                .willReturn(Optional.of(savedOrder));
        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, changeOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계산 완료된 주문은 상태를 변경할 수 없습니다.");
    }
}