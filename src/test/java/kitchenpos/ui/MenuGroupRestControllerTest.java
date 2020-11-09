package kitchenpos.ui;

import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP_FIXTURE_1;
import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
    private MenuGroupDao menuGroupDao;

    @Test
    void create() throws Exception {
        MenuGroup request = new MenuGroup();
        request.setName("menuGroupName");

        String response = mockMvc.perform(post("/api/menu-groups")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        MenuGroup responseMenuGroup = mapper.readValue(response, MenuGroup.class);

        assertAll(
            () -> assertThat(responseMenuGroup.getId()).isNotNull(),
            () -> assertThat(responseMenuGroup).isEqualToComparingOnlyGivenFields(request, "name")
        );
    }

    @Test
    void list() throws Exception {
        MenuGroup menuGroup1 = menuGroupDao.save(MENU_GROUP_FIXTURE_1);
        MenuGroup menuGroup2 = menuGroupDao.save(MENU_GROUP_FIXTURE_2);

        String response = mockMvc.perform(get("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<MenuGroup> responseTables = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, MenuGroup.class));

        assertThat(responseTables).usingElementComparatorIgnoringFields("id")
            .containsAll(Arrays.asList(menuGroup1, menuGroup2));
    }
}