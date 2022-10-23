package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class MenuDaoTest {

    @Autowired
    private DataSource dataSource;

    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        this.menuDao = new JdbcTemplateMenuDao(dataSource);
        this.menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    void 메뉴를_저장할_수_있다() {
        // given
        final long menuGroupId = menuGroupDao.save(new MenuGroup("메뉴그룹"))
                .getId();
        final Menu menu = new Menu("메뉴", new BigDecimal(10000), menuGroupId);

        // when
        final Menu savedMenu = menuDao.save(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    void 메뉴를_아이디로_조회할_수_있다() {
        // given
        final long menuGroupId = menuGroupDao.save(new MenuGroup("메뉴그룹"))
                .getId();
        final Menu savedMenu = menuDao.save(new Menu("메뉴", new BigDecimal(10000), menuGroupId));

        // when
        final Menu foundMenu = menuDao.findById(savedMenu.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
                () -> assertThat(foundMenu.getName()).isEqualTo("메뉴"),
                () -> assertThat(foundMenu.getPrice().intValue()).isEqualTo(10000),
                () -> assertThat(foundMenu.getMenuGroupId()).isEqualTo(menuGroupId)
        );
    }

    @Test
    void 모든_메뉴를_조회할_수_있다() {
        // given
        final int alreadyExistCount = menuDao.findAll()
                .size();
        final long menuGroupId = menuGroupDao.save(new MenuGroup("메뉴그룹"))
                .getId();
        final Menu savedMenu = menuDao.save(new Menu("메뉴", new BigDecimal(10000), menuGroupId));

        // when
        final List<Menu> menus = menuDao.findAll();

        // then
        assertThat(menus).usingFieldByFieldElementComparator()
                .hasSize(alreadyExistCount + 1)
                .contains(savedMenu);
    }

    @Test
    void 아이디_목록으로_존재하는_메뉴의_개수를_반환한다() {
        // given
        final long menuGroupId = menuGroupDao.save(new MenuGroup("메뉴그룹"))
                .getId();
        final Menu savedMenu = menuDao.save(new Menu("메뉴", new BigDecimal(10000), menuGroupId));
        final long notExistId = Long.MAX_VALUE;

        // when
        final long count = menuDao.countByIdIn(List.of(savedMenu.getId(), notExistId));

        // then
        assertThat(count).isOne();
    }
}
