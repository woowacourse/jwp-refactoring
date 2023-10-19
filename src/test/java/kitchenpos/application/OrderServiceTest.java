package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문_시_주문하려는_메뉴를_입력하지_않으면_예외발생() {
        // given
        Order order = new Order();
        order.setOrderLineItems(Collections.emptyList());

        // when
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuRepository).should(never()).countByIdIn(anyList());
    }

    @Test
    void 주문_시_주문하려는_메뉴가_존재하지_않는_메뉴일_경우_예외발생() {
        // given
        Order order = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        order.setOrderLineItems(List.of(orderLineItem));

        // 존재하지 않는 상품
        given(menuRepository.countByIdIn(anyList()))
                .willReturn(0L);
        // when
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(orderTableDao).should(never()).findById(anyLong());
    }

    @Test
    void 주문_시_주문하려는_메뉴_간_중복이_있으면_예외발생() {
        // given
        Order order = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        // 중복 아이템 존재
        order.setOrderLineItems(List.of(orderLineItem, orderLineItem));

        given(menuRepository.countByIdIn(anyList()))
                .willReturn(1L);

        // when
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(orderTableDao).should(never()).findById(anyLong());
    }

    @Test
    void 주문_하는_테이블이_빈_테이블이면_예외발생() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(List.of(new OrderLineItem(), new OrderLineItem()));

        given(menuRepository.countByIdIn(anyList()))
                .willReturn(2L);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderDao).should(never()).save(any());
    }

    @Test
    void 주문을_생성한다() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setId(1L);
        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(), new OrderLineItem());
        order.setOrderLineItems(orderLineItems);

        given(menuRepository.countByIdIn(anyList()))
                .willReturn(2L);

        OrderTable orderTable = new OrderTable();
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.save(any()))
                .willReturn(order);

        // when, then
        orderService.create(order);
        then(orderLineItemDao).should(times(orderLineItems.size())).save(any());
    }

    @Test
    void 주문_상태_변경_시_변경하려는_주문이_존재하지_않으면_예외발생() {
        // given
        given(orderDao.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderDao).should(never()).save(any());
    }

    @Test
    void 상태를_변경하려는_주문이_이미_완료_상태면_예외발생() {
        // given
        Order order = new Order();
        order.setOrderStatus("COMPLETION");
        given(orderDao.findById(anyLong()))
                .willReturn(Optional.of(order));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderDao).should(never()).save(any());
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        Order order = new Order();
        order.setOrderStatus("MEAL");
        given(orderDao.findById(anyLong()))
                .willReturn(Optional.of(order));

        // when
        orderService.changeOrderStatus(1L, order);

        // then
        then(orderDao).should(times(1)).save(any());
        then(orderLineItemDao).should(times(1)).findAllByOrderId(anyLong());
    }
}
