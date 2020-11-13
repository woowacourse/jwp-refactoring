package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.MenuCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest {
    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() throws Exception {
        MenuCreateRequest request = createMenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                4L,
                Arrays.asList(
                        createMenuProductRequest(1L, 3),
                        createMenuProductRequest(2L, 2)
                )
        );
        List<MenuProduct> menuProducts = Arrays.asList(createMenuProduct(3L, 1L, 3, 1L), createMenuProduct(4L, 2L, 2, 1L));
        given(menuService.create(any(MenuCreateRequest.class))).willReturn(createMenu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), 4L, menuProducts));
        byte[] content = objectMapper.writeValueAsBytes(request);

        mockMvc.perform(post("/api/menus")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("후라이드+후라이드"))
                .andExpect(jsonPath("$.price").value(19000))
                .andExpect(jsonPath("$.menuGroupId").value(4))
                .andExpect(jsonPath("$.menuProducts[0].productId").value(1))
                .andExpect(jsonPath("$.menuProducts[0].quantity").value(3))
                .andExpect(jsonPath("$.menuProducts[1].productId").value(2))
                .andExpect(jsonPath("$.menuProducts[1].quantity").value(2));
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다")
    void list() throws Exception {
        LinkedMultiValueMap<Long, MenuProduct> menuProducts = new LinkedMultiValueMap<Long, MenuProduct>() {{
            add(1L, createMenuProduct(1L, 2L, 2, 1L));
            add(1L, createMenuProduct(2L, 3L, 2, 1L));
            add(1L, createMenuProduct(3L, 4L, 1, 1L));
            add(2L, createMenuProduct(4L, 2L, 1, 2L));
            add(3L, createMenuProduct(5L, 2L, 1, 3L));
            add(3L, createMenuProduct(6L, 4L, 3, 3L));
        }};
        List<Menu> persistedMenus = Arrays.asList(
                createMenu(1L, "후라이드+후라이드", BigDecimal.valueOf(1000L), 3L, null),
                createMenu(2L, "후라이드+후라이드", BigDecimal.valueOf(1000L), 3L, null),
                createMenu(3L, "후라이드+후라이드", BigDecimal.valueOf(1000L), 3L, null)
        );
        for (Menu menu : persistedMenus) {
            menu.setMenuProducts(menuProducts.get(menu.getId()));
        }
        given(menuService.list()).willReturn(persistedMenus);

        byte[] responseBody = mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        List<Menu> result = objectMapper.readValue(responseBody, new TypeReference<List<Menu>>() {
        });
        assertThat(result).usingRecursiveComparison().isEqualTo(persistedMenus);
    }
}