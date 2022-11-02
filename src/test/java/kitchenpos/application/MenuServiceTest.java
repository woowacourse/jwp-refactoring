package kitchenpos.application;

import static kitchenpos.support.DataFixture.createMenu;
import static kitchenpos.support.DataFixture.createMenuGroup;
import static kitchenpos.support.DataFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup savedMenuGroup;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
        savedMenuGroup = menuGroupRepository.save(createMenuGroup("추천메뉴"));
        savedProduct = productRepository.save(createProduct("후라이드", 16_000L));
    }

    @DisplayName("메뉴를 저장한다.")
    @Test
    void create() {
        // given
        final MenuProductRequest menuProductRequest = createMenuProductRequest(savedProduct.getId(), 3);
        final MenuRequest menuRequest = createMenuRequest("후라후라후라", 19_000L, savedMenuGroup.getId(),
                List.of(menuProductRequest));

        // when
        final MenuResponse response = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(response).usingRecursiveComparison()
                        .ignoringFields("id", "price", "menuProducts")
                        .isEqualTo(menuRequest),
                () -> assertThat(response.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴 상품 리스트가 없는 경우 예외를 반환한다.")
    @Test
    void create_throwException_ifMenuProductsNull() {
        // given
        final MenuRequest menuRequest = createMenuRequest("후라후라후라", 19_000L, savedMenuGroup.getId(), List.of());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 상품들은 null일 수 없습니다.");
    }

    @DisplayName("메뉴의 가격이 0보다 작은 경우 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceNotPositive() {
        // given
        final MenuProductRequest menuProductRequest = createMenuProductRequest(savedProduct.getId(), 3);
        final MenuRequest menuRequest = createMenuRequest("후라후라후라", -1L, savedMenuGroup.getId(),
                List.of(menuProductRequest));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 음수일 수 없습니다.");
    }

    @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외를 반환한다. ")
    @Test
    void create_throwException_ifMenuGroupNotExist() {
        // given
        final Long noExistMenuGroupId = 900L;
        final MenuProductRequest menuProductRequest = createMenuProductRequest(savedProduct.getId(), 3);
        final MenuRequest menuRequest = createMenuRequest("후라후라후라", 19_000L, noExistMenuGroupId,
                List.of(menuProductRequest));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포함된 메뉴 그룹이 있어야 합니다.");
    }

    @DisplayName("메뉴 상품이 존재하지 않을 경우 예외를 반환한다.")
    @Test
    void create_throwException_ifProductNotExist() {
        // given
        final Long noExistProductId = 900L;
        final MenuProductRequest menuProductRequest = createMenuProductRequest(noExistProductId, 3);
        final MenuRequest menuRequest = createMenuRequest("후라후라", 19_000L, savedMenuGroup.getId(),
                List.of(menuProductRequest));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품 총액보다 크면 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceMoreExpensiveThanTotalMenuProduct() {
        // given
        final MenuProductRequest menuProductRequest = createMenuProductRequest(savedProduct.getId(), 3);
        final MenuRequest menuRequest = createMenuRequest("후라후라후라", 50_000L, savedMenuGroup.getId(),
                List.of(menuProductRequest));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품들의 총액보다 비쌀 수 없습니다.");
    }

    @DisplayName("메뉴 전체를 조회한다.")
    @Test
    void findAll() {
        // given
        menuRepository.save(createMenu("후라후라후라", 19_000L, savedMenuGroup, List.of(savedProduct)));

        // when, then
        assertThat(menuService.findAll()).hasSize(1);

    }

    private MenuRequest createMenuRequest(final String name, final Long price, final Long menuGroupId,
                                          final List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, new BigDecimal(price), menuGroupId, menuProductRequests);
    }

    private MenuProductRequest createMenuProductRequest(final Long productId, final long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
