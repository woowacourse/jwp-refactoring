package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.sql.DataSource;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;

@JdbcTest
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        this.menuGroupService = new MenuGroupService(new JdbcTemplateMenuGroupDao(dataSource));
    }

    @Test
    @DisplayName("메뉴 그룹의 이름을 제공하여 메뉴 그룹을 저장할 수 있다.")
    void givenName() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(9987L);
        menuGroup.setName("메뉴메뉴~!");
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup).isNotNull();
        assertThat(savedMenuGroup.getId())
                .as("식별자는 주어진 값과 무관하게 할당받는다.")
                .isNotEqualTo(menuGroup.getId());
    }

    @Test
    @DisplayName("메뉴 그룹의 이름은 255자까지 표현할 수 있다.")
    void nameSize() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(9987L);
        menuGroup.setName("맴".repeat(256));
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .as("이름의 길이가 255자를 초과하면 저장할 수 없다.")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
