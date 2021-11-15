package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.fixture.MenuFixture.createMenuProductRequest;
import static kitchenpos.menu.fixture.MenuFixture.createMenuRequest;
import static kitchenpos.menu.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.menu.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuService menuService;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    @BeforeEach
    void setUp() {
        menuGroupId = menuGroupRepository.save(createMenuGroup()).getId();
        Product product1 = productRepository.save(createProduct());
        Product product2 = productRepository.save(createProduct());
        menuProducts = Arrays.asList(
                createMenuProductRequest(1L, product1.getId(), 1),
                createMenuProductRequest(2L, product2.getId(), 1)
        );
    }

    @DisplayName("메뉴 생성")
    @Nested
    class CreateMenu {

        @DisplayName("메뉴를 생성한다.")
        @Test
        void create() {
            MenuRequest request = createMenuRequest(menuGroupId, menuProducts);
            MenuResponse result = menuService.create(request);
            assertSoftly(it -> {
                it.assertThat(result.getId()).isNotNull();
                it.assertThat(result.getName()).isEqualTo(request.getName());
                it.assertThat(result.getPrice().compareTo(request.getPrice())).isEqualTo(0);
                it.assertThat(result.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
                it.assertThat(result.getMenuProducts()).hasSize(request.getMenuProductRequests().size());
            });
        }

        @DisplayName("메뉴 가격은 음수일 수 없다.")
        @Test
        void createWithInvalidPrice1() {
            BigDecimal invalidPrice = BigDecimal.valueOf(-1);
            assertThatThrownBy(() -> menuService.create(createMenuRequest(invalidPrice, menuGroupId, menuProducts)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 상품 가격의 합보다 큰 가격으로 메뉴를 생성할 수 없다.")
        @Test
        void createWithInvalidPrice2() {
            BigDecimal invalidPrice = BigDecimal.valueOf(Long.MAX_VALUE);
            assertThatThrownBy(() -> menuService.create(createMenuRequest(invalidPrice, menuGroupId, menuProducts)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 그룹에 메뉴를 생성할 수 없다.")
        @Test
        void createWithInvalidMenuGroup() {
            Long invalidMenuGroupId = Long.MAX_VALUE;
            assertThatThrownBy(() -> menuService.create(createMenuRequest(invalidMenuGroupId, menuProducts)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 목록을 출력한다.")
    @Test
    void list() {
        MenuResponse menu1 = menuService.create(createMenuRequest(menuGroupId, menuProducts));
        MenuResponse menu2 = menuService.create(createMenuRequest(menuGroupId, menuProducts));
        List<MenuResponse> result = menuService.list();
        assertSoftly(it -> {
            it.assertThat(result).hasSize(2);
            it.assertThat(result)
                    .extracting("id", "name")
                    .usingFieldByFieldElementComparator()
                    .contains(
                            tuple(menu1.getId(), menu1.getName()),
                            tuple(menu2.getId(), menu2.getName())
                    );
        });
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        productRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}
