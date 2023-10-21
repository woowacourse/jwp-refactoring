package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.fakedao.InMemoryOrderDao;
import kitchenpos.dao.fakedao.InMemoryOrderTableDao;
import kitchenpos.domain.OrderFactory;
import kitchenpos.domain.OrderLineItemFactory;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableFactory;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.CannotChangeEmptyException;
import kitchenpos.exception.InvalidGuestNumberException;
import kitchenpos.ui.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.request.OrderTableCreateRequest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    private OrderDao fakeOrderDao;
    private OrderTableDao fakeOrderTableDao;

    @BeforeEach
    void setUp() {
        fakeOrderDao = new InMemoryOrderDao();
        fakeOrderTableDao = new InMemoryOrderTableDao();
    }

    @Nested
    class 테이블_등록시 {

        @Test
        void 손님이_0명이고_비어있는_테이블은_정상_등록된다() {
            // given
            final var request = new OrderTableCreateRequest(0, true);
            final var tableService = new TableService(fakeOrderTableDao);

            // when
            final var saved = tableService.create(request);

            // then
            assertAll(
                    () -> assertThat(saved.isEmpty()).isTrue(),
                    () -> assertThat(saved.getNumberOfGuests()).isZero()
            );
        }

        @Test
        void 손님이_0명이고_비어있지_않은_테이블은_정상_등록된다() {
            // given
            final var table = new OrderTableCreateRequest(0, false);
            final var tableService = new TableService(fakeOrderTableDao);

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
            final var table = new OrderTableCreateRequest(1, true);
            final var tableService = new TableService(fakeOrderTableDao);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.create(table);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidGuestNumberException.class);
        }
    }

    @Nested
    class 비어있는지_여부를_수정할시 {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 주문_상태가_조리중_또는_식사중인_테이블은_비어있는지_여부를_수정할_수_없다(final OrderStatus orderStatus) {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = fakeOrderTableDao.save(table);
            final var savedOrder = fakeOrderDao.save(OrderFactory.createOrderOf(savedTable.getId(), OrderLineItemFactory.createOrderLineItemOf(1L, 1L)));
            savedOrder.setOrderStatus(orderStatus.name());
            table.addOrder(savedOrder);

            final var tableService = new TableService(fakeOrderTableDao);

            final var request = new OrderTableChangeEmptyRequest(true);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.changeEmpty(savedTable.getId(), request);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(CannotChangeEmptyException.class);
        }

        @Test
        void 테이블_그룹이_지정되어있다면_비어있는_상태로_변경할_수_없다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = fakeOrderTableDao.save(table);
            final var tableGroup = new TableGroup(List.of(savedTable));

            final var tableService = new TableService(fakeOrderTableDao);

            final var request = new OrderTableChangeEmptyRequest(true);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.changeEmpty(savedTable.getId(), request);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(CannotChangeEmptyException.class);
        }

        @Test
        void 비어있는_테이블은_비어있지_않은_상태로_변경할_수_있다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, true);
            final var savedTable = fakeOrderTableDao.save(table);
            final var tableService = new TableService(fakeOrderTableDao);

            final var previousState = savedTable.isEmpty();

            final var request = new OrderTableChangeEmptyRequest(false);

            // when
            final var changed = tableService.changeEmpty(savedTable.getId(), request);

            // then
            assertThat(changed.isEmpty()).isNotEqualTo(previousState);
        }

        @Test
        void 비어있지_않은_테이블은_비어있는_상태로_변경할_수_있다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = fakeOrderTableDao.save(table);
            final var tableService = new TableService(fakeOrderTableDao);

            final var previousState = table.isEmpty();

            final var request = new OrderTableChangeEmptyRequest(true);


            // when
            final var changed = tableService.changeEmpty(savedTable.getId(), request);

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
            final var savedTable = fakeOrderTableDao.save(table);
            final var tableService = new TableService(fakeOrderTableDao);
            final var request = new OrderTableChangeNumberOfGuestsRequest(-1);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.changeNumberOfGuests(savedTable.getId(), request);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidGuestNumberException.class);
        }

        @Test
        void 비어있는_테이블의_손님의_수를_변경할_수_없다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, true);
            final var savedTable = fakeOrderTableDao.save(table);
            final var tableService = new TableService(fakeOrderTableDao);
            final var request = new OrderTableChangeNumberOfGuestsRequest(1);

            // when
            final ThrowingCallable throwingCallable = () -> tableService.changeNumberOfGuests(savedTable.getId(), request);

            // then
            assertThatThrownBy(throwingCallable).isInstanceOf(InvalidGuestNumberException.class);
        }

        @Test
        void 비어있지_않고_손님의_수가_올바르면_정상적으로_변경된다() {
            // given
            final var table = OrderTableFactory.createOrderTableOf(0, false);
            final var savedTable = fakeOrderTableDao.save(table);
            final var tableService = new TableService(fakeOrderTableDao);
            final var request = new OrderTableChangeNumberOfGuestsRequest(2);

            // when
            final var changed = tableService.changeNumberOfGuests(savedTable.getId(), request);

            // then
            assertThat(changed.getNumberOfGuests()).isEqualTo(2);
        }
    }
}
