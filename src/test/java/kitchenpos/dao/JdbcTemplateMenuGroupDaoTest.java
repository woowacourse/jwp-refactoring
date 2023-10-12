package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import kitchenpos.helper.JdbcTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateMenuGroupDaoTest extends JdbcTestHelper {

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @Test
    void 메뉴_그룹을_저장한다() {
        // given
        int beforeSize = jdbcTemplateMenuGroupDao.findAll().size();
        MenuGroup menuGroup = 메뉴_그룹_생성("테스트 그룹");

        // when
        jdbcTemplateMenuGroupDao.save(menuGroup);

        // then
        int afterSize = jdbcTemplateMenuGroupDao.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    void id를_기준으로_찾는다() {
        // given
        MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(메뉴_그룹_생성("테스트 그룹"));

        // when
        Optional<MenuGroup> result = jdbcTemplateMenuGroupDao.findById(menuGroup.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get()).usingRecursiveComparison().isEqualTo(menuGroup);
        });
    }

    @Test
    void 모두_찾는다() {
        // given
        jdbcTemplateMenuGroupDao.save(메뉴_그룹_생성("테스트 그룹"));

        // when
        List<MenuGroup> result = jdbcTemplateMenuGroupDao.findAll();

        // then
        assertThat(result).hasSize(1);
    }
}
