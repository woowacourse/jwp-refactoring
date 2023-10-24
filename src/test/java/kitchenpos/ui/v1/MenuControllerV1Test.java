package kitchenpos.ui.v1;

import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.LongStream;
import kitchenpos.application.MenuService;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import kitchenpos.ui.dto.MenuProductResponse;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(ReplaceUnderscores.class)
@WebMvcTest(MenuControllerV1.class)
class MenuControllerV1Test {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MenuService menuService;

    @Test
    @DisplayName("/api/v1/menus로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        var request = new MenuCreateRequest(1000, 1L, "맥주세트", List.of(
            new MenuProductCreateRequest(10, 1L)
        ));
        var response = new MenuResponse(1L, "맥주세트", BigDecimal.valueOf(1000), 1L, List.of(
            new MenuProductResponse(1L,  1L, 10)
        ));

        given(menuService.create(any(MenuCreateRequest.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/v1/menus/1"));
    }

    @Test
    @DisplayName("/api/v1/menus로 GET 요청을 보내면 200 응답과 결과가 조회된다.")
    void findAll_with_200() throws Exception {
        // given
        var response = LongStream.rangeClosed(1, 5)
            .mapToObj(id -> {
                var menuProducts = List.of(new MenuProductResponse(id, 1L, 10));
                return new MenuResponse(id, "맥주세트" + id, BigDecimal.valueOf(1000), 1L, menuProducts);
            })
            .collect(toList());
        given(menuService.findAll())
            .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/menus")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(5));
    }
}
