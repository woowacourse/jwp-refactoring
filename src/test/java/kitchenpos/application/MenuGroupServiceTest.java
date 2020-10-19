package kitchenpos.application;

import kitchenpos.TestDomainFactory;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("새로운 메뉴 그룹 생성")
    @Test
    void createMenuGroupTest() {
        MenuGroup menuGroup = TestDomainFactory.createMenuGroup("반마리메뉴");

        MenuGroup savedMenuGroup = this.menuGroupService.create(menuGroup);

        assertAll(
                () -> assertThat(savedMenuGroup).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @DisplayName("존재하는 모든 메뉴 그룹을 조회")
    @Test
    void listMenuGroupTest() {
        MenuGroup menuGroup1 = TestDomainFactory.createMenuGroup("두마리메뉴");
        MenuGroup menuGroup2 = TestDomainFactory.createMenuGroup("세마리메뉴");
        List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2);
        menuGroups.forEach(menuGroup -> this.menuGroupService.create(menuGroup));

        List<MenuGroup> actualMenuGroups = this.menuGroupService.list();

        assertAll(
                () -> assertThat(actualMenuGroups.size()).isEqualTo(menuGroups.size()),
                () -> assertThat(actualMenuGroups.contains(menuGroup1)),
                () -> assertThat(actualMenuGroups.contains(menuGroup2))
        );
    }
}