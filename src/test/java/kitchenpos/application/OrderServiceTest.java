package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @InjectMocks
    private OrderService orderService;

    @Nested
    class 주문_등록 {

        @Test
        void 주문을_등록할_수_있다() {
            // given
            final var order = OrderFixture.주문_망고치킨_2개();
            given(orderDao.save(any()))
                    .willReturn(order);
            given(menuRepository.countByIdIn(any()))
                    .willReturn(1L);
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(OrderTableFixture.주문테이블_2명_id_1()));

            // when
            final var actual = orderService.create(order);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(order);
        }

        @Test
        void 주문_항목이_비어있으면_예외가_발생한다() {
            // given
            final var order = OrderFixture.주문_망고치킨_2개();
            order.setOrderLineItems(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목_중_실제_메뉴에_존재하지_않는_메뉴가_있으면_예외가_발생한다() {
            // given
            final var order = OrderFixture.주문_망고치킨_2개();
            given(menuRepository.countByIdIn(any()))
                    .willReturn(0L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문의_주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final var order = OrderFixture.주문_망고치킨_2개();
            given(menuRepository.countByIdIn(any()))
                    .willReturn(1L);
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블이면_예외가_발생한다() {
            // given
            final var order = OrderFixture.주문_망고치킨_2개();
            given(menuRepository.countByIdIn(any()))
                    .willReturn(1L);
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(OrderTableFixture.빈테이블_1명()));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_조리로_등록된다() {
            // given
            final var order = OrderFixture.주문_망고치킨_2개();
            given(orderDao.save(any()))
                    .willReturn(order);
            given(menuRepository.countByIdIn(any()))
                    .willReturn(1L);
            given(orderTableDao.findById(any()))
                    .willReturn(Optional.of(OrderTableFixture.주문테이블_2명_id_1()));

            // when
            final var actual = orderService.create(order);

            // then
            assertThat(actual.getOrderStatus())
                    .isEqualTo(OrderStatus.COOKING.name());
        }
    }

    @Nested
    class 주문_목록_조회 {

        @Test
        void 주문_목록을_조회할_수_있다() {
            // given
            final var orders = Collections.singletonList(OrderFixture.주문_망고치킨_2개());
            given(orderDao.findAll())
                    .willReturn(orders);
            given(orderLineItemDao.findAllByOrderId(any()))
                    .willReturn(Collections.singletonList(OrderLineItemFixture.주문항목_망고치킨_2개()));

            // when
            final var actual = orderService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(orders);
        }
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_변경할_수_있다() {
            // given
            final var order = OrderFixture.주문_망고치킨_2개();
            given(orderDao.findById(any()))
                    .willReturn(Optional.of(order));

            final var changedOrder = OrderFixture.주문_망고치킨_2개_식사();
            given(orderDao.save(any()))
                    .willReturn(changedOrder);
            given(orderLineItemDao.findAllByOrderId(any()))
                    .willReturn(Collections.singletonList(OrderLineItemFixture.주문항목_망고치킨_2개()));

            // when
            final var actual = orderService.changeOrderStatus(order.getId(), changedOrder);

            // then
            assertThat(actual.getOrderStatus())
                    .isEqualTo(OrderStatus.MEAL.name());
        }

        @Test
        void 주문이_존재하지_않으면_예외가_발생한다() {
            // given
            final var order = OrderFixture.주문_망고치킨_2개();
            given(orderDao.findById(any()))
                    .willReturn(Optional.empty());

            // when & then
            final var id = order.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(id, order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_계산_완료면_예외가_발생한다() {
            // given
            final var order = OrderFixture.주문_망고치킨_2개_주문완료();
            given(orderDao.findById(any()))
                    .willReturn(Optional.of(order));

            // when & then
            final var id = order.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(id, order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
