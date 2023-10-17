package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MenuGroupListApiTest extends ApiTestConfig {

    @DisplayName("메뉴 그룹 전체 조회 API 테스트")
    @Test
    void listMenuGroup() throws Exception {
        // given
        final MenuGroupResponse response = MenuGroupResponse.from(new MenuGroup(1L, "여우가 좋아하는 메뉴 그룹"));

        // when
        when(menuGroupService.list()).thenReturn(List.of(response));

        // then
        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }
}
