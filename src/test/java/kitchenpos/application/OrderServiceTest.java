package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Collections;
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
import kitchenpos.supports.OrderFixture;
import kitchenpos.supports.OrderLineItemFixture;
import kitchenpos.supports.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
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
    OrderService orderService;

    @Nested
    class 주문_생성 {

        @Test
        void 주문_항목이_null이면_예외() {
            // given
            List<OrderLineItem> orderLineItems = null;
            Order order = OrderFixture.fixture().orderLineItems(orderLineItems).build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 없습니다.");
        }

        @Test
        void 주문_항목이_없으면_예외() {
            // given
            List<OrderLineItem> orderLineItems = Collections.emptyList();
            Order order = OrderFixture.fixture().orderLineItems(orderLineItems).build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 없습니다.");
        }

        @Test
        void 주문_항목에_중복된_메뉴가_포함될_수_없다() {
            // given
            List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemFixture.fixture().menuId(1L).build(),
                OrderLineItemFixture.fixture().menuId(1L).build()
            );
            Order order = OrderFixture.fixture().orderLineItems(orderLineItems).build();

            given(menuDao.countByIdIn(List.of(1L, 1L)))
                .willReturn(1L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 메뉴입니다.");
        }

        @Test
        void 주문_항목의_메뉴들은_전부_DB에_존재해야한다() {
            // given
            List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemFixture.fixture().menuId(1L).build(),
                OrderLineItemFixture.fixture().menuId(2L).build()
            );
            Order order = OrderFixture.fixture().orderLineItems(orderLineItems).build();

            given(menuDao.countByIdIn(List.of(1L, 2L)))
                .willReturn(1L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 메뉴입니다.");
        }

        @Test
        void 주문_테이블은_DB에_존재해야한다() {
            // given
            List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemFixture.fixture().menuId(1L).build(),
                OrderLineItemFixture.fixture().menuId(2L).build()
            );
            Order order = OrderFixture.fixture().orderTableId(1L).orderLineItems(orderLineItems).build();

            given(menuDao.countByIdIn(List.of(1L, 2L)))
                .willReturn(2L);
            given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블이_비어있으면_예외() {
            // given
            List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemFixture.fixture().menuId(1L).build(),
                OrderLineItemFixture.fixture().menuId(2L).build()
            );
            OrderTable orderTable = OrderTableFixture.fixture().id(1L).empty(true).build();
            Order order = OrderFixture.fixture().orderTableId(orderTable.getId()).orderLineItems(orderLineItems)
                .build();

            given(menuDao.countByIdIn(List.of(1L, 2L)))
                .willReturn(2L);
            given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 주문 테이블입니다.");
        }

        @Test
        void 주문을_생성한다() {
            // given
            List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemFixture.fixture().menuId(1L).build(),
                OrderLineItemFixture.fixture().menuId(2L).build()
            );
            OrderTable orderTable = OrderTableFixture.fixture().id(1L).empty(false).build();
            OrderFixture orderFixture = OrderFixture.fixture().orderLineItems(orderLineItems);
            Order order = orderFixture.build();
            OrderFixture savedOrderFixture = orderFixture.id(1L).orderTableId(orderTable.getId())
                .orderStatus(OrderStatus.COOKING.name())
                .orderedTime(LocalDateTime.now());
            Order savedOrder = savedOrderFixture.build();
            List<OrderLineItem> savedOrderLineItems = List.of(
                OrderLineItemFixture.fixture().seq(1L).menuId(1L).orderId(savedOrder.getId()).build(),
                OrderLineItemFixture.fixture().seq(2L).menuId(2L).orderId(savedOrder.getId()).build()
            );

            given(menuDao.countByIdIn(List.of(1L, 2L)))
                .willReturn(2L);
            given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));
            given(orderDao.save(order))
                .willReturn(savedOrder);
            given(orderLineItemDao.save(orderLineItems.get(0)))
                .willReturn(savedOrderLineItems.get(0));
            given(orderLineItemDao.save(orderLineItems.get(1)))
                .willReturn(savedOrderLineItems.get(1));

            // when
            Order actual = orderService.create(order);

            // then
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(savedOrderFixture.orderLineItems(savedOrderLineItems).build());
        }
    }

    @Nested
    class 주문_전체_조회 {

        @Test
        void 전체_주문을_주문_항목과_함께_조회한다() {
            List<Order> orders = List.of(
                OrderFixture.fixture().id(1L).build(),
                OrderFixture.fixture().id(2L).build()
            );

            List<List<OrderLineItem>> orderLineItems = List.of(
                List.of(
                    OrderLineItemFixture.fixture().seq(1L).orderId(1L).build(),
                    OrderLineItemFixture.fixture().seq(2L).orderId(1L).build()
                ),
                List.of(
                    OrderLineItemFixture.fixture().seq(3L).orderId(2L).build(),
                    OrderLineItemFixture.fixture().seq(4L).orderId(2L).build()
                )
            );

            given(orderDao.findAll())
                .willReturn(orders);
            given(orderLineItemDao.findAllByOrderId(orders.get(0).getId()))
                .willReturn(orderLineItems.get(0));
            given(orderLineItemDao.findAllByOrderId(orders.get(1).getId()))
                .willReturn(orderLineItems.get(1));

            // when
            List<Order> actual = orderService.list();

            // then
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(
                    List.of(
                        OrderFixture.fixture().id(1L).orderLineItems(orderLineItems.get(0)).build(),
                        OrderFixture.fixture().id(2L).orderLineItems(orderLineItems.get(1)).build()
                    )
                );
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문은_DB에_존재해야한다() {
            // given
            Long orderId = 1L;
            Order order = OrderFixture.fixture().build();
            given(orderDao.findById(orderId))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문입니다.");
        }

        @Test
        void 기존_주문_상태가_COMPLETION이면_예외() {
            // given
            Long orderId = 1L;
            OrderStatus orderStatus = OrderStatus.COMPLETION;
            OrderFixture orderFixture = OrderFixture.fixture().orderStatus(orderStatus);
            Order order = orderFixture.build();
            Order savedOrder = orderFixture.id(orderId).build();

            given(orderDao.findById(orderId))
                .willReturn(Optional.of(savedOrder));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 계산 완료된 주문입니다.");
        }

        @ValueSource(strings = {"COOKING", "MEAL"})
        @ParameterizedTest
        void 주문id와_주문상태를_입력받아_주문상태를_변경한다(String orderStatus) {
            // given
            Long orderId = 1L;
            OrderFixture orderFixture = OrderFixture.fixture().orderStatus(orderStatus);
            Order order = orderFixture.build();
            Order savedOrder = orderFixture.id(orderId).build();
            List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemFixture.fixture().seq(1L).build(),
                OrderLineItemFixture.fixture().seq(2L).build()
            );

            given(orderDao.findById(orderId))
                .willReturn(Optional.of(savedOrder));
            given(orderLineItemDao.findAllByOrderId(orderId))
                .willReturn(orderLineItems);

            // when
            Order actual = orderService.changeOrderStatus(orderId, order);

            // then
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(
                    orderFixture
                        .id(orderId)
                        .orderLineItems(orderLineItems)
                        .build()
                );
        }
    }
}
