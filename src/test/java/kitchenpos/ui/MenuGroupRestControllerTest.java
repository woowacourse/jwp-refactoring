package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;

@WebMvcTest(
    controllers = MenuGroupRestController.class,
    includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {MenuGroupService.class, JdbcTemplateMenuGroupDao.class})
)
@AutoConfigureJdbc
class MenuGroupRestControllerTest {
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .build();
    }

    @Test
    void create() throws Exception {
        String name = "메뉴";
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        MenuGroup result = new MenuGroup();
        result.setId(5L);
        result.setName(name);

        mvc.perform(post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menuGroup))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @Test
    void list() throws Exception {
        MenuGroup first = new MenuGroup();
        first.setId(1L);
        first.setName("두마리메뉴");
        MenuGroup second = new MenuGroup();
        second.setId(2L);
        second.setName("한마리메뉴");
        MenuGroup third = new MenuGroup();
        third.setId(3L);
        third.setName("순살파닭두마리메뉴");
        MenuGroup fourth = new MenuGroup();
        fourth.setId(4L);
        fourth.setName("신메뉴");
        MenuGroup fifth = new MenuGroup();
        fifth.setId(5L);
        fifth.setName("메뉴");
        List<MenuGroup> list = Arrays.asList(first, second, third, fourth, fifth);

        mvc.perform(get("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }
}
