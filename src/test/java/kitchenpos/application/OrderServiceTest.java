package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


    @DisplayName("새로운 주문을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final Order order = new Order();
        order.setId(1L);

        final Menu menu1 = new Menu(10L, "후라이드 양념 세트", BigDecimal.valueOf(30000), 1L);
        final Menu menu2 = new Menu(11L, "후라이드 간장 세트", BigDecimal.valueOf(30000), 1L);

        final OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setQuantity(1);
        orderLineItem1.setMenuId(menu1.getId());

        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(1L);
        orderLineItem2.setQuantity(1);
        orderLineItem2.setMenuId(menu2.getId());

        order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));

        given(menuRepository.countByIdIn(any()))
                .willReturn(2L);

        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1000L);
        order.setOrderTableId(orderTable.getId());

        given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));

        given(orderDao.save(any()))
                .willReturn(order);

        given(orderLineItemDao.save(orderLineItem1))
                .willReturn(orderLineItem1);

        given(orderLineItemDao.save(orderLineItem2))
                .willReturn(orderLineItem2);

        // when & then
        assertThat(orderService.create(order)).isEqualTo(order);
        then(menuRepository).should(times(1)).countByIdIn(any());
        then(orderTableDao).should(times(1)).findById(anyLong());
        then(orderDao).should(times(1)).save(any());
        then(orderLineItemDao).should(times(2)).save(any());
    }

    @DisplayName("주문 항목이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_FailWhenOrderLineItemsNotExist() {
        // given
        final Order order = new Order();
        order.setId(1L);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 메뉴가 존재하지 않습니다.");
    }

    @DisplayName("주문 항목에 존재하는 메뉴가 존재하지 않는 메뉴이면 등록할 수 없다.")
    @Test
    void create_FailWhenMenuNotExist() {
        // given
        final Order order = new Order();
        order.setId(1L);

        final Menu menu1 = new Menu(10L, "후라이드 양념 세트", BigDecimal.valueOf(30000), 1L);

        final OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setQuantity(1);
        orderLineItem1.setMenuId(menu1.getId());

        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(1L);
        orderLineItem2.setQuantity(1);
        orderLineItem2.setMenuId(11L);

        order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @DisplayName("주문 테이블의 상태가 비어있으면 등록할 수 없다.")
    @Test
    void create_FailWhenOrderTableIsEmpty() {
        // given
        final Order order = new Order();
        order.setId(1L);

        final Menu menu1 = new Menu(10L, "후라이드 양념 세트", BigDecimal.valueOf(30000), 1L);
        final Menu menu2 = new Menu(11L, "후라이드 간장 세트", BigDecimal.valueOf(30000), 1L);

        final OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setQuantity(1);
        orderLineItem1.setMenuId(menu1.getId());

        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(1L);
        orderLineItem2.setQuantity(1);
        orderLineItem2.setMenuId(menu2.getId());

        order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));

        given(menuRepository.countByIdIn(any()))
                .willReturn(2L);

        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1000L);
        orderTable.setEmpty(true);
        order.setOrderTableId(orderTable.getId());

        given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블의 상태가 비어있습니다.");
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Order order1 = new Order();
        order1.setId(1L);

        final Order order2 = new Order();
        order2.setId(2L);

        final List<Order> orders = List.of(order1, order2);

        final OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(order1.getId());

        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(order2.getId());

        given(orderDao.findAll())
                .willReturn(orders);
        given(orderLineItemDao.findAllByOrderId(order1.getId()))
                .willReturn(List.of(orderLineItem1));
        given(orderLineItemDao.findAllByOrderId(order2.getId()))
                .willReturn(List.of(orderLineItem2));

        // when & then
        assertThat(orderService.list()).isEqualTo(orders);
        then(orderDao).should(times(1)).findAll();
        then(orderLineItemDao).should(times(2)).findAllByOrderId(anyLong());
    }

    @DisplayName("특정 주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());

        given(orderDao.findById(1L))
                .willReturn(Optional.of(order));
        given(orderDao.save(order))
                .willReturn(order);
        given(orderLineItemDao.findAllByOrderId(order.getId()))
                .willReturn(List.of(new OrderLineItem()));

        // when
        orderService.changeOrderStatus(1L, order);

        // then
        then(orderDao).should(times(1)).findById(1L);
        then(orderDao).should(times(1)).save(order);
        then(orderLineItemDao).should(times(1)).findAllByOrderId(1L);
    }

    @DisplayName("해당 주문이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatus_FailWhenOrderNotExist() {
        // given
        final Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(2L, order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문입니다.");
    }

    @DisplayName("해당 주문의 상태가 이미 완료이면 변경할 수 없다.")
    @Test
    void changeOrderStatus_FailWhenOrderStatusAlreadyCompletion() {
        // given
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setId(1L);

        given(orderDao.findById(1L))
                .willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }
}
