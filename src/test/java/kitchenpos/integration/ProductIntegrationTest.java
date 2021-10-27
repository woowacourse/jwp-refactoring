package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.product.request.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("Product 통합테스트")
class ProductIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/products";

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        final ProductRequest productRequest = new ProductRequest("강정치킨", BigDecimal.valueOf(17_000));

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(productRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(productRequest.getName()))
            .andExpect(jsonPath("$.price").value(productRequest.getPrice()))
        ;

        final List<Product> foundProducts = productRepository.findAll();
        assertThat(foundProducts).hasSize(1);

        final Product foundProduct = foundProducts.get(0);
        assertThat(foundProduct.getName()).isEqualTo(productRequest.getName());
        assertThat(foundProduct.getPrice()).isEqualByComparingTo(productRequest.getPrice());
    }

    @DisplayName("생성 - 실패 - price가 null일 때")
    @Test
    void create_Fail_When_PriceIsNull() throws Exception {
        // given
        final ProductRequest productRequest = new ProductRequest("강정치킨", null);

        // when
        // then
        API를_요청하면_BadRequest를_응답한다(API_PATH, productRequest);
        Repository가_비어있다(productRepository);
    }

    @DisplayName("생성 - 실패 - 0보다 작을 때")
    @Test
    void create_Fail_When_PriceIsLessThanZero() throws Exception {
        // given
        final ProductRequest productRequest = new ProductRequest("강정치킨", BigDecimal.valueOf(-1));

        // when
        // then
        API를_요청하면_BadRequest를_응답한다(API_PATH, productRequest);
        Repository가_비어있다(productRepository);
    }

    @DisplayName("모든 Product들 조회 - 성공")
    @Test
    void findAll_Success() throws Exception {
        // given
        final Product product1 = Product를_저장한다("후라이드", 16_000);
        final Product product2 = Product를_저장한다("양념치킨", 17_000);

        // when
        // then
        mockMvc.perform(get(API_PATH))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").isNumber())
            .andExpect(jsonPath("$[0].name").value(product1.getName()))
            .andExpect(jsonPath("$[0].price").value(product1.getPrice()))
            .andExpect(jsonPath("$[1].id").isNumber())
            .andExpect(jsonPath("$[1].name").value(product2.getName()))
            .andExpect(jsonPath("$[1].price").value(product2.getPrice()))
        ;
    }
}
