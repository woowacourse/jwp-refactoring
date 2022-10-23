package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateTableGroupDaoTest {

    private final TableGroupDao tableGroupDao;

    @Autowired
    JdbcTemplateTableGroupDaoTest(final DataSource dataSource) {
        this.tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @Test
    void 저장한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        LocalDateTime now = LocalDateTime.now();
        tableGroup.setCreatedDate(now);

        // when
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // then
        Assertions.assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isEqualTo(now)
        );
    }

    @Test
    void ID로_table_group을_조회할_수_있다() {
        // given
        TableGroup tableGroup = new TableGroup();
        LocalDateTime now = LocalDateTime.now();
        tableGroup.setCreatedDate(now);
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // when
        Optional<TableGroup> foundTableGroup = tableGroupDao.findById(savedTableGroup.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(foundTableGroup).isPresent(),
                () -> assertThat(foundTableGroup.get().getCreatedDate()).isEqualTo(now)
        );
    }

    @Test
    void 일치하는_ID가_없으면_empty를_반환한다() {
        // given
        TableGroup tableGroup = new TableGroup();
        LocalDateTime now = LocalDateTime.now();
        tableGroup.setCreatedDate(now);
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // when
        Optional<TableGroup> foundTableGroup = tableGroupDao.findById(101L);

        // then
        assertThat(foundTableGroup).isEmpty();
    }

    @Test
    void table_group_목록을_조회한다() {
        // given
        TableGroup tableGroup1 = new TableGroup();
        LocalDateTime now = LocalDateTime.now();
        tableGroup1.setCreatedDate(now);
        tableGroupDao.save(tableGroup1);
        TableGroup tableGroup2 = new TableGroup();
        tableGroup2.setCreatedDate(now);
        tableGroupDao.save(tableGroup2);

        // when
        List<TableGroup> tableGroups = tableGroupDao.findAll();

        // then
        assertThat(tableGroups).hasSize(3);
    }
}
