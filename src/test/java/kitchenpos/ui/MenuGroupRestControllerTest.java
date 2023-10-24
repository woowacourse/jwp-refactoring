package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        // given
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("반반 치킨세트");

        given(menuGroupService.create(any()))
                .willReturn(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menu-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroupRequest)));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/api/menu-groups/1"));
    }

    @DisplayName("이름이 비어있으면 예외 처리한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void create_FailWhenRequestNameIsBlank(final String name) throws Exception {
        // given
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);

        given(menuGroupService.create(any()))
                .willReturn(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menu-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroupRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("메뉴 그룹 목록을 반환할 수 있다.")
    @Test
    void list() throws Exception {
        // given
        final List<MenuGroupResponse> menuGroupResponses = List.of(
                MenuGroupResponse.from(new MenuGroup("반반 치킨세트")),
                MenuGroupResponse.from(new MenuGroup("양념 간장 치킨세트"))
        );

        given(menuGroupService.list())
                .willReturn(menuGroupResponses);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/menu-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroupResponses)));

        // then
        resultActions.andExpect(status().isOk());
    }
}
