package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.support.DataCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }

    @DisplayName("새로운 메뉴를 생성한다.")
    @Test
    void create_newMenu() {
        // given
        final Product product = productRepository.save(Product.forSave("상품", 10000));
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.forSave("메뉴 그룹"));
        final MenuRequest request = new MenuRequest("메뉴",
            8000,
            menuGroup.getId(),
            List.of(new MenuProductDto(null, null, product.getId(), 1L)));

        // when
        final MenuResponse result = menuService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(1L);
            softly.assertThat(result.getName()).isEqualTo(request.getName());
            softly.assertThat(result.getPrice()).isEqualTo(request.getPrice());
            softly.assertThat(result.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
        });

        final List<MenuProductDto> menuProductsResult = result.getMenuProducts();
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
        final Product product = productRepository.save(Product.forSave("상품", 10000));
        final MenuRequest request = new MenuRequest("메뉴",
            8000,
            invalidMenuGroupId,
            List.of(new MenuProductDto(null, null, product.getId(), 1L)));

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
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.forSave("메뉴 그룹"));
        final MenuRequest request = new MenuRequest("메뉴",
            8000,
            menuGroup.getId(),
            List.of(new MenuProductDto(null, null, invalidProductId, 1L)));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품들의 가격 총합보다 메뉴 가격이 크면 메뉴를 만들 수 없다.")
    @Test
    void create_fail_menuPrice_expensive_than_all_product_price() {
        // given
        final Product product = productRepository.save(Product.forSave("상품", 10000));
        final MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.forSave("메뉴 그룹"));
        final MenuRequest request = new MenuRequest("메뉴",
            20000,
            menuGroup.getId(),
            List.of(new MenuProductDto(null, null, product.getId(), 1L)));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴를 조회할 수 있다.")
    @Test
    void find_all_menus() {
        // given
        final Product product1 = productRepository.save(Product.forSave("상품1", 10000));
        final MenuGroup menuGroup1 = menuGroupRepository.save(MenuGroup.forSave("메뉴 그룹1"));
        final MenuRequest request1 = new MenuRequest("메뉴1",
            8000,
            menuGroup1.getId(),
            List.of(new MenuProductDto(null, null, product1.getId(), 1L)));

        final Product product2 = productRepository.save(Product.forSave("상품2", 10000));
        final MenuGroup menuGroup2 = menuGroupRepository.save(MenuGroup.forSave("메뉴 그룹2"));
        final MenuRequest request2 = new MenuRequest("메뉴2",
            8000,
            menuGroup2.getId(),
            List.of(new MenuProductDto(null, null, product2.getId(), 1L)));

        final MenuResponse menuResponse1 = menuService.create(request1);
        final MenuResponse menuResponse2 = menuService.create(request2);

        // when
        final List<MenuResponse> result = menuService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
