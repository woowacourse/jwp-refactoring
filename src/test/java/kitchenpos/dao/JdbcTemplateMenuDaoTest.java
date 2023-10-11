package kitchenpos.dao;

import static kitchenpos.common.MenuFixtures.MENU1_MENU_GROUP_ID;
import static kitchenpos.common.MenuFixtures.MENU1_NAME;
import static kitchenpos.common.MenuFixtures.MENU1_PRICE;
import static kitchenpos.common.MenuFixtures.MENU1_REQUEST;
import static kitchenpos.common.MenuFixtures.MENU2_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class JdbcTemplateMenuDaoTest {

    @Autowired
    private DataSource dataSource;

    private MenuDao menuDao;

    @BeforeEach
    void setUp() {
        this.menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @DisplayName("Menu를 영속화한다.")
    void saveMenu() {
        // given
        final Menu menu = MENU1_REQUEST();

        // when
        Menu savedMenu = menuDao.save(menu);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedMenu.getId()).isNotNull();
            softly.assertThat(savedMenu.getName()).isEqualTo(MENU1_NAME);
            softly.assertThat(savedMenu.getPrice()).isEqualTo(MENU1_PRICE);
            softly.assertThat(savedMenu.getMenuGroupId()).isEqualTo(MENU1_MENU_GROUP_ID);
        });
    }

    @Nested
    @DisplayName("모든 Menu 조회 시")
    class FindAll {

        @Test
        @DisplayName("여러 개의 값이 있을 경우 모두 반환한다.")
        void findById() {
            // given
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
            jdbcTemplate.execute("truncate table menu");
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

            final Menu menu1 = MENU1_REQUEST();
            final Menu menu2 = MENU2_REQUEST();
            final Menu savedMenu1 = menuDao.save(menu1);
            final Menu savedMenu2 = menuDao.save(menu2);
            final List<Menu> expected = List.of(savedMenu1, savedMenu2);

            // when
            List<Menu> menus = menuDao.findAll();

            // then
            assertThat(menus).usingRecursiveFieldByFieldElementComparator()
                    .isEqualTo(expected);
        }
    }
}
