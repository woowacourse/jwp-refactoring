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
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;

    @Test
    @DisplayName("메뉴 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 메뉴_등록_성공_저장() {
        // given
        final Product chicken = productRepository.save(치킨_8000원());
        final Product pizza = productRepository.save(피자_8000원());
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("양식"));

        // when
        final Menu menu = menuService.create(
                세트_메뉴_1개씩("치킨_피자_세트", BigDecimal.valueOf(8000), menuGroup, List.of(chicken, pizza))
        );

        // then
        assertThat(menuService.list())
                .map(Menu::getId)
                .filteredOn(id -> Objects.equals(id, menu.getId()))
                .hasSize(1);
        assertThat(menuProductRepository.findAll())
                .map(MenuProduct::getMenu)
                .map(Menu::getId)
                .filteredOn(id -> Objects.equals(id, menu.getId()))
                .hasSize(2);
    }

    @Test
    @DisplayName("등록되지 않은 메뉴 그룹으로 메뉴를 등록할 수 없다.")
    void 메뉴_등록_실패_등록되지_않은_메뉴_그룹() {
        // given
        final Product chicken = productRepository.save(치킨_8000원());
        final Product pizza = productRepository.save(피자_8000원());
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
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("양식"));
        final Product savedProduct = productRepository.save(치킨_8000원());
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
