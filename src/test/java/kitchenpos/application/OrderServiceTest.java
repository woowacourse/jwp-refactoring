package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static kitchenpos.application.OrderFixture.createOrder;
import static kitchenpos.application.OrderFixture.createOrderRequest;
import static kitchenpos.application.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.application.OrderTableFixture.createOrderTable;
import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 서비스")
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
    @DisplayName("생성 메서드는")
    class CreateOrder {
        private Order request;

        private Order subject() {
            return orderService.create(request);
        }

        @Nested
        @DisplayName("주문 테이블, 주문 항목들이 주어지면")
        class IfGiven {
            private OrderTable orderTable;

            @BeforeEach
            void setUp() {
                List<OrderLineItem> orderLineItems =
                        Arrays.asList(createOrderLineItem(1L, 2L), createOrderLineItem(2L, 1L));
                orderTable = createOrderTable(10L, false, 5L, 1);
                request = createOrderRequest(orderLineItems, 10L);
            }

            @Test
            @DisplayName("초기상태가 조리 중인 주문을 생성한다")
            void createOrder() {
                given(menuDao.countByIdIn(anyList())).willReturn((long) request.getOrderLineItems().size());
                given(orderTableDao.findById(10L)).willReturn(Optional.of(orderTable));
                given(orderDao.save(any(Order.class))).willAnswer(i -> {
                    Order savedOrder = i.getArgument(0, Order.class);
                    savedOrder.setId(1L);
                    return savedOrder;
                });
                given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(mock(OrderLineItem.class));

                Order result = subject();

                assertAll(
                        () -> assertThat(result.getId()).isNotNull(),
                        () -> assertThat(result.getOrderedTime()).isNotNull(),
                        () -> assertThat(result.getOrderTableId()).isNotNull(),
                        () -> assertThat(result.getOrderStatus()).isEqualTo(COOKING.name()),
                        () -> assertThat(result.getOrderLineItems()).hasSize(2)
                );
            }
        }

        @Nested
        @DisplayName("요청에서 주문 항목이 비어있을 경우")
        class EmptyOrderLineItems {
            @BeforeEach
            void setUp() {
                request = createOrderRequest(null, 5L);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateOrder.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("요청의 주문 테이블 id에 해당하는 주문 테이블이 없는 경우")
        class NotExistIOrderTable {
            @BeforeEach
            void setUp() {
                request = createOrderRequest(Arrays.asList(new OrderLineItem(), new OrderLineItem()), 5L);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                given(menuDao.countByIdIn(anyList())).willReturn(2L);
                given(orderTableDao.findById(5L)).willReturn(Optional.empty());

                assertThatThrownBy(CreateOrder.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("조회 메서드는")
    class FindOrder {
        private List<Order> subject() {
            return orderService.list();
        }

        @Nested
        @DisplayName("주문과 주문 항목들이 저장되어 있으면")
        class IfGiven {
            private List<Order> orders;
            private Map<Long, List<OrderLineItem>> orderLineItems;

            @BeforeEach
            void setUp() {
                orders = Arrays.asList(
                        createOrder(1L, COOKING, 5L),
                        createOrder(2L, COOKING, 5L),
                        createOrder(3L, COOKING, 6L)
                );
                orderLineItems = new HashMap<Long, List<OrderLineItem>>() {{
                    put(1L, Arrays.asList(createOrderLineItem(1L, 1L, 1L)));
                    put(2L, Arrays.asList(createOrderLineItem(2L, 1L, 2L), createOrderLineItem(2L, 2L, 1L)));
                    put(3L, Arrays.asList(createOrderLineItem(3L, 1L, 1L), createOrderLineItem(3L, 2L, 2L)));
                }};
            }

            @Test
            @DisplayName("주문과 주문 항목들을 조회한다")
            void findOrderAndOrderLineItems() {
                given(orderDao.findAll()).willReturn(orders);
                given(orderLineItemDao.findAllByOrderId(anyLong()))
                        .willAnswer(i -> orderLineItems.get(i.getArgument(0, Long.class)));

                List<Order> result = subject();

                assertThat(result).usingElementComparatorIgnoringFields("orderLineItems").isEqualTo(orders);
                assertThat(result).extracting(Order::getOrderLineItems).isEqualTo(new ArrayList<>(orderLineItems.values()));
            }
        }
    }

    @Nested
    @DisplayName("수정 메서드는")
    class ChangeOrder {
        private Long orderId;
        private Order request;

        private Order subject() {
            return orderService.changeOrderStatus(orderId, request);
        }

        @Nested
        @DisplayName("주문이 저장되어 있고, 주문 id와 주문 상태가 주어지면")
        class IfGiven {
            @BeforeEach
            void setUp() {
                orderId = 1L;
                request = OrderFixture.updateOrderRequest(MEAL);
            }

            @Test
            @DisplayName("주문의 상태를 수정하고 바뀐 주문을 반환한다")
            void findOrderAndOrderLineItems() {
                given(orderDao.findById(orderId))
                        .willReturn(Optional.of(createOrder(orderId, COOKING, 5L)));

                Order result = subject();

                verify(orderDao).save(refEq(createOrder(orderId, MEAL, 5L), "orderedTime", "orderLineItems"));
                assertThat(result.getOrderStatus()).isEqualTo(MEAL.name());
            }
        }

        @Nested
        @DisplayName("주문 id에 해당하는 주문이 없을 경우")
        class NotExistOrder {
            @BeforeEach
            void setUp() {
                orderId = 1L;
                request = OrderFixture.updateOrderRequest(MEAL);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                given(orderDao.findById(orderId)).willReturn(Optional.empty());

                assertThatThrownBy(ChangeOrder.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("기존 주문 상태가 '계산 완료'인 경우")
        class CompletedOrder {
            @BeforeEach
            void setUp() {
                orderId = 1L;
                request = OrderFixture.updateOrderRequest(COOKING);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                given(orderDao.findById(orderId))
                        .willReturn(Optional.of(createOrder(orderId, COMPLETION, 5L)));

                assertThatThrownBy(ChangeOrder.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}