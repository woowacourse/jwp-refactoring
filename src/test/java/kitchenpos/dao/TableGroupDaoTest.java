package kitchenpos.dao;

import static kitchenpos.constants.DaoConstants.TEST_TABLE_GROUP_CREATED_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupDaoTest extends KitchenPosDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("TableGroup 저장 - 성공")
    @Test
    void save_Success() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(TEST_TABLE_GROUP_CREATED_DATE);

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getCreatedDate()).isEqualTo(TEST_TABLE_GROUP_CREATED_DATE);
        assertThat(savedTableGroup.getOrderTables()).isNull();
    }

    @DisplayName("TableGroup ID로 TableGroup 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(TEST_TABLE_GROUP_CREATED_DATE);
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        TableGroup foundTableGroup = tableGroupDao.findById(savedTableGroup.getId())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 TableGroup이 없습니다."));

        assertThat(foundTableGroup.getId()).isEqualTo(savedTableGroup.getId());
        assertThat(foundTableGroup.getCreatedDate()).isEqualTo(savedTableGroup.getCreatedDate());
        assertThat(foundTableGroup.getOrderTables()).isEqualTo(savedTableGroup.getOrderTables());
    }

    @DisplayName("TableGroup ID로 TableGroup 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(TEST_TABLE_GROUP_CREATED_DATE);
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        Optional<TableGroup> foundTableGroup = tableGroupDao.findById(savedTableGroup.getId() + 1);

        assertThat(foundTableGroup.isPresent()).isFalse();
    }

    @DisplayName("전체 TableGroup 조회 - 성공")
    @Test
    void findAll_Success() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(TEST_TABLE_GROUP_CREATED_DATE);
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        List<TableGroup> tableGroups = tableGroupDao.findAll();

        assertThat(tableGroups).isNotNull();
        assertThat(tableGroups).isNotEmpty();

        List<Long> tableGroupIds = tableGroups.stream()
            .map(TableGroup::getId)
            .collect(Collectors.toList());

        assertThat(tableGroupIds).contains(savedTableGroup.getId());
    }
}
