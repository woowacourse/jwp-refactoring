package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.MenuGroup;

@SpringBootTest
@Transactional
class MenuGroupRestControllerTest {
    private static final String MENU_GROUP_URL = "/api/menu-groups/";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("create: 이름을 body message에 포함해 제품 등록을 요청시 , 요청값을 메뉴 그룹을 생성 후 생성 성공 시 201 응답을 반환한다.")
    @Test
    void create() throws Exception {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("legacy");

        mockMvc.perform(post(MENU_GROUP_URL)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(menuGroup)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is("legacy")));
    }

    @DisplayName("list: 메뉴 그룹의 목록 요청시, 전체 목록을 body message로 가지고 있는 status code 200 응답을 반환한다.\"")
    @Test
    void list() throws Exception {
        mockMvc.perform(get(MENU_GROUP_URL)
        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }
}