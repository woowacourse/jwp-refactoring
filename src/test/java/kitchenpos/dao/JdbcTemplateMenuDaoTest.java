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
    private static final String DELETE_MENUS = "delete from menu where id in (:id)";
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
        Menu menu = new Menu();
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(2000));
        menu.setMenuGroupId(1L);

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
        Menu menu = new Menu();
        menu.setName("후라이드");
        menu.setPrice(BigDecimal.valueOf(2000));
        menu.setMenuGroupId(1L);
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
        Menu fried = new Menu();
        fried.setName("후라이드");
        fried.setPrice(BigDecimal.valueOf(2000));
        fried.setMenuGroupId(1L);
        Menu source = new Menu();
        source.setName("양념치킨");
        source.setPrice(BigDecimal.valueOf(2000));
        source.setMenuGroupId(1L);

        Menu savedFried = jdbcTemplateMenuDao.save(fried);
        Menu savedSource = jdbcTemplateMenuDao.save(source);

        List<Menu> allMenus = jdbcTemplateMenuDao.findAll();
        menuIds.add(savedFried.getId());
        menuIds.add(savedSource.getId());

        assertThat(allMenus).hasSize(2);
    }

    @DisplayName("입력한 메뉴 아이디에 맞는 메뉴 개수를 반환한다.")
    @Test
    void countByIdIn() {
        Menu fried = new Menu();
        fried.setName("후라이드");
        fried.setPrice(BigDecimal.valueOf(2000));
        fried.setMenuGroupId(1L);
        Menu source = new Menu();
        source.setName("양념치킨");
        source.setPrice(BigDecimal.valueOf(2000));
        source.setMenuGroupId(1L);

        Menu savedFried = jdbcTemplateMenuDao.save(fried);
        Menu savedSource = jdbcTemplateMenuDao.save(source);
        menuIds.add(savedFried.getId());
        menuIds.add(savedSource.getId());

        long menuCount = jdbcTemplateMenuDao.countByIdIn(menuIds);

        assertThat(menuCount).isEqualTo(2L);
    }

    @AfterEach
    private void tearDown() {
        Map<String, Object> params = Collections.singletonMap("id", menuIds);
        namedParameterJdbcTemplate.update(DELETE_MENUS, params);
    }
}
