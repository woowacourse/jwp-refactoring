package kitchenpos.ui;

import static kitchenpos.fixture.ProductFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class ProductRestControllerTest extends AbstractControllerTest {
    @Autowired
    private ProductDao productDao;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        Product productRequest = createProductRequest("치킨", 0L);

        mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(productRequest))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(productRequest.getName()))
            .andExpect(jsonPath("$.price").value(productRequest.getPrice().longValue()));
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<Product> products = productDao.findAll();

        String json = mockMvc.perform(get("/api/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        List<Product> response = objectMapper.readValue(json,
            objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));

        assertThat(response).usingFieldByFieldElementComparator().containsAll(products);
    }
}
