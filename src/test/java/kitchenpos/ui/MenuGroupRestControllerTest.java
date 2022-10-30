package kitchenpos.ui;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;

@DisplayName("MenuGroup API 테스트")
class MenuGroupRestControllerTest extends RestControllerTest {

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void create() throws Exception {
        final MenuGroupRequest request = new MenuGroupRequest("치킨 세트");
        final String body = objectMapper.writeValueAsString(request);

        final MenuGroupResponse response = new MenuGroupResponse(1L, "치킨 세트");
        BDDMockito.given(menuGroupService.create(any()))
                .willReturn(response);

        mockMvc.perform(post("/api/menu-groups")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/menu-groups/" + response.getId()))
            .andExpect(jsonPath("name", containsString("치킨 세트")))
        ;
    }

    @DisplayName("메뉴 그룹의 목록을 조회한다")
    @Test
    void list() throws Exception {
        final MenuGroupResponse response = new MenuGroupResponse(1L, "치킨 세트");
        BDDMockito.given(menuGroupService.list())
                .willReturn(List.of(response));

        mockMvc.perform(get("/api/menu-groups"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
        ;
    }
}
