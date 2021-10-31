package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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

    private OrderLineItem 후라이드_치킨;
    private OrderLineItem 모짜_치즈볼_5pc;
    private OrderLineItem 치킨윙_4pc;
    private OrderLineItem 맥주_500_cc;

    @BeforeEach
    void setUp() {
        후라이드_치킨 = orderLineItemConstructor(1L, 1L, 1L, 1);
        모짜_치즈볼_5pc = orderLineItemConstructor(2L, 2L, 2L, 5);
        치킨윙_4pc = orderLineItemConstructor(3L, 3L, 3L, 4);
        맥주_500_cc = orderLineItemConstructor(4L, 4L, 4L, 1);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        Order order = orderConstructor(1L, Arrays.asList(
            후라이드_치킨,
            모짜_치즈볼_5pc
        ));
        OrderTable orderTable = orderTableConstructor(1L, null, 4, false);
        Order expected = orderConstructor(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(
            후라이드_치킨,
            모짜_치즈볼_5pc
        ));

        given(menuDao.countByIdIn(Arrays.asList(후라이드_치킨.getMenuId(), 모짜_치즈볼_5pc.getMenuId()))).willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(expected);
        given(orderLineItemDao.save(후라이드_치킨)).willReturn(후라이드_치킨);
        given(orderLineItemDao.save(모짜_치즈볼_5pc)).willReturn(모짜_치즈볼_5pc);

        //when
        Order actual = orderService.create(order);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("주문 물품 목록이 비어있으면 예외가 발생한다.")
    @Test
    void orderLineItemsEmpty() {
        //given
        Order order = orderConstructor(1L, null);

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 물품 목록의 개수와 메뉴 개수가 다르면 예외가 발생한다.")
    @Test
    void orderLineItemsAndMenuSizeDifferent() {
        //given
        Order order = orderConstructor(1L, Arrays.asList(
            후라이드_치킨,
            모짜_치즈볼_5pc
        ));

        given(menuDao.countByIdIn(Arrays.asList(후라이드_치킨.getMenuId(), 모짜_치즈볼_5pc.getMenuId()))).willReturn(3L);

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 아이디가 없으면 예외가 발생한다.")
    @Test
    void orderTableEmpty() {
        //given
        Order order = orderConstructor(1L, Arrays.asList(
            후라이드_치킨,
            모짜_치즈볼_5pc
        ));

        given(menuDao.countByIdIn(Arrays.asList(후라이드_치킨.getMenuId(), 모짜_치즈볼_5pc.getMenuId()))).willReturn(2L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> orderService.create(order))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 리스트를 조회한다.")
    @Test
    void readAll() {
        //given
        Order orderA = orderConstructor(1L, Arrays.asList(후라이드_치킨, 모짜_치즈볼_5pc));
        Order orderB = orderConstructor(2L, Arrays.asList(후라이드_치킨, 맥주_500_cc));
        Order orderC = orderConstructor(3L, Arrays.asList(모짜_치즈볼_5pc, 치킨윙_4pc, 맥주_500_cc));
        List<Order> expected = Arrays.asList(
            orderA,
            orderB,
            orderC
        );
        given(orderDao.findAll()).willReturn(expected);
        given(orderLineItemDao.findAllByOrderId(orderA.getId())).willReturn(Arrays.asList(후라이드_치킨, 모짜_치즈볼_5pc));
        given(orderLineItemDao.findAllByOrderId(orderB.getId())).willReturn(Arrays.asList(후라이드_치킨, 맥주_500_cc));
        given(orderLineItemDao.findAllByOrderId(orderC.getId())).willReturn(Arrays.asList(모짜_치즈볼_5pc, 치킨윙_4pc, 맥주_500_cc));

        //when
        List<Order> actual = orderService.list();

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderState() {
        //given
        Long orderId = 5L;
        LocalDateTime localDateTime = LocalDateTime.now();
        Order orderStatus = orderConstructor(OrderStatus.MEAL);
        Order order = orderConstructor(orderId, 1L, OrderStatus.COOKING.name(), localDateTime, Arrays.asList(후라이드_치킨, 맥주_500_cc));
        Order expected = orderConstructor(orderId, 1L, OrderStatus.MEAL.name(), localDateTime, Arrays.asList(후라이드_치킨, 맥주_500_cc));
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(Arrays.asList(후라이드_치킨, 맥주_500_cc));

        //when
        Order actual = orderService.changeOrderStatus(orderId, orderStatus);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("변경하려는 상태가 완료 상태면 예외가 발생한다.")
    @Test
    void changeOrderStateWhenCompletion() {
        //given
        Long orderId = 5L;
        LocalDateTime localDateTime = LocalDateTime.now();
        Order order = orderConstructor(orderId, 1L, OrderStatus.COMPLETION.name(), localDateTime, Arrays.asList(후라이드_치킨, 맥주_500_cc));
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private Order orderConstructor(final OrderStatus orderStatus) {
        return orderConstructor(null, null, orderStatus.name(), LocalDateTime.now(), null);
    }

    private Order orderConstructor(final Long id, final List<OrderLineItem> orderLineItems) {
        return orderConstructor(id, null, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    private Order orderConstructor(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
        final List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    private OrderLineItem orderLineItemConstructor(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    private OrderTable orderTableConstructor(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
