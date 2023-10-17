package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class TableServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderDao orderDao;

    @Test
    void 테이블을_등록한다() {
        final OrderTable orderTable = new OrderTable(null, null, 0, true);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTable);
    }

    @Test
    void 테이블의_목록을_조회한다() {
        final List<OrderTable> expected = tableService.list();
        for (int i = 0; i < 3; i++) {
            final OrderTable orderTable = new OrderTable(null, null, 0, true);
            expected.add(tableService.create(orderTable));
        }

        final List<OrderTable> result = tableService.list();

        assertThat(result).hasSize(expected.size());
    }

    @Nested
    class 테이블의_상태를_변경한다 {
        private OrderTable savedOrderTable;

        @BeforeEach
        void setUp() {
            final OrderTable orderTable = new OrderTable(null, null, 0, true);
            savedOrderTable = tableService.create(orderTable);
        }

        @Test
        void 테이블의_상태를_정상적으로_변경한다() {
            final OrderTable newOrderTable = new OrderTable(null, null, 0, false);
            final OrderTable changeOrderTable = tableService.changeEmpty(savedOrderTable.getId(), newOrderTable);

            assertThat(changeOrderTable.isEmpty()).isFalse();
        }

        @Test
        void 테이블이_존재하지_않으면_예외가_발생한다() {
            final OrderTable newOrderTable = new OrderTable(null, null, 0, false);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId() + 1, newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_단체_지정_되어있으면_예외가_발생한다() {
            final OrderTable savedOrderTable2 = tableService.create(new OrderTable(null, null, 0, true));
            final List<OrderTable> orderTables = List.of(savedOrderTable, savedOrderTable2);
            final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(null, LocalDateTime.now(), orderTables));
            for (final OrderTable orderTable : orderTables) {
                orderTable.setTableGroupId(savedTableGroup.getId());
                orderTableDao.save(orderTable);
            }

            final OrderTable newOrderTable = new OrderTable(null, null, 0, false);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_완료_상태일때_테이블_상태를_변경한다() {
            orderDao.save(new Order(null, savedOrderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), List.of()));

            final OrderTable newOrderTable = new OrderTable(null, null, 0, false);
            final OrderTable changeOrderTable = tableService.changeEmpty(savedOrderTable.getId(), newOrderTable);

            assertThat(changeOrderTable.isEmpty()).isFalse();
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        void 테이블이_조리_혹은_식사_주문_상태일때_예외가_발생한다(String status) {
            orderDao.save(new Order(null, savedOrderTable.getId(), status, LocalDateTime.now(), List.of()));
            final OrderTable newOrderTable = new OrderTable(null, null, 0, false);

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블의_방문_손님_수를_변경한다 {
        private OrderTable savedOrderTable;

        @BeforeEach
        void setUp() {
            final OrderTable orderTable = new OrderTable(null, null, 0, false);
            savedOrderTable = tableService.create(orderTable);
        }

        @Test
        void 테이블의_방문_손님_수를_정상적으로_변경한다() {
            final OrderTable newOrderTable = new OrderTable(null, null, 5, false);
            final OrderTable changeOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable);

            assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(5);
        }

        @Test
        void 테이블의_방문_손님_수가_0보다_작으면_예외가_발생한다() {
            final OrderTable newOrderTable = new OrderTable(null, null, -1, false);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_존재하지_않으면_예외가_발생한다() {
            final OrderTable newOrderTable = new OrderTable(null, null, 5, false);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId() + 1, newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_비어있는_상태면_예외가_발생한다() {
            final OrderTable orderTable = new OrderTable(null, null, 0, true);
            final OrderTable savedOrderTable = tableService.create(orderTable);
            final OrderTable newOrderTable = new OrderTable(null, null, 5, false);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
