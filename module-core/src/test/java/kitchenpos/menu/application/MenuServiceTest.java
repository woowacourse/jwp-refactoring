package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.menu.MenuProductRequest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.repository.ProductRepository;
import kitchenpos.support.application.ServiceTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static kitchenpos.support.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class MenuServiceTest {

    private final MenuService menuService;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    @Autowired
    public MenuServiceTest(final MenuService menuService,
                           final MenuRepository menuRepository,
                           final MenuGroupRepository menuGroupRepository,
                           final ProductRepository productRepository
    ) {
        this.menuService = menuService;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @ServiceTest
    class CreateTest {

        private MenuGroup savedMenuGroup;
        private Product savedProduct;

        @BeforeEach
        void setUp() {
            this.savedMenuGroup = menuGroupRepository.save(new MenuGroup("중식"));
            this.savedProduct = productRepository.save(makeProduct("자장면", 5000L));
        }

        @DisplayName("메뉴를 추가한다")
        @Test
        void create() {
            final var menuProductRequest = new MenuProductRequest(savedProduct.getId(), 10);
            final var request = makeMenuRequest(savedProduct.getPrice(), savedMenuGroup.getId(), menuProductRequest);
            final var actual = menuService.create(request);

            assertThat(actual.getId()).isPositive();
        }

        @DisplayName("메뉴 가격이 양수여야 한다")
        @Test
        void createWithNegativePrice() {
            final var negativePrice = -1;

            final var request = makeMenuRequest(negativePrice, savedMenuGroup.getId());

            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 음수가 아니어야 합니다.");
        }

        @DisplayName("메뉴 가격이 상품 전체 금액보다 작거나 같아야 한다")
        @ParameterizedTest
        @CsvSource(value = {"1000,999"})
        void createWithBiggerPriceThenSum(final long menuPrice, final long productPrice) {
            final var product = productRepository.save(makeProduct("상품", productPrice));

            final var menuProductRequest = new MenuProductRequest(product.getId(), 1);
            final var request = makeMenuRequest(menuPrice, savedMenuGroup.getId(), menuProductRequest);

            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격은 상품 금액 합산보다 클 수 없습니다.");
        }

        @DisplayName("존재하는 메뉴 그룹이어야 한다")
        @Test
        void createWithNonExistMenuGroup() {
            final var nonExistMenuGroupId = 0L;

            final var menuProductRequest = new MenuProductRequest(savedProduct.getId(), 1);
            final var request = makeMenuRequest(10, nonExistMenuGroupId, menuProductRequest);

            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 그룹을 찾을 수 없습니다.");
        }

        @DisplayName("존재하는 상품이어야 한다")
        @Test
        void createWithNonExistProduct() {
            final var nonExistProductId = 0L;

            final var menuProductRequest = new MenuProductRequest(nonExistProductId, 1);
            final var request = makeMenuRequest(10, savedMenuGroup.getId(), menuProductRequest);

            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품을 찾을 수 없습니다.");
        }
    }

    @DisplayName("메뉴를 전체 조회한다")
    @Test
    void list() {
        final var expectedSize = 4;
        saveMenuForTimes(expectedSize);

        final var actual = menuService.list();
        assertThat(actual).hasSize(expectedSize);
    }

    private void saveMenuForTimes(final int times) {
        for (int i = 0; i < times; i++) {
            final var menu = makeMenu("메뉴", 23000L, 1L, new ArrayList<>());
            menuRepository.save(menu);
        }
    }
}
