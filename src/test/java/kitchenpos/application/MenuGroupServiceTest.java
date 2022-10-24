package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixtures.두마리메뉴;
import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/truncate.sql")
class MenuGroupServiceTest {

    private final MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupServiceTest(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @DisplayName("menuGroup을 생성한다.")
    @Test
    void createMenuGroup() {
        MenuGroup 한마리메뉴 = 한마리메뉴();
        MenuGroup actual = menuGroupService.create(한마리메뉴);

        assertThat(actual.getName()).isEqualTo(한마리메뉴.getName());
    }

    @DisplayName("menuGroup을 전체 조회한다.")
    @Test
    void findAllMenuGroup() {
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴());
        MenuGroup 두마리메뉴 = menuGroupService.create(두마리메뉴());
        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups.get(0).getName()).isEqualTo(한마리메뉴.getName()),
                () -> assertThat(menuGroups.get(1).getName()).isEqualTo(두마리메뉴.getName())
        );
    }
}
