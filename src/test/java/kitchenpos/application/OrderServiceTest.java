package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderlineitem.OrderLineItem;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        private OrderRequest request;
        private List<OrderLineItemDto> orderLineItemDtos;

        @BeforeEach
        void setUp() {
            request = mock(OrderRequest.class);
            orderLineItemDtos = List.of(
                    new OrderLineItemDto(1L, 2L),
                    new OrderLineItemDto(2L, 3L),
                    new OrderLineItemDto(3L, 4L)
            );
        }

        @Test
        @DisplayName("주문에 해당하는 주문항목(orderLineItem)이 비어있으면 예외가 발생한다.")
        void emptyOrderLineItems() {
            // given
            final OrderRequest request = mock(OrderRequest.class);
            given(request.getOrderLineItems()).willReturn(Collections.emptyList());

            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문항목의 갯수와 저장된 메뉴의 갯수가 다른면 예외가 발생한다.")
        void differentSizeOfOrderLineItemAndMenu() {
            // given
            given(request.getOrderLineItems()).willReturn(orderLineItemDtos);
            given(menuDao.countByIdIn(any())).willReturn(2L);

            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블(orderTable)을 찾지 못하면 예외가 발생한다.")
        void cannotFindOrderTableDao() {
            // given
            given(request.getOrderLineItems()).willReturn(orderLineItemDtos);
            given(menuDao.countByIdIn(any())).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청에 해당하는 orderTable이 비어있으면 예외가 발생한다.")
        void emptyOrderTable() {
            // given
            final OrderRequest request = new OrderRequest(1L, orderLineItemDtos);
            final OrderTable orderTable = mock(OrderTable.class);
            given(menuDao.countByIdIn(any())).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderTable.isEmpty()).willReturn(true);

            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문을 생성한다.")
        void createOrder() {
            // given
            final OrderRequest request = new OrderRequest(1L, orderLineItemDtos);
            final OrderTable orderTable = mock(OrderTable.class);
            final Order order = new Order(1L, OrderStatus.COOKING, LocalDateTime.now());
            given(menuDao.countByIdIn(any())).willReturn(3L);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderDao.save(any())).willReturn(order);

            final List<OrderLineItem> savedOrderLineItems = List.of(
                    new OrderLineItem(1L, 1L, 1L, new Quantity(2L)),
                    new OrderLineItem(2L, 1L, 2L, new Quantity(3L)),
                    new OrderLineItem(3L, 1L, 3L, new Quantity(4L))
            );
            when(orderLineItemDao.save(any()))
                    .thenReturn(savedOrderLineItems.get(0))
                    .thenReturn(savedOrderLineItems.get(1))
                    .thenReturn(savedOrderLineItems.get(2));


            // when
            final Order result = orderService.create(request);

            // then
            assertSoftly(softly -> {
                verify(orderDao, times(1)).save(any());
                verify(orderLineItemDao, times(3)).save(any());
                assertThat(result).usingRecursiveComparison()
                        .ignoringFields("orderedTime")
                        .isEqualTo(order);
                assertThat(result).extracting("orderLineItems")
                        .usingRecursiveComparison()
                        .isEqualTo(savedOrderLineItems);
            });
        }
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void list() {
        // given
        final List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(1L, 1L, 1L, new Quantity(2)),
                new OrderLineItem(2L, 1L, 2L, new Quantity(3)),
                new OrderLineItem(3L, 1L, 3L, new Quantity(4))
        );
        final Order order = new Order(1L, OrderStatus.COOKING, LocalDateTime.now());
        order.updateOrderLineItems(orderLineItems);
        given(orderDao.findAll()).willReturn(List.of(order));

        // when
        final List<Order> result = orderService.list();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(order));
    }

    @Nested
    class ChangeOrderStatusTest {
        @Test
        @DisplayName("요청한 주문을 찾을 수 없으면 예외가 발생한다.")
        void cannotFindOrder() {
            // given
            given(orderDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("수정 요청의 주문상태가 COMPLETION이라면 예외가 발생한다.")
        void alreadyOrderStatusIsCompletion() {
            // given
            final Order order = mock(Order.class);
            given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
            given(order.getOrderStatus()).willReturn(OrderStatus.COMPLETION);

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusRequest("MEAL")))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문의 상태를 변경한다.")
        void changeOrderStatus() {
            // given
            final Order order = new Order(1L, OrderStatus.COOKING, LocalDateTime.now());
            given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

            // when
            final Order result = orderService.changeOrderStatus(1L, new OrderStatusRequest("COMPLETION"));

            // then
            assertSoftly(softly -> {
                verify(orderDao, times(1)).save(any());
                verify(orderLineItemDao, times(1)).findAllByOrderId(anyLong());
                assertThat(result).usingRecursiveComparison()
                        .ignoringFields("orderStatus", "orderedTime")
                        .isEqualTo(order);
                assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
            });
        }
    }
}
