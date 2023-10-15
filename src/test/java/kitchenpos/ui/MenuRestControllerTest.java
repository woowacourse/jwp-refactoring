package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;
import kitchenpos.ui.dto.MenuCreationRequest;
import kitchenpos.ui.dto.MenuProductWithQuantityRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Test
    void create() throws Exception {
        // given
        final Menu result = menuFixtureWithId(1L, "chicken-set", 28000L);
        given(menuService.create(any())).willReturn(result);

        final MenuCreationRequest request = new MenuCreationRequest(
                "chicken-set",
                BigDecimal.valueOf(28000),
                1L,
                List.of(new MenuProductWithQuantityRequest(1L, 1L))
        );

        // when
        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/menus/1"));
    }

    @Test
    void list() throws Exception {
        // given
        final Menu resultA = menuFixtureWithId(1L, "chicken-setA", 23000L);
        final Menu resultB = menuFixtureWithId(2L, "chicken-setB", 26000L);
        given(menuService.list()).willReturn(List.of(resultA, resultB));

        // when
        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(resultA, resultB))));
    }

    private Menu menuFixtureWithId(final Long id, final String name, final long price) {
        return new Menu(id, name, Price.from(price), null, null);
    }
}
