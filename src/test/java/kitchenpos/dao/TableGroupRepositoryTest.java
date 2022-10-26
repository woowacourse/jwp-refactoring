package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.TableGroupFixtures;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class TableGroupRepositoryTest {

    private TableGroupRepository tableGroupRepository;

    @Autowired
    public TableGroupRepositoryTest(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Test
    void save() {
        // given
        TableGroup tableGroup = TableGroupFixtures.createTableGroup();
        // when
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        TableGroup tableGroup = TableGroupFixtures.createTableGroup();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        // when
        Optional<TableGroup> foundTableGroup = tableGroupRepository.findById(savedTableGroup.getId());

        // then
        assertThat(foundTableGroup).isPresent();
    }

    @Test
    void findAll() {
        // given
        TableGroup tableGroup = TableGroupFixtures.createTableGroup();
        tableGroupRepository.save(tableGroup);

        // when
        List<TableGroup> tableGroups = tableGroupRepository.findAll();

        // then
        assertThat(tableGroups).hasSize(1);
    }
}
