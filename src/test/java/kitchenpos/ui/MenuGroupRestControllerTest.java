package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.emptyList;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 생성하기")
    void create() throws Exception {
        //given
        final String request = createRequest("추천 메뉴");
        given(menuGroupService.create(any()))
                .willReturn(new MenuGroup());

        //when, then
        mockMvc.perform(post("/api/menu-groups")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("메뉴 그룹 전체 조회하기")
    void list() throws Exception {
        //given
        given(menuGroupService.list())
                .willReturn(emptyList());

        //when, then
        mockMvc.perform(get("/api/menu-groups"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String createRequest(final String name) throws JsonProcessingException {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return objectMapper.writeValueAsString(menuGroup);
    }
}
