package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ObjectMapperForTest;
import kitchenpos.application.ProductService;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest extends ObjectMapperForTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        ProductRequest product = new ProductRequest("후라이드 치킨", 15000L);
        ProductResponse expected = new ProductResponse(1L, "후라이드 치킨", 15000L);
        given(productService.create(any(ProductRequest.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(product))
        );

        //then
        response.andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/products/%s", expected.getId())))
            .andExpect(content().json(objectToJson(expected)));
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void readAll() throws Exception {
        //given
        ProductResponse productA = new ProductResponse(1L, "후라이드 치킨", 15000L);
        ProductResponse productB = new ProductResponse(2L, "양념 치킨", 16000L);
        ProductResponse productC = new ProductResponse(3L, "간장 치킨", 17000L);
        List<ProductResponse> expected = Arrays.asList(productA, productB, productC);
        given(productService.list()).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(get("/api/products"));

        //then
        response.andExpect(status().isOk())
            .andExpect(content().json(objectToJson(expected)));
    }
}
