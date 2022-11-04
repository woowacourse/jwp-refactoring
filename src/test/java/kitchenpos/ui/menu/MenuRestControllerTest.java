package kitchenpos.ui.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.menu.request.MenuCreateRequest;
import kitchenpos.dto.menu.request.MenuProductCreateRequest;
import kitchenpos.dto.menu.response.MenuProductResponse;
import kitchenpos.dto.menu.response.MenuResponse;
import kitchenpos.exception.badrequest.ExpensiveMenuPriceException;
import kitchenpos.exception.badrequest.MenuGroupNotExistsException;
import kitchenpos.exception.badrequest.NegativePriceException;
import kitchenpos.exception.badrequest.ProductNotExistsException;
import kitchenpos.ui.product.RestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class MenuRestControllerTest extends RestControllerTest {

    @Test
    void 메뉴_생성에_성공한다() throws Exception {
        MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(1L, 1);
        MenuCreateRequest menuRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(1_000), 1L,
                List.of(menuProductRequest));
        MenuProductResponse expectedMenuProduct =
                new MenuProductResponse(1L, 1L, 1);
        MenuResponse expected =
                new MenuResponse(1L, "메뉴", BigDecimal.valueOf(1_000), 1L, List.of(expectedMenuProduct));

        when(menuService.create(any(MenuCreateRequest.class))).thenReturn(expected);

        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(menuRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 메뉴가_존재하지_않는_메뉴_그룹에_속할_경우_400() throws Exception {
        MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(1L, 1);
        MenuCreateRequest menuRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(1_000), 1L,
                List.of(menuProductRequest));

        when(menuService.create(any(MenuCreateRequest.class))).thenThrow(new MenuGroupNotExistsException());

        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(menuRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 메뉴_가격이_0원_미만이면_400() throws Exception {
        MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(1L, 1);
        MenuCreateRequest menuRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(-1), 1L,
                List.of(menuProductRequest));

        when(menuService.create(any(MenuCreateRequest.class))).thenThrow(new NegativePriceException());

        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(menuRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 메뉴에_존재하지_않는_상품이_포함되어_있으면_400() throws Exception {
        MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(1L, 1);
        MenuCreateRequest menuRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(1_000), 1L,
                List.of(menuProductRequest));

        when(menuService.create(any(MenuCreateRequest.class))).thenThrow(new ProductNotExistsException());

        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(menuRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격_총합보다_크면_400() throws Exception {
        MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(1L, 1);
        MenuCreateRequest menuRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(1_000), 1L,
                List.of(menuProductRequest));

        when(menuService.create(any(MenuCreateRequest.class))).thenThrow(new ExpensiveMenuPriceException());

        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(menuRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 메뉴_목록_조회에_성공한다() throws Exception {
        MenuProductResponse expectedMenuProduct = new MenuProductResponse(1L, 1L, 1);
        MenuResponse expected =
                new MenuResponse(1L, "메뉴", BigDecimal.valueOf(1_000), 1L, List.of(expectedMenuProduct));

        when(menuService.list()).thenReturn(List.of(expected));

        MvcResult mvcResult = mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<MenuResponse> content = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<List<MenuResponse>>() {
                });

        assertThat(content).hasSize(1)
                .extracting("id")
                .containsExactly(1L);
    }
}
