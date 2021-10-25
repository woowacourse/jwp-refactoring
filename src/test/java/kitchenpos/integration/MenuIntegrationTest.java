package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("Menu 통합테스트")
class MenuIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/menus";

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        menuGroup = menuGroupDao.save(menuGroup);

        Product product = new Product();
        product.setName("후라이드");
        final int productPriceValue = 10_000;
        product.setPrice(BigDecimal.valueOf(productPriceValue));
        product = productDao.save(product);

        final Map<String, Object> params = new HashMap<>();
        final String menuName = "후라이드+후라이드";
        params.put("name", menuName);
        final int menuPriceValue = 20_000;
        params.put("price", BigDecimal.valueOf(menuPriceValue));
        params.put("menuGroupId", menuGroup.getId());

        final Map<String, Object> menuProductParams = new HashMap<>();
        menuProductParams.put("productId", product.getId());
        final long menuProductQuantity = 2L;
        menuProductParams.put("quantity", menuProductQuantity);

        final List<Object> menuProductsParams = new ArrayList<>();
        menuProductsParams.add(menuProductParams);

        params.put("menuProducts", menuProductsParams);

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(menuName))
            .andExpect(jsonPath("$.price").value(menuPriceValue))
            .andExpect(jsonPath("$.menuGroupId").value(menuGroup.getId()))
            .andExpect(jsonPath("$.menuProducts.length()").value(menuProductsParams.size()))
            .andExpect(jsonPath("$.menuProducts[0].seq").isNumber())
            .andExpect(jsonPath("$.menuProducts[0].menuId").isNumber())
            .andExpect(jsonPath("$.menuProducts[0].productId").value(product.getId()))
            .andExpect(jsonPath("$.menuProducts[0].quantity").value(menuProductQuantity))
        ;

        final List<Menu> foundMenus = menuDao.findAll();
        assertThat(foundMenus).hasSize(1);

        final Menu foundMenu = foundMenus.get(0);
        assertThat(foundMenu.getId()).isPositive();

        final List<MenuProduct> foundMenuProducts = menuProductDao.findAllByMenuId(foundMenu.getId());
        assertThat(foundMenuProducts).hasSize(1);

        final MenuProduct foundMenuProduct = foundMenuProducts.get(0);
        assertThat(foundMenuProduct.getSeq()).isPositive();
        assertThat(foundMenuProduct.getMenuId()).isEqualTo(foundMenu.getId());
        assertThat(foundMenuProduct.getProductId()).isEqualTo(product.getId());
        assertThat(foundMenuProduct.getQuantity()).isEqualTo(menuProductQuantity);
    }

    @DisplayName("생성 - 실패 - 가격이 null일 때")
    @Test
    void create_Fail_When_PriceIsNull() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        menuGroup = menuGroupDao.save(menuGroup);

        Product product = new Product();
        product.setName("후라이드");
        final int productPriceValue = 10_000;
        product.setPrice(BigDecimal.valueOf(productPriceValue));
        product = productDao.save(product);

        final Map<String, Object> params = new HashMap<>();
        final String menuName = "후라이드+후라이드";
        params.put("name", menuName);
        params.put("price", null);
        params.put("menuGroupId", menuGroup.getId());

        final Map<String, Object> menuProductParams = new HashMap<>();
        menuProductParams.put("productId", product.getId());
        final long menuProductQuantity = 2L;
        menuProductParams.put("quantity", menuProductQuantity);

        final List<Object> menuProductsParams = new ArrayList<>();
        menuProductsParams.add(menuProductParams);

        params.put("menuProducts", menuProductsParams);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Menu> foundMenus = menuDao.findAll();
        assertThat(foundMenus).isEmpty();

        final List<MenuProduct> foundMenuProducts = menuProductDao.findAll();
        assertThat(foundMenuProducts).isEmpty();
    }

    @DisplayName("생성 - 실패 - 가격이 0보다 작을 때")
    @Test
    void create_Fail_When_PriceIsLessThanZero() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        menuGroup = menuGroupDao.save(menuGroup);

        Product product = new Product();
        product.setName("후라이드");
        final int productPriceValue = 10_000;
        product.setPrice(BigDecimal.valueOf(productPriceValue));
        product = productDao.save(product);

        final Map<String, Object> params = new HashMap<>();
        final String menuName = "후라이드+후라이드";
        params.put("name", menuName);
        params.put("price", BigDecimal.valueOf(-1));
        params.put("menuGroupId", menuGroup.getId());

        final Map<String, Object> menuProductParams = new HashMap<>();
        menuProductParams.put("productId", product.getId());
        final long menuProductQuantity = 2L;
        menuProductParams.put("quantity", menuProductQuantity);

        final List<Object> menuProductsParams = new ArrayList<>();
        menuProductsParams.add(menuProductParams);

        params.put("menuProducts", menuProductsParams);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Menu> foundMenus = menuDao.findAll();
        assertThat(foundMenus).isEmpty();

        final List<MenuProduct> foundMenuProducts = menuProductDao.findAll();
        assertThat(foundMenuProducts).isEmpty();
    }

    @DisplayName("생성 - 실패 - Menu의 MenuGroupId가 DB에 존재하지 않을 때")
    @Test
    void create_Fail_When_MenuGroupIdNotExistsInDB() {
        // given
        Product product = new Product();
        product.setName("후라이드");
        final int productPriceValue = 10_000;
        product.setPrice(BigDecimal.valueOf(productPriceValue));
        product = productDao.save(product);

        final Map<String, Object> params = new HashMap<>();
        final String menuName = "후라이드+후라이드";
        params.put("name", menuName);
        params.put("price", BigDecimal.valueOf(-1));
        params.put("menuGroupId", 0L);

        final Map<String, Object> menuProductParams = new HashMap<>();
        menuProductParams.put("productId", product.getId());
        final long menuProductQuantity = 2L;
        menuProductParams.put("quantity", menuProductQuantity);

        final List<Object> menuProductsParams = new ArrayList<>();
        menuProductsParams.add(menuProductParams);

        params.put("menuProducts", menuProductsParams);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Menu> foundMenus = menuDao.findAll();
        assertThat(foundMenus).isEmpty();

        final List<MenuProduct> foundMenuProducts = menuProductDao.findAll();
        assertThat(foundMenuProducts).isEmpty();
    }

    @DisplayName("생성 - 실패 - Product가 DB에 존재하지 않을 때")
    @Test
    void create_Fail_When_ProductNotExistsInDB() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        menuGroup = menuGroupDao.save(menuGroup);

        final Map<String, Object> params = new HashMap<>();
        final String menuName = "후라이드+후라이드";
        params.put("name", menuName);
        params.put("price", null);
        params.put("menuGroupId", menuGroup.getId());

        final Map<String, Object> menuProductParams = new HashMap<>();
        menuProductParams.put("productId", 0L);
        final long menuProductQuantity = 2L;
        menuProductParams.put("quantity", menuProductQuantity);

        final List<Object> menuProductsParams = new ArrayList<>();
        menuProductsParams.add(menuProductParams);

        params.put("menuProducts", menuProductsParams);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Menu> foundMenus = menuDao.findAll();
        assertThat(foundMenus).isEmpty();

        final List<MenuProduct> foundMenuProducts = menuProductDao.findAll();
        assertThat(foundMenuProducts).isEmpty();
    }

    @DisplayName("생성 - 실패 - Menu의 가격이 MenuProduct들에 있는 모든 Product들의 가격의 합보다 클 때")
    @Test
    void create_Fail_When_MenuPriceIsGreaterThanPriceSumOfProductsOfMenuProducts() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        menuGroup = menuGroupDao.save(menuGroup);

        Product product = new Product();
        product.setName("후라이드");
        final int productPriceValue = 10_000;
        product.setPrice(BigDecimal.valueOf(productPriceValue));
        product = productDao.save(product);

        final Map<String, Object> params = new HashMap<>();
        final String menuName = "후라이드+후라이드";
        params.put("name", menuName);
        final int menuPriceValue = 20_001;
        params.put("price", BigDecimal.valueOf(menuPriceValue));
        params.put("menuGroupId", menuGroup.getId());

        final Map<String, Object> menuProductParams = new HashMap<>();
        menuProductParams.put("productId", product.getId());
        final long menuProductQuantity = 2L;
        menuProductParams.put("quantity", menuProductQuantity);

        final List<Object> menuProductsParams = new ArrayList<>();
        menuProductsParams.add(menuProductParams);

        params.put("menuProducts", menuProductsParams);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Menu> foundMenus = menuDao.findAll();
        assertThat(foundMenus).isEmpty();

        final List<MenuProduct> foundMenuProducts = menuProductDao.findAll();
        assertThat(foundMenuProducts).isEmpty();
    }

    @DisplayName("모든 Menu들 조회 - 성공")
    @Test
    void findAll_Success() throws Exception {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        menuGroup = menuGroupDao.save(menuGroup);

        Product product1 = new Product();
        product1.setName("후라이드치킨상품");
        final int product1PriceValue = 16_000;
        product1.setPrice(BigDecimal.valueOf(product1PriceValue));
        product1 = productDao.save(product1);

        Product product2 = new Product();
        product2.setName("양념치킨상품");
        final int product2PriceValue = 17_000;
        product2.setPrice(BigDecimal.valueOf(product2PriceValue));
        product2 = productDao.save(product2);

        final Menu menu1 = new Menu();
        final int menu1PriceValue = 16_000;
        menu1.setName("후라이드치킨");
        menu1.setPrice(BigDecimal.valueOf(menu1PriceValue));
        menu1.setMenuGroupId(menuGroup.getId());

        final MenuProduct menu1MenuProduct = new MenuProduct();
        menu1MenuProduct.setProductId(product1.getId());
        menu1MenuProduct.setQuantity(1L);

        final List<MenuProduct> menu1MenuProducts = new ArrayList<>();
        menu1MenuProducts.add(menu1MenuProduct);
        menu1.setMenuProducts(menu1MenuProducts);

        final Menu menu2 = new Menu();
        final int menu2PriceValue = 34_000;
        menu2.setName("양념치킨");
        menu2.setPrice(BigDecimal.valueOf(menu2PriceValue));
        menu2.setMenuGroupId(menuGroup.getId());

        final MenuProduct menu2MenuProduct = new MenuProduct();
        menu2MenuProduct.setProductId(product2.getId());
        menu2MenuProduct.setQuantity(2L);
        final List<MenuProduct> menu2MenuProducts = new ArrayList<>();
        menu2MenuProducts.add(menu2MenuProduct);

        menu2.setMenuProducts(menu2MenuProducts);

        final Menu savedMenu1 = menuDao.save(menu1);
        menu1MenuProduct.setMenuId(savedMenu1.getId());
        final Menu savedMenu2 = menuDao.save(menu2);
        menu2MenuProduct.setMenuId(savedMenu2.getId());
        final MenuProduct savedMenu1MenuProduct = menuProductDao.save(menu1MenuProduct);
        final MenuProduct savedMenu2MenuProduct = menuProductDao.save(menu2MenuProduct);

        // when
        // then
        mockMvc.perform(get(API_PATH))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$[0].id").value(savedMenu1.getId()))
            .andExpect(jsonPath("$[0].name").value(menu1.getName()))
            .andExpect(jsonPath("$[0].price").value(menu1PriceValue))
            .andExpect(jsonPath("$[0].menuGroupId").value(menuGroup.getId()))
            .andExpect(jsonPath("$[0].menuProducts.length()").value(menu1MenuProducts.size()))
            .andExpect(jsonPath("$[0].menuProducts[0].seq").value(savedMenu1MenuProduct.getSeq()))
            .andExpect(jsonPath("$[0].menuProducts[0].menuId").value(savedMenu1.getId()))
            .andExpect(jsonPath("$[0].menuProducts[0].productId").value(product1.getId()))
            .andExpect(jsonPath("$[0].menuProducts[0].quantity").value(menu1MenuProduct.getQuantity()))
            .andExpect(jsonPath("$[1].id").value(savedMenu2.getId()))
            .andExpect(jsonPath("$[1].name").value(menu2.getName()))
            .andExpect(jsonPath("$[1].price").value(menu2PriceValue))
            .andExpect(jsonPath("$[1].menuGroupId").value(menuGroup.getId()))
            .andExpect(jsonPath("$[1].menuProducts.length()").value(menu2MenuProducts.size()))
            .andExpect(jsonPath("$[1].menuProducts[0].seq").value(savedMenu2MenuProduct.getSeq()))
            .andExpect(jsonPath("$[1].menuProducts[0].menuId").value(savedMenu2.getId()))
            .andExpect(jsonPath("$[1].menuProducts[0].productId").value(product2.getId()))
            .andExpect(jsonPath("$[1].menuProducts[0].quantity").value(menu2MenuProduct.getQuantity()))
        ;
    }
}
