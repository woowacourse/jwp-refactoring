package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

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
        final MenuGroup menuGroup = menuGroupDao.save(추천메뉴());
        final Menu menu = menuDao.save(후라이드후라이드(menuGroup.getId()));
        assertThat(menu.getId()).isNotNull();
    }
}
