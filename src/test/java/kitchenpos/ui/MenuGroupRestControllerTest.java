package kitchenpos.ui;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @Test
    void create() throws Exception {
        final MenuGroup menuGroupDto = new MenuGroup();
        menuGroupDto.setName("메뉴그룹이름");
        final String content = objectMapper.writeValueAsString(menuGroupDto);
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        when(menuGroupService.create(any())).thenReturn(menuGroup);

        final MockHttpServletResponse response = mockMvc.perform(post("/api/menu-groups")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.getHeader("Location")).isEqualTo("/api/menu-groups/1")
        );
    }

    @Test
    void list() throws Exception {
        when(menuGroupService.list()).thenReturn(Collections.singletonList(new MenuGroup()));

        final MockHttpServletResponse response = mockMvc.perform(get("/api/menu-groups"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
