package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.createMenuProductRequest;
import static kitchenpos.fixture.MenuFixture.createMenuRequest;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
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
            assertAll(
                    () -> assertThat(result.getId()).isNotNull(),
                    () -> assertThat(result.getName()).isEqualTo(request.getName()),
                    () -> assertThat(result.getPrice().compareTo(request.getPrice())).isEqualTo(0),
                    () -> assertThat(result.getMenuGroupId()).isEqualTo(request.getMenuGroupId()),
                    () -> assertThat(result.getMenuProducts()).hasSize(request.getMenuProductRequests().size())
            );
        }

        @DisplayName("메뉴 가격은 음수일 수 없다.")
        @Test
        void createWithInvalidPrice1() {
            BigDecimal price = BigDecimal.valueOf(-1);
            MenuRequest menu = createMenuRequest(price, menuGroupId, menuProducts);
            assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
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
            MenuRequest menu = createMenuRequest(invalidMenuGroupId, menuProducts);
            assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 목록을 출력한다.")
    @Test
    void list() {
        MenuResponse menu1 = menuService.create(createMenuRequest(menuGroupId, menuProducts));
        MenuResponse menu2 = menuService.create(createMenuRequest(menuGroupId, menuProducts));
        List<MenuResponse> result = menuService.list();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(result)
                .as("크기가 다름")
                .hasSize(2);
        softAssertions.assertThat(result)
                .extracting("name")
                .usingFieldByFieldElementComparator()
                .isEqualTo(Arrays.asList(menu1.getName(), menu2.getName()));
        softAssertions.assertAll();

        SoftAssertions.assertSoftly(it -> {
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
        List<Menu> menus = menuRepository.findAll();
        for (Menu menu : menus) {
            menu.setMenuProducts(null);
        }
        menuRepository.saveAll(menus);
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        productRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}
