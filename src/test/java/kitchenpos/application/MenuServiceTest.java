package kitchenpos.application;

import static kitchenpos.fixtures.domain.MenuProductFixture.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    private MenuGroupResponse savedMenuGroup;
    private ProductResponse savedProduct;

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

            MenuRequest request = new MenuRequest(
                    "세트1",
                    BigDecimal.valueOf(20_000),
                    savedMenuGroup.getId(),
                    List.of(new MenuProductRequest(menuProduct))
            );

            // when
            MenuResponse actual = menuService.create(request);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getName()).isEqualTo(request.getName());
                assertThat(actual.getPrice().doubleValue()).isEqualTo(request.getPrice().doubleValue());
            });
        }

        @DisplayName("메뉴의 메뉴 그룹이 존재하지 않는다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuGroupDoesNotExist() {
            // given
            MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 1L);

            MenuRequest request = new MenuRequest(
                    "세트1",
                    BigDecimal.valueOf(20_000),
                    savedMenuGroup.getId() + 1,
                    List.of(new MenuProductRequest(menuProduct))
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 메뉴 상품 리스트 중 존재하지 않은 상품이 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_ProductDoesNotExistInMenuProductList() {
            // given
            MenuProduct notSavedMenuProduct = createMenuProduct(savedProduct.getId() + 1, 1L);

            MenuRequest request = new MenuRequest(
                    "세트1",
                    BigDecimal.valueOf(20_000),
                    savedMenuGroup.getId(),
                    List.of(new MenuProductRequest(notSavedMenuProduct))
            );

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 모든 메뉴 상품 목록에 대해 '상품 가격 * 메뉴 상품 수량' 의 합이 메뉴의 가격보다 크다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuPriceIsBiggerThanSumOfProductOfProductPriceAndQuantity() {
            // given
            MenuProduct menuProduct = createMenuProduct(savedProduct.getId(), 1L);

            MenuRequest request = new MenuRequest(
                    "세트1",
                    BigDecimal.valueOf(1_000_000),
                    savedMenuGroup.getId(),
                    List.of(new MenuProductRequest(menuProduct))
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
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

            ProductResponse product = saveProduct("상품", 1_000_000);
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1L);

            int expected = 4;
            for (int i = 0; i < expected; i++) {
                MenuRequest request = new MenuRequest(
                        "세트 " + i,
                        BigDecimal.valueOf(20_000),
                        menuGroup.getId(),
                        List.of(new MenuProductRequest(menuProduct))
                );

                menuService.create(request);
            }

            // when
            List<MenuResponse> actual = menuService.list();

            // then
            assertAll(() -> {
                assertThat(actual).hasSize(expected);
            });
        }
    }
}
