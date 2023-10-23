package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuGroupCreateApiTest extends ApiTestConfig {

    @DisplayName("메뉴 그룹 생성 API 테스트")
    @Test
    void createMenuGroup() throws Exception {
        // given
        final MenuGroupCreateRequest request = new MenuGroupCreateRequest("추천 메뉴");

        // when
        final MenuGroupResponse response = new MenuGroupResponse(1L, request.getName());
        when(menuGroupService.create(eq(request))).thenReturn(response);

        // then
        mockMvc.perform(post("/api/menu-groups")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/menu-groups/%d", response.getId())));
    }
}
