package kitchenpos.ui;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Product;
import kitchenpos.dto.request.CreateProductRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("MenuGroupRestController 단위 테스트")
class ProductRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void create() throws Exception {
        // given
        CreateProductRequest 강정치킨 = new CreateProductRequest("강정치킨", BigDecimal.valueOf(17000));
        Product expected = new Product(1L, "강정치킨", 17000);
        given(productService.create(any(CreateProductRequest.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(post("/api/products")
                .content(objectToJsonString(강정치킨))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/products/" + expected.getId()))
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("상품 가격은 비어있을 수 없다.")
    void createWrongPriceNull() throws Exception {
        // given
        CreateProductRequest 강정치킨 = new CreateProductRequest("강정치킨", null);
        willThrow(new IllegalArgumentException("상품의 가격은 비어있을 수 없고 0 이상이어야 합니다.")).given(productService).create(any(CreateProductRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/products")
                .content(objectToJsonString(강정치킨))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("상품의 가격은 비어있을 수 없고 0 이상이어야 합니다."));
    }

    @Test
    @DisplayName("상품 가격이 음수일 수 없다.")
    void createWrongPriceUnderZero() throws Exception {
        // given
        CreateProductRequest 강정치킨 = new CreateProductRequest("강정치킨", BigDecimal.valueOf(-1));
        willThrow(new IllegalArgumentException("상품의 가격은 비어있을 수 없고 0 이상이어야 합니다.")).given(productService).create(any(CreateProductRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/products")
                .content(objectToJsonString(강정치킨))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("상품의 가격은 비어있을 수 없고 0 이상이어야 합니다."));
    }

    @Test
    @DisplayName("전체 상품을 조회할 수 있다.")
    void list() throws Exception {
        // given
        Product 강정치킨 = new Product(1L, "강정치킨", 17000);
        Product 구운치킨 = new Product(2L, "구운치킨", 14000);
        List<Product> expected = Arrays.asList(강정치킨, 구운치킨);
        given(productService.list()).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectToJsonString(expected)));
    }
}
