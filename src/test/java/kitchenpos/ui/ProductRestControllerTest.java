package kitchenpos.ui;

import static kitchenpos.fixture.ProductFixture.PRODUCT_FIXTURE_1;
import static kitchenpos.fixture.ProductFixture.PRODUCT_FIXTURE_2;
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
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
    private ProductDao productDao;

    @Test
    void create() throws Exception {
        Product request = new Product();
        request.setName("product");
        request.setPrice(BigDecimal.TEN);

        String response = mockMvc.perform(post("/api/products")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Product responseProduct = mapper.readValue(response, Product.class);

        assertAll(
            () -> assertThat(responseProduct.getId()).isNotNull(),
            () -> assertThat(responseProduct.getName()).isEqualTo(request.getName()),
            () -> assertThat(responseProduct.getPrice().longValue()).isEqualTo(request.getPrice().longValue())
        );
    }

    @Test
    void list() throws Exception {
        productDao.save(PRODUCT_FIXTURE_1);
        productDao.save(PRODUCT_FIXTURE_2);

        String response = mockMvc.perform(get("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<Product> responseProducts = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, Product.class));

        assertThat(responseProducts).usingElementComparatorOnFields("name")
            .containsAll(Arrays.asList(PRODUCT_FIXTURE_1, PRODUCT_FIXTURE_2));
    }
}