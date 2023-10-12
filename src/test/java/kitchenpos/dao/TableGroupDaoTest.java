package kitchenpos.dao;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TableGroupDaoTest extends DaoTest {

    @Autowired
    TableGroupDao tableGroupDao;

    @DisplayName("TableGroup 저장 - 성공")
    @Test
    void save_Success() {
        // given && when
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);

        // then
        assertThat(saveTableGroup.getId()).isNotNull();
    }

    @DisplayName("TableGroup ID로 TableGroup 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnTableGroup() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);

        // when
        final TableGroup foundTableGroup = tableGroupDao.findById(saveTableGroup.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 TableGroup이 없습니다."));

        // then
        assertThat(foundTableGroup.getId()).isEqualTo(saveTableGroup.getId());
    }

    @DisplayName("TableGroup ID로 TableGroup 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        // given
        final TableGroup saveTableGroup = saveTableGroup(new OrderTable[0]);

        // when
        final Optional<TableGroup> foundTableGroup = tableGroupDao.findById(saveTableGroup.getId() + 1);

        // then
        assertThat(foundTableGroup.isPresent()).isFalse();
    }

    @DisplayName("전체 TableGroup 조회 - 성공")
    @Test
    void findAll_Success() {
        // given
        final TableGroup saveTableGroup1 = saveTableGroup(new OrderTable[0]);
        final TableGroup saveTableGroup2 = saveTableGroup(new OrderTable[0]);
        final TableGroup saveTableGroup3 = saveTableGroup(new OrderTable[0]);

        // when
        final List<TableGroup> tableGroups = tableGroupDao.findAll();

        // then
        assertThat(tableGroups).hasSize(3);
    }
}
