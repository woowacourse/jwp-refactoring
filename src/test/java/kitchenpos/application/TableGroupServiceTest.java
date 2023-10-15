package kitchenpos.application;

import static kitchenpos.support.fixture.domain.OrderFixture.getOrder;
import static kitchenpos.support.fixture.domain.OrderTableFixture.getOrderTable;
import static kitchenpos.support.fixture.domain.TableGroupFixture.getTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
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
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Nested
    class 테이블들을_단체로_지정_할_때 {

        @Test
        void 지정하려는_테이블이_없을_떄_예외를_던진다() {
            //given
            final TableGroup tableGroup = getTableGroup(Collections.emptyList());

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 지정하려는_테이블의_수가_1개_이하일_떄_예외를_던진다() {
            //given
            final OrderTable orderTable = orderTableDao.save(getOrderTable(OrderTable.EMPTY));
            final TableGroup tableGroup = getTableGroup(List.of(orderTable));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이_있으면_예외를_던진다() {
            //given
            final OrderTable savedOrderTable = orderTableDao.save(getOrderTable(OrderTable.EMPTY));
            final OrderTable unsavedOrderTable = getOrderTable(OrderTable.EMPTY);
            final TableGroup tableGroup = getTableGroup(List.of(savedOrderTable, unsavedOrderTable));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 지정하려는_테이블_중에_비어있지_않은_테이블이_존재하면_예외를_던진다() {
            //given
            final OrderTable orderTable1 = orderTableDao.save(getOrderTable(false));
            final OrderTable orderTable2 = orderTableDao.save(getOrderTable(OrderTable.EMPTY));
            final TableGroup tableGroup = getTableGroup(List.of(orderTable1, orderTable2));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 지정하려는_테이블_중에_이미_단체가_지정된_테이블이_존재하면_예외를_던진다() {
            //given
            final TableGroup tableGroup1 = tableGroupDao.save(getTableGroup());
            final OrderTable orderTable1 = orderTableDao.save(getOrderTable(1, OrderTable.EMPTY));
            final OrderTable orderTable2 = orderTableDao.save(getOrderTable(tableGroup1.getId(), 1, OrderTable.EMPTY));
            final OrderTable orderTable3 = orderTableDao.save(getOrderTable(1, OrderTable.EMPTY));

            final TableGroup tableGroup2 = getTableGroup(List.of(orderTable1, orderTable2, orderTable3));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공시_테이블의_상태가_비어있지_않음으로_변경된다() {
            //given
            final OrderTable orderTable1 = orderTableDao.save(getOrderTable(OrderTable.EMPTY));
            final OrderTable orderTable2 = orderTableDao.save(getOrderTable(OrderTable.EMPTY));
            final TableGroup tableGroup = getTableGroup(List.of(orderTable1, orderTable2));

            //when
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            //then
            assertThat(savedTableGroup.getOrderTables())
                    .extracting(OrderTable::getEmpty)
                    .containsExactly(false, false);
            final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
            assertThat(orderTables)
                    .extracting(OrderTable::getEmpty)
                    .containsExactly(false, false);
        }
    }

    @Nested
    class 단체_지정을_해제_할_때 {

        @CsvSource(value = {"COOKING", "MEAL"})
        @ParameterizedTest(name = "테이블의 주문 상태가 {0}이면 예외를 던진다")
        void 테이블의_주문_상태가_조리중이거나_식사중이면_예외를_던진다(final OrderStatus orderStatus) {
            //given
            final TableGroup tableGroup = tableGroupDao.save(getTableGroup());

            final OrderTable orderTable1 = orderTableDao.save(getOrderTable(tableGroup.getId(), 0, OrderTable.EMPTY));
            final OrderTable orderTable2 = orderTableDao.save(getOrderTable(tableGroup.getId(), 0, OrderTable.EMPTY));
            orderDao.save(getOrder(orderTable1.getId(), orderStatus));
            orderDao.save(getOrder(orderTable2.getId(), orderStatus));

            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공시_테이블의_단체_번호가_null_로_변경된다() {
            //given
            final TableGroup tableGroup = tableGroupDao.save(getTableGroup());

            final OrderTable orderTable1 = orderTableDao.save(getOrderTable(tableGroup.getId(), 0, OrderTable.EMPTY));
            final OrderTable orderTable2 = orderTableDao.save(getOrderTable(tableGroup.getId(), 0, OrderTable.EMPTY));

            //when
            tableGroupService.ungroup(tableGroup.getId());

            //then
            assertThat(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                    .extracting(OrderTable::getTableGroupId)
                    .containsExactly(null, null);
        }
    }
}
