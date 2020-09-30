package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class JdbcTemplateMenuDaoTest {
    private static final String INSERT_MENU_GROUP = "insert into menu_group (id, name) values (:id, :name)";
    private static final String DELETE_MENUS = "delete from menu where id in (:ids)";
    private static final int BIG_DECIMAL_FLOOR_SCALE = 2;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplateMenuDao jdbcTemplateMenuDao;

    private List<Long> menuIds;

    @BeforeEach
    private void setUp() {
        menuIds = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);
        params.put("name", "test");
        namedParameterJdbcTemplate.update(INSERT_MENU_GROUP, params);
    }

    @DisplayName("메뉴 저장")
    @Test
    void saveTest() {
        Menu menu = createMenu("후라이드", BigDecimal.valueOf(2000), 1L);

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
        Menu menu = createMenu("후라이드", BigDecimal.valueOf(2000), 1L);
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

    @DisplayName("저장된 모든 메뉴를 찾는다.")
    @Test
    void findAllTest() {
        Menu fried = createMenu("후라이드", BigDecimal.valueOf(2000), 1L);
        Menu source = createMenu("양념치킨", BigDecimal.valueOf(2000), 1L);

        jdbcTemplateMenuDao.save(fried);
        jdbcTemplateMenuDao.save(source);

        List<Menu> allMenus = jdbcTemplateMenuDao.findAll();
        allMenus.forEach(menu -> menuIds.add(menu.getId()));

        assertThat(allMenus).hasSize(2);
    }

    @DisplayName("입력한 메뉴 아이디에 맞는 메뉴 개수를 반환한다.")
    @Test
    void countByIdIn() {
        Menu fried = createMenu("후라이드", BigDecimal.valueOf(2000), 1L);
        Menu source = createMenu("양념치킨", BigDecimal.valueOf(2000), 1L);

        Menu savedFried = jdbcTemplateMenuDao.save(fried);
        Menu savedSource = jdbcTemplateMenuDao.save(source);
        menuIds.add(savedFried.getId());
        menuIds.add(savedSource.getId());

        long menuCount = jdbcTemplateMenuDao.countByIdIn(menuIds);

        assertThat(menuCount).isEqualTo(2L);
    }

    private Menu createMenu(String name, BigDecimal price, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    @AfterEach
    private void tearDown() {
        Map<String, Object> params = Collections.singletonMap("ids", menuIds);
        namedParameterJdbcTemplate.update(DELETE_MENUS, params);
    }
}
