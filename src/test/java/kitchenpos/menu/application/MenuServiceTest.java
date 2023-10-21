package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;

    private Product savedProduct;
    private MenuGroup savedMenuGroup;

    @BeforeEach
    void setUp() {
        savedProduct = productRepository.save(new Product("후라이드", new Price(BigDecimal.valueOf(2000))));
        savedMenuGroup = menuGroupRepository.save(new MenuGroup("치킨"));
    }

    @Nested
    class 메뉴를_등록할_때 {

        @Test
        void success() {
            // given
            MenuRequest menuRequest = createMenuRequest("1000", savedMenuGroup, savedProduct, 1L);

            // when
            MenuResponse result = menuService.create(menuRequest);

            // then
            assertThat(result.getName()).isEqualTo(menuRequest.getName());
            assertThat(result.getPrice()).isEqualTo(menuRequest.getPrice());
        }

        @Test
        void 가격_정보가_없거나_0보다_작은_경우_실패() {
            // given
            MenuRequest menuRequest = createMenuRequest("-10", savedMenuGroup, savedProduct, 1L);

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 등록되지_않은_메뉴그룹에_속할_시_실패() {
            MenuGroup unsavedMenuGroup = new MenuGroup("치킨");
            MenuRequest menuRequest = createMenuRequest("1000", unsavedMenuGroup, savedProduct, 1L);

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_메뉴_내부의_상품의_총_합계_가격보다_크면_실패() {
            MenuRequest menuRequest = createMenuRequest("3000", savedMenuGroup, savedProduct, 1L);

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴_목록_조회() {
        MenuRequest menuRequest = createMenuRequest("1000.00", savedMenuGroup, savedProduct, 1L);
        MenuResponse savedMenu = menuService.create(menuRequest);

        List<MenuResponse> actual = menuService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(List.of(savedMenu));
    }

    private MenuRequest createMenuRequest(String price, MenuGroup menuGroup, Product product, long quantity) {
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), quantity);

        return new MenuRequest(
                "후라이드 치킨",
                new BigDecimal(price),
                menuGroup.getId(),
                List.of(menuProductRequest)
        );
    }

}
