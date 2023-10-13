package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 메뉴_그룹을_저장한다() throws Exception {
        // given
        MenuGroup savedMenuGroup = new MenuGroup();
        savedMenuGroup.setId(1L);
        savedMenuGroup.setName("Fried Chicken");
        when(menuGroupService.create(any(MenuGroup.class))).thenReturn(savedMenuGroup);

        MenuGroup requestMenuGroup = new MenuGroup();
        requestMenuGroup.setName("Fired Chicken");
        String jsonRequestMenuGroup = objectMapper.writeValueAsString(requestMenuGroup);

        // when
        ResultActions result = mockMvc.perform(post("/api/menu-groups")
                .content(jsonRequestMenuGroup)
                .contentType(APPLICATION_JSON)
        );

        // then
        String expectContent = objectMapper.writeValueAsString(savedMenuGroup);

        result.andExpectAll(
                status().isCreated(),
                header().string("Location", "/api/menu-groups/" + savedMenuGroup.getId()),
                content().json(expectContent)
        );
    }

    @Test
    void 모든_메뉴_그룹을_조회한다() throws Exception {
        // given
        List<MenuGroup> expected = List.of(new MenuGroup(), new MenuGroup());
        when(menuGroupService.list()).thenReturn(expected);
        String expectedContent = objectMapper.writeValueAsString(expected);

        // when
        ResultActions result = mockMvc.perform(get("/api/menu-groups"));

        // then
        result.andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(expectedContent)
        );
    }
}
