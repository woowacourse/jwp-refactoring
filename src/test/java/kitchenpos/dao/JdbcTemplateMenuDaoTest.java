package kitchenpos.dao;

import static kitchenpos.support.fixture.domain.MenuFixture.CHICKEN_1000;
import static kitchenpos.support.fixture.domain.MenuFixture.PIZZA_2000;
import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JdbcTemplateMenuDaoTest extends JdbcTemplateTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("Menu를 저장한다.")
        void success() {
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            Menu menu = CHICKEN_1000.getMenu(menuGroup.getId());

            Menu savedMenu = jdbcTemplateMenuDao.save(menu);

            Long actual = savedMenu.getId();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private Menu menu;

        @BeforeEach
        void setUp() {
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            menu = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
        }

        @Test
        @DisplayName("Menu ID로 Menu 단일 조회한다.")
        void success() {
            Long id = menu.getId();

            Menu foundMenu = jdbcTemplateMenuDao.findById(id)
                .orElseThrow();

            assertThat(foundMenu).usingRecursiveComparison()
                .isEqualTo(menu);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @BeforeEach
        void setUp() {
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
        }

        @Test
        @DisplayName("Menu 전체 목록을 조회한다.")
        void success() {
            List<Menu> menus = jdbcTemplateMenuDao.findAll();

            assertThat(menus).hasSize(2);
        }
    }

    @Nested
    @DisplayName("countByIdIn 메서드는")
    class CountByIdIn {

        private Menu menu1;
        private Menu menu2;

        @BeforeEach
        void setUp() {
            MenuGroup menuGroup = jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            menu1 = jdbcTemplateMenuDao.save(CHICKEN_1000.getMenu(menuGroup.getId()));
            menu2 = jdbcTemplateMenuDao.save(PIZZA_2000.getMenu(menuGroup.getId()));
        }

        @Test
        @DisplayName("Menu Id 리스트를 전달하면 데이터베이스에 존재하는 개수를 조회한다.")
        void success() {
            Long actual = jdbcTemplateMenuDao.countByIdIn(List.of(menu1.getId(), menu2.getId()));

            assertThat(actual).isEqualTo(2);
        }
    }
}
