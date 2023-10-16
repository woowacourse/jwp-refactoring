package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
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
    class 주문_생성 {

        @Test
        void 주문을_생성한다() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_조리중();
            OrderLineItem orderLineItem = OrderLineItem.builder().build();
            given(menuDao.countByIdIn(any()))
                    .willReturn((long) order.getOrderLineItems().size());
            given(orderTableDao.findById(order.getOrderTableId()))
                    .willReturn(Optional.of(ORDER_TABLE.주문_테이블_1_비어있는가(false)));
            given(orderDao.save(any(Order.class)))
                    .willReturn(order);
            given(orderLineItemDao.save(any(OrderLineItem.class)))
                    .willReturn(orderLineItem);

            // when
            Order result = orderService.create(order);

            // then
            List<OrderLineItem> expect = IntStream.range(0, order.getOrderLineItems().size())
                    .mapToObj(i -> orderLineItem).collect(Collectors.toList());

            assertThat(result.getOrderLineItems())
                    .usingRecursiveComparison()
                    .isEqualTo(expect);
        }

        @Test
        void 주문_항목이_비어있으면_예외() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_조리중().updateOrderLineItems(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목의_개수와_메뉴의_개수가_일치하지_않으면_예외() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_조리중();

            given(menuDao.countByIdIn(anyList()))
                    .willReturn(0L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_조리중();

            given(menuDao.countByIdIn(anyList()))
                    .willReturn((long) order.getOrderLineItems().size());
            given(orderTableDao.findById(order.getOrderTableId()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_빈_테이블이_포함되어있면_예외() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_조리중();

            given(menuDao.countByIdIn(anyList()))
                    .willReturn((long) order.getOrderLineItems().size());
            given(orderTableDao.findById(order.getOrderTableId()))
                    .willReturn(Optional.of(ORDER_TABLE.주문_테이블_1_비어있는가(true)));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_조회 {

        @Test
        void 주문_목록을_조회한다() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_조리중();
            List<OrderLineItem> orderLineItems = List.of(OrderLineItem.builder().build());
            given(orderDao.findAll())
                    .willReturn(List.of(order));
            given(orderLineItemDao.findAllByOrderId(anyLong()))
                    .willReturn(orderLineItems);

            // when
            List<Order> result = orderService.list();

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(result.get(0).getOrderLineItems()).usingRecursiveComparison().isEqualTo(orderLineItems);
                        softly.assertThat(result.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
                        softly.assertThat(result.get(0).getOrderedTime()).isEqualTo(order.getOrderedTime());
                        softly.assertThat(result.get(0).getOrderTableId()).isEqualTo(order.getOrderTableId());
                    }
            );
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_변경한다() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_조리중();
            Order orderForChange = OrderFixture.ORDER.주문_요청_식사중();
            given(orderDao.findById(anyLong()))
                    .willReturn(Optional.of(order));
            given(orderDao.save(any(Order.class)))
                    .willReturn(orderForChange);
            given(orderLineItemDao.findAllByOrderId(anyLong()))
                    .willReturn(List.of(OrderLineItem.builder().build()));

            // when
            Order result = orderService.changeOrderStatus(order.getId(), orderForChange);

            // then
            assertThat(result.getOrderStatus())
                    .isEqualTo(orderForChange.getOrderStatus());
        }

        @Test
        void 주문_상태가_올바르지_않으면_예외() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_계산_완료();
            Order orderForChange = OrderFixture.ORDER.주문_요청_조리중();
            given(orderDao.findById(anyLong()))
                    .willReturn(Optional.of(order));

            // when & then
            Long id = order.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(id, orderForChange))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "{0} -> {1}이면 예외")
        @CsvSource(value = {"COMPLETION,MEAL", "COMPLETION,COOKING"}, delimiter = ',')
        void 계산_완료_상태에서_주문을_변경하면_예외(OrderStatus previous, OrderStatus current) {
            // given
            Order order = OrderFixture.ORDER.주문_요청(previous);
            Order orderForChange = OrderFixture.ORDER.주문_요청(current);
            given(orderDao.findById(anyLong()))
                    .willReturn(Optional.of(order));

            // when & then
            Long id = order.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(id, orderForChange))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
