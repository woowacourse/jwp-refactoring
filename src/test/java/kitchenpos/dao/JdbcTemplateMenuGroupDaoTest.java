package kitchenpos.dao;

import static kitchenpos.dao.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import kitchenpos.domain.MenuGroup;

@JdbcTest
class JdbcTemplateMenuGroupDaoTest {
    private JdbcTemplateMenuGroupDao menuGroupDao;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:delete.sql")
            .addScript("classpath:initialize.sql")
            .build();
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    void save() {
        MenuGroup menuGroup = createMenuGroup("menuGroup");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void findById() {
        MenuGroup menuGroup = createMenuGroup("menuGroup");

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        MenuGroup expectedMenuGroup = menuGroupDao.findById(savedMenuGroup.getId()).get();

        assertThat(expectedMenuGroup.getId()).isEqualTo(savedMenuGroup.getId());
    }

    @Test
    void findAll() {
        MenuGroup menuGroup1 = createMenuGroup("menuGroup1");
        MenuGroup menuGroup2 = createMenuGroup("menuGroup2");

        menuGroupDao.save(menuGroup1);
        menuGroupDao.save(menuGroup2);
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        assertThat(menuGroups.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("해당 id의 메뉴 그룹이 있는지 확인")
    void existsById() {
        MenuGroup menuGroup = createMenuGroup("menuGroup");

        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        assertThat(menuGroupDao.existsById(savedMenuGroup.getId())).isTrue();
        assertThat(menuGroupDao.existsById(2L)).isFalse();
    }
}
