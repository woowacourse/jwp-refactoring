package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableDaoTest extends DaoTest {

    @Autowired
    OrderTableDao orderTableDao;

    @DisplayName("OrderTable 저장 - 성공")
    @Test
    void save_Success() {
        // given && when
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);

        // then
        assertThat(saveOrderTable.getId()).isNotNull();
    }

    @DisplayName("OrderTable ID로 OrderTable 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnOrderTable() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);

        // when
        final OrderTable foundOrderTable = orderTableDao.findById(saveOrderTable.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 orderTable이 없습니다."));

        // then
        assertThat(foundOrderTable.getId()).isEqualTo(saveOrderTable.getId());
    }

    @DisplayName("OrderTable ID로 OrderTable 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable = saveOrderTable(saveTableGroup.getId(), 3, true);

        // when
        final Optional<OrderTable> foundOrderTable = orderTableDao.findById(saveOrderTable.getId() + 1);

        // then
        assertThat(foundOrderTable.isPresent()).isFalse();
    }

    @DisplayName("전체 OrderTable 조회 - 성공")
    @Test
    void findAll_Success() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable1 = saveOrderTable(saveTableGroup.getId(), 3, true);
        final OrderTable saveOrderTable2 = saveOrderTable(saveTableGroup.getId(), 3, true);

        // when
        final List<OrderTable> orderTables = orderTableDao.findAll();

        // then
        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("TableGroup ID로 OrderTable들 조회 - 조회됨, TableGroup ID에 매치되는 경우")
    @Test
    void findAllByTableGroupId_MatchedTableGroupId_ReturnOrderTable() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable1 = saveOrderTable(saveTableGroup.getId(), 3, true);
        final OrderTable saveOrderTable2 = saveOrderTable(saveTableGroup.getId(), 3, true);

        // when
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(saveTableGroup.getId());

        // then
        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("TableGroup ID로 OrderTable들 조회 - 조회되지 않음, TableGroup ID에 매치되지 않는 경우")
    @Test
    void findAllByTableGroupId_NotMatchedTableGroupId_ReturnOrderTable() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);
        final OrderTable saveOrderTable1 = saveOrderTable(saveTableGroup.getId(), 3, true);
        final OrderTable saveOrderTable2 = saveOrderTable(saveTableGroup.getId(), 3, true);

        // when
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(saveTableGroup.getId()+1);

        // then
        assertThat(orderTables).isEmpty();
    }
}
