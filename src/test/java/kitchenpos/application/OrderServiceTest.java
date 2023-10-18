package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.spy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private Menu noodle;

    private Menu potato;

    private OrderLineItem wooDong;

    private OrderLineItem frenchFries;

    @BeforeEach
    void setUp() {
        noodle = mock(Menu.class);
        potato = mock(Menu.class);
        wooDong = new OrderLineItem(null, noodle, 1);
        frenchFries = new OrderLineItem(null, potato, 1);
    }

    @Nested
    class Create {

        @Test
        void 주문을_생성할_수_있다() {
            // given
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);

            final OrderTable orderTable = mock(OrderTable.class);
            final Order order = new Order(orderTable, COOKING, LocalDateTime.now(), orderLineItems);
            given(menuRepository.countByIdIn(anyList())).willReturn((long) orderLineItems.size());

            final Order spyOrder = spy(new Order(order.getOrderTable(), order.getOrderStatus(), order.getOrderedTime(), new ArrayList<>()));
            given(orderRepository.save(any(Order.class))).willReturn(spyOrder);
            given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(orderTable));

            given(orderLineItemRepository.save(any(OrderLineItem.class)))
                    .willReturn(wooDong)
                    .willReturn(frenchFries);

            final long orderTableId = 1L;
            given(orderTable.getId()).willReturn(orderTableId);

            final long orderId = 1L;
            given(spyOrder.getId()).willReturn(orderId);

            // when
            final Order actual = orderService.create(order);

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTable()).isEqualTo(orderTable),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(COOKING),
                    () -> assertThat(actual.getOrderLineItems())
                            .usingRecursiveFieldByFieldElementComparator()
                            .containsExactly(wooDong, frenchFries)
            );
        }

        @Test
        void 주문_항목이_없으면_예외가_발생한다() {
            // given
            final Order order = new Order(COMPLETION, LocalDateTime.now(), emptyList());

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목의_총합과_메뉴의_총합이_다르면_예외를_발생한다() {
            // given
            final Order order = new Order(COMPLETION, LocalDateTime.now(), List.of(wooDong, frenchFries));

            // when
            final long incorrectMenuSize = order.getOrderLineItems().size() - 1;
            when(menuRepository.countByIdIn(anyList())).thenReturn(incorrectMenuSize);

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_있는_주문_테이블이_없으면_예외가_발생한다() {
            // given
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);
            final Order order = spy(new Order(COMPLETION, LocalDateTime.now(), orderLineItems));

            given(menuRepository.countByIdIn(anyList())).willReturn((long) orderLineItems.size());
            final OrderTable mockOrderTable = mock(OrderTable.class);
            given(order.getOrderTable()).willReturn(mockOrderTable);
            final long orderTableId = 1L;
            given(mockOrderTable.getId()).willReturn(orderTableId);

            // when
            when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_있는_주문_테이블이_비어있으면_예외가_발생한다() {
            // given
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);
            final Order order = spy(new Order(COMPLETION, LocalDateTime.now(), orderLineItems));

            given(menuRepository.countByIdIn(anyList())).willReturn((long) orderLineItems.size());

            final OrderTable mockOrderTable = mock(OrderTable.class);
            given(order.getOrderTable()).willReturn(mockOrderTable);
            final long orderTableId = 1L;
            given(mockOrderTable.getId()).willReturn(orderTableId);

            // when
            final OrderTable emptyOrderTable = new OrderTable(6, true);
            when(orderTableRepository.findById(any())).thenReturn(Optional.ofNullable(emptyOrderTable));

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FindAll {

        @Test
        void 주문을_전체_조회_할_수_있다() {
            // given
            final long orderId = 1L;
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);

            given(orderLineItemRepository.findAllByOrderId(orderId)).willReturn(orderLineItems);

            final Order spyOrder = spy(new Order(COMPLETION, LocalDateTime.now(), new ArrayList<>()));
            given(spyOrder.getId()).willReturn(orderId);
            given(orderRepository.findAll()).willReturn(List.of(spyOrder));

            // when
            final List<Order> actual = orderService.list();

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(1)
                            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "orderLineItems")
                            .containsExactly(spyOrder),
                    () -> assertThat(actual.get(0).getOrderLineItems())
                            .usingRecursiveFieldByFieldElementComparatorIgnoringFields("seq")
                            .containsExactly(wooDong, frenchFries)
            );

        }
    }

    @Nested
    class ChangeOrderStatus {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 주문_상태를_변경할_수_있다(final OrderStatus validedOrderStatus) {
            // given
            final long orderId = 1;
            final List<OrderLineItem> orderLineItems = new ArrayList<>(List.of(wooDong, frenchFries));

            final Order order = new Order(validedOrderStatus, LocalDateTime.now(), orderLineItems);
            given(orderRepository.findById(orderId)).willReturn(Optional.ofNullable(order));

            // when
            final Order expected = new Order(COMPLETION, LocalDateTime.now(), orderLineItems);
            final Order actual = orderService.changeOrderStatus(orderId, expected);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
        }

        @Test
        void 주문_상태가_완료_상태라면_상태를_변경할_수_없다() {
            // given
            final long orderId = 1;
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);

            final Order expected = new Order(COMPLETION, LocalDateTime.now(), orderLineItems);
            final Order actual = spy(new Order(expected.getOrderStatus(), expected.getOrderedTime(), new ArrayList<>()));
            given(orderRepository.findById(orderId)).willReturn(Optional.ofNullable(actual));

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

