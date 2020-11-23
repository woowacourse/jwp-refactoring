package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.menuGroup.MenuGroupCreateRequest;
import kitchenpos.dto.menuGroup.MenuGroupCreateResponse;
import kitchenpos.dto.menuGroup.MenuGroupFindAllResponses;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    @MockBean
    private MenuGroupService menuGroupService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private MenuGroup menuGroup;

    private MenuGroupCreateRequest menuGroupCreateRequest;

    private MenuGroupCreateResponse menuGroupCreateResponse;

    private MenuGroupFindAllResponses menuGroupFindAllResponses;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        objectMapper = new ObjectMapper();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        menuGroup = new MenuGroup(1L, "메뉴그룹");

        menuGroupCreateRequest = new MenuGroupCreateRequest(menuGroup);

        menuGroupCreateResponse = new MenuGroupCreateResponse(menuGroup);

        menuGroupFindAllResponses = MenuGroupFindAllResponses.from(Collections.singletonList(menuGroup));
    }

    @Test
    void create() throws Exception {
        given(menuGroupService.create(any())).willReturn(menuGroupCreateResponse);

        mockMvc.perform(post("/api/menu-groups")
                .content(objectMapper.writeValueAsString(menuGroupCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menu-groups/1"));
    }

    @Test
    void list() throws Exception {
        given(menuGroupService.findAll()).willReturn(menuGroupFindAllResponses);

        mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}