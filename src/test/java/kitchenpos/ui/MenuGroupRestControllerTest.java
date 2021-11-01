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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.testutils.TestDomainBuilder.menuGroupBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("POST /api/menu-groups - (이름)으로 메뉴 그룹을 추가한다.")
    @Test
    void create() throws Exception {
        // given
        MenuGroup newMenuGroup = menuGroupBuilder()
                .name("두마리메뉴")
                .build();

        MenuGroup expectedMenuGroup = menuGroupBuilder()
                .name("두마리메뉴")
                .id(1L)
                .build();

        given(menuGroupService.create(refEq(newMenuGroup))).willReturn(expectedMenuGroup);

        // when
        MockHttpServletResponse response =
                mockMvc.perform(postApiMenuGroups(newMenuGroup))
                        .andReturn()
                        .getResponse();

        String responseBody = response.getContentAsString(Charset.defaultCharset());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("location")).isEqualTo("/api/menu-groups/" + expectedMenuGroup.getId());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(expectedMenuGroup));

        then(menuGroupService).should(times(1)).create(refEq(newMenuGroup));
    }

    private RequestBuilder postApiMenuGroups(MenuGroup newMenuGroup) throws JsonProcessingException {
        return post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMenuGroup));
    }

    @DisplayName("GET /api/menu-groups - 전체 메뉴 그룹의 리스트를 가져온다.")
    @Test
    void list() throws Exception {
        // given
        List<MenuGroup> expectedMenuGroups = Arrays.asList(
                menuGroupBuilder().id(1L).name("두마리메뉴").build(),
                menuGroupBuilder().id(2L).name("한마리메뉴").build(),
                menuGroupBuilder().id(3L).name("순살파닭두마리메뉴").build()
        );

        given(menuGroupService.list()).willReturn(expectedMenuGroups);

        // when
        MockHttpServletResponse response =
                mockMvc.perform(getApiMenuGroups())
                        .andReturn()
                        .getResponse();

        String responseBody = response.getContentAsString(Charset.defaultCharset());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(expectedMenuGroups));

        then(menuGroupService).should(times(1)).list();
    }

    private RequestBuilder getApiMenuGroups() {
        return get("/api/menu-groups");
    }

}
