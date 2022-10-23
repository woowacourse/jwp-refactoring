package kitchenpos.ui;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @Test
    void create() throws Exception {
        MenuGroup menuGroup = MenuGroup.of(1L, "name");
        String request = objectMapper.writeValueAsString(menuGroup);

        given(this.menuGroupService.create(menuGroup)).willReturn(1L);

        this.mvc.perform(post("/api/menu-groups")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    void list() throws Exception {
        this.mvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk());
    }
}
