package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuGroupDaoTest extends DaoTest {
    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void save() throws Exception {
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        MenuGroup foundMenuGroup = menuGroupDao
            .findById(savedMenuGroup.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedMenuGroup.getId()).isEqualTo(foundMenuGroup.getId());
        assertThat(savedMenuGroup.getName()).isEqualTo(foundMenuGroup.getName());
    }

    @Test
    void findById() throws Exception {
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        MenuGroup foundMenuGroup = menuGroupDao
            .findById(savedMenuGroup.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedMenuGroup.getId()).isEqualTo(foundMenuGroup.getId());
        assertThat(savedMenuGroup.getName()).isEqualTo(foundMenuGroup.getName());
    }

    @Test
    void findAll() {
        menuGroupDao.save(new MenuGroup("메뉴그룹1"));
        menuGroupDao.save(new MenuGroup("메뉴그룹2"));
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        assertThat(menuGroups).hasSize(2);
    }

    @Test
    void existsById() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹"));
        boolean result = menuGroupDao.existsById(menuGroup.getId());
        assertThat(result).isEqualTo(true);
    }
}
