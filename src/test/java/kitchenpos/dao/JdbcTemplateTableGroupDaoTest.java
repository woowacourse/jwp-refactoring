package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import kitchenpos.helper.JdbcTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateTableGroupDaoTest extends JdbcTestHelper {

    @Autowired
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @Test
    void 저장한다() {
        // given
        TableGroup tableGroup = 단체_지정_생성(List.of());

        // when
        TableGroup result = jdbcTemplateTableGroupDao.save(tableGroup);

        // then
        assertThat(result.getOrderTables()).isNull();
    }

    @Test
    void id값으로_조회한다() {
        // given
        TableGroup tableGroup = 단체_지정_생성(List.of());

        TableGroup saved = jdbcTemplateTableGroupDao.save(tableGroup);

        // when
        Optional<TableGroup> result = jdbcTemplateTableGroupDao.findById(saved.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getId()).isEqualTo(saved.getId());
        });
    }

    @Test
    void 모두_조회한다() {
        // when
        List<TableGroup> result = jdbcTemplateTableGroupDao.findAll();

        // then
        assertThat(result.size()).isZero();
    }
}
