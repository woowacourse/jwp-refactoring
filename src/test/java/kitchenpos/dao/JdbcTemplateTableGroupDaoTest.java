package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcTemplateTableGroupDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    @Test
    void saveAndFindById() {
        //when
        final TableGroup tableGroup = jdbcTemplateTableGroupDao.save(new TableGroup(LocalDateTime.now(), null));

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
