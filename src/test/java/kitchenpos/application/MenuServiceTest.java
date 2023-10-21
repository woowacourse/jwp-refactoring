package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.세트_메뉴_1개씩;
import static kitchenpos.fixture.ProductFixture.치킨_8000원;
import static kitchenpos.fixture.ProductFixture.피자_8000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 메뉴_등록_성공_저장() {
        // given
        final List<Product> products = List.of(productService.create(치킨_8000원()), productService.create(피자_8000원()));
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroupCreateRequest("양식"));

        // when
        final Menu menu = menuService.create(
                세트_메뉴_1개씩("치킨_피자_세트", BigDecimal.valueOf(8000), menuGroup, products)
        );

        // then
        menuService.list()
                .stream()
                .filter(found -> Objects.equals(found.getId(), menu.getId()))
                .findFirst()
                .ifPresentOrElse(
                        found -> assertThat(found.getMenuProducts())
                                .hasSize(products.size()),
                        Assertions::fail
                );
    }

    @Test
    @DisplayName("등록되지 않은 메뉴 그룹으로 메뉴를 등록할 수 없다.")
    void 메뉴_등록_실패_등록되지_않은_메뉴_그룹() {
        // given
        final Product chicken = productService.create(치킨_8000원());
        final Product pizza = productService.create(피자_8000원());
        final MenuGroup unsavedMenuGroup = new MenuGroup("등록되지 않은 메뉴 그룹");

        // when
        final Menu menu = 세트_메뉴_1개씩("치킨_피자_세트", BigDecimal.valueOf(10000), unsavedMenuGroup, List.of(chicken, pizza));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 상품이 포함된 메뉴를 등록할 수 없다.")
    void 메뉴_등록_실패_등록되지_않은_상품() {
        // given
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroupCreateRequest("양식"));
        final Product savedProduct = productService.create(치킨_8000원());
        final Product unsavedProduct = new Product("등록되지 않은 상품", BigDecimal.ONE);

        // when
        final Menu menu = 세트_메뉴_1개씩("치킨_피자_세트", BigDecimal.valueOf(8000), menuGroup,
                List.of(savedProduct, unsavedProduct)
        );

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
