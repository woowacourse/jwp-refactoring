package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.TableGroupFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class TableGroupDaoTest {

    @Autowired
    DataSource dataSource;

    TableGroupDao tableGroupDao;

    TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        tableGroup = TableGroupFixture.createWithoutId(null);
    }

    @DisplayName("Save Table Group")
    @Test
    void save() {
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        assertThat(savedTableGroup).isEqualToIgnoringGivenFields(tableGroup, "id");
        assertThat(savedTableGroup).extracting(TableGroup::getId).isEqualTo(1L);
    }

    @DisplayName("Select table group by id")
    @Test
    void findById() {
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        assertThat(tableGroupDao.findById(savedTableGroup.getId()).get())
            .isEqualToComparingFieldByField(savedTableGroup);
    }

    @DisplayName("Select all table group")
    @Test
    void findAll() {
        TableGroup saved1 = tableGroupDao.save(tableGroup);
        TableGroup saved2 = tableGroupDao.save(tableGroup);

        assertThat(tableGroupDao.findAll()).usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }
}
