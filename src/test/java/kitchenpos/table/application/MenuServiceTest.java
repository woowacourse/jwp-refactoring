package kitchenpos.table.application;

import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuGroupRepository;
import kitchenpos.menu.MenuProductRepository;
import kitchenpos.menu.MenuRepository;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.MenuProductRequest;
import kitchenpos.menu.ui.MenuRequest;
import kitchenpos.menu.ui.MenuResponse;
import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    private MenuGroup menuGroup;
    private Product product;
    private MenuProductRequest menuProductRequest;

    @BeforeEach
    void setUp() {
        this.menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));
        this.product = productRepository.save(new Product("상품1", new BigDecimal(10000)));
        this.menuProductRequest = new MenuProductRequest(product.getId(), 4L);
    }

    @Nested
    class 몌뉴등록 {
        @Test
        void 메뉴를_등록한다() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));
            Product product = productRepository.save(new Product("productName", BigDecimal.valueOf(10000L)));
            MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 3L);

            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal("30000.00"),
                    menuGroup.getId(), List.of(menuProductRequest));
            MenuResponse savedMenu = menuService.create(menuRequest);

            assertSoftly(softly -> {
                softly.assertThat(savedMenu.getId()).isNotNull();
                softly.assertThat(savedMenu.getPrice()).isEqualByComparingTo(menuRequest.getPrice());
                softly.assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "menuProducts")
                        .isEqualTo(menuRequest);
            });
        }

        @Test
        void 메뉴_가격이_0원_미만이면_등록할_수_없다() {
            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal(-1),
                    menuGroup.getId(), List.of(menuProductRequest));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격은 0 이상이어야 합니다.");
        }

        @Test
        void 포함될_메뉴_그룹이_존재하지_않으면_등록할_수_없다() {
            long notExistMenuGroupId = 100000L;
            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal("30000.00"),
                    notExistMenuGroupId, List.of(menuProductRequest));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴그룹입니다. 메뉴를 등록할 수 없습니다.");
        }

        @Test
        void 메뉴_상품이_존재하지_않으면_등록할_수_없다() {
            long notExistMenuId = Long.MIN_VALUE;
            MenuProductRequest notExistMenuProduct = new MenuProductRequest(notExistMenuId, 2L);
            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal("30000.00"),
                    menuGroup.getId(), List.of(notExistMenuProduct));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴상품입니다. 메뉴를 등록할 수 없습니다.");
        }

        @Test
        void 메뉴의_가격이_메뉴_상품들의_가격_합보다_비싸면_등록할_수_없다() {
            Product product1 = productRepository.save(new Product("상품2", new BigDecimal(9999)));
            MenuProductRequest menuProductRequest2 = new MenuProductRequest(product1.getId(), 1L);
            MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal("50000.00"), menuGroup.getId(),
                    List.of(menuProductRequest, menuProductRequest2));

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격은 포함한 메뉴 상품들의 가격 합보다 비쌀 수 없습니다.");
        }
    }

    @Test
    void 메뉴의_목록을_조회할_수_있다() {
        MenuRequest menuRequest = new MenuRequest("메뉴", new BigDecimal("1000.00"),
                menuGroup.getId(), List.of(menuProductRequest));
        menuService.create(menuRequest);

        List<MenuResponse> menuList = menuService.list();

        assertThat(menuList).hasSize(1)
                .extracting("name")
                .containsOnly("메뉴");
    }
}
