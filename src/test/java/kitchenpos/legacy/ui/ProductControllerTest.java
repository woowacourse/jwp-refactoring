package kitchenpos.legacy.ui;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.legacy.application.LegacyProductService;
import kitchenpos.legacy.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(ReplaceUnderscores.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LegacyProductService productService;

    @Test
    @DisplayName("/api/products로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        Product request = new Product();
        Product response = new Product();
        response.setId(1L);
        given(productService.create(any(Product.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/products/1"));
    }

    @Test
    @DisplayName("/api/products로 GET 요청을 보내면 200 응답과 결과가 반환된다.")
    void findAll_with_200() throws Exception {
        // given
        given(productService.findAll())
            .willReturn(List.of(new Product(), new Product()));

        // when & then
        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2));
    }
}
