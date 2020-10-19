package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.domain.MenuGroup;

class MenuGroupRestControllerTest extends ControllerTest {
    private static final String MENU_GROUP_API_URL = "/api/menu-groups/";

    @DisplayName("create: 이름을 body message에 포함해 제품 등록을 요청시 , 메뉴 그룹을 생성 후 생성 성공 시 201 응답을 반환한다.")
    @Test
    void createMenuGroup() throws Exception {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("legacy");

        final ResultActions resultActions = create(MENU_GROUP_API_URL, menuGroup);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("legacy")));
    }

    @DisplayName("list: 메뉴 그룹의 목록 요청시, 전체 목록을 body message로 가지고 있는 status code 200 응답을 반환한다.\"")
    @Test
    void list() throws Exception {
        final ResultActions resultActions = findList(MENU_GROUP_API_URL);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }
}