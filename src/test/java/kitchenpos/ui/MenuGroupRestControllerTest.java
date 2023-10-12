package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MenuGroupService menuGroupService;

    @BeforeEach
    public void setUp() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("menuGroup1");
        given(menuGroupService.create(menuGroup)).willReturn(menuGroup);
    }

    @Test
    @DisplayName("POST: /api/menu-groups 요청시 생성 요청이 정상적으로 수행되는지 테스트")
    public void create() throws Exception {
        // given
        final ObjectMapper objectMapper = new ObjectMapper();
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("menuGroup1");
        given(menuGroupService.create(any(MenuGroup.class))).willReturn(menuGroup);

        // when
        mockMvc.perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroup)))
                .andDo(print())
                .andExpect(status().isCreated())
        ;

        // then
        Assertions.assertThat(menuGroupService.create(menuGroup)).isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("GET: /api/menu-groups 요청시 목록 요청이 정상적으로 수행되는지 테스트")
    public void list() throws Exception {
        // when
        mockMvc.perform(get("/api/menu-groups"))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        // then
        Assertions.assertThat(menuGroupService.list()).isNotNull();
    }
}
