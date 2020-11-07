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
@Sql("classpath:/truncate.sql")
class MenuDaoTest {

    private MenuDao menuDao;

    private Menu menu;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        menuDao = new JdbcTemplateMenuDao(dataSource);

        menu = MenuFixture.createWithoutId(1L, null, 16000L);
    }

    @DisplayName("Menu를 저장한다.")
    @Test
    void save() {
        Menu saved = menuDao.save(menu);

        assertThat(saved)
            .usingComparatorForType(Comparator.comparingLong(BigDecimal::longValue),
                BigDecimal.class)
            .isEqualToIgnoringGivenFields(menu, "id");
        assertThat(saved).extracting(Menu::getId).isEqualTo(1L);
    }

    @DisplayName("아이디에 해당하는 Menu를 조회한다.")
    @Test
    void findById() {
        Menu saved = menuDao.save(menu);

        assertThat(menuDao.findById(saved.getId()).get())
            .isEqualToComparingFieldByField(saved);
    }

    @DisplayName("모든 Menu를 조회한다.")
    @Test
    void findAll() {
        Menu saved1 = menuDao.save(menu);
        Menu saved2 = menuDao.save(menu);

        assertThat(menuDao.findAll())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }

    @DisplayName("id들에 포함되는 menu 갯수를 조회한다.")
    @Test
    void countByIdIn() {
        Menu saved1 = menuDao.save(menu);
        Menu saved2 = menuDao.save(menu);

        assertThat(menuDao.countByIdIn(Arrays.asList(saved1.getId(), saved2.getId())))
            .isEqualTo(2);
    }
}
