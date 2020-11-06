package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Menu;
import kitchenpos.fixture.MenuFixture;

@JdbcTest
@Sql("classpath:truncate.sql")
class MenuDaoTest {

    @Autowired
    DataSource dataSource;

    MenuDao menuDao;

    Menu menu;

    @BeforeEach
    void setUp() {
        menuDao = new JdbcTemplateMenuDao(dataSource);

        menu = MenuFixture.createWithoutId(1L, null, 16000L);
    }

    @DisplayName("Insert a menu")
    @Test
    void save() {
        Menu saved = menuDao.save(menu);

        assertThat(saved)
            .usingComparatorForType(Comparator.comparingLong(BigDecimal::longValue),
                BigDecimal.class)
            .isEqualToIgnoringGivenFields(menu, "id");
        assertThat(saved).extracting(Menu::getId).isEqualTo(1L);
    }

    @DisplayName("Select a menu")
    @Test
    void findById() {
        Menu saved = menuDao.save(menu);

        assertThat(menuDao.findById(saved.getId()).get())
            .isEqualToComparingFieldByField(saved);
    }

    @DisplayName("Select all menus")
    @Test
    void findAll() {
        Menu saved1 = menuDao.save(menu);
        Menu saved2 = menuDao.save(menu);

        assertThat(menuDao.findAll())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }

    @DisplayName("Select count by ids")
    @Test
    void countByIdIn() {
        Menu saved1 = menuDao.save(menu);
        Menu saved2 = menuDao.save(menu);

        assertThat(menuDao.countByIdIn(Arrays.asList(saved1.getId(), saved2.getId())))
            .isEqualTo(2);
    }
}
