package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.support.DataCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private DataCleaner dataCleaner;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }

    @DisplayName("새로운 메뉴를 생성한다.")
    @Test
    void create_newMenu() {
        // given
        final Product product = productRepository.save(new Product("상품", 10000));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final MenuRequest request = new MenuRequest("메뉴",
            8000,
            menuGroup.getId(),
            List.of(new MenuProductRequest(product.getId(), 1L)));

        // when
        final MenuResponse result = menuService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(1L);
            softly.assertThat(result.getName()).isEqualTo(request.getName());
            softly.assertThat(result.getPrice()).isEqualTo(request.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
        });

        final List<MenuProductResponse> menuProductsResult = result.getMenuProducts();
        assertSoftly(softly -> {
            softly.assertThat(menuProductsResult).hasSize(1);
            softly.assertThat(menuProductsResult.get(0).getMenuId()).isEqualTo(result.getId());
            softly.assertThat(menuProductsResult.get(0).getProductId()).isEqualTo(product.getId());
            softly.assertThat(menuProductsResult.get(0).getQuantity()).isEqualTo(1L);
        });
    }

    @DisplayName("메뉴 그룹에 포함되어 있지 않은 메뉴이면 메뉴를 생성할 수 없다.")
    @Test
    void create_fail_menu_not_contained_menuGroup() {
        // given
        final Long invalidMenuGroupId = 0L;
        final Product product = productRepository.save(new Product("상품", 10000));
        final MenuRequest request = new MenuRequest("메뉴",
            8000,
            invalidMenuGroupId,
            List.of(new MenuProductRequest(product.getId(), 1L)));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품이 존재하지 않는 상품이면 메뉴를 생성할 수 없다.")
    @Test
    void create_fail_menu_contain_notExistProduct() {
        // given
        final Long invalidProductId = 0L;
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final MenuRequest request = new MenuRequest("메뉴",
            8000,
            menuGroup.getId(),
            List.of(new MenuProductRequest(invalidProductId, 1L)));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품들의 가격 총합보다 메뉴 가격이 크면 메뉴를 만들 수 없다.")
    @Test
    void create_fail_menuPrice_expensive_than_all_product_price() {
        // given
        final Product product = productRepository.save(new Product("상품", 10000));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final MenuRequest request = new MenuRequest("메뉴",
            20000,
            menuGroup.getId(),
            List.of(new MenuProductRequest(product.getId(), 1L)));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴 금액은 상품들의 금액 합보다 클 수 없습니다.");
    }

    @DisplayName("전체 메뉴를 조회할 수 있다.")
    @Test
    void find_all_menus() {
        // given
        final Product product1 = productRepository.save(new Product("상품", 10000));
        final MenuProducts menuProducts1 = new MenuProducts(List.of(new MenuProduct(product1, 1)));
        final MenuGroup menuGroup1 = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));
        final Menu menu1 = menuRepository.save(new Menu("메뉴1", 8000, menuGroup1.getId(), menuProducts1));

        final Product product2 = productRepository.save(new Product("상품", 10000));
        final MenuProducts menuProducts2 = new MenuProducts(List.of(new MenuProduct(product2, 1)));
        final MenuGroup menuGroup2 = menuGroupRepository.save(new MenuGroup("메뉴 그룹2"));
        final Menu menu2 = menuRepository.save(new Menu("메뉴1", 8000, menuGroup2.getId(), menuProducts2));

        // when
        final List<MenuResponse> result = menuService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
