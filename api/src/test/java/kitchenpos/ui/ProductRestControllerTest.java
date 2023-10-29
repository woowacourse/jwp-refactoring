package kitchenpos.ui;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.api.product.ui.ProductRestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Map;
import kitchenpos.core.product.application.ProductService;
import kitchenpos.core.product.application.dto.ProductResponse;
import kitchenpos.core.product.domain.Product;
import kitchenpos.core.product.domain.Name;
import kitchenpos.core.product.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService productService;

    @Test
    @DisplayName("POST /api/products")
    void createProduct() throws Exception {
        final Product product = new Product(null, new Name("프로덕트"), new Price(new BigDecimal(4000L)));

        when(productService.create(any(Name.class), any(Price.class))).thenReturn(ProductResponse.from(product));
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "강정치킨", "price", 17000))))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
