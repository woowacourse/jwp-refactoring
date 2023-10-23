package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
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
        final MenuGroup menuGroup = spyMenuGroup(request);
        final MenuGroupResponse response = MenuGroupResponse.from(menuGroup);

        when(menuGroupService.create(eq(request))).thenReturn(response);

        // then
        mockMvc.perform(post("/api/menu-groups")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/menu-groups/%d", response.getId())));
    }

    private MenuGroup spyMenuGroup(final MenuGroupCreateRequest menuGroupCreateRequest) {
        final MenuGroup menuGroup = new MenuGroup(menuGroupCreateRequest.getName());
        final MenuGroup spyMenuGroup = spy(menuGroup);
        when(spyMenuGroup.getId()).thenReturn(1L);
        return spyMenuGroup;
    }
}
