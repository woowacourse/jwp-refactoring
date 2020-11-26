package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.dto.menu.*;
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
import java.util.Collections;
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
public class MenuRestControllerTest {
    private static final String API = "/api";
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final Long 메뉴_ID_1 = 1L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");
    private static final String 상품_후라이드_치킨 = "후라이드 치킨";
    private static final String 상품_코카콜라 = "코카콜라";
    private static final Long 상품_1개 = 1L;
    private static final Long 상품_2개 = 2L;
    private static final Long 상품_ID_1 = 1L;
    private static final Long 상품_ID_2 = 2L;
    private static final BigDecimal 상품_가격_15000원 = new BigDecimal("15000.0");
    private static final BigDecimal 상품_가격_1000원 = new BigDecimal("1000.0");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    MenuService menuService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @DisplayName("'/menus'로 POST 요청 시, 메뉴 그룹을 생성한다")
    @Test
    void createTest() throws Exception {
        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개),
                new ProductQuantityRequest(상품_ID_2, 상품_2개)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, 메뉴_그룹_ID_1, productQuantityRequests);

        List<ProductResponse> products = Arrays.asList(
                new ProductResponse(상품_ID_1, 상품_후라이드_치킨, 상품_가격_15000원),
                new ProductResponse(상품_ID_2, 상품_코카콜라, 상품_가격_1000원)
        );
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        MenuResponse menuResponse = new MenuResponse(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroupResponse, products);

        when(menuService.create(any(MenuCreateRequest.class))).thenReturn(menuResponse);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Menu 생성 요청의 이름이 유효하지 않으면, 예외를 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void createWithInvalidNameExceptionTest(String invalidName) throws Exception {

        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개),
                new ProductQuantityRequest(상품_ID_2, 상품_2개)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(invalidName, 메뉴_가격_16000원, 메뉴_그룹_ID_1, productQuantityRequests);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Menu 생성 요청의 가격이 양수가 아니면, 예외를 반환한다.")
    @ValueSource(longs = {-1000, 0})
    @ParameterizedTest
    void createWithInvalidPriceExceptionTest(Long invalidPriceParam) throws Exception {
        BigDecimal invalidPrice = new BigDecimal(invalidPriceParam);
        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개),
                new ProductQuantityRequest(상품_ID_2, 상품_2개)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(메뉴_이름_후라이드_치킨, invalidPrice, 메뉴_그룹_ID_1, productQuantityRequests);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Menu 생성 요청의 가격이 null이면, 예외를 반환한다.")
    @Test
    void createWithNullPriceExceptionTest() throws Exception {
        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개),
                new ProductQuantityRequest(상품_ID_2, 상품_2개)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(메뉴_이름_후라이드_치킨, null, 메뉴_그룹_ID_1, productQuantityRequests);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Menu 생성 요청의 그룹 ID가 null이면, 예외를 반환한다.")
    @Test
    void createWithNullGroupIdExceptionTest() throws Exception {
        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개),
                new ProductQuantityRequest(상품_ID_2, 상품_2개)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, null, productQuantityRequests);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Menu 생성 요청의 메뉴 상품 리스트가 비어있으면, 예외를 반환한다.")
    @Test
    void createWithEmptyMenuProductListExceptionTest() throws Exception {
        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개),
                new ProductQuantityRequest(상품_ID_2, 상품_2개)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, 메뉴_ID_1, Collections.emptyList());
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Menu 생성 요청의 메뉴 상품 리스트가 null이면, 예외를 반환한다.")
    @Test
    void createWithNullMenuProductListExceptionTest() throws Exception {
        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개),
                new ProductQuantityRequest(상품_ID_2, 상품_2개)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, 메뉴_ID_1, null);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Menu 생성 요청의 메뉴 상품의 id가 null이면, 예외를 반환한다.")
    @Test
    void createWithNullMenuProductIdExceptionTest() throws Exception {
        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(null, 상품_1개)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, 메뉴_ID_1, null);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Menu 생성 요청의 메뉴 상품의 개수가 null이면, 예외를 반환한다.")
    @Test
    void createWithNullQuantityExceptionTest() throws Exception {
        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, null)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, 메뉴_ID_1, null);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 Menu 생성 요청의 메뉴 상품의 개수가 유효하지 않으면, 예외를 반환한다.")
    @ValueSource(longs = {-1000, 0})
    @ParameterizedTest
    void createWithInvalidQuantityExceptionTest(Long invalidQuantity) throws Exception {
        List<ProductQuantityRequest> productQuantityRequests = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, invalidQuantity)
        );
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, 메뉴_ID_1, null);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(menuCreateRequest);

        this.mockMvc.perform(post(API + "/menus").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("'/menus'로 GET 요청 시, 메뉴의 목록을 반환한다.")
    @Test
    void listTest() throws Exception {

        List<ProductResponse> products = Arrays.asList(
                new ProductResponse(상품_ID_1, 상품_후라이드_치킨, 상품_가격_15000원),
                new ProductResponse(상품_ID_2, 상품_코카콜라, 상품_가격_1000원)
        );
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        List<MenuResponse> menuResponses = Arrays.asList(
                new MenuResponse(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroupResponse, products)
        );

        when(menuService.list()).thenReturn(menuResponses);

        this.mockMvc.perform(get(API + "/menus").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].id").value(메뉴_ID_1)).
                andExpect(jsonPath("$[0].name").value(메뉴_이름_후라이드_치킨)).
                andExpect(jsonPath("$[0].price").value(메뉴_가격_16000원)).
                andExpect(jsonPath("$[0].menuGroup.id").value(메뉴_그룹_ID_1)).
                andExpect(jsonPath("$[0].menuGroup.name").value(메뉴_그룹_이름_후라이드_세트)).
                andExpect(jsonPath("$[0].products", hasSize(2))).
                andExpect(jsonPath("$[0].products[0].id").value(상품_ID_1)).
                andExpect(jsonPath("$[0].products[0].name").value(상품_후라이드_치킨)).
                andExpect(jsonPath("$[0].products[0].price").value(상품_가격_15000원)).
                andExpect(jsonPath("$[0].products[1].id").value(상품_ID_2)).
                andExpect(jsonPath("$[0].products[1].name").value(상품_코카콜라)).
                andExpect(jsonPath("$[0].products[1].price").value(상품_가격_1000원));
    }
}
