package kitchenpos.dao;

import static kitchenpos.common.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(value = "classpath:test_truncate_table.sql", executionPhase = BEFORE_TEST_METHOD)
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
    void 단체_지정을_저장한다() {
        // given
        TableGroup tableGroup = 단체_지정();

        // when
        TableGroup savedTableGroup = jdbcTemplateTableGroupDao.save(tableGroup);

        // then
        assertThat(savedTableGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(단체_지정());
    }

    @Test
    void ID로_단체_지정을_조회한다() {
        // given
        Long tableGroupId = jdbcTemplateTableGroupDao.save(단체_지정()).getId();

        // when
        TableGroup tableGroup = jdbcTemplateTableGroupDao.findById(tableGroupId).get();

        // then
        assertThat(tableGroup).usingRecursiveComparison()
                .isEqualTo(단체_지정(tableGroupId));
    }

    @Test
    void 전체_단체_지정을_조회한다() {
        // given
        Long tableGroupId_A = jdbcTemplateTableGroupDao.save(단체_지정()).getId();
        Long tableGroupId_B = jdbcTemplateTableGroupDao.save(단체_지정()).getId();

        // when
        List<TableGroup> tableGroups = jdbcTemplateTableGroupDao.findAll();

        // then
        assertThat(tableGroups).usingRecursiveComparison()
                .isEqualTo(List.of(단체_지정(tableGroupId_A), 단체_지정(tableGroupId_B)));
    }

}
