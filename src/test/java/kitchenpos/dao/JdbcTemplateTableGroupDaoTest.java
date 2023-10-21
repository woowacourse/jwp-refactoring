package kitchenpos.dao;

import static kitchenpos.common.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.common.DaoTest;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
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
        assertSoftly(softly -> {
            softly.assertThat(savedTableGroup.getId()).isNotNull();
            softly.assertThat(savedTableGroup).usingRecursiveComparison()
                    .ignoringFields("id", "orderTables")
                    .isEqualTo(단체_지정());
        });
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
                .ignoringFields("orderTables")
                .isEqualTo(List.of(단체_지정(tableGroupId_A), 단체_지정(tableGroupId_B)));
    }
}
