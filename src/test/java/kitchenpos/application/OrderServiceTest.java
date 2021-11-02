package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.factory.KitchenPosFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private final Order standardOrder = KitchenPosFactory.getStandardOrder();
    private final List<Order> standardOrders = KitchenPosFactory.getStandardOrders();

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

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        //given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(KitchenPosFactory.getStandardOrderTable()));
        given(orderDao.save(standardOrder)).willReturn(standardOrder);
        given(orderLineItemDao.save(any())).willReturn(KitchenPosFactory.getStandardOrderLineItem());

        //when
        Order order = orderService.create(standardOrder);

        //then
        assertThat(order).usingRecursiveComparison()
            .isEqualTo(standardOrder);
    }

    @Test
    @DisplayName("OrderLineItems 가 비어있다면 주문 생성시 에러가 발생한다.")
    void createExceptionWithEmptyOrderLineItems() {
        //given
        Order request = KitchenPosFactory.getStandardOrder();
        request.setOrderLineItems(new ArrayList<>());

        //when
        ThrowingCallable callable = () -> orderService.create(request);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문한 메뉴 id size 와 db에 저장된 size 가 다르다면 주문 생성시 에러가 발생한다.")
    void createExceptionWithNotEqualsItemSize() {
        //given
        Order request = KitchenPosFactory.getStandardOrder();
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());

        //when
        ThrowingCallable callable = () -> orderService.create(request);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문한 테이블이 없다면 주문 생성시 에러가 발생한다.")
    void createExceptionWithNotExistOrderTable() {
        //given
        Order request = KitchenPosFactory.getStandardOrder();
        request.setOrderTableId(2L);
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(2L)).willReturn(Optional.empty());

        //when
        ThrowingCallable callable = () -> orderService.create(request);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문한 테이블이 비어있다면 주문 생성시 에러가 발생한다.")
    void createExceptionWithOrderTableEmpty() {
        //given
        OrderTable requestOrderTable = KitchenPosFactory.getStandardOrderTable();
        requestOrderTable.setEmpty(true);
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(requestOrderTable));

        //when
        ThrowingCallable callable = () -> orderService.create(standardOrder);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 주문을 가져온다.")
    void list() {
        //given
        given(orderDao.findAll()).willReturn(standardOrders);
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(KitchenPosFactory.getStandardOrderLineItems());

        //when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders).usingRecursiveComparison()
            .isEqualTo(standardOrders);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        //given
        Order request = KitchenPosFactory.getStandardOrder();
        request.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(1L)).willReturn(Optional.of(standardOrder));
        given(orderDao.save(standardOrder)).willReturn(request);
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(KitchenPosFactory.getStandardOrderLineItems());

        //when
        Order order = orderService.changeOrderStatus(1L, request);

        //then
        assertThat(order).usingRecursiveComparison()
            .isEqualTo(standardOrder);
    }

    @Test
    @DisplayName("존재하지 않는 주문을 수정하면 에러가 발생한다.")
    void changeOrderStatusExceptionWithNotExistOrder() {
        //given
        Order request = KitchenPosFactory.getStandardOrder();
        given(orderDao.findById(2L)).willReturn(Optional.empty());

        //when
        ThrowingCallable callable = () -> orderService.changeOrderStatus(2L, request);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("완료 상태의 주문을 수정하면 에러가 발생한다.")
    void changeOrderStatusExceptionWithOrderStatusCompletion() {
        //given
        Order request = KitchenPosFactory.getStandardOrder();
        request.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(1L)).willReturn(Optional.of(request));

        //when
        ThrowingCallable callable = () -> orderService.changeOrderStatus(1L, request);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
