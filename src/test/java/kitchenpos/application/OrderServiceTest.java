package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Nested
    class CreateTest {
        @Test
        @DisplayName("orderLineItems가 비어있으면 예외가 발생한다.")
        void emptyOrderLineItems() {
            // given
            final OrderRequest request = new OrderRequest(1L, Collections.emptyList());
            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청한 메뉴가 존재하지 않으면 예외가 발생한다.")
        void doesNotMatchMenuSize() {
            // given
            final List<OrderLineItemDto> orderLineItemDtos = List.of(
                    new OrderLineItemDto(1L, 2L),
                    new OrderLineItemDto(2L, 3L),
                    new OrderLineItemDto(3L, 4L)
            );
            final OrderRequest request = new OrderRequest(1L, orderLineItemDtos);
            given(menuDao.countByIdIn(any())).willReturn(999L);

            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청에 해당하는 orderTable을 찾지 못하면 예외가 발생한다.")
        void cannotFindOrderTable() {
            // given
            final List<OrderLineItemDto> orderLineItemDtos = List.of(
                    new OrderLineItemDto(1L, 2L),
                    new OrderLineItemDto(2L, 3L),
                    new OrderLineItemDto(3L, 4L)
            );
            final OrderRequest request = new OrderRequest(1L, orderLineItemDtos);
            given(menuDao.countByIdIn(any())).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청에 해당하는 orderTable이 비어있으면(empty) 예외가 발생한다.")
        void emptyOrderTable() {
            // given
            final List<OrderLineItemDto> orderLineItemDtos = List.of(
                    new OrderLineItemDto(1L, 2L),
                    new OrderLineItemDto(2L, 3L),
                    new OrderLineItemDto(3L, 4L)
            );
            final OrderRequest request = new OrderRequest(1L, orderLineItemDtos);
            final OrderTable orderTable = new OrderTable(1L, 2L, 0, true);
            given(menuDao.countByIdIn(any())).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문을 생성한다.")
        void createOrder() {
            // given
            final List<OrderLineItemDto> orderLineItemDtos = List.of(
                    new OrderLineItemDto(1L, 2L),
                    new OrderLineItemDto(2L, 3L),
                    new OrderLineItemDto(3L, 4L)
            );
            final OrderRequest request = new OrderRequest(1L, orderLineItemDtos);

            final OrderTable orderTable = new OrderTable(1L, 2L, 3, false);
            given(menuDao.countByIdIn(any())).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderDao.save(any())).willReturn(mock(Order.class));

            // when
            final Order result = orderService.create(request);

            // then
            assertSoftly(softly -> {
                verify(orderDao, times(1)).save(any());
                verify(orderLineItemDao, times(3)).save(any());
            });
        }
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void list() {
        // given
        final List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(1L, 1L, 1L, 2),
                new OrderLineItem(2L, 1L, 2L, 3),
                new OrderLineItem(3L, 1L, 3L, 4)
        );
        final List<Order> orders = List.of(
                new Order(1L, 1L, "orderStatus", LocalDateTime.now(), orderLineItems)
        );
        given(orderDao.findAll()).willReturn(orders);

        // when
        final List<Order> result = orderService.list();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(orders);
    }

    @Nested
    class ChangeOrderStatusTest {
        @Test
        @DisplayName("이미 orderStatus가 COMPLETION으로 되었다면 예외가 발생한다.")
        void alreadyOrderStatusIsCompletion() {
            // given
            final Order order = mock(Order.class);
            given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
            given(order.getOrderStatus()).willReturn("COMPLETION");

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusRequest("MEAL")))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문의 상태를 변경한다.")
        void changeOrderStatus() {
            // given
            final Order order = mock(Order.class);
            given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
            given(order.getOrderStatus()).willReturn("MEAL");

            // when
            final Order result = orderService.changeOrderStatus(1L, new OrderStatusRequest("COMPLETION"));

            // then
            assertSoftly(softly -> {
                verify(orderDao, times(1)).save(any());
                verify(orderLineItemDao, times(1)).findAllByOrderId(anyLong());
            });
        }
    }
}
