package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.factory.MenuFactory;
import kitchenpos.factory.MenuProductFactory;
import kitchenpos.factory.OrderFactory;
import kitchenpos.factory.OrderLineItemFactory;
import kitchenpos.factory.OrderTableFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.util.ReflectionTestUtils;

@MockitoSettings
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("모든 Order 를 조회한다")
    @Test
    void list() {
        // given
        Order order = OrderFactory.builder()
            .orderLineItems(OrderLineItemFactory.builder().build())
            .build();
        given(orderRepository.findAll()).willReturn(Collections.singletonList(order));

        // when
        ThrowingCallable throwingCallable = () -> orderService.list();

        // then
        assertThatCode(throwingCallable)
            .doesNotThrowAnyException();
    }

    @Nested
    class CreateTest {

        private List<Long> menuIds;

        private OrderTable orderTable;

        private Order order;

        private Long savedOrderId;

        private Order savedOrder;

        private OrderRequest orderRequest;

        @BeforeEach
        void setUp() {
            MenuProduct menuProduct = MenuProductFactory.builder()
                .productId(1L)
                .quantity(1L)
                .build();

            Menu menu = MenuFactory.builder()
                .id(1L)
                .name("후라이드+후라이드")
                .price(new BigDecimal(19000))
                .menuGroupId(1L)
                .menuProducts(menuProduct)
                .build();

            menuIds = Collections.singletonList(menu.getId());

            OrderLineItem orderLineItem = OrderLineItemFactory.builder()
                .seq(1L)
                .orderId(1L)
                .menuId(menu.getId())
                .quantity(1L)
                .build();

            orderTable = OrderTableFactory.builder()
                .id(1L)
                .numberOfGuests(2)
                .empty(false)
                .build();

            order = OrderFactory.builder()
                .orderTableId(orderTable.getId())
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(orderLineItem)
                .build();

            savedOrderId = 1L;

            savedOrder = OrderFactory.copy(order)
                .id(savedOrderId)
                .orderStatus(OrderStatus.COOKING)
                .build();

            orderRequest = OrderFactory.dto(order);
        }

        @DisplayName("Order 를 생성한다")
        @Test
        void create() {
            // given
            given(menuRepository.countByIdIn(menuIds)).willReturn(1L);
            given(orderTableRepository.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));
            given(orderRepository.save(any(Order.class))).willAnswer(
                invocation -> {
                    Order toSave = invocation.getArgument(0);
                    ReflectionTestUtils.setField(toSave, "id", savedOrderId);
                    return null;
                }
            );
            given(orderLineItemRepository.saveAll(anyList())).willReturn(anyList());

            // when
            OrderResponse result = orderService.create(orderRequest);

            // then
            assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("orderedTime", "orderLineItems")
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(savedOrder);
            assertThat(result.getOrderLineItems())
                .usingRecursiveComparison()
                .isEqualTo(savedOrder.getOrderLineItems().toList());
        }

        @DisplayName("Order 생성 실패한다 - orderLineItems 가 비어있는 경우")
        @Test
        void createFail_whenOrderLineItemDoesNotExist() {
            // given
            order = OrderFactory.copy(order)
                .orderLineItems()
                .build();
            orderRequest = OrderFactory.dto(order);

            // when
            ThrowingCallable throwingCallable = () -> orderService.create(orderRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 생성 실패한다 - orderLineItem 개수가 menu 수와 일치하지 않는 경우")
        @Test
        void createFail_whenOrderLineItemCountIsNotEqualToMenuCount() {
            // given
            given(orderTableRepository.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));
            given(menuRepository.countByIdIn(menuIds)).willReturn(0L);

            // when
            ThrowingCallable throwingCallable = () -> orderService.create(orderRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 생성 실패한다 - orderTable 이 존재하지 않는 경우")
        @Test
        void createFail_whenOrderTableDoesNotExist() {
            // given
            given(orderTableRepository.findById(order.getOrderTableId())).willReturn(Optional.empty());

            // when
            ThrowingCallable throwingCallable = () -> orderService.create(orderRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 생성 실패한다 - orderTable 이 비어있는 경우")
        @Test
        void createFail_whenOrderTableIsEmpty() {
            // given
            orderTable = OrderTableFactory.copy(orderTable)
                .empty(true)
                .build();
            given(orderTableRepository.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));

            // when
            ThrowingCallable throwingCallable = () -> orderService.create(orderRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class ChangeTest {

        private Long orderId;

        private Order order;

        private OrderRequest orderRequest;

        @BeforeEach
        void setUp() {
            orderId = 1L;

            order = OrderFactory.builder()
                .id(orderId)
                .orderTableId(1L)
                .orderStatus(OrderStatus.COOKING)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(OrderLineItemFactory.builder().build())
                .build();

            orderRequest = OrderFactory.dto(order);
        }

        @DisplayName("Order 의 상태를 변경한다")
        @Test
        void changeOrderStatus() {
            // given
            given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

            // when
            OrderResponse result = orderService.changeOrderStatus(orderId, orderRequest);

            // then
            assertThat(result.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());
        }

        @DisplayName("Order 상태 변경 실패한다 - orderId 에 대한 order 가 존재하지 않는 경우")
        @Test
        void changeOrderStatusFail_whenOrderDoesNotExist() {
            // given
            given(orderRepository.findById(orderId)).willReturn(Optional.empty());

            // when
            ThrowingCallable throwingCallable =
                () -> orderService.changeOrderStatus(orderId, orderRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 상태 변경 실패한다 - order 가 이미 완료된 order 인 경우")
        @Test
        void changeOrderStatusFail_whenOrderIsAlreadyCompleted() {
            // given
            order = OrderFactory.copy(order)
                .orderStatus(OrderStatus.COMPLETION)
                .build();
            given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

            // when
            ThrowingCallable throwingCallable =
                () -> orderService.changeOrderStatus(orderId, orderRequest);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }
}
