package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Import(value = JdbcTemplateMenuGroupDao.class)
@JdbcTest
class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @Test
    void 메뉴_그룹을_저장한다() {
        // given
        int beforeSize = jdbcTemplateMenuGroupDao.findAll().size();
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("테스트 그룹");

        // when
        jdbcTemplateMenuGroupDao.save(menuGroup);

        // then
        int afterSize = jdbcTemplateMenuGroupDao.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    void id를_기준으로_찾는다() {
        // given
        Long fixtureId = 1L;

        // when
        Optional<MenuGroup> result = jdbcTemplateMenuGroupDao.findById(fixtureId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getId()).isEqualTo(fixtureId);
        });
    }

    @Test
    void 모두_찾는다() {
        // given
        int fixtureSize = 4;

        // when
        List<MenuGroup> result = jdbcTemplateMenuGroupDao.findAll();

        // then
        assertThat(result.size()).isEqualTo(fixtureSize);
    }
}
