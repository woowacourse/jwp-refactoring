package kitchenpos.ui;

import static kitchenpos.util.ObjectCreator.getObject;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.entity.MenuGroup;
import kitchenpos.dto.request.menugroup.CreateMenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MenuGroupRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void create() throws Exception {
        // given
        final CreateMenuGroupRequest request = getObject(CreateMenuGroupRequest.class, "test");
        final MenuGroup menuGroup = getObject(MenuGroup.class, 1L, "test");

        when(menuGroupService.create(any()))
                .thenReturn(MenuGroupResponse.from(menuGroup));

        // when & then
        mockMvc.perform(post("/api/menu-groups")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("name").isString());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void list() throws Exception {
        // given
        when(menuGroupService.list())
                .thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
