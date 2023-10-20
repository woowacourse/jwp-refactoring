package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuServiceTest extends ServiceTestConfig {

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository);
    }

    @DisplayName("메뉴 생성")
    @Nested
    class Create {

        private MenuGroup menuGroup;
        private Product product;

        @BeforeEach
        void setUp() {
            menuGroup = saveMenuGroup();
            product = saveProduct();
        }

        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(product.getId(), 2L);
            final BigDecimal sumOfProductPrice = product.getPrice().multiply(menuProductCreateRequest.getQuantity()).getValue();
            final MenuCreateRequest request = new MenuCreateRequest(
                    "여우 메뉴",
                    sumOfProductPrice,
                    menuGroup.getId(),
                    List.of(menuProductCreateRequest)
            );

            // when
            final MenuResponse actual = menuService.create(request);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getName()).isEqualTo(request.getName());
                softly.assertThat(actual.getPrice()).isEqualByComparingTo(request.getPrice());
                softly.assertThat(actual.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
                softly.assertThat(actual.getMenuProducts().size()).isEqualTo(request.getMenuProducts().size());
                softly.assertThat(actual.getMenuProducts().get(0).getProductId()).isEqualTo(request.getMenuProducts().get(0).getProductId());
                softly.assertThat(actual.getMenuProducts().get(0).getQuantity()).isEqualTo(request.getMenuProducts().get(0).getQuantity());
            });
        }

        @DisplayName("가격을 입력하지 않으면 실패한다.")
        @Test
        void fail_if_price_is_null() {
            // given
            final MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(product.getId(), 2L);
            final MenuCreateRequest request = new MenuCreateRequest(
                    "여우 메뉴",
                    null,
                    menuGroup.getId(),
                    List.of(menuProductCreateRequest)
            );

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0원 미만이면 실패한다.")
        @Test
        void fail_if_price_under_zero() {
            // given
            final MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(product.getId(), 2L);
            final MenuCreateRequest request = new MenuCreateRequest(
                    "여우 메뉴",
                    BigDecimal.valueOf(-1),
                    menuGroup.getId(),
                    List.of(menuProductCreateRequest)
            );

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 MenuGroup 이면 실패한다.")
        @Test
        void fail_if_menuGroup_not_exist() {
            // given
            final MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(product.getId(), 2L);
            final BigDecimal sumOfProductPrice = product.getPrice().multiply(menuProductCreateRequest.getQuantity()).getValue();
            final MenuCreateRequest request = new MenuCreateRequest(
                    "여우 메뉴",
                    sumOfProductPrice,
                    -1L,
                    List.of(menuProductCreateRequest)
            );

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOfAny(IllegalArgumentException.class, InvalidDataAccessApiUsageException.class);
        }

        @DisplayName("Menu 가격이 Product 의 가격 합보다 크면 실패한다.")
        @Test
        void fail_if_menu_price_is_bigger_than_sum_of_product_price() {
            // given
            final MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(product.getId(), 2L);
            final BigDecimal sumOfProductPrice = product.getPrice().multiply(menuProductCreateRequest.getQuantity()).getValue();
            final MenuCreateRequest request = new MenuCreateRequest(
                    "여우 메뉴",
                    sumOfProductPrice.add(BigDecimal.ONE),
                    menuGroup.getId(),
                    List.of(menuProductCreateRequest)
            );

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOfAny(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 전체 조회")
    @Nested
    class ReadAll {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final MenuGroup menuGroup = saveMenuGroup();
            final Product product = saveProduct();
            final Menu menu = saveMenu(menuGroup, product);

            // when
            final List<MenuResponse> actual = menuService.list();

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(menu.getId());
            });
        }
    }
}
