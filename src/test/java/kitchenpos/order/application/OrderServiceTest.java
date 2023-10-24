package kitchenpos.order.application;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuName;
import kitchenpos.menu.MenuPrice;
import kitchenpos.menu.application.MenuRepository;
import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;
import kitchenpos.order.OrderLineItemQuantity;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.application.request.OrderLineItemDto;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.request.OrderStatusRequest;
import kitchenpos.ordertable.Empty;
import kitchenpos.ordertable.NumberOfGuests;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.application.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
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

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

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
            given(menuRepository.countByIdIn(any())).willReturn(2L);

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴가 포함되었습니다.");
        }

        @Test
        @DisplayName("주문 테이블(orderTable)을 찾지 못하면 예외가 발생한다.")
        void cannotFindOrderTableDao() {
            // given
            given(request.getOrderLineItems()).willReturn(orderLineItemDtos);
            given(menuRepository.countByIdIn(any())).willReturn(3L);
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("요청에 해당하는 orderTable이 비어있지 않으면 예외가 발생한다.")
        void emptyOrderTable() {
            // given
            final OrderRequest request = new OrderRequest(1L, orderLineItemDtos);
            final OrderTable orderTable = mock(OrderTable.class);
            given(menuRepository.countByIdIn(any())).willReturn(3L);
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderTable.isEmpty()).willReturn(false);

            // when, then
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문을 생성한다.")
        void createOrder() {
            // given
            final OrderTable orderTable = new OrderTable(new NumberOfGuests(0), Empty.EMPTY);
            final OrderRequest request = new OrderRequest(1L, orderLineItemDtos);
            final Order order = new Order(1L, OrderStatus.COOKING, LocalDateTime.now(), Collections.emptyList());
            final Menu menu = new Menu(new MenuName("menuName"), new MenuPrice(BigDecimal.TEN), 1L);
            given(menuRepository.countByIdIn(any())).willReturn(3L);
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(orderRepository.save(any())).willReturn(order);

            // when
            final Order result = orderService.create(request);

            // then
            assertSoftly(softly -> {
                verify(orderRepository, times(1)).save(any());
                assertThat(result).usingRecursiveComparison()
                        .ignoringFields("orderedTime")
                        .isEqualTo(order);
            });
        }
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void list() {
        // given
        final List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(new MenuName("name1"), new MenuPrice(BigDecimal.valueOf(10)), new OrderLineItemQuantity(2)),
                new OrderLineItem(new MenuName("name2"), new MenuPrice(BigDecimal.valueOf(20)), new OrderLineItemQuantity(3)),
                new OrderLineItem(new MenuName("name3"), new MenuPrice(BigDecimal.valueOf(30)), new OrderLineItemQuantity(4))
        );
        final Order order = new Order(1L, OrderStatus.COOKING, LocalDateTime.now(), Collections.emptyList());
        given(orderRepository.findAll()).willReturn(List.of(order));

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
            given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("수정 요청의 주문상태가 COMPLETION이라면 예외가 발생한다.")
        void alreadyOrderStatusIsCompletion() {
            // given
            final Order order = mock(Order.class);
            given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
            given(order.getOrderStatus()).willReturn(OrderStatus.COMPLETION);

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusRequest("MEAL")))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문의 상태를 변경한다.")
        void changeOrderStatus() {
            // given
            final Order order = new Order(1L, OrderStatus.COOKING, LocalDateTime.now(), Collections.emptyList());
            given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));

            // when
            final Order result = orderService.changeOrderStatus(1L, new OrderStatusRequest("COMPLETION"));

            // then
            assertSoftly(softly -> {
                assertThat(result).usingRecursiveComparison()
                        .ignoringFields("orderStatus", "orderedTime")
                        .isEqualTo(order);
                assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
            });
        }
    }
}
