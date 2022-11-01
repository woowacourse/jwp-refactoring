package kitchenpos.core.menu.repository;

import static kitchenpos.fixture.MenuFixture.getMenuGroup;
import static kitchenpos.fixture.MenuFixture.getMenuRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.core.menu.domain.Menu;
import kitchenpos.core.menugroup.domain.MenuGroup;
import kitchenpos.core.menugroup.domain.MenuGroupDao;
import kitchenpos.core.menugroup.repository.JdbcTemplateMenuGroupDao;
import kitchenpos.common.JdbcTemplateTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateMenuDaoTest extends JdbcTemplateTest {

    private JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("데이터 베이스에 저장할 경우 id 값을 가진 엔티티로 반환한다.")
    void save() {
        final MenuGroup menuGroup = menuGroupDao.save(getMenuGroup(1L));
        final Menu menu = jdbcTemplateMenuDao.save(getMenuRequest(menuGroup.getId()));
        assertThat(menu.getId()).isNotNull();
    }

    @Test
    @DisplayName("목록을 조회한다.")
    void list() {
        final List<Menu> actual = jdbcTemplateMenuDao.findAll();
        assertThat(actual).hasSize(6);
    }
}
