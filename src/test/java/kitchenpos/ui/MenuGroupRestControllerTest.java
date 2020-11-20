package kitchenpos.ui;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.dao.MenuGroupDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class MenuGroupRestControllerTest extends AbstractControllerTest {
    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        MenuGroupCreateRequest menuGroupCreateRequest = createMenuGroupRequest("메뉴그룹1");

        mockMvc.perform(
            post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(menuGroupCreateRequest))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(menuGroupCreateRequest.getName()));
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<MenuGroupResponse> menuGroups = MenuGroupResponse.listOf(menuGroupDao.findAll());

        String json = mockMvc.perform(get("/api/menu-groups"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        List<MenuGroupResponse> response = objectMapper.readValue(json,
            objectMapper.getTypeFactory()
                .constructCollectionType(List.class, MenuGroupResponse.class));

        assertThat(response).usingFieldByFieldElementComparator().containsAll(menuGroups);
    }
}
