package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.application.fakedao.InMemoryOrderDao;
import kitchenpos.application.fakedao.InMemoryOrderTableDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderFactory;
import kitchenpos.domain.OrderLineItemFactory;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;


    @BeforeEach
    void setUp() {
        orderDao = new InMemoryOrderDao();
        orderTableDao = new InMemoryOrderTableDao();
    }

    @Nested
    class 테이블_등록시 {

        @Test
        void 손님이_0명이고_비어있는_테이블은_정상_등록된다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, true);
            final var tableService = new TableService(orderDao, orderTableDao);

            // when
            final var saved = tableService.create(table);

            // then
            assertAll(
                    () -> assertThat(saved.isEmpty()).isTrue(),
                    () -> assertThat(saved.getNumberOfGuests()).isZero()
            );
        }

        @Test
        void 손님이_0명이고_비어있지_않은_테이블은_정상_등록된다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var tableService = new TableService(orderDao, orderTableDao);

            // when
            final var saved = tableService.create(table);

            // then
            assertAll(
                    () -> assertThat(saved.isEmpty()).isFalse(),
                    () -> assertThat(saved.getNumberOfGuests()).isZero()
            );
        }

        @Test
        void 손님이_0명이_아니지만_비어있다면_예외가_발생한다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(1, true);
            final var tableService = new TableService(orderDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.create(table);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 비어있는지_여부를_수정할시 {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 주문_상태가_조리중_또는_식사중인_테이블은_비어있는지_여부를_수정할_수_없다(final OrderStatus orderStatus) {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = orderTableDao.save(table);
            final var savedOrder = orderDao.save(OrderFactory.createOrderOf(savedTable.getId(), 1L, OrderLineItemFactory.createOrderLineItemOf(1L, 1L, 1L, 1L)));
            savedOrder.setOrderStatus(orderStatus.name());

            final var tableService = new TableService(orderDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.changeEmpty(savedTable.getId(), table);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹이_지정되어있다면_비어있는_상태로_변경할_수_없다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = orderTableDao.save(table);
            final var tableService = new TableService(orderDao, orderTableDao);

            table.setTableGroupId(1L);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.changeEmpty(savedTable.getId(), table);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_테이블은_비어있지_않은_상태로_변경할_수_있다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = orderTableDao.save(table);
            final var tableService = new TableService(orderDao, orderTableDao);

            final var previousState = savedTable.isEmpty();
            table.setEmpty(true);

            // when
            final var changed = tableService.changeEmpty(savedTable.getId(), table);

            // then
            assertThat(changed.isEmpty()).isNotEqualTo(previousState);
        }

        @Test
        void 비어있지_않은_테이블은_비어있는_상태로_변경할_수_있다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = orderTableDao.save(table);
            final var tableService = new TableService(orderDao, orderTableDao);

            final var previousState = table.isEmpty();
            table.setEmpty(true);

            // when
            final var changed = tableService.changeEmpty(savedTable.getId(), table);

            // then
            assertThat(changed.isEmpty()).isNotEqualTo(previousState);
        }
    }

    @Nested
    class 손님의_수를_변경할시 {

        @Test
        void 손님의_수가_음수라면_예외가_발생한다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = orderTableDao.save(table);
            final var tableService = new TableService(orderDao, orderTableDao);
            table.setNumberOfGuests(-1);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.changeNumberOfGuests(savedTable.getId(), table);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_테이블의_손님의_수를_변경할_수_없다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, true);
            final var savedTable = orderTableDao.save(table);
            final var tableService = new TableService(orderDao, orderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.changeNumberOfGuests(savedTable.getId(), table);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있지_않고_손님의_수가_올바르면_정상적으로_변경된다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = orderTableDao.save(table);
            final var tableService = new TableService(orderDao, orderTableDao);
            table.setNumberOfGuests(2);

            // when
            final var changed = tableService.changeNumberOfGuests(savedTable.getId(), table);

            // then
            assertThat(changed.getNumberOfGuests()).isEqualTo(2);
        }
    }
}
