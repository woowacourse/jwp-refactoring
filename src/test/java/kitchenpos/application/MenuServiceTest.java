package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuServiceTest extends ServiceTest {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuService menuService;

    static Stream<Arguments> invalidPrices() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(BigDecimal.valueOf(-1000))
        );
    }

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
    }

    @DisplayName("새로운 메뉴 저장")
    @Test
    void createTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Product savedProduct = saveProduct(productRepository, "후라이드치킨", BigDecimal.valueOf(16_000));
        MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 2L);
        MenuRequest menuRequest = new MenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19_000),
                savedMenuGroup.getId(),
                Collections.singletonList(menuProductRequest)
        );

        MenuResponse menuResponse = menuService.create(menuRequest);

        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(savedMenuGroup.getId()),
                () -> assertThat(menuResponse.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(menuRequest.getPrice())
        );
    }

    @DisplayName("새로운 메뉴 저장 시 가격을 잘못 입력했을 때 예외 출력")
    @ParameterizedTest
    @MethodSource("invalidPrices")
    void createWithInvalidPriceTest(BigDecimal price) {
        MenuRequest menuRequest = new MenuRequest(price);

        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴 저장 시 존재하지 않는 메뉴 그룹 아이디 입력 시 예외 출력")
    @Test
    void createWithInvalidMenuGroupIdTest() {
        MenuRequest menuRequest = new MenuRequest(BigDecimal.valueOf(16_000), 0L);

        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴 저장 시 잘못된 메뉴 상품 입력 시 예외 출력")
    @Test
    void createMenuWithInvalidMenuProductTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        MenuProductRequest menuProductRequest = new MenuProductRequest(0L, 2L);
        MenuRequest menuRequest = new MenuRequest("잘못된 메뉴", BigDecimal.valueOf(16_000),
                savedMenuGroup.getId(), Collections.singletonList(menuProductRequest));

        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴 저장 시 입력한 메뉴의 가격이 메뉴 상품 가격의 총 합보다 더 클 때 예외 출력")
    @Test
    void createMenuWithInvalidMenuProductPriceSumTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Product savedFriedProduct = saveProduct(productRepository, "후라이드치킨", BigDecimal.valueOf(16_000));
        MenuProductRequest menuProductRequest = new MenuProductRequest(savedFriedProduct.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("잘못된 메뉴", BigDecimal.valueOf(20_000),
                savedMenuGroup.getId(), Collections.singletonList(menuProductRequest));

        assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장되어있는 모든 메뉴 조회")
    @Test
    void listTest() {
        MenuGroup savedMenuGroup = saveMenuGroup(menuGroupRepository, "한마리메뉴");
        Product savedFriedProduct = saveProduct(productRepository, "후라이드치킨", BigDecimal.valueOf(16_000));
        Menu savedFriedMenu = saveMenu(menuRepository, savedMenuGroup.getId(), "후라이드치킨", BigDecimal.valueOf(16_000));
        saveMenuProduct(menuProductRepository, savedFriedMenu.getId(), savedFriedProduct.getId(), 1L);

        List<MenuResponse> menuResponses = menuService.list();

        assertAll(
                () -> assertThat(menuResponses).hasSize(1),
                () -> assertThat(menuResponses.get(0).getMenuProducts()).hasSize(1)
        );
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        productRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}