package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.ui.dto.MenuMapper;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class MenuRestControllerTest extends RestControllerTest {

    @Autowired
    private MenuMapper menuMapper;

    @Test
    void 메뉴_생성에_성공한다() throws Exception {
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(1L, 1);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(1_000), 1L,
                List.of(menuProductCreateRequest));
        Menu expectedMenu = new Menu(1L, "메뉴", new Price(BigDecimal.valueOf(1_000)), 1L, new ArrayList<>());
        new MenuProduct(1L, expectedMenu, 1L, 1);

        when(menuService.create(menuMapper.menuCreateRequestToMenu(menuCreateRequest)))
                .thenReturn(expectedMenu);

        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(menuCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 메뉴_목록_조회에_성공한다() throws Exception {
        Menu expectedMenu = new Menu(1L, "메뉴", new Price(BigDecimal.valueOf(1_000)), 1L, new ArrayList<>());
        new MenuProduct(1L, expectedMenu, 1L, 1);

        when(menuService.list()).thenReturn(List.of(expectedMenu));

        MvcResult mvcResult = mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<MenuResponse> menuResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<List<MenuResponse>>() {
                });

        assertThat(menuResponses).hasSize(1)
                .extracting("id")
                .containsExactly(1L);
    }
}
