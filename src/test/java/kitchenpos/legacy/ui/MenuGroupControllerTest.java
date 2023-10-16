package kitchenpos.legacy.ui;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.legacy.application.LegacyMenuGroupService;
import kitchenpos.legacy.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(ReplaceUnderscores.class)
@WebMvcTest(MenuGroupController.class)
class MenuGroupControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LegacyMenuGroupService menuGroupService;

    @Test
    @DisplayName("/api/menu-groups로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        MenuGroup request = new MenuGroup();
        MenuGroup response = new MenuGroup();
        response.setId(1L);
        given(menuGroupService.create(any(MenuGroup.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/menu-groups/1"));
    }

    @Test
    @DisplayName("/api/menu-groups로 GET 요청을 보내면 200 응답과 결과가 조회된다.")
    void findAll_with_200() throws Exception {
        // given
        given(menuGroupService.findAll())
            .willReturn(List.of(new MenuGroup(), new MenuGroup()));

        // when & then
        mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2));
    }
}
