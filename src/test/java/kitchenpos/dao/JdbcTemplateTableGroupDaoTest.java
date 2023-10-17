package kitchenpos.dao;

import kitchenpos.common.repository.RepositoryTest;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateTableGroupDaoTest extends RepositoryTest {

    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @Test
    void saveAndFindById() {
        //when
        final TableGroup tableGroup = jdbcTemplateTableGroupDao.save(new TableGroup(LocalDateTime.now()));

        //then
        assertThat(jdbcTemplateTableGroupDao.findById(tableGroup.getId())).isNotNull();
    }

    @Test
    void findAll() {
        //when
        final List<TableGroup> all = jdbcTemplateTableGroupDao.findAll();

        //then
        assertThat(all).hasSize(1);
    }
}
