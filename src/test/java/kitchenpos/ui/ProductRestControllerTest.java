package kitchenpos.ui;

import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("상품 문서화 테스트")
class ProductRestControllerTest extends ApiDocument {
    @DisplayName("상품 저장 - 성공")
    @Test
    void product_create() throws Exception {
        //given
        final Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(new BigDecimal(17000));
        //when
        willReturn(ProductFixture.강정치킨).given(productService).create(any(Product.class));
        final ResultActions result = 상품_저장_요청(product);
        //then
        상품_저장_성공함(result, ProductFixture.강정치킨);
    }

    @DisplayName("상품 조회 - 성공")
    @Test
    void product_findAll() throws Exception {
        //given
        //when
        willReturn(ProductFixture.products()).given(productService).list();
        final ResultActions result = 상품_조회_요청();
        //then
        상품_조회_성공함(result, ProductFixture.products());
    }

    private ResultActions 상품_저장_요청(Product product) throws Exception {
        return mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(product))
        );
    }

    private ResultActions 상품_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void 상품_저장_성공함(ResultActions result, Product product) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(product)))
                .andExpect(header().string("Location", "/api/products/" + product.getId()))
                .andDo(toDocument("product-create"));
    }

    private void 상품_조회_성공함(ResultActions result, List<Product> products) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(products)))
                .andDo(toDocument("product-findAll"));
    }
}