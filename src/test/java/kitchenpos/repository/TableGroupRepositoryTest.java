package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.table.Table;
import kitchenpos.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupRepositoryTest extends KitchenPosRepositoryTest {

    @DisplayName("TableGroup 저장 - 성공")
    @Test
    void save_Success() {
        List<Table> createdTables = getCreatedTables();
        TableGroup tableGroup = TableGroup.entityOf(createdTables);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getCreatedDate()).isNotNull();
        assertThat(savedTableGroup.getTables()).containsAll(createdTables);
    }

    @DisplayName("TableGroup ID로 TableGroup 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnTableGroup() {
        TableGroup tableGroup = TableGroup.entityOf(getCreatedTables());
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        TableGroup foundTableGroup = tableGroupRepository.findById(savedTableGroup.getId())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 TableGroup이 없습니다."));

        assertThat(foundTableGroup.getId()).isEqualTo(savedTableGroup.getId());
        assertThat(foundTableGroup.getCreatedDate()).isEqualTo(savedTableGroup.getCreatedDate());
        assertThat(foundTableGroup.getTables()).isEqualTo(savedTableGroup.getTables());
    }

    @DisplayName("TableGroup ID로 TableGroup 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        TableGroup tableGroup = TableGroup.entityOf(getCreatedTables());
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        Optional<TableGroup> foundTableGroup = tableGroupRepository
            .findById(savedTableGroup.getId() + 1);

        assertThat(foundTableGroup.isPresent()).isFalse();
    }

    @DisplayName("전체 TableGroup 조회 - 성공")
    @Test
    void findAll_Success() {
        TableGroup tableGroup = TableGroup.entityOf(getCreatedTables());
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        List<TableGroup> tableGroups = tableGroupRepository.findAll();

        assertThat(tableGroups).isNotNull();
        assertThat(tableGroups).isNotEmpty();
        assertThat(tableGroups).contains(savedTableGroup);
    }

    private List<Table> getCreatedTables() {
        Table table = Table.entityOf(10, true);
        Table savedTable = tableRepository.save(table);

        Table otherTable = Table.entityOf(10, true);
        Table savedOtherTable = tableRepository.save(otherTable);
        return Arrays.asList(savedTable, savedOtherTable);
    }
}
