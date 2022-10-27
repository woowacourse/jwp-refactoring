package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("TableGroup을 저장한다.")
    void save() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));

        assertThat(tableGroup).isEqualTo(tableGroupRepository.findById(tableGroup.getId()).orElseThrow());
    }

    @Test
    @DisplayName("모든 TableGroup을 조회한다.")
    void findAll() {
        TableGroup tableGroup1 = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        TableGroup tableGroup2 = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));

        List<TableGroup> tableGroups = tableGroupRepository.findAll();
        assertThat(tableGroups).containsExactly(tableGroup1, tableGroup2);
    }
}
