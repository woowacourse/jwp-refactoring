package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends MvcTest {

    @MockBean
    private ProductService productService;

    @DisplayName("/api/products로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(productService.create(any())).willReturn(PRODUCT_1);

        ProductCreateRequest productCreateRequest =
            new ProductCreateRequest(PRODUCT_NAME_1, PRODUCT_PRICE_1.getValue().longValue());

        String inputJson = objectMapper.writeValueAsString(productCreateRequest);
        MvcResult mvcResult = postAction("/api/products", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/products/%d", PRODUCT_ID_1)))
            .andReturn();

        Product productResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Product.class);
        assertThat(productResponse).usingRecursiveComparison().isEqualTo(PRODUCT_1);
    }

    @DisplayName("/api/products로 GET요청 성공 테스트")
    @Test
    void listTest() throws Exception {
        given(productService.list()).willReturn(Arrays.asList(PRODUCT_1, PRODUCT_2));

        MvcResult mvcResult = getAction("/api/products")
            .andExpect(status().isOk())
            .andReturn();

        List<Product> productsResponse = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new TypeReference<List<Product>>() {});
        assertAll(
            () -> assertThat(productsResponse.size()).isEqualTo(2),
            () -> assertThat(productsResponse.get(0)).usingRecursiveComparison().isEqualTo(PRODUCT_1),
            () -> assertThat(productsResponse.get(1)).usingRecursiveComparison().isEqualTo(PRODUCT_2)
        );
    }
}