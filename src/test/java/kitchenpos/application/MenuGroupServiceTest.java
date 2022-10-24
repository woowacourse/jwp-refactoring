package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.BeanAssembler;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupServiceTest(DataSource dataSource) {
        this.menuGroupService = BeanAssembler.createMenuGroupService(dataSource);
    }

    @Test
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup("메뉴그룹");
        // when
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);
        // then
        assertThat(createdMenuGroup.getId()).isNotNull();
    }

    @Test
    void list() {
        // given & when
        List<MenuGroup> menuGroups = menuGroupService.list();
        // then
        int defaultSize = 4;
        assertThat(menuGroups).hasSize(defaultSize);
    }
}
