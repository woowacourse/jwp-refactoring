package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Import(value = JdbcTemplateMenuDao.class)
@JdbcTest
class JdbcTemplateMenuDaoTest {

    @Autowired
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;

    @Test
    void 메뉴를_저장한다() {
        // given
        int beforeSize = jdbcTemplateMenuDao.findAll().size();
        Menu menu = new Menu();
        menu.setName("테스트메뉴");
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuGroupId(2L);

        // when
        jdbcTemplateMenuDao.save(menu);

        // then
        int afterSize = jdbcTemplateMenuDao.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }

    @Test
    void 메뉴를_id로_찾는다() {
        // when
        Optional<Menu> result = jdbcTemplateMenuDao.findById(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getId()).isEqualTo(1L);
        });
    }

    @Test
    void 모든_메뉴를_찾는다() {
        // given
        int initFixtureDataSize = 6;

        // when
        List<Menu> result = jdbcTemplateMenuDao.findAll();

        // then
        assertThat(result.size()).isEqualTo(initFixtureDataSize);
    }

    @Test
    void id가_들어간_메뉴의_수를_조회한다() {
        // given

        // when
        long size = jdbcTemplateMenuDao.countByIdIn(List.of(1L, 2L));

        // then
        assertThat(size).isEqualTo(2);
    }
}
