package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static kitchenpos.fixture.FixtureFactory.createMenuGroup;
import static kitchenpos.ui.MenuGroupRestController.MENU_GROUP_API;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupRestControllerTest extends ControllerTest {
    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성 요청")
    @Test
    void create() throws Exception {
        MenuGroup request = createMenuGroup(null, "추천메뉴");
        String body = objectMapper.writeValueAsString(request);

        when(menuGroupService.create(any())).thenReturn(new MenuGroup());

        requestWithPost(MENU_GROUP_API, body);
    }

    @DisplayName("메뉴 그룹 목록 조회 요청")
    @Test
    void list() throws Exception {
        requestWithGet(MENU_GROUP_API);
    }
}