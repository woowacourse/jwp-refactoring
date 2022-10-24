package kitchenpos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.ProductRestController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductRestController.class)
@Disabled
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ProductService productService;

    @ParameterizedTest
    @NullSource
    @CsvSource(value = {"-1"})
    @DisplayName("메뉴 가격이 null이거나 0보다 작을 수 없다.")
    void returnBadRequest_priceIsNullOrZero(final Long price) throws Exception {
        // given
        given(productService.create(any(Product.class)))
                .willThrow(IllegalArgumentException.class);
        final Product product = ProductFixture.createWithPrice(price);
        final byte[] serializedValue = objectMapper.writeValueAsBytes(product);

        // when, then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedValue))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }
}
