package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import support.IntegrationTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 서비스 테스트")
@IntegrationTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    private Product validProduct;
    private MenuGroup validMenuGroup;
    private MenuGroup invalidMenuGroup;
    private Product invalidProduct;

    @BeforeEach
    void setUp() {
        validMenuGroup = new MenuGroup();
        validMenuGroup.setId(1L);
        validMenuGroup.setName("교촌 셋트");

        invalidMenuGroup = new MenuGroup();
        invalidMenuGroup.setId(100L);
        invalidMenuGroup.setName("없는 셋트");

        validProduct = new Product();
        validProduct.setId(1L);
        validProduct.setName("후라이드");
        validProduct.setPrice(BigDecimal.valueOf(16_000));

        invalidProduct = new Product();
        invalidMenuGroup.setId(100L);
        invalidMenuGroup.setName("황천의 뒤틀린 치킨");
        invalidProduct.setPrice(BigDecimal.valueOf(100_000));
    }

    @Nested
    @DisplayName("[메뉴 추가]")
    class AddingMenu {

        @DisplayName("성공")
        @Test
        void create() {
            //given
            MenuProduct menuProduct = testMenuProduct(validProduct, 1);
            Menu menu = testMenu(validMenuGroup, menuProduct, validProduct.getPrice());

            //when
            Menu actual = menuService.create(menu);

            //then
            assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            assertThat(actual.getPrice().longValue()).isEqualTo(validProduct.getPrice().longValue());
            assertThat(actual.getMenuProducts()).hasSize(1);
            assertThat(actual.getMenuProducts().get(0).getMenuId()).isEqualTo(actual.getId());
            assertThat(actual.getMenuProducts().get(0).getProductId()).isEqualTo(menuProduct.getProductId());
            assertThat(actual.getMenuProducts().get(0).getQuantity()).isEqualTo(menuProduct.getQuantity());
        }

        @DisplayName("실패 - 가격이 null인 경우")
        @ParameterizedTest
        @NullSource
        void createWhenPriceIsNull(BigDecimal price) {
            //given
            MenuProduct menuProduct = testMenuProduct(validProduct, 1);
            Menu menu = testMenu(validMenuGroup, menuProduct, price);

            //when //then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("실패 - 가격이 0원 이하인 경우")
        @Test
        void createWhenPriceIsNegative() {
            //given
            MenuProduct menuProduct = testMenuProduct(validProduct, 1);
            Menu menu = testMenu(validMenuGroup, menuProduct, BigDecimal.valueOf(-1));

            //when //then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("실패 - 메뉴 그룹이 존재하지 않은 경우")
        @Test
        void createWhenMenuGroupIsNotExist() {
            //given
            MenuProduct menuProduct = testMenuProduct(validProduct, 1);
            Menu menu = testMenu(invalidMenuGroup, menuProduct, validProduct.getPrice());

            //when //then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("실패 - 메뉴 상품들 중에 존재하지 않는 상품이 있는 경우")
        @Test
        void createWhenHavingNotExistMenuProduct() {
            //given
            MenuProduct menuProduct = testMenuProduct(invalidProduct, 1);
            Menu menu = testMenu(validMenuGroup, menuProduct, invalidProduct.getPrice());

            //when //then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("실패 - 메뉴의 가격이 개별로 주문한 금액을 더한 값보다 큰 경우")
        @Test
        void createWhenMenuPriceIsBiggerThanSumOfEachOrderProducts() {
            //given
            MenuProduct menuProduct = testMenuProduct(validProduct, 1);
            Menu menu = testMenu(validMenuGroup, menuProduct, validProduct.getPrice().add(BigDecimal.ONE));

            //when //then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("[매뉴 조회] 성공")
    @Test
    void list() {
        //when
        List<Menu> actual = menuService.list();

        //then
        assertThat(actual).hasSize(6);
    }

    private MenuProduct testMenuProduct(Product product, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    private Menu testMenu(MenuGroup menuGroup, MenuProduct menuProduct, BigDecimal price) {
        Menu menu = new Menu();
        menu.setName("menu");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setPrice(price);
        menu.setMenuProducts(Collections.singletonList(menuProduct));
        return menu;
    }
}
