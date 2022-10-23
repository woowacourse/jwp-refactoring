package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class MenuGroupDaoTest {

    @Autowired
    private DataSource dataSource;

    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        this.menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    void 메뉴그룹을_저장할_수_있다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴그룹");

        // when
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    void 메뉴그룹을_아이디로_조회할_수_있다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        // when
        final MenuGroup foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(foundMenuGroup.getName()).isEqualTo("메뉴그룹");
    }

    @Test
    void 모든_메뉴그룹을_조회할_수_있다() {
        // given
        final int alreadyExistCount = menuGroupDao.findAll()
                .size();
        final MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        // when
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        assertThat(menuGroups).usingFieldByFieldElementComparator()
                .hasSize(alreadyExistCount + 1)
                .contains(savedMenuGroup);
    }

    @Test
    void 메뉴그룹이_존재하는지_확인한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        // when
        final boolean exist = menuGroupDao.existsById(savedMenuGroup.getId());

        // then
        assertThat(exist).isTrue();
    }
}
