package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.application.support.domain.OrderTableTestSupport;
import kitchenpos.application.support.domain.OrderTestSupport;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
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
    MenuDao menuDao;
    @Mock
    OrderDao orderDao;
    @Mock
    OrderLineItemDao orderLineItemDao;
    @Mock
    OrderTableDao orderTableDao;
    @InjectMocks
    OrderService target;

    @DisplayName("주문을 생성에 성공하면 처음 주문의 상태는 조리상태이다.")
    @Test
    void create() {
        //given
        final OrderTable orderTable = OrderTableTestSupport.builder().build();
        final OrderTestSupport.Builder builder = OrderTestSupport.builder().orderTableId(orderTable.getId());
        final Order order = builder.build();
        final OrderCreateRequest request = builder.buildToOrderCreateRequest();
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        given(menuDao.countByIdIn(anyList())).willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.save(any(Order.class))).willReturn(order);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(orderLineItems.get(0));

        //when
        final Order result = target.create(request);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("메뉴 정보가 아무것도 없으면 예외처리한다.")
    @Test
    void create_fail_no_OrderLineItem() {
        //given
        final OrderCreateRequest request = OrderTestSupport.builder().orderLineItems(Collections.emptyList()).buildToOrderCreateRequest();

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 정보들에 존재하는 메뉴가 DB에 존재하지 않으면 예외 처리한다.")
    @Test
    void create_fail_invalid_menu() {
        //given
        final OrderCreateRequest request = OrderTestSupport.builder().buildToOrderCreateRequest();

        given(menuDao.countByIdIn(anyList())).willReturn(0L);

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 정보가 DB에 존재하지 않으면 예외처리한다.")
    @Test
    void create_fail_invalid_table() {
        //given
        final OrderTable orderTable = OrderTableTestSupport.builder().build();
        final OrderTestSupport.Builder builder = OrderTestSupport.builder();
        final Order order = builder.orderTableId(orderTable.getId()).build();
        final OrderCreateRequest request = builder.buildToOrderCreateRequest();
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        given(menuDao.countByIdIn(anyList())).willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> target.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 전체 조회한다.")
    @Test
    void list() {
        //given
        final Order order1 = OrderTestSupport.builder().build();
        final Order order2 = OrderTestSupport.builder().build();
        final Order order3 = OrderTestSupport.builder().build();
        given(orderDao.findAll()).willReturn(List.of(order1, order2, order3));

        //when

        //then
        assertThat(target.list()).contains(order1, order2, order3);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        //given
        final OrderStatus beforeStatus = OrderStatus.COOKING;
        final Order order = OrderTestSupport.builder().orderStatus(beforeStatus.name()).build();

        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
        given(orderDao.save(any(Order.class))).willReturn(order);
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(order.getOrderLineItems());

        final OrderStatusChangeRequest request = new OrderStatusChangeRequest(MEAL.name());

        //when
        final Order result = target.changeOrderStatus(order.getId(), request);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("이미 계산 완료되었으면 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_fail_COMPLETION() {
        //given
        final OrderStatus beforeStatus = OrderStatus.COMPLETION;
        final Order order = OrderTestSupport.builder().orderStatus(beforeStatus.name()).build();

        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
        final OrderStatusChangeRequest request = new OrderStatusChangeRequest(MEAL.name());
        //when

        //then
        assertThatThrownBy(() -> target.changeOrderStatus(order.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
