package kitchenpos.ui.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
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
@WebMvcTest(MenuGroupControllerV1.class)
class MenuGroupControllerV1Test {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("/api/v1/menu-groups로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        var request = new MenuGroupCreateRequest("주류");
        var response = new MenuGroupResponse(1L, "주류");
        given(menuGroupService.create(any(MenuGroupCreateRequest.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/v1/menu-groups/1"));
    }

    @Test
    @DisplayName("/api/v1/menu-groups로 GET 요청을 보내면 200 응답과 결과가 조회된다.")
    void findAll_with_200() throws Exception {
        // given
        var response = List.of(
            new MenuGroupResponse(1L, "주류"),
            new MenuGroupResponse(2L, "튀김류")
        );

        given(menuGroupService.findAll())
            .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/menu-groups")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2));
    }
}
