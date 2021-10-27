package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.request.MenuProductRequest;
import kitchenpos.dto.menu.request.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("Menu 통합테스트")
class MenuIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/menus";

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        final MenuGroup menuGroup = MenuGroup을_저장한다("추천메뉴");
        final Product product = Product를_저장한다("후라이드", 10_000);

        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 2L);
        final MenuRequest menuRequest = new MenuRequest(
            "후라이드+후라이드",
            20_000,
            menuGroup.getId(),
            Collections.singletonList(menuProductRequest)
        );

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(menuRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(menuRequest.getName()))
            .andExpect(jsonPath("$.price").value(menuRequest.getPrice()))
            .andExpect(jsonPath("$.menuGroupId").value(menuGroup.getId()))
            .andExpect(jsonPath("$.menuProducts.length()").value(1))
            .andExpect(jsonPath("$.menuProducts[0].seq").isNumber())
            .andExpect(jsonPath("$.menuProducts[0].menuId").isNumber())
            .andExpect(jsonPath("$.menuProducts[0].productId").value(product.getId()))
            .andExpect(jsonPath("$.menuProducts[0].quantity").value(menuProductRequest.getQuantity()))
        ;

        final List<Menu> foundMenus = menuRepository.findAll();
        assertThat(foundMenus).hasSize(1);

        final Menu foundMenu = foundMenus.get(0);
        final List<MenuProduct> foundMenuProducts = menuProductRepository.findAllByMenu(foundMenu);
        assertThat(foundMenuProducts).hasSize(1);

        final MenuProduct foundMenuProduct = foundMenuProducts.get(0);
        assertThat(foundMenuProduct.getMenuId()).isEqualTo(foundMenu.getId());
        assertThat(foundMenuProduct.getProduct()).isEqualTo(product);
        assertThat(foundMenuProduct.getQuantity()).isEqualTo(menuProductRequest.getQuantity());
    }

    @DisplayName("생성 - 실패 - 가격이 null일 때")
    @Test
    void create_Fail_When_PriceIsNull() throws Exception {
        // given
        final MenuGroup menuGroup = MenuGroup을_저장한다("추천메뉴");
        final Product product = Product를_저장한다("후라이드", 10_000);

        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 2L);
        final MenuRequest menuRequest = new MenuRequest(
            "후라이드+후라이드",
            null,
            menuGroup.getId(),
            Collections.singletonList(menuProductRequest)
        );

        // when
        // then
        API를_요청하면_BadRequest를_응답한다(API_PATH, menuRequest);
        Repository가_비어있다(menuRepository);
        Repository가_비어있다(menuProductRepository);
    }

    @DisplayName("생성 - 실패 - 가격이 0보다 작을 때")
    @Test
    void create_Fail_When_PriceIsLessThanZero() throws Exception {
        // given
        final MenuGroup menuGroup = MenuGroup을_저장한다("추천메뉴");
        final Product product = Product를_저장한다("후라이드", 10_000);

        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 2L);
        final MenuRequest menuRequest = new MenuRequest(
            "후라이드+후라이드",
            -1,
            menuGroup.getId(),
            Collections.singletonList(menuProductRequest)
        );

        // when
        // then
        API를_요청하면_BadRequest를_응답한다(API_PATH, menuRequest);
        Repository가_비어있다(menuRepository);
        Repository가_비어있다(menuProductRepository);
    }

    @DisplayName("생성 - 실패 - Menu의 MenuGroupId가 DB에 존재하지 않을 때")
    @Test
    void create_Fail_When_MenuGroupIdNotExistsInDB() throws Exception {
        // given
        final Product product = Product를_저장한다("후라이드", 10_000);

        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 2L);
        final MenuRequest menuRequest = new MenuRequest(
            "후라이드+후라이드",
            10_000,
            0L,
            Collections.singletonList(menuProductRequest)
        );

        // when
        // then
        API를_요청하면_BadRequest를_응답한다(API_PATH, menuRequest);
        Repository가_비어있다(menuRepository);
        Repository가_비어있다(menuProductRepository);
    }

    @DisplayName("생성 - 실패 - Product가 DB에 존재하지 않을 때")
    @Test
    void create_Fail_When_ProductNotExistsInDB() throws Exception {
        // given
        final MenuGroup menuGroup = MenuGroup을_저장한다("추천메뉴");

        final MenuProductRequest menuProductRequest = new MenuProductRequest(0L, 2L);
        final MenuRequest menuRequest = new MenuRequest(
            "후라이드+후라이드",
            10_000,
            menuGroup.getId(),
            Collections.singletonList(menuProductRequest)
        );

        // when
        // then
        API를_요청하면_BadRequest를_응답한다(API_PATH, menuRequest);
        Repository가_비어있다(menuRepository);
        Repository가_비어있다(menuProductRepository);
    }

    @DisplayName("생성 - 실패 - Menu의 가격이 MenuProduct들에 있는 모든 Product들의 가격의 합보다 클 때")
    @Test
    void create_Fail_When_MenuPriceIsGreaterThanPriceSumOfProductsOfMenuProducts() throws Exception {
        // given
        final MenuGroup menuGroup = MenuGroup을_저장한다("추천메뉴");
        final Product product = Product를_저장한다("후라이드", 10_000);

        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 2L);
        final MenuRequest menuRequest = new MenuRequest(
            "후라이드+후라이드",
            20_001,
            menuGroup.getId(),
            Collections.singletonList(menuProductRequest)
        );

        // when
        // then
        API를_요청하면_BadRequest를_응답한다(API_PATH, menuRequest);
        Repository가_비어있다(menuRepository);
        Repository가_비어있다(menuProductRepository);
    }

    @DisplayName("모든 Menu들 조회 - 성공")
    @Test
    void findAll_Success() throws Exception {
        // given
        final MenuGroup menuGroup = MenuGroup을_저장한다("추천메뉴");
        
        final Menu menu1 = Menu를_저장한다("후라이드메뉴", 16_000, menuGroup);
        final Menu menu2 = Menu를_저장한다("양념치킨메뉴", 34_000, menuGroup);

        final Product product1 = Product를_저장한다("후라이드치킨", 16_000);
        final Product product2 = Product를_저장한다("양념치킨", 17_000);

        final MenuProduct menu1MenuProduct = new MenuProduct(menu1, product1, 1L);
        final MenuProduct menu2MenuProduct = new MenuProduct(menu2, product2, 2L);
        menuProductRepository.saveAll(Arrays.asList(menu1MenuProduct, menu2MenuProduct));

        // when
        // then
        mockMvc.perform(get(API_PATH))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$[0].id").value(menu1.getId()))
            .andExpect(jsonPath("$[0].name").value(menu1.getName()))
            .andExpect(jsonPath("$[0].price").value(menu1.getPrice()))
            .andExpect(jsonPath("$[0].menuGroupId").value(menuGroup.getId()))
            .andExpect(jsonPath("$[0].menuProducts.length()").value(1))
            .andExpect(jsonPath("$[0].menuProducts[0].seq").value(menu1MenuProduct.getSeq()))
            .andExpect(jsonPath("$[0].menuProducts[0].menuId").value(menu1.getId()))
            .andExpect(jsonPath("$[0].menuProducts[0].productId").value(product1.getId()))
            .andExpect(jsonPath("$[0].menuProducts[0].quantity").value(menu1MenuProduct.getQuantity()))
            .andExpect(jsonPath("$[1].id").value(menu2.getId()))
            .andExpect(jsonPath("$[1].name").value(menu2.getName()))
            .andExpect(jsonPath("$[1].price").value(menu2.getPrice()))
            .andExpect(jsonPath("$[1].menuGroupId").value(menuGroup.getId()))
            .andExpect(jsonPath("$[1].menuProducts.length()").value(1))
            .andExpect(jsonPath("$[1].menuProducts[0].seq").value(menu2MenuProduct.getSeq()))
            .andExpect(jsonPath("$[1].menuProducts[0].menuId").value(menu2.getId()))
            .andExpect(jsonPath("$[1].menuProducts[0].productId").value(product2.getId()))
            .andExpect(jsonPath("$[1].menuProducts[0].quantity").value(menu2MenuProduct.getQuantity()))
        ;
    }
}
