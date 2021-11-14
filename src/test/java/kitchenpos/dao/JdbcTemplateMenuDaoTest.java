package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("Menu Dao 테스트")
class JdbcTemplateMenuDaoTest extends JdbcTemplateDaoTest {

    private JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);
        jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @DisplayName("Menu를 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 Menu는 저장에 성공한다.")
        @Test
        void success() {
            // given
            MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("버거킹 세트"));
            Menu menu = Menu를_생성한다("스테커2 버거", BigDecimal.valueOf(11_900), savedMenuGroup.getId());

            // when
            Menu savedMenu = jdbcTemplateMenuDao.save(menu);

            // then
            assertThat(jdbcTemplateMenuDao.findById(savedMenu.getId())).isPresent();
            assertThat(savedMenu.getName()).isEqualTo(menu.getName());
            assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            assertThat(savedMenu.getMenuProducts()).isEqualTo(menu.getMenuProducts());
            assertThat(savedMenu.getPrice().compareTo(menu.getPrice())).isEqualTo(0);
        }

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("버거킹 세트"));
            Menu menu = Menu를_생성한다(null, BigDecimal.valueOf(11_900), savedMenuGroup.getId());

            // when, then
            assertThatThrownBy(() -> jdbcTemplateMenuDao.save(menu))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }

        @DisplayName("price가 Null인 경우 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("버거킹 세트"));
            Menu menu = Menu를_생성한다("스테커3 버거", null, savedMenuGroup.getId());

            // when, then
            assertThatThrownBy(() -> jdbcTemplateMenuDao.save(menu))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }

        @DisplayName("menuGroupId가 Null인 경우 예외가 발생한다.")
        @Test
        void menuGroupIdNullException() {
            // given
            Menu menu = Menu를_생성한다("스테커3 버거", BigDecimal.valueOf(12_900), null);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateMenuDao.save(menu))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }

        @DisplayName("menuGroupId가 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void noExistGroupIdException() {
            // given
            Menu menu = Menu를_생성한다("스테커3 버거", BigDecimal.valueOf(12_900), Long.MAX_VALUE);

            // when, then
            assertThatThrownBy(() -> jdbcTemplateMenuDao.save(menu))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @DisplayName("ID를 통해 Menu를 조회할 때")
    @Nested
    class FindById {

        @DisplayName("ID가 존재한다면 Menu 조회에 성공한다.")
        @Test
        void present() {
            // given
            MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("버거킹 세트"));
            Menu savedMenu = jdbcTemplateMenuDao.save(Menu를_생성한다("스테커2 버거", BigDecimal.valueOf(11_900), savedMenuGroup.getId()));

            // when
            Optional<Menu> foundMenu = jdbcTemplateMenuDao.findById(savedMenu.getId());

            // then
            assertThat(foundMenu).isPresent();
            assertThat(foundMenu.get()).usingRecursiveComparison()
                .isEqualTo(savedMenu);
        }

        @DisplayName("ID가 존재하지 않는다면 Product 조회에 실패한다.")
        @Test
        void isNotPresent() {
            // when
            Optional<Menu> foundMenu = jdbcTemplateMenuDao.findById(Long.MAX_VALUE);

            // then
            assertThat(foundMenu).isNotPresent();
        }
    }

    @DisplayName("모든 Menu를 조회한다.")
    @Test
    void findAll() {
        // given
        List<Menu> beforeSavedMenus = jdbcTemplateMenuDao.findAll();

        MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("버거킹 세트"));
        beforeSavedMenus.add(jdbcTemplateMenuDao.save(Menu를_생성한다("스테커2 버거", BigDecimal.valueOf(11_900), savedMenuGroup.getId())));
        beforeSavedMenus.add(jdbcTemplateMenuDao.save(Menu를_생성한다("스테커3 버거", BigDecimal.valueOf(12_900), savedMenuGroup.getId())));
        beforeSavedMenus.add(jdbcTemplateMenuDao.save(Menu를_생성한다("스테커4 버거", BigDecimal.valueOf(13_900), savedMenuGroup.getId())));

        // when
        List<Menu> afterSavedMenus = jdbcTemplateMenuDao.findAll();

        // then
        assertThat(afterSavedMenus).hasSize(beforeSavedMenus.size());
        assertThat(afterSavedMenus).usingRecursiveComparison()
            .isEqualTo(beforeSavedMenus);
    }

    @DisplayName("ID가 일치하는 Menu의 개수를 확인한다.")
    @Test
    void countByIdIn() {
        // given
        MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(MenuGroup을_생성한다("버거킹 세트"));
        Menu 스테커2_버거 = jdbcTemplateMenuDao.save(Menu를_생성한다("스테커2 버거", BigDecimal.valueOf(11_900), savedMenuGroup.getId()));
        Menu 스테커3_버거 = jdbcTemplateMenuDao.save(Menu를_생성한다("스테커3 버거", BigDecimal.valueOf(12_900), savedMenuGroup.getId()));
        Menu 스테커4_버거 = jdbcTemplateMenuDao.save(Menu를_생성한다("스테커4 버거", BigDecimal.valueOf(13_900), savedMenuGroup.getId()));

        // when
        long countByIdIn = jdbcTemplateMenuDao.countByIdIn(Arrays.asList(스테커2_버거.getId(), 스테커3_버거.getId(), 스테커4_버거.getId()));

        // then
        assertThat(countByIdIn).isEqualTo(3);
    }

    private Menu Menu를_생성한다(String name, BigDecimal price, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);

        return menu;
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }
}