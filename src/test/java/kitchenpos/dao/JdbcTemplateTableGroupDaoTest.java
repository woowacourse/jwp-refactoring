package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

@DaoTest
class JdbcTemplateTableGroupDaoTest {

    private final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    public JdbcTemplateTableGroupDaoTest(final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao) {
        this.jdbcTemplateTableGroupDao = jdbcTemplateTableGroupDao;
    }

    @Test
    void save_table_group() {
        // given
        final TableGroup savedMenuGroupId = tableGroupFixture();

        // when
        final TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(savedMenuGroupId);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @Test
    void find_by_id() {
        // given
        final TableGroup savedMenuGroupId = tableGroupFixture();
        final TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(savedMenuGroupId);
        final Long findId = savedTableGroup.getId();

        // when
        final Optional<TableGroup> tableGroupById = jdbcTemplateTableGroupDao.findById(findId);

        // then
        assertThat(tableGroupById).isPresent();
        assertThat(tableGroupById.get())
                .usingRecursiveComparison()
                .isEqualTo(savedTableGroup);
    }

    @Test
    void find_by_id_return_empty_when_result_doesnt_exist() {
        // given
        final long doesntExistId = 10000L;

        // when
        final Optional<TableGroup> tableGroupById = jdbcTemplateTableGroupDao.findById(doesntExistId);

        // then
        assertThat(tableGroupById).isEmpty();
    }

    @Test
    void find_all() {
        // given
        jdbcTemplateTableGroupDao.save(tableGroupFixture());
        jdbcTemplateTableGroupDao.save(tableGroupFixture());

        // when
        final List<TableGroup> findAll = jdbcTemplateTableGroupDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }

    private TableGroup tableGroupFixture() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
