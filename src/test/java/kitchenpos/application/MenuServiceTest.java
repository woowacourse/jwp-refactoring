package kitchenpos.application;

import static kitchenpos.fixtures.domain.MenuProductFixture.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.fixtures.domain.MenuFixture.MenuRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    private MenuGroupResponse savedMenuGroup;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        savedMenuGroup = saveMenuGroup("메뉴 그룹");
        savedProduct = saveProduct("상품", 100_000);
    }

    @DisplayName("create 메소드는 ")
    @Nested
    class CreateMethod {

        @DisplayName("메뉴를 생성한다.")
        @Test
        void Should_CreateMenu() {
            // given
            MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 1L);

            Menu menu = new MenuRequestBuilder()
                    .name("세트1")
                    .price(20_000)
                    .menuGroupId(savedMenuGroup.getId())
                    .menuProducts(menuProduct)
                    .build();

            // when
            Menu actual = menuService.create(menu);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getName()).isEqualTo(menu.getName());
                assertThat(actual.getPrice().doubleValue()).isEqualTo(menu.getPrice().doubleValue());
            });
        }

        @DisplayName("메뉴 가격이 null이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceOfMenuIsNull() {
            // given
            MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 1L);

            Menu menu = new MenuRequestBuilder()
                    .price(null)
                    .menuGroupId(savedMenuGroup.getId())
                    .menuProducts(menuProduct)
                    .build();

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }


        @DisplayName("메뉴의 메뉴 그룹이 존재하지 않는다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuGroupDoesNotExist() {
            // given
            MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 1L);

            Menu menu = new MenuRequestBuilder()
                    .menuGroupId(savedMenuGroup.getId() + 1)
                    .menuProducts(menuProduct)
                    .build();

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 메뉴 상품 리스트 중 존재하지 않은 상품이 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_ProductDoesNotExistInMenuProductList() {
            // given
            MenuProduct notSavedMenuProduct = createMenuProduct(savedProduct.getId() + 1, 1L);

            Menu menu = new MenuRequestBuilder()
                    .menuGroupId(savedMenuGroup.getId())
                    .menuProducts(notSavedMenuProduct)
                    .build();

            // when
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 모든 메뉴 상품 목록에 대해 '상품 가격 * 메뉴 상품 수량' 의 합이 메뉴의 가격보다 크다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuPriceIsBiggerThanSumOfProductOfProductPriceAndQuantity() {
            // given
            MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 1L);

            Menu menu = new MenuRequestBuilder()
                    .price(1_000_000)
                    .menuGroupId(savedMenuGroup.getId())
                    .menuProducts(menuProduct)
                    .build();

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {

        @DisplayName("저장된 전체 메뉴 리스트를 조회한다.")
        @Test
        void Should_ReturnAllMenuList() {
            // given
            MenuGroupResponse menuGroup = saveMenuGroup("메뉴 그룹");

            Product product = saveProduct("상품", 1_000_000);
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1L);

            int expected = 4;
            for (int i = 0; i < expected; i++) {
                Menu menu = new MenuRequestBuilder()
                        .name("menu " + i)
                        .menuGroupId(menuGroup.getId())
                        .menuProducts(menuProduct)
                        .build();

                menuService.create(menu);
            }

            // when
            List<Menu> actual = menuService.list();

            // then
            assertAll(() -> {
                assertThat(actual).hasSize(expected);
            });
        }
    }
}
