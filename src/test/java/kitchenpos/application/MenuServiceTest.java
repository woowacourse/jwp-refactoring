package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupName;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductName;
import kitchenpos.domain.product.ProductPrice;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;

    private Long savedMenuGroupId;
    private List<MenuProductRequest> menuProducts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        final Product product = new Product(new ProductName("상품"), new ProductPrice(BigDecimal.ONE));
        final Product savedProduct = productRepository.save(product);
        final MenuGroup menuGroup = new MenuGroup(new MenuGroupName("메뉴 그룹"));
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        savedMenuGroupId = savedMenuGroup.getId();
        menuProducts.add(new MenuProductRequest(savedProduct.getId(), 2));
    }

    @Nested
    class 메뉴를_등록한다 {
        @Test
        void 메뉴가_정상적으로_등록된다() {
            final MenuCreateRequest menu = new MenuCreateRequest("메뉴", BigDecimal.ONE, savedMenuGroupId, menuProducts);
            final MenuResponse response = menuService.create(menu);

            assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                softly.assertThat(response.getName()).isEqualTo(menu.getName());
                softly.assertThat(response.getPrice()).isEqualByComparingTo(menu.getPrice());
                softly.assertThat(response.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            });
        }

        @Test
        void 메뉴_그룹이_없으면_예외가_발생한다() {
            final MenuCreateRequest menu = new MenuCreateRequest("메뉴", BigDecimal.ONE, null, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
            final MenuCreateRequest menu = new MenuCreateRequest("메뉴", BigDecimal.ONE, savedMenuGroupId + 1, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_상품이_존재하지_않으면_예외가_발생한다() {
            final Product product = productRepository.save(new Product(new ProductName("상품"), new ProductPrice(BigDecimal.ONE)));
            menuProducts.add(new MenuProductRequest(product.getId() + 1, 1));

            final MenuCreateRequest menu = new MenuCreateRequest("메뉴", BigDecimal.ZERO, savedMenuGroupId, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴의_목록을_조회한다() {
        final List<MenuResponse> expected = menuService.list();
        for (int i = 0; i < 3; i++) {
            final MenuCreateRequest menu = new MenuCreateRequest("메뉴" + i, BigDecimal.ONE, savedMenuGroupId, menuProducts);
            expected.add(menuService.create(menu));
        }

        final List<MenuResponse> result = menuService.list();

        assertThat(result).hasSize(expected.size());
    }
}
