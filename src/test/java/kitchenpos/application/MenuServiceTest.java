package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuFixture.getMenu;
import static kitchenpos.support.fixture.domain.MenuGroupFixture.getMenuGroup;
import static kitchenpos.support.fixture.domain.MenuProductFixture.getMenuProduct;
import static kitchenpos.support.fixture.domain.ProductFixture.getProduct;
import static kitchenpos.support.fixture.dto.MenuCreateRequestFixture.menuCreateRequest;
import static kitchenpos.support.fixture.dto.MenuProductRequestFixture.menuProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuProductResponse;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

    private static final List<MenuProductRequest> EMPTY_MENU_PRODUCTS = Collections.emptyList();

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class 메뉴를_등록시 {

        @Test
        void 가격이_0보다_작으면_예외를_던진다() {
            //given
            final MenuCreateRequest request = menuCreateRequest("menu", -1L, 1L, EMPTY_MENU_PRODUCTS);

            //when
            //then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹ID에_해당하는_메뉴그룹이_존재하지_않으면_예외를_던진다() {
            //given
            final MenuCreateRequest request = menuCreateRequest("menu", -1L, 1L, EMPTY_MENU_PRODUCTS);

            //when
            //then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 없는_메뉴_상품이_메뉴에_있으면_예외를_던진다() {
            //given
            final MenuGroup menuGroup = menuGroupRepository.save(getMenuGroup("menuGroup"));
            final MenuCreateRequest request = menuCreateRequest("menu", -1L, menuGroup.getId(), EMPTY_MENU_PRODUCTS);

            //when
            //then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_메뉴_상품들의_총금액보다_크면_예외를_던진다() {
            //given
            final MenuGroup menuGroup = menuGroupRepository.save(getMenuGroup("menuGroup"));
            final Product product = productRepository.save(getProduct("product", 100L));
            final MenuProductRequest menuProductRequest = menuProductRequest(product.getId(), 2L);
            final MenuCreateRequest request =
                    menuCreateRequest("menu", -1L, menuGroup.getId(), List.of(menuProductRequest));

            //when
            //then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공한다() {
            //given
            final MenuGroup menuGroup = menuGroupRepository.save(getMenuGroup("menuGroup"));
            final Product product = productRepository.save(getProduct("prodcut", 100L));
            final MenuProductRequest menuProductRequest = menuProductRequest(product.getId(), 2L);
            final MenuCreateRequest request =
                    menuCreateRequest("menu", 190L, menuGroup.getId(), List.of(menuProductRequest));

            //when
            final MenuResponse response = menuService.create(request);

            //then
            final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(response.getId());
            final List<MenuProductResponse> expected = MenuProductResponse.listOf(menuProducts);

            assertAll(
                    () -> assertThat(menuRepository.findById(response.getId())).isPresent(),
                    () -> assertThat(expected)
                            .usingRecursiveComparison()
                            .isEqualTo(response.getMenuProducts())
            );
        }
    }

    @Test
    void 모든_메뉴를_조회한다() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(getMenuGroup("menuGroup"));

        final Menu menu1 = menuRepository.save(getMenu("menu1", 90L, menuGroup.getId()));
        final Menu menu2 = menuRepository.save(getMenu("menu2", 80L, menuGroup.getId()));

        final Product product = productRepository.save(getProduct("product", 100L));

        final MenuProduct menuProduct1 = menuProductRepository.save(getMenuProduct(menu1.getId(), product.getId(), 1L));
        final MenuProduct menuProduct2 = menuProductRepository.save(getMenuProduct(menu2.getId(), product.getId(), 1L));

        menu1.addMenuProducts(List.of(menuProduct1));
        menu2.addMenuProducts(List.of(menuProduct2));

        // when
        final List<MenuResponse> allMenu = menuService.list();

        // then
        assertThat(allMenu)
                .usingRecursiveComparison()
                .isEqualTo(MenuResponse.listOf(List.of(menu1, menu2)));
    }
}
