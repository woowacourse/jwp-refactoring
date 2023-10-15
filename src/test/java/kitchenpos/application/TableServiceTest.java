package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.support.fixture.domain.OrderFixture.getOrder;
import static kitchenpos.support.fixture.domain.OrderTableFixture.getOrderTable;
import static kitchenpos.support.fixture.domain.TableGroupFixture.getTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableServiceTest {

    private static final long NOT_EXIST_ORDER_TABLE_ID = -1L;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void 테이블을_생성한다() {
        //given
        final OrderTable orderTable = getOrderTable(1, true);

        //when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        //then
        assertThat(orderTableDao.findById(savedOrderTable.getId())).isPresent();
    }

    @Nested
    class 테이블의_상태를_변경시 {

        @Test
        void 존재하지_않는_테이블이면_예외를_던진다() {
            //given
            //when
            //then
            assertThatThrownBy(() -> tableService.changeEmpty(NOT_EXIST_ORDER_TABLE_ID, getOrderTable(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정이_되어있는_테이블이면_예외를_던진다() {
            //given
            final TableGroup tableGroup = tableGroupDao.save(getTableGroup());
            final OrderTable orderTable = orderTableDao.save(getOrderTable(tableGroup.getId(), false));

            //when
            //then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @CsvSource(value = {"COOKING", "MEAL"})
        @ParameterizedTest(name = "테이블의 주문상태가 {0}일 때 예외를 던진다")
        void 테이블의_주문상태가_조리중이거나_식사중일_떄_예외를_던진다(final OrderStatus orderStatus) {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(false));
            orderDao.save(getOrder(orderTable.getId(), orderStatus));

            //when
            //then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), getOrderTable(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블의_주문_상태가_완료이면_성공한다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(false));
            orderDao.save(getOrder(orderTable.getId(), COMPLETION));

            //when
            final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), getOrderTable(true));

            //then
            assertThat(changedOrderTable.isEmpty()).isTrue();
        }
    }

    @Nested
    class 테이블의_손님수를_설정할_때 {

        @Test
        void 손님수가_0보다_작으면_예외를_던진다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(false));

            //when
            //then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), getOrderTable(-1, false)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경하려는_테이블이_없으면_예외를_던진다() {
            //when
            //then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(NOT_EXIST_ORDER_TABLE_ID, getOrderTable(1, false))
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경하려는_테이블이_비어있으면_예외를_던진다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(true));

            //when
            //then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), getOrderTable(0, true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 정상적으로_설정된다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(1, false));

            //when
            final OrderTable updatedOrderTable =
                    tableService.changeNumberOfGuests(orderTable.getId(), getOrderTable(2, false));

            //then
            assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(2);
        }
    }
}
