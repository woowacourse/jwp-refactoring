package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.vo.Price;
import kitchenpos.supports.IntegrationTest;
import kitchenpos.supports.MenuFixture;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("메뉴 서비스 테스트")
@IntegrationTest
class MenuServiceTest {

    private static final long INVALID_ID = -1L;

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("메뉴를 생성할 때")
    class Create {

        @DisplayName("정상적으로 생성할 수 있다")
        @ParameterizedTest
        @ValueSource(ints = {0, 1000})
        void success(int price) {
            // given
            final ProductResponse product = productService.create(ProductFixture.create());
            final MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupFixture.create());
            final MenuRequest request = MenuFixture.of(
                    BigDecimal.valueOf(price),
                    menuGroup.getId(),
                    List.of(product)
            );

            // when
            final MenuResponse savedMenu = menuService.create(request);

            // then
            assertSoftly(softly -> {
                assertThat(savedMenu.getId()).isPositive();
                assertThat(savedMenu.getName()).isEqualTo(request.getName());
                assertThat(savedMenu.getMenuProducts().get(0).getMenuId()).isPositive();
            });
        }

        @DisplayName("존재하지 않는 메뉴 그룹이면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidMenuGroup() {
            // given
            final ProductResponse product = productService.create(ProductFixture.create());
            final MenuRequest request = MenuFixture.of(
                    INVALID_ID,
                    List.of(product)
            );

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴 그룹입니다.");
        }

        @DisplayName("존재하지 않는 상품이면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidProduct() {
            // given
            final MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupFixture.create());
            final MenuRequest request = new MenuRequest(
                    "떡볶이",
                    new Price(BigDecimal.valueOf(5000)),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(INVALID_ID, 2))
            );

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 상품이 포함되어 있습니다.");
        }

        @DisplayName("메뉴 가격이 상품과 수량의 곱의 총 합보다 크면 예외처리 한다.")
        @Test
        void throwExceptionWhenInvalidPrice() {
            // given
            final ProductResponse product = productService.create(ProductFixture.create());
            final MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupFixture.create());

            int productCount = 2;
            final Price normalPrice = new Price(product.getPrice().multiply(BigDecimal.valueOf(2)));
            final Price higherPrice = normalPrice.add(new Price(BigDecimal.ONE));
            final MenuRequest request = new MenuRequest(
                    "떡볶이",
                    higherPrice,
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), productCount))
            );

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격 조정이 필요합니다.");
        }
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void findAllMenus() {
        // given
        final ProductResponse product = productService.create(ProductFixture.create());
        final MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupFixture.create());
        final MenuResponse menu = menuService.create(MenuFixture.of(menuGroup.getId(), List.of(product)));

        // when
        final List<MenuResponse> response = menuService.list();

        // then
        assertSoftly(softly -> {
            assertThat(response).hasSize(1);
            assertThat(response.get(0))
                    .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .usingRecursiveComparison()
                    .isEqualTo(menu);
        });
    }
}
