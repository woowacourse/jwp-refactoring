package kitchenpos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.ui.MenuGroupRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("메뉴 그룹 생성 controller")
    @Test
    void menuGroup() throws Exception {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("한마리치킨");

        MenuGroup menuGroup = Fixtures.makeMenuGroup();

        String content = objectMapper.writeValueAsString(menuGroupRequest);

        given(menuGroupService.create(any(MenuGroupRequest.class)))
            .willReturn(menuGroup);

        mvc.perform(post("/api/menu-groups")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @DisplayName("메뉴 그룹 불러오기")
    @Test
    void menuGroupList() throws Exception {
        List<MenuGroup> menuGroups = new ArrayList<>();

        given(menuGroupService.list())
            .willReturn(menuGroups);

        mvc.perform(get("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    }
}
