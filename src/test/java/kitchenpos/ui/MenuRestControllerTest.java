package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.service.MenuService;
import kitchenpos.test.ObjectCreator;
import kitchenpos.value.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MenuRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() throws Exception {
        // given
        final CreateMenuRequest request = ObjectCreator.getObject(
                CreateMenuRequest.class,
                "test",
                BigDecimal.ZERO,
                1L,
                List.of()
        );
        final Menu menu = ObjectCreator.getObject(Menu.class, 1L, "test", new Price(BigDecimal.ZERO), 1L, List.of());

        when(menuService.create(any()))
                .thenReturn(MenuResponse.from(menu));

        // when & then
        mockMvc.perform(post("/api/menus")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("price").isNumber())
                .andExpect(jsonPath("menuGroupId").isNumber())
                .andExpect(jsonPath("menuProducts").isArray());
    }

    @Test
    void list() throws Exception {
        // given
        when(menuService.list())
                .thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
