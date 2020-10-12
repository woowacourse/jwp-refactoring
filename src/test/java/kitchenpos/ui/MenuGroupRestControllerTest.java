package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends MvcTest {

    private static final int MENU_GROUP_ID_1 = 1;
    private static final int MENU_GROUP_ID_2 = 2;
    private static final String MENU_GROUP_NAME_1 = "메뉴그룹1";
    private static final String MENU_GROUP_NAME_2 = "메뉴그룹2";
    private static final MenuGroup MENU_GROUP_1 = new MenuGroup();
    private static final MenuGroup MENU_GROUP_2 = new MenuGroup();

    @MockBean
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        MENU_GROUP_1.setId((long)MENU_GROUP_ID_1);
        MENU_GROUP_1.setName(MENU_GROUP_NAME_1);
        MENU_GROUP_2.setId((long)MENU_GROUP_ID_2);
        MENU_GROUP_2.setName(MENU_GROUP_NAME_2);
    }

    @DisplayName("/api/menu-groups로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(menuGroupService.create(any())).willReturn(MENU_GROUP_1);

        String inputJson = objectMapper.writeValueAsString(MENU_GROUP_1);
        postAction("/api/menu-groups", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/menu-groups/1"))
            .andExpect(jsonPath("$.id", is(MENU_GROUP_ID_1)))
            .andExpect(jsonPath("$.name", is(MENU_GROUP_NAME_1)));
    }

    @DisplayName("/api/menu-groups로 GET요청 성공 테스트")
    @Test
    void listTest() throws Exception {
        given(menuGroupService.list()).willReturn(Arrays.asList(MENU_GROUP_1, MENU_GROUP_2));

        getAction("/api/menu-groups")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(MENU_GROUP_ID_1)))
            .andExpect(jsonPath("$[0].name", is(MENU_GROUP_NAME_1)))
            .andExpect(jsonPath("$[1].id", is(MENU_GROUP_ID_2)))
            .andExpect(jsonPath("$[1].name", is(MENU_GROUP_NAME_2)));
    }
}