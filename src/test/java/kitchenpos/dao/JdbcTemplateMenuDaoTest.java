package kitchenpos.dao;

import static kitchenpos.common.fixture.MenuFixture.새_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
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
class JdbcTemplateMenuDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private Long menuGroupId;

    @BeforeEach
    void setUp() {
        jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);

        JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        menuGroupId = jdbcTemplateMenuGroupDao.save(new MenuGroup("menuGroup")).getId();
    }

    @Test
    void 메뉴를_저장한다() {
        // given
        Menu menu = 새_메뉴(menuGroupId);

        // when
        Menu savedMenu = jdbcTemplateMenuDao.save(menu);

        // then
        assertThat(savedMenu).usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringActualNullFields()
                .isEqualTo(새_메뉴(menuGroupId));
    }

    @Test
    void ID로_메뉴를_조회한다() {
        // given
        Long menuId = jdbcTemplateMenuDao.save(새_메뉴(menuGroupId)).getId();

        // when
        Menu menu = jdbcTemplateMenuDao.findById(menuId).get();

        // then
        assertThat(menu).usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(새_메뉴(menuId, menuGroupId));
    }

    @Test
    void 전체_메뉴를_조회한다() {
        // given
        Long menuId1 = jdbcTemplateMenuDao.save(새_메뉴(menuGroupId)).getId();
        Long menuId2 = jdbcTemplateMenuDao.save(새_메뉴(menuGroupId)).getId();

        // when
        List<Menu> menus = jdbcTemplateMenuDao.findAll();

        // then
        assertThat(menus).usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(List.of(새_메뉴(menuId1, menuGroupId), 새_메뉴(menuId2, menuGroupId)));
    }

    @Test
    void ID_목록을_입력하면_ID_목록에_속하는_메뉴의_개수를_센다() {
        // given
        List<Long> ids = List.of(jdbcTemplateMenuDao.save(새_메뉴(menuGroupId)).getId());

        // when
        long count = jdbcTemplateMenuDao.countByIdIn(ids);

        // then
        assertThat(count).isEqualTo(1);
    }
}
