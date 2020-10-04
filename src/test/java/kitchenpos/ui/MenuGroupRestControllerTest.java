package kitchenpos.ui;

import static kitchenpos.constants.Constants.TEST_MENU_ID;
import static kitchenpos.constants.Constants.TEST_MENU_NAME;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 추가")
    @Test
    void create() throws Exception {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(TEST_MENU_ID);
        menuGroup.setName(TEST_MENU_NAME);

        given(menuGroupService.create(any()))
            .willReturn(menuGroup);

        final ResultActions resultActions = mockMvc.perform(post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"" + TEST_MENU_NAME + " \"}"))
            .andDo(print());

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(menuGroup.getId().intValue())))
            .andExpect(jsonPath("$.name", is(menuGroup.getName())))
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andDo(print());
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() throws Exception {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(TEST_MENU_ID);
        menuGroup.setName(TEST_MENU_NAME);

        given(menuGroupService.list())
            .willReturn(Collections.singletonList(menuGroup));

        final ResultActions resultActions = mockMvc.perform(get("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(menuGroup.getId().intValue())))
            .andExpect(jsonPath("$[0].name", is(menuGroup.getName())))
            .andDo(print());
    }
}
