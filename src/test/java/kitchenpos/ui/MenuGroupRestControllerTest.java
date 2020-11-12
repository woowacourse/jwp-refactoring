package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() throws Exception {
        MenuGroup request = new MenuGroup("menuGroupName");

        String response = mockMvc.perform(post("/api/menu-groups")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        MenuGroupResponse responseMenuGroup = mapper.readValue(response, MenuGroupResponse.class);

        assertAll(
            () -> assertThat(responseMenuGroup.getId()).isNotNull(),
            () -> assertThat(responseMenuGroup).isEqualToComparingOnlyGivenFields(request, "name")
        );
    }

    @Test
    void list() throws Exception {
        MenuGroupResponse menuGroup1 = menuGroupService.create(new MenuGroupRequest("고기"));
        MenuGroupResponse menuGroup2 = menuGroupService.create(new MenuGroupRequest("국"));

        String response = mockMvc.perform(get("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<MenuGroupResponse> responseTables = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, MenuGroupResponse.class));

        assertThat(responseTables).usingElementComparatorIgnoringFields("id")
            .containsAll(Arrays.asList(menuGroup1, menuGroup2));
    }
}