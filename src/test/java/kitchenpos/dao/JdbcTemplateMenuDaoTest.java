package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kitchenpos.DomainFactory.createMenu;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JdbcTemplateMenuDaoTest extends JdbcTemplateDaoTest {
    @Autowired
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;

    @BeforeEach
    private void setUp() {
        menuIds = new ArrayList<>();

        saveMenuGroup(1L, "한마리메뉴");
    }

    @DisplayName("메뉴 저장")
    @Test
    void saveTest() {
        Menu menu = createMenu(1L, "후라이드", BigDecimal.valueOf(2_000));

        Menu savedMenu = jdbcTemplateMenuDao.save(menu);
        menuIds.add(savedMenu.getId());

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualTo(
                        menu.getPrice().setScale(BIG_DECIMAL_FLOOR_SCALE, BigDecimal.ROUND_FLOOR)),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId())
        );
    }

    @DisplayName("아이디로 메뉴를 찾는다.")
    @Test
    void findByIdTest() {
        Menu menu = createMenu(1L, "후라이드", BigDecimal.valueOf(2_000));
        Menu savedMenu = jdbcTemplateMenuDao.save(menu);

        Menu findMenu = jdbcTemplateMenuDao.findById(savedMenu.getId()).get();
        menuIds.add(savedMenu.getId());

        assertAll(
                () -> assertThat(findMenu.getId()).isEqualTo(savedMenu.getId()),
                () -> assertThat(findMenu.getName()).isEqualTo(savedMenu.getName()),
                () -> assertThat(findMenu.getPrice()).isEqualTo(savedMenu.getPrice()),
                () -> assertThat(findMenu.getMenuGroupId()).isEqualTo(savedMenu.getMenuGroupId())
        );
    }

    @DisplayName("존재하지 않는 메뉴 아이디 입력 시 빈 객체 반환")
    @Test
    void findByIdWithInvalidMenuIdTest() {
        Optional<Menu> findMenu = jdbcTemplateMenuDao.findById(0L);

        assertThat(findMenu).isEqualTo(Optional.empty());
    }

    @DisplayName("저장된 모든 메뉴를 찾는다.")
    @Test
    void findAllTest() {
        Menu fried = createMenu(1L, "후라이드", BigDecimal.valueOf(2_000));
        Menu source = createMenu(1L, "양념치킨", BigDecimal.valueOf(2_000));

        jdbcTemplateMenuDao.save(fried);
        jdbcTemplateMenuDao.save(source);

        List<Menu> allMenus = jdbcTemplateMenuDao.findAll();
        allMenus.forEach(menu -> menuIds.add(menu.getId()));

        assertThat(allMenus).hasSize(2);
    }

    @DisplayName("입력한 메뉴 아이디에 맞는 메뉴 개수를 반환한다.")
    @Test
    void countByIdIn() {
        Menu fried = createMenu(1L, "후라이드", BigDecimal.valueOf(2_000));
        Menu source = createMenu(1L, "양념치킨", BigDecimal.valueOf(2_000));

        Menu savedFried = jdbcTemplateMenuDao.save(fried);
        Menu savedSource = jdbcTemplateMenuDao.save(source);
        menuIds.add(savedFried.getId());
        menuIds.add(savedSource.getId());

        long menuCount = jdbcTemplateMenuDao.countByIdIn(menuIds);

        assertThat(menuCount).isEqualTo(2L);
    }

    @AfterEach
    private void tearDown() {
        deleteMenu();
    }
}
