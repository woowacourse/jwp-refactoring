package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.dto.menu.ProductCreateRequest;
import kitchenpos.dto.menu.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ProductRestControllerTest {
    private static final String API = "/api";
    private static final String 상품_이름_치킨 = "후라이드 치킨";
    private static final BigDecimal 상품_가격_16000원 = new BigDecimal("16000.0");
    private static final Long 상품_ID_1 = 1L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    private ProductService productService;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @DisplayName("'/products'로 POST 요청 시, 상품을 생성한다")
    @Test
    void createTest() throws Exception {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(상품_이름_치킨, 상품_가격_16000원);
        ProductResponse productResponse = new ProductResponse(1L, 상품_이름_치킨, 상품_가격_16000원);
        when(productService.create(any(ProductCreateRequest.class))).thenReturn(productResponse);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(productCreateRequest);

        this.mockMvc.perform(post(API + "/products").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Product 생성 요청의 이름이 유효하지 않으면, 예외를 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void createWithCreateRequestInvalidNameExceptionTest(String invalidName) throws Exception {
        ProductCreateRequest invalidRequest = new ProductCreateRequest(invalidName, 상품_가격_16000원);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(post(API + "/products").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Product 생성 요청의 가격이 양수가 아니면, 예외를 반환한다.")
    @ValueSource(longs = {-1000, 0})
    @ParameterizedTest
    void createWithCreateRequestInvalidPriceExceptionTest(long invalidPriceParam) throws Exception {
        BigDecimal invalidPrice = new BigDecimal(invalidPriceParam);
        ProductCreateRequest invalidRequest = new ProductCreateRequest(상품_이름_치킨, invalidPrice);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(post(API + "/products").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Product 생성 요청의 가격이 Null이면, 예외를 반환한다.")
    @Test
    void createWithCreateRequestNullPriceExceptionTest() throws Exception {
        ProductCreateRequest invalidRequest = new ProductCreateRequest(상품_이름_치킨, null);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(post(API + "/products").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("'/products'로 GET 요청 시, 상품의 목록을 반환한다.")
    @Test
    void listTest() throws Exception {
        ProductResponse productResponse = new ProductResponse(상품_ID_1, 상품_이름_치킨, 상품_가격_16000원);
        List<ProductResponse> productResponses = Arrays.asList(productResponse);

        when(productService.list()).thenReturn(productResponses);

        this.mockMvc.perform(get(API + "/products").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].id").value(상품_ID_1)).
                andExpect(jsonPath("$[0].name").value(상품_이름_치킨)).
                andExpect(jsonPath("$[0].price").value(상품_가격_16000원));
    }
}
