package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

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

    /**
     * TODO
     * - [ ] 주문을 생성한다.
     * - [ ] 주문 정보가 올바르지 않으면 주문을 생성할 수 없다.
     * - [ ] 주문 항목의 수량은 0보다 커야한다.
     * - [ ] 주문 항목 개수와 실제 메뉴의 개수는 일치해야한다.
     * - [ ] 주문 테이블이 존재해야한다.
     * - [ ] 빈 테이블이면 주문할 수 없다.
     * - [ ] 주문 목록을 조회한다.
     * - [ ] 주문 상태를 변경한다.
     * - [ ] 주문 상태가 올바르지 않으면 상태를 변경할 수 없다.
     * - [ ] 주문 상태는 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다.
     * - [ ] 주문 상태가 조리 중이면 식사 혹은 계산완료 변경할 수 있다.
     * - [ ] 주문 상태가 식사 중이면 계산 완료로 변경할 수 있다.
     */

    @Nested
    class 주문_생성 {

        @Test
        void 주문을_생성한다() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_조리중();
            given(menuDao.countByIdIn(anyList()))
                    .willReturn((long) order.getOrderLineItems().size());
            given(orderTableDao.findById(order.getOrderTableId()))
                    .willReturn(Optional.of(ORDER_TABLE.주문_테이블_1_점유중()));
            given(orderDao.save(any(Order.class)))
                    .willReturn(order);
            given(orderLineItemDao.save(any(OrderLineItem.class)))
                    .willReturn(OrderLineItem.builder().build());

            // when
            Order result = orderService.create(order);

            // then
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(order);
        }

        @Test
        void 주문_항목이_비어있으면_예외() {
            // given
            Order order = OrderFixture.ORDER.주문_요청_조리중();
            order.setOrderLineItems(Collections.emptyList());

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
                    .willReturn(Optional.of(ORDER_TABLE.주문_테이블_1_비어있음()));

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
            given(orderDao.findAll())
                    .willReturn(List.of(order));
            given(orderLineItemDao.findAllByOrderId(anyLong()))
                    .willReturn(List.of(OrderLineItem.builder().build()));

            // when
            List<Order> result = orderService.list();

            // then
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(order));
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
                    .isEqualTo(OrderStatus.MEAL.name());
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
