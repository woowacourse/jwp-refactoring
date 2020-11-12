package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProductService productService;

    @Test
    void create() throws Exception {
        ProductRequest request = new ProductRequest("product", BigDecimal.TEN);

        String response = mockMvc.perform(post("/api/products")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ProductResponse responseProduct = mapper.readValue(response, ProductResponse.class);

        assertAll(
            () -> assertThat(responseProduct.getId()).isNotNull(),
            () -> assertThat(responseProduct.getName()).isEqualTo(request.getName()),
            () -> assertThat(responseProduct.getPrice().longValue()).isEqualTo(request.getPrice().longValue())
        );
    }

    @Test
    void list() throws Exception {
        ProductResponse savedProduct = productService.create(new ProductRequest("1", BigDecimal.TEN));
        ProductResponse savedProduct2 = productService.create(new ProductRequest("2", BigDecimal.TEN));

        String response = mockMvc.perform(get("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<ProductResponse> responseProducts = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, ProductResponse.class));

        assertThat(responseProducts).usingElementComparatorOnFields("name")
            .containsAll(Arrays.asList(savedProduct, savedProduct2));
    }
}