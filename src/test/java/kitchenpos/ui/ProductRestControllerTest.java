package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        // given
        final ProductRequest productRequest = new ProductRequest("후라이드 치킨", BigDecimal.valueOf(17000));

        given(productService.create(any()))
                .willReturn(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/products")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/api/products/1"));
    }

    @DisplayName("이름이 비어있으면 예외 처리한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void create_FailWhenRequestNameIsBlank(final String name) throws Exception {
        // given
        final ProductRequest productRequest = new ProductRequest(name, BigDecimal.valueOf(17000));

        given(productService.create(any()))
                .willReturn(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/products")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("가격이 null이거나 0원 미만이면 예외 처리한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1"})
    void create_FailWhenRequestPriceIsInvalid(final BigDecimal price) throws Exception {
        // given
        final ProductRequest productRequest = new ProductRequest("치킨", price);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/products")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("상품 목록을 반환할 수 있다.")
    @Test
    void list() throws Exception {
        // given
        final List<ProductResponse> productResponses = List.of(
                ProductResponse.from(new Product("후라이드 치킨", new Price(BigDecimal.valueOf(17000)))),
                ProductResponse.from(new Product("양념 치킨", new Price(BigDecimal.valueOf(18000))
                )));

        given(productService.list())
                .willReturn(productResponses);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/products")
                .contentType(APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }
}
