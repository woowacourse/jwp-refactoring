package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

@DaoTest
class JdbcTemplateMenuGroupDaoTest {

    private final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    public JdbcTemplateMenuGroupDaoTest(final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao) {
        this.jdbcTemplateMenuGroupDao = jdbcTemplateMenuGroupDao;
    }

    @Test
    void save_menu_group() {
        // given
        final MenuGroup menu = menuGroupFixtureFrom("Chicken-group");

        // when
        final MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menu);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    void find_by_id() {
        // given
        final MenuGroup menu = menuGroupFixtureFrom("Chicken-group");
        final MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menu);
        final Long savedMenuGroupId = savedMenuGroup.getId();

        // when
        final Optional<MenuGroup> menuGroupDaoById = jdbcTemplateMenuGroupDao.findById(savedMenuGroupId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(menuGroupDaoById).isPresent();
            softly.assertThat(menuGroupDaoById.get())
                    .usingRecursiveComparison()
                    .isEqualTo(savedMenuGroup);
        });
    }

    @Test
    void find_by_id_return_empty_when_result_doesnt_exist() {
        // given
        final long doesntExistId = 10000L;

        // when
        final Optional<MenuGroup> menuGroupDaoById = jdbcTemplateMenuGroupDao.findById(doesntExistId);

        // then
        assertThat(menuGroupDaoById).isEmpty();
    }

    @Test
    void find_all() {
        // given
        final MenuGroup chickenGroup = menuGroupFixtureFrom("Chicken-group");
        final MenuGroup pizzaGroup = menuGroupFixtureFrom("Pizza-group");
        jdbcTemplateMenuGroupDao.save(chickenGroup);
        jdbcTemplateMenuGroupDao.save(pizzaGroup);

        // when
        final List<MenuGroup> findAll = jdbcTemplateMenuGroupDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }

    @Test
    void exists_by_id_when_exist() {
        // given
        final MenuGroup chickenGroup = menuGroupFixtureFrom("Chicken-group");
        final Long savedId = jdbcTemplateMenuGroupDao.save(chickenGroup).getId();

        // when
        final boolean existsById = jdbcTemplateMenuGroupDao.existsById(savedId);

        // then
        assertThat(existsById).isTrue();
    }

    @Test
    void exists_by_id_when_doesnt_exist() {
        // when
        final boolean existsById = jdbcTemplateMenuGroupDao.existsById(1L);

        // then
        assertThat(existsById).isFalse();
    }

    private MenuGroup menuGroupFixtureFrom(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
