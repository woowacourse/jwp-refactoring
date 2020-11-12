package kitchenpos.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;

class ProductRestIntegrationTest extends IntegrationTest {

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {

        Product createdProduct = TestObjectUtils.createProduct(null, "참치뱃살",
                BigDecimal.valueOf(30000));
        String createdProductJson = objectMapper.writeValueAsString(createdProduct);

        mockMvc.perform(
                post("/api/products")
                        .content(createdProductJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(createdProduct.getName()))
                .andExpect(jsonPath("price").value(createdProduct.getPrice().longValue()));
    }

    @DisplayName("상품을 조회 할 수 있다.")
    @Test
    void listTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/products")
        )
                .andExpect(status().isOk())
                .andReturn();

        List<OrderLineItem> orderLineItems = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, OrderLineItem.class));

        assertThat(orderLineItems.size()).isEqualTo(6);
    }
}