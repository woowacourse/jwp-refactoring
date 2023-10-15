package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateTableGroupDaoTest extends JdbcTemplateTest {

    private TableGroupDao tableGroupDao;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        tableGroup = makeTableGroup();
    }

    @Test
    void 테이블_그룹을_저장한다_() {
        TableGroup saved = tableGroupDao.save(tableGroup);

        assertThat(saved.getCreatedDate()).isEqualTo(tableGroup.getCreatedDate());
    }

    @Test
    void 식별자로_테이블_그룹을_조회한() {
        TableGroup saved = tableGroupDao.save(tableGroup);

        TableGroup expected = tableGroupDao.findById(saved.getId()).get();
        assertThat(expected.getId()).isEqualTo(saved.getId());
    }

    @Test
    void 모든_테이블그룹을_조회한다() {
        tableGroupDao.save(tableGroup);
        tableGroupDao.save(tableGroup);

        List<TableGroup> tableGroups = tableGroupDao.findAll();
        assertThat(tableGroups.size()).isEqualTo(2);
    }

    private TableGroup makeTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
