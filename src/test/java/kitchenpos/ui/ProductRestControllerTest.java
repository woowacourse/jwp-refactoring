package kitchenpos.ui;

import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest extends ControllerTest {

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    public void create() throws Exception {
        // given
        Product product = createProduct("강정치킨", 17_000L);
        given(productService.create(any())).willReturn(createProduct(1L));

        // when
        ResultActions perform = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(product)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("상품을 조회한다.")
    @Test
    public void list() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
