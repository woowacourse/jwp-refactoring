package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuGroupDaoTest {

    private MenuGroupDao menuGroupDao;

    @Autowired
    public MenuGroupDaoTest(MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Test
    void save() {
        // given
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");

        // when
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));

        // when
        Optional<MenuGroup> foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId());

        // then
        assertThat(foundMenuGroup).isPresent();
    }

    @Test
    void findAll() {
        // given
        menuGroupDao.save(new MenuGroup("메뉴그룹A"));
        menuGroupDao.save(new MenuGroup("메뉴그룹B"));

        // when
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        int defaultSize = 4;
        assertThat(menuGroups).hasSize(defaultSize + 2);
    }

    @Test
    void existsById() {
        // given
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));

        // when
        boolean exists = menuGroupDao.existsById(savedMenuGroup.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByIdWithEmptyId() {
        // given
        long invalidId = 999L;

        // when
        boolean exists = menuGroupDao.existsById(invalidId);

        // then
        assertThat(exists).isFalse();
    }
}
