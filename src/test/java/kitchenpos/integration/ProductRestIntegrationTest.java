package kitchenpos.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.Product;

class ProductRestIntegrationTest extends IntegrationTest {

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {

        Product createProduct = TestObjectUtils.createProduct(null, "참치뱃살",
                BigDecimal.valueOf(30000));
        String json = objectMapper.writeValueAsString(createProduct);

        mockMvc.perform(
                post("/api/products")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(createProduct.getName()))
                .andExpect(jsonPath("price").value(createProduct.getPrice().longValue()));
    }

    @DisplayName("상품을 조회 할 수 있다.")
    @Test
    void listTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/products")
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<Product> products = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        assertThat(products.size()).isEqualTo(7);
    }
}