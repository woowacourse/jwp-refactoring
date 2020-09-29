package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
class ProductRestControllerTest {
    private static final String PRODUCT_API_URL = "/api/products";

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .build();
    }

    @DisplayName("create: 이름, 가격을 body message에 포함해 제품 등록을 요청시 , 요청값을 바탕으로 제품을 생성 후 생성 성공 시 201 응답을 반환한다.")
    @Test
    void create() throws Exception {
        final Product product = new Product();
        product.setName("치킨");
        product.setPrice(BigDecimal.valueOf(16000));

        mockMvc.perform(post(PRODUCT_API_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @DisplayName("list: 제품 목록 요청시, 등록된 제품의 목록을 body message로 가지고 있는 status code 200 응답을 반환한다.")
    @Test
    void list() throws Exception {
        mockMvc.perform(get(PRODUCT_API_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(6)));
    }
}