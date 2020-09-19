package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
class JdbcTemplateMenuGroupDaoTest {
    @Autowired
    private JdbcTemplateMenuGroupDao menuGroupDao;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup");
    }

    @Test
    void save() {
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void findById() {
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        MenuGroup foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId()).get();

        assertThat(foundMenuGroup.getId()).isEqualTo(savedMenuGroup.getId());
    }

    @Test
    void findAll() {
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("menuGroup1");
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        menuGroupDao.save(menuGroup1);
        List<MenuGroup> foundMenuGroups = menuGroupDao.findAll();

        assertThat(foundMenuGroups.size()).isEqualTo(menuGroups.size() + 1);
    }

    @Test
    void existsById() {
        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("menuGroup1");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup1);

        assertThat(menuGroupDao.existsById(savedMenuGroup.getId())).isTrue();
        assertThat(menuGroupDao.existsById(0L)).isFalse();
    }
}
