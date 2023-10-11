package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateMenuGroupDaoTest extends JdbcTemplateTest {

    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    void 메뉴그룹을_저장한다() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("한마리메뉴");
        MenuGroup saved = menuGroupDao.save(menuGroup);

        assertThat(saved.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void 식별자로_메뉴그룹을_찾는다() {
        MenuGroup menuGroup = menuGroupDao.findById(1L).get();

        assertThat(menuGroup.getId()).isEqualTo(1L);
    }

    @Test
    void 모든_메뉴그룹을_조회한다() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        assertThat(menuGroups.size()).isEqualTo(4);
    }

    @Test
    void existsById() {
        boolean expected = menuGroupDao.existsById(1L);

        assertThat(expected).isTrue();
    }
}
