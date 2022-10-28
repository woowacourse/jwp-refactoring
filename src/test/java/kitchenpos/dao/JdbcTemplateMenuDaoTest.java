package kitchenpos.dao;

import static kitchenpos.fixture.MenuFixture.getMenuGroupRequest;
import static kitchenpos.fixture.MenuFixture.getMenuRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateMenuDaoTest extends JdbcTemplateTest{

    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("데이터 베이스에 저장할 경우 id 값을 가진 엔티티로 반환한다.")
    void save() {
        final MenuGroup menuGroup = menuGroupDao.save(getMenuGroupRequest(1L));
        final Menu menu = menuDao.save(getMenuRequest(menuGroup.getId()));
        assertThat(menu.getId()).isNotNull();
    }

    @Test
    @DisplayName("목록을 조회한다.")
    void list() {
        final List<Menu> actual = menuDao.findAll();
        assertThat(actual).hasSize(6);
    }
}
