package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuService;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Menu;

@WebMvcTest(controllers = MenuRestController.class)
@AutoConfigureJdbc
@ContextConfiguration(classes = {
    MenuRestController.class,
    MenuService.class,
    JdbcTemplateMenuDao.class,
    JdbcTemplateMenuGroupDao.class,
    JdbcTemplateMenuProductDao.class,
    JdbcTemplateProductDao.class
})
class MenuRestControllerTest {
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
        Menu menu = new Menu();
        menu.setMenuGroupId(1L);
        menu.setName("메뉴");
        menu.setMenuProducts(Collections.emptyList());
        menu.setPrice(BigDecimal.valueOf(0));
        Menu result = new Menu();
        result.setId(7L);
        result.setMenuGroupId(1L);
        result.setName("메뉴");
        result.setMenuProducts(Collections.emptyList());
        result.setPrice(BigDecimal.valueOf(0));

        mvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menu))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @Test
    void list() throws Exception {
        String body = "{\"id\":1,\"name\":\"후라이드치킨\","
            + "\"price\":16000.00,\"menuGroupId\":2,"
            + "\"menuProducts\":"
            + "[{\"seq\":1,\"menuId\":1,\"productId\":1,\"quantity\":1}]}";
        
        mvc.perform(get("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(body)));
    }
}
