package kitchenpos.ui;

import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
class MenuRestControllerTest {
    private static final String MENU_API_URL = "/api/menus";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    ProductDao productDao;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .build();
    }

    @DisplayName("create: 이름을 body message에 포함해 메뉴 등록을 요청시 ,메뉴 생성 성공 시 201 응답을 반환한다.")
    @Test
    void create() throws Exception {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("delicious");
        final MenuGroup saveMenuGroup = menuGroupDao.save(menuGroup);

        final Menu menu = new Menu();
        menu.setName("맛있는 치킨 세마리 세트");
        menu.setPrice(BigDecimal.valueOf(40_000));
        menu.setMenuGroupId(saveMenuGroup.getId());
        final Product product = new Product();
        product.setName("맛있는 치킨");
        product.setPrice(BigDecimal.valueOf(15_000));
        final Product savedProduct = productDao.save(product);

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(3L);
        menuProduct.setProductId(savedProduct.getId());

        menu.setMenuProducts(asList(menuProduct));

        mockMvc.perform(post(MENU_API_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("맛있는 치킨 세마리 세트")))
                .andExpect(jsonPath("$.price", is(40_000d)))
                .andExpect(jsonPath("$.menuProducts", hasSize(1)))
                .andExpect(jsonPath("$.menuProducts[0].quantity", is(3)));
    }

    @DisplayName("list: 전체 메뉴 목록 요청시, 전체 메뉴 목록을 body message로 가지고 있는 status code 200 응답을 반환한다.")
    @Test
    void list() throws Exception {
        mockMvc.perform(get(MENU_API_URL)
        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }
}