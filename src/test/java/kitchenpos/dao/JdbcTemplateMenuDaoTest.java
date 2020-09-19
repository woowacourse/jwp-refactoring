package kitchenpos.dao;

import static kitchenpos.dao.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

class JdbcTemplateMenuDaoTest {
    private DataSource dataSource;
    private JdbcTemplateMenuDao menuDao;
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:delete.sql")
            .addScript("classpath:initialize.sql")
            .build();
        menuDao = new JdbcTemplateMenuDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);

        MenuGroup menuGroup = createMenuGroup("Menu Group1");
        menuGroupDao.save(menuGroup);
    }

    @Test
    void save() {
        Menu menu = createMenu("menu1", 1L, BigDecimal.valueOf(2000));

        Menu expectedMenu = menuDao.save(menu);

        assertAll(
            () -> assertThat(expectedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
            () -> assertThat(expectedMenu.getName()).isEqualTo(menu.getName()),
            () -> assertThat(expectedMenu.getPrice().toBigInteger()).isEqualTo(menu.getPrice().toBigInteger())
        );
    }

    @Test
    void findById() {
        Menu menu = createMenu("menu", 1L, BigDecimal.valueOf(2000));

        Menu savedMenu = menuDao.save(menu);
        Menu expectedMenu = menuDao.findById(savedMenu.getId()).get();

        assertAll(
            () -> assertThat(expectedMenu.getId()).isEqualTo(savedMenu.getId()),
            () -> assertThat(expectedMenu.getPrice().toBigInteger()).isEqualTo(savedMenu.getPrice().toBigInteger()),
            () -> assertThat(expectedMenu.getName()).isEqualTo(savedMenu.getName()),
            () -> assertThat(expectedMenu.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId())
        );
    }

    @Test
    void findAll() {
        Menu menu1 = createMenu("menu1", 1L, BigDecimal.valueOf(2000));
        Menu menu2 = createMenu("menu2", 1L, BigDecimal.valueOf(2000));

        menuDao.save(menu1);
        menuDao.save(menu2);

        List<Menu> menus = menuDao.findAll();

        assertThat(menus.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("ids 에 포함되는 메뉴 아이디의 개수 반환")
    void countByIdIn() {
        Menu menu1 = createMenu("menu1", 1L, BigDecimal.valueOf(2000));
        Menu menu2 = createMenu("menu2", 1L, BigDecimal.valueOf(2000));

        menuDao.save(menu1); //1L
        menuDao.save(menu2); //2L

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Long count = menuDao.countByIdIn(ids);

        assertThat(count).isEqualTo(2);
    }
}
