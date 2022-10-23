package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupDaoTest extends DaoTest {

    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @Test
    @DisplayName("TableGroup을 저장한다.")
    void save() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));

        assertThat(tableGroup).isEqualTo(tableGroupDao.findById(tableGroup.getId()).orElseThrow());
    }

    @Test
    @DisplayName("모든 TableGroup을 조회한다.")
    void findAll() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));

        List<TableGroup> tableGroups = tableGroupDao.findAll();
        assertAll(
                () -> assertThat(tableGroups).isNotEmpty(),
                () -> assertThat(tableGroups).contains(tableGroup)
        );
    }
}
