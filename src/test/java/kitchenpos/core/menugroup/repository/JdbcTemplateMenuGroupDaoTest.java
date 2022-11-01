package kitchenpos.core.menugroup.repository;

import static kitchenpos.fixture.MenuFixture.getMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.core.menugroup.domain.MenuGroup;
import kitchenpos.core.menugroup.domain.MenuGroupDao;
import kitchenpos.common.JdbcTemplateTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateMenuGroupDaoTest extends JdbcTemplateTest {

    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("데이터 베이스에 저장할 경우 id 값을 가진 엔티티로 반환한다.")
    void save() {
        final MenuGroup savedMenuGroup = menuGroupDao.save(getMenuGroup(1L));
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하는 id일 경우 true 를 반환한다.")
    void exist() {
        final MenuGroup savedMenuGroup = menuGroupDao.save(getMenuGroup(1L));
        assertThat(menuGroupDao.existsById(savedMenuGroup.getId())).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 id일 경우 false 를 반환한다.")
    void nonExist() {
        assertThat(menuGroupDao.existsById(0L)).isFalse();
    }

    @Test
    @DisplayName("목록을 조회한다.")
    void list() {
        final List<MenuGroup> actual = menuGroupDao.findAll();
        assertThat(actual).hasSize(4);
    }
}
