package kitchenpos.ui;

import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class ProductRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("Product를 생성한다.")
    void create() throws Exception {
        Product product = new Product(1L, PRODUCT1_NAME, PRODUCT1_PRICE);

        given(productService.create(any(Product.class))).willReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/products/1"));
    }

    @Test
    @DisplayName("모든 Product를 조회한다.")
    void list() throws Exception {
        List<Product> products = List.of(new Product(1L, PRODUCT1_NAME, PRODUCT1_PRICE),
                new Product(2L, PRODUCT2_NAME, PRODUCT2_PRICE));

        given(productService.list()).willReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(products)));
    }
}
