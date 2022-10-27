package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderChangeRequest;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemDto;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class create는 {

        private final Long id = 1L;
        private final Long orderTableId = 11L;
        private final String orderStatus = "COOKING";
        private final LocalDateTime orderedTime = LocalDateTime.now();
        private final OrderLineItem orderLineItem = new OrderLineItem();
        private final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        @Test
        void order_line_itmes가_비어있으면_예외를_반환한다() {
            OrderCreateRequest request = 주문_생성_dto를_만든다(id, orderTableId, orderStatus, orderedTime, new ArrayList<>());
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_line_itmes_크기와_메뉴_ID의_개수가_맞지_않으면_예외를_반환한다() {
            // given
            OrderCreateRequest request = 주문_생성_dto를_만든다(id, orderTableId, orderStatus, orderedTime,
                    orderLineItems);
            when(menuDao.countByIdIn(any())).thenReturn(3L);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table_id에_맞는_order_table이_없으면_예외를_반환한다() {
            // given
            Long notExistOrderTableId = 1234L;
            OrderCreateRequest request = 주문_생성_dto를_만든다(id, notExistOrderTableId, orderStatus, orderedTime,
                    orderLineItems);

            when(menuDao.countByIdIn(any())).thenReturn(Long.valueOf(orderLineItems.size()));
            when(orderTableDao.findById(notExistOrderTableId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table이_비어있으면_예외를_반환한다() {
            // given
            OrderCreateRequest request = 주문_생성_dto를_만든다(id, orderTableId, orderStatus, orderedTime,
                    orderLineItems);
            when(menuDao.countByIdIn(any())).thenReturn(Long.valueOf(orderLineItems.size()));

            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order를_생성할_수_있다() {
            // given
            OrderCreateRequest request = 주문_생성_dto를_만든다(id, orderTableId, orderStatus, orderedTime,
                    orderLineItems);
            when(menuDao.countByIdIn(any())).thenReturn(Long.valueOf(orderLineItems.size()));

            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));

            Order order = request.toOrder(orderTableId, orderStatus, orderedTime);
            when(orderDao.save(any(Order.class))).thenReturn(order);

            // when
            OrderResponse response = orderService.create(request);

            // then
            Assertions.assertAll(
                    () -> assertThat(response.getOrderTableId()).isEqualTo(order.getOrderTableId()),
                    () -> assertThat(response.getOrderStatus()).isEqualTo(order.getOrderStatus()),
                    () -> assertThat(response.getOrderedTime()).isEqualTo(order.getOrderedTime())
            );
        }

        private OrderCreateRequest 주문_생성_dto를_만든다(final Long id,
                                                  final Long orderTableId,
                                                  final String orderStatus,
                                                  final LocalDateTime orderedTime,
                                                  final List<OrderLineItem> orderLineItems) {
            return new OrderCreateRequest(
                    id,
                    orderTableId,
                    orderStatus,
                    orderedTime,
                    orderLineItems.stream()
                            .map(OrderLineItemDto::new)
                            .collect(Collectors.toList()));
        }
    }

    @Nested
    class list는 {

        @Test
        void order_목록을_조회할_수_있다() {
            // given
            Order order1 = new Order();
            Order order2 = new Order();
            when(orderDao.findAll()).thenReturn(Arrays.asList(order1, order2));

            // when
            List<OrderResponse> responses = orderService.list();

            // then
            assertThat(responses).hasSize(2);
        }
    }

    @Nested
    class change_status는 {
        @Test
        void 일치하는_order_id가_없을_경우_예외를_반환한다() {
            // given
            Long notExistOrderId = 1234L;
            when(orderDao.findById(notExistOrderId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, new OrderChangeRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_완료상태이면_예외를_반환한다() {
            // given
            Order order = new Order();
            order.setOrderStatus(COMPLETION.name());
            Long orderId = 1L;
            when(orderDao.findById(orderId)).thenReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, new OrderChangeRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void aorder_status를_변경할_수_있다() {
            // given
            Order order = new Order();
            order.setOrderStatus(COOKING.name());
            when(orderDao.findById(1L)).thenReturn(Optional.of(order));

            // when
            OrderResponse response = orderService.changeOrderStatus(1L, new OrderChangeRequest("COMPLETION"));

            // then
            assertThat(response.getOrderStatus()).isEqualTo(COMPLETION.name());
        }
    }
}
