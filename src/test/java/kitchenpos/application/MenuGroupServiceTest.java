package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/truncate.sql", "/init-data.sql"})
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        MenuGroup menuGroup = TestObjectFactory.createMenuGroup("세마리메뉴");
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertAll(() -> {
            assertThat(savedMenuGroup).isInstanceOf(MenuGroup.class);
            assertThat(savedMenuGroup).isNotNull();
            assertThat(savedMenuGroup.getId()).isNotNull();
            assertThat(savedMenuGroup.getName()).isNotNull();
            assertThat(savedMenuGroup.getName()).isEqualTo("세마리메뉴");
        });
    }

    @DisplayName("메뉴 그룹 리스트를 조회한다.")
    @Test
    void list() {
        List<MenuGroup> menuGroupList = menuGroupService.list();

        assertAll(() -> {
            assertThat(menuGroupList).isNotEmpty();
            assertThat(menuGroupList).hasSize(4);
        });
    }
}
