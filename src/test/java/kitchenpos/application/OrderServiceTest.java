package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

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
    class create_성공_테스트 {

        @Test
        void 주문_생성을_할_수_있다() {
            // given
            final var time = LocalDateTime.now();
            final var orderTable = new OrderTable(1L, 3, false);
            orderTable.setId(1L);
            final var orderLineItem = new OrderLineItem(1L, 1L, 5);
            final var order = new Order(1L, "COOKING", time, List.of(orderLineItem));
            order.setId(1L);

            given(menuDao.countByIdIn(List.of(1L))).willReturn(1L);
            given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
            given(orderDao.save(any(Order.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(orderLineItemDao.save(any(OrderLineItem.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            final var actual = orderService.create(order);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("orderedTime")
                    .isEqualTo(order);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 주문_항목이_비어있으면_에러를_반환한다() {
            // given
            final var order = new Order(1L, "COOKING", LocalDateTime.now(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 항목이 비어있습니다.");
        }

        @Test
        void 메뉴의_수와_실제_주문한_메뉴의_수가_다르면_예외를_반환한다() {
            // given
            final var orderLineItem1 = new OrderLineItem(1L, 1L, 5);
            final var orderLineItem2 = new OrderLineItem(1L, 2L, 3);
            final var order = new Order(1L, "COOKING", LocalDateTime.now(), List.of(orderLineItem1, orderLineItem2));

            given(menuDao.countByIdIn(List.of(1L, 2L))).willReturn(1L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 메뉴의 수와 실제 주문한 메뉴의 수가 다릅니다.");
        }

        @Test
        void 존재하지_않는_주문_테이블을_사용하면_예외를_반환한다() {
            // given
            final var orderLineItem = new OrderLineItem(1L, 1L, 5);
            final var order = new Order(1L, "COOKING", LocalDateTime.now(), List.of(orderLineItem));

            given(menuDao.countByIdIn(List.of(1L))).willReturn(1L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 주문_테이블이_비어있으면_예외를_반환한다() {
            // given
            final var orderLineItem = new OrderLineItem(1L, 1L, 5);
            final var orderTable = new OrderTable(1L, 5, true);
            final var order = new Order(1L, "COOKING", LocalDateTime.now(), List.of(orderLineItem));
            order.setId(1L);

            given(menuDao.countByIdIn(List.of(1L))).willReturn(1L);
            given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 주문_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given
            given(orderService.list()).willReturn(Collections.emptyList());

            // when
            final var actual = orderService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문이_하나_이상_존재하면_주문_목록을_반환한다() {
            // given
            final var orderLineItem = new OrderLineItem(1L, 1L, 5);
            final var order = new Order(1L, "COOKING", LocalDateTime.now(), List.of(orderLineItem));

            given(orderService.list()).willReturn(List.of(order));

            final var expected = List.of(order);

            // when
            final var actual = orderService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class list_실패_테스트 {
    }

    @Nested
    class changeOrderStatus_성공_테스트 {

        @Test
        void 주문_상태_변경을_할_수_있다() {
            // given
            final var time = LocalDateTime.now();
            final var orderLineItem = new OrderLineItem(1L, 1L, 5);
            final var order1 = new Order(1L, "COOKING", time, List.of(orderLineItem));
            final var order2 = new Order(1L, "MEAL", time, List.of(orderLineItem));

            given(orderDao.findById(1L)).willReturn(Optional.of(order1));
            given(orderDao.save(any(Order.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(orderLineItemDao.findAllByOrderId(1L)).willReturn(List.of(orderLineItem));

            // when
            final var actual = orderService.changeOrderStatus(1L, order2);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .comparingOnlyFields()
                    .isEqualTo(order2);
        }
    }

    @Nested
    class changeOrderStatus_실패_테스트 {

        @Test
        void 존재하지_않는_주문의_상태를_변경하면_에러를_반환한다() {
            // given
            final var order = new Order(1L, "COOKING", LocalDateTime.now(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 주문입니다.");
        }

        @Test
        void 주문이_완료됐는데_주문_상태를_변경하면_에러를_반환한다() {
            // given
            final var order = new Order(1L, "COMPLETION", LocalDateTime.now(), Collections.emptyList());

            given(orderDao.findById(1L)).willReturn(Optional.of(order));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 완료된 주문은 상태 변경이 불가능합니다.");
        }
    }
}
