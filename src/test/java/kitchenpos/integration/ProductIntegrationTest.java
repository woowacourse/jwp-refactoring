package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("Product 통합테스트")
class ProductIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/products";

    @Autowired
    private ProductDao productDao;

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        final Map<String, Object> params = new HashMap<>();
        final String productName = "강정치킨";
        params.put("name", productName);

        final int productPriceValue = 17_000;
        final BigDecimal productPrice = BigDecimal.valueOf(productPriceValue);
        params.put("price", productPrice);

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(productName))
            .andExpect(jsonPath("$.price").value(productPriceValue))
        ;

        final List<Product> foundProducts = productDao.findAll();
        assertThat(foundProducts).hasSize(1);

        final Product foundProduct = foundProducts.get(0);
        assertThat(foundProduct.getId()).isPositive();
        assertThat(foundProduct.getName()).isEqualTo(productName);
        assertThat(foundProduct.getPrice()).isEqualByComparingTo(productPrice);
    }

    @DisplayName("생성 - 실패 - price가 null일 때")
    @Test
    void create_Fail_When_PriceIsNull() {
        // given
        final Map<String, Object> params = new HashMap<>();
        final String productName = "강정치킨";
        params.put("name", productName);
        params.put("price", null);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Product> foundProducts = productDao.findAll();
        assertThat(foundProducts).isEmpty();
    }

    @DisplayName("생성 - 실패 - 0보다 작을 때")
    @Test
    void create_Fail_When_PriceIsLessThanZero() {
        // given
        final Map<String, Object> params = new HashMap<>();
        final String productName = "강정치킨";
        params.put("name", productName);

        final BigDecimal productPrice = BigDecimal.valueOf(-1);
        params.put("price", productPrice);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Product> foundProducts = productDao.findAll();
        assertThat(foundProducts).isEmpty();
    }

    @DisplayName("모든 Product들 조회 - 성공")
    @Test
    void findAll_Success() throws Exception {
        // given
        final Product product1 = new Product();
        product1.setName("후라이드");
        final int product1PriceValue = 16_000;
        product1.setPrice(BigDecimal.valueOf(product1PriceValue));

        final Product product2 = new Product();
        product2.setName("양념치킨");
        final int product2PriceValue = 17_000;
        product2.setPrice(BigDecimal.valueOf(product2PriceValue));

        productDao.save(product1);
        productDao.save(product2);

        // when
        // then
        mockMvc.perform(get(API_PATH))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$[0].id").isNumber())
            .andExpect(jsonPath("$[0].name").value(product1.getName()))
            .andExpect(jsonPath("$[0].price").value(product1PriceValue))
            .andExpect(jsonPath("$[1].id").isNumber())
            .andExpect(jsonPath("$[1].name").value(product2.getName()))
            .andExpect(jsonPath("$[1].price").value(product2PriceValue))
        ;
    }
}
