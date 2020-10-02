package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.MenuGroup;

class MenuGroupRestControllerTest extends ControllerTest {

    @DisplayName("create: 메뉴 그룹 등록 테스트")
    @Test
    void createTest() throws Exception {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("한마리 메뉴");

        create("/api/menu-groups", menuGroup)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value(menuGroup.getName()));
    }

    @DisplayName("findMenuGroups: 전체 메뉴 그룹 조회 테스트")
    @Test
    void findMenuGroupsTest() throws Exception {
        findLists("/api/menu-groups")
                .andExpect(jsonPath("$[0].name").value("두마리메뉴"))
                .andExpect(jsonPath("$[1].name").value("한마리메뉴"));
    }
}
