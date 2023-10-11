package kitchenpos.application;

import java.util.List;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 단쳬_지정_저장 {

        @Test
        void 단쳬_지정을_저장할_수_있다() {
            // given
            final OrderTable firstOrderTable = new OrderTable();
            final OrderTable secondOrderTable = new OrderTable();

            firstOrderTable.setEmpty(true);
            secondOrderTable.setEmpty(true);

            final OrderTable firstSavedOrderTable = orderTableDao.save(firstOrderTable);
            final OrderTable secondSavedOrderTable = orderTableDao.save(secondOrderTable);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(
                    firstSavedOrderTable, secondSavedOrderTable
            ));

            // when
            final TableGroup actual = tableGroupService.create(expected);

            // then
            assertAll(
                    () -> assertThat(actual.getOrderTables()).hasSize(2),
                    () -> assertThat(actual.getId()).isNotNull()
            );
        }

        @Test
        void 주문_테이블이_없다면_예외가_발생한다() {
            // given
            final TableGroup expected = new TableGroup();

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_2개_미만이면_예외가_발생한다() {
            // given
            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(new OrderTable()));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_저장되지_않은_경우_예외가_발생한다() {
            // given
            final OrderTable firstOrderTable = new OrderTable();
            final OrderTable secondOrderTable = new OrderTable();

            firstOrderTable.setEmpty(true);
            secondOrderTable.setEmpty(true);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(
                    firstOrderTable, secondOrderTable
            ));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 저장된_단체_지정_크기가_실제_사이즈와_다르면_예외가_발생한다() {
            // given
            final OrderTable firstOrderTable = new OrderTable();
            final OrderTable secondOrderTable = new OrderTable();

            firstOrderTable.setEmpty(true);
            secondOrderTable.setEmpty(true);

            orderTableDao.save(firstOrderTable);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(
                    firstOrderTable, secondOrderTable
            ));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_빈_상태가_아니라면_예외가_발생한다() {
            // given
            final OrderTable firstOrderTable = new OrderTable();
            final OrderTable secondOrderTable = new OrderTable();

            firstOrderTable.setEmpty(false);
            secondOrderTable.setEmpty(false);

            final OrderTable firstSavedOrderTable = orderTableDao.save(firstOrderTable);
            final OrderTable secondSavedOrderTable = orderTableDao.save(secondOrderTable);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(
                    firstSavedOrderTable, secondSavedOrderTable
            ));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블에_주문_지정이_연결되지_않았다면_예외가_발생한다() {
            // given
            final OrderTable firstOrderTable = new OrderTable();
            final OrderTable secondOrderTable = new OrderTable();

            firstOrderTable.setEmpty(false);
            secondOrderTable.setEmpty(false);

            final OrderTable firstSavedOrderTable = orderTableDao.save(firstOrderTable);
            final OrderTable secondSavedOrderTable = orderTableDao.save(secondOrderTable);

            final TableGroup expected = new TableGroup();
            expected.setOrderTables(List.of(
                    firstSavedOrderTable, secondSavedOrderTable
            ));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("제발 여기");
        }

    }

}
