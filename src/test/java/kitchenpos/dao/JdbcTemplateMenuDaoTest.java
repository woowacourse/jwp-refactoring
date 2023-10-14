package kitchenpos.dao;

import static kitchenpos.common.fixture.MenuFixture.메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.common.DaoTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
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
        Menu menu = 메뉴(menuGroupId);

        // when
        Menu savedMenu = jdbcTemplateMenuDao.save(menu);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedMenu).isNotNull();
            softly.assertThat(savedMenu).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(메뉴(menuGroupId));
        });
    }

    @Test
    void ID로_메뉴를_조회한다() {
        // given
        Long menuId = jdbcTemplateMenuDao.save(메뉴(menuGroupId)).getId();

        // when
        Menu menu = jdbcTemplateMenuDao.findById(menuId).get();

        // then
        assertThat(menu).usingRecursiveComparison()
                .isEqualTo(메뉴(menuId, menuGroupId));
    }

    @Test
    void 전체_메뉴를_조회한다() {
        // given
        Long menuId_A = jdbcTemplateMenuDao.save(메뉴(menuGroupId)).getId();
        Long menuId_B = jdbcTemplateMenuDao.save(메뉴(menuGroupId)).getId();

        // when
        List<Menu> menus = jdbcTemplateMenuDao.findAll();

        // then
        assertThat(menus).usingRecursiveComparison()
                .isEqualTo(List.of(메뉴(menuId_A, menuGroupId), 메뉴(menuId_B, menuGroupId)));
    }

    @Test
    void ID_목록을_입력하면_ID_목록에_속하는_메뉴의_개수를_센다() {
        // given
        List<Long> ids = List.of(jdbcTemplateMenuDao.save(메뉴(menuGroupId)).getId());

        // when
        long count = jdbcTemplateMenuDao.countByIdIn(ids);

        // then
        assertThat(count).isEqualTo(1);
    }
}
