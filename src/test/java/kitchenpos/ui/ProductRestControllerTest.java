package kitchenpos.ui;

import static kitchenpos.utils.TestObjects.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
class ProductRestControllerTest extends ControllerTest {

    @DisplayName("create: 이름, 가격을 body message에 포함해 제품 등록을 요청시 , 요청값을 바탕으로 제품을 생성 후 생성 성공 시 201 응답을 반환한다.")
    @Test
    void create() throws Exception {
        Product 치킨단품 = createProduct("치킨", BigDecimal.valueOf(16_000));

        String 제품추가_API_URL = "/api/products";
        ResultActions resultActions = create(제품추가_API_URL, 치킨단품);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue(Long.class)))
                .andExpect(jsonPath("$.name", is("치킨")))
                .andExpect(jsonPath("$.price", is(16_000d)));

    }

    @DisplayName("list: 제품 목록 요청시, 등록된 제품의 목록을 body message로 가지고 있는 status code 200 응답을 반환한다.")
    @Test
    void list() throws Exception {
        final String 제품목록조회_API_URL = "/api/products";
        final ResultActions resultActions = findList(제품목록조회_API_URL);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}