package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() throws Exception {
        // given
        MenuGroup createdMenuGroup = new MenuGroup(1L, "Test Menu Group");

        // when
        when(menuGroupService.create(any(MenuGroupRequest.class))).thenReturn(createdMenuGroup);

        // then
        mockMvc.perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MenuGroupRequest("Test Menu Group"))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menu-groups/" + createdMenuGroup.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(createdMenuGroup)));
    }

    @Test
    void 메뉴_그룹을_전체_조회한다() throws Exception {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "Test Menu Group 1");
        MenuGroup menuGroup1 = new MenuGroup(2L, "Test Menu Group 2");

        // when
        when(menuGroupService.list()).thenReturn(List.of(menuGroup, menuGroup1));

        // then
        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(menuGroup, menuGroup1))));
    }
}

