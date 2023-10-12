package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

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
        final MenuGroup menu = menuGroupFixtureOf("Chicken-group");

        // when
        final MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menu);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    void find_by_id() {
        // given
        final MenuGroup menu = menuGroupFixtureOf("Chicken-group");
        final MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menu);
        final Long savedMenuGroupId = savedMenuGroup.getId();

        // when
        Optional<MenuGroup> menuGroupDaoById = jdbcTemplateMenuGroupDao.findById(savedMenuGroupId);

        // then
        assertThat(menuGroupDaoById).isPresent();
        assertThat(menuGroupDaoById.get().getId()).isEqualTo(savedMenuGroupId);
    }

    @Test
    void find_by_id_return_empty_when_result_doesnt_exist() {
        // given
        long doesntExistId = 10000L;

        // when
        Optional<MenuGroup> menuGroupDaoById = jdbcTemplateMenuGroupDao.findById(doesntExistId);

        // then
        assertThat(menuGroupDaoById).isEmpty();
    }

    @Test
    void find_all() {
        // given
        final MenuGroup chickenGroup = menuGroupFixtureOf("Chicken-group");
        final MenuGroup pizzaGroup = menuGroupFixtureOf("Pizza-group");
        jdbcTemplateMenuGroupDao.save(chickenGroup);
        jdbcTemplateMenuGroupDao.save(pizzaGroup);

        // when
        List<MenuGroup> findAll = jdbcTemplateMenuGroupDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }

    @Test
    void exists_by_id() {
        // given
        final MenuGroup chickenGroup = menuGroupFixtureOf("Chicken-group");
        final Long savedId = jdbcTemplateMenuGroupDao.save(chickenGroup).getId();

        // when
        boolean existsById = jdbcTemplateMenuGroupDao.existsById(savedId);

        // then
        assertThat(existsById).isTrue();
    }

    private MenuGroup menuGroupFixtureOf(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
