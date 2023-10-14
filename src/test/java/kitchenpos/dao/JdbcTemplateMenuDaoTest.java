package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

@DaoTest
class JdbcTemplateMenuDaoTest {

    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    public JdbcTemplateMenuDaoTest(
            final JdbcTemplateMenuDao jdbcTemplateMenuDao,
            final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao
    ) {
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
        this.jdbcTemplateMenuGroupDao = jdbcTemplateMenuGroupDao;
    }

    @Test
    void save_menu() {
        // given
        final Long savedMenuGroupId = getMenuGroupFixtureIdFrom("Chicken-group");
        final Menu menu = menuFixtureOf("chicken", 22000L, savedMenuGroupId);

        // when
        final Menu savedMenu = jdbcTemplateMenuDao.save(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    void find_by_id() {
        // given
        final Long savedMenuGroupId = getMenuGroupFixtureIdFrom("Chicken-group");
        final Menu menu = menuFixtureOf("chicken", 22000L, savedMenuGroupId);
        final Menu savedMenu = jdbcTemplateMenuDao.save(menu);
        final Long findId = savedMenu.getId();

        // when
        final Optional<Menu> findByIdMenu = jdbcTemplateMenuDao.findById(findId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(findByIdMenu).isPresent();
            softly.assertThat(findByIdMenu.get())
                    .usingRecursiveComparison()
                    .isEqualTo(savedMenu);
        });
    }

    @Test
    void find_by_id_return_empty_when_result_doesnt_exist() {
        // given
        final long doesntExistId = 10000L;

        // when
        final Optional<Menu> findByIdMenu = jdbcTemplateMenuDao.findById(doesntExistId);

        // then
        assertThat(findByIdMenu).isEmpty();
    }

    @Test
    void find_all() {
        // given
        final Long savedMenuGroupId = getMenuGroupFixtureIdFrom("Chicken-group");
        final Menu chicken = menuFixtureOf("chicken", 22000L, savedMenuGroupId);
        final Menu seasoningChicken = menuFixtureOf("seasoningChicken", 24000L, savedMenuGroupId);
        jdbcTemplateMenuDao.save(chicken);
        jdbcTemplateMenuDao.save(seasoningChicken);

        // when
        final List<Menu> findAll = jdbcTemplateMenuDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }

    @Test
    void count_by_id_in() {
        // given
        final Long savedMenuGroupId = getMenuGroupFixtureIdFrom("Chicken-group");
        final Menu chicken = menuFixtureOf("chicken", 22000L, savedMenuGroupId);
        final Menu seasoningChicken = menuFixtureOf("seasoningChicken", 24000L, savedMenuGroupId);
        final List<Long> findIds = List.of(
                jdbcTemplateMenuDao.save(chicken).getId(),
                jdbcTemplateMenuDao.save(seasoningChicken).getId()
        );

        // when
        final long countByIdIn = jdbcTemplateMenuDao.countByIdIn(findIds);

        // then
        assertThat(countByIdIn).isEqualTo(2);
    }

    private Menu menuFixtureOf(final String name, final Long price, final Long menuGroupId) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    private Long getMenuGroupFixtureIdFrom(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return jdbcTemplateMenuGroupDao.save(menuGroup).getId();
    }
}
