package product.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import product.domain.Product;
import product.dto.request.CreateProductRequest;
import product.dto.response.ProductResponse;
import product.service.ProductService;
import test.ObjectCreator;
import value.Price;

@WebMvcTest(ProductRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 생성한다")
    @Test
    void create() throws Exception {
        // given
        final CreateProductRequest request = ObjectCreator.getObject(CreateProductRequest.class, "test", BigDecimal.ZERO);
        final Product product = ObjectCreator.getObject(Product.class, 1L, "test", new Price(BigDecimal.ZERO));
        when(productService.create(any()))
                .thenReturn(ProductResponse.from(product));

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("name").isString())
                .andExpect(jsonPath("price").isNumber());
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void list() throws Exception {
        // given
        when(productService.list())
                .thenReturn(List.of(
                        ProductResponse.from(ObjectCreator.getObject(Product.class, 1L, "test", new Price(BigDecimal.ZERO))),
                        ProductResponse.from(ObjectCreator.getObject(Product.class, 2L, "test", new Price(BigDecimal.ZERO)))
                ));

        // when & then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
