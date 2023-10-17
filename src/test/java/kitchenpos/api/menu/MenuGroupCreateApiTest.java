package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
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
        final String request = "{\n" +
                "  \"name\": \"추천메뉴\"\n" +
                "}";

        // when
        // FIXME: domain -> dto 로 변경
        final Long expectedId = 1L;
        final MenuGroup expectedMenuGroup = new MenuGroup();
        expectedMenuGroup.setId(expectedId);
        when(menuGroupService.create(any(MenuGroupCreateRequest.class))).thenReturn(expectedMenuGroup);

        // then
        mockMvc.perform(post("/api/menu-groups")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/menu-groups/%d", expectedId)));
    }
}
