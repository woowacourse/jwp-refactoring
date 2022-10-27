package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    private Long validMenuGroupId;
    private Long validProductId;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
        final MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        validMenuGroupId = menuGroup.getId();
        final Product product = productDao.save(createProduct("후라이드", 16_000L));
        validProductId = product.getId();
    }

    @DisplayName("메뉴를 저장한다.")
    @Test
    void create() {
        // given
        final MenuProduct menuProductRequest = createMenuProductRequest(validProductId, 3);
        final Menu menuRequest = createMenuRequest("후라후라후라", 19_000L, validMenuGroupId, List.of(menuProductRequest));

        // when
        final Menu savedMenu = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "price", "menuProducts")
                        .isEqualTo(menuRequest),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴의 가격이 0보다 작은 경우 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceNotPositive() {
        // given
        final MenuProduct menuProductRequest = createMenuProductRequest(validProductId, 3);
        final Menu menuRequest = createMenuRequest("후라후라후라", -1L, validMenuGroupId, List.of(menuProductRequest));

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
        final MenuProduct menuProductRequest = createMenuProductRequest(validProductId, 3);
        final Menu menuRequest = createMenuRequest("후라후라후라", 19_000L, noExistMenuGroupId, List.of(menuProductRequest));

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
        final MenuProduct menuProductRequest = createMenuProductRequest(noExistProductId, 3);
        final Menu menuRequest = createMenuRequest("후라후라", 19_000L, validMenuGroupId, List.of(menuProductRequest));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품 총액보다 크면 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceMoreExpensiveThanTotalMenuProduct() {
        // given
        final MenuProduct menuProductRequest = createMenuProductRequest(validProductId, 3);
        final Menu menuRequest = createMenuRequest("후라후라후라", 50_000L, validMenuGroupId, List.of(menuProductRequest));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품들의 총액보다 비쌀 수 없습니다.");
    }

    @DisplayName("메뉴 전체를 조회한다.")
    @Test
    void findAll() {
        // given
        menuDao.save(createMenu("후라후라후라", 19_000L, validMenuGroupId));

        // when, then
        assertThat(menuService.findAll()).hasSize(1);
    }

    private Menu createMenuRequest(final String name, final Long price, final Long menuGroupId,
                                   final List<MenuProduct> menuProducts) {
        final Menu menuRequest = new Menu();
        menuRequest.setName(name);
        menuRequest.setPrice(new BigDecimal(price));
        menuRequest.setMenuGroupId(menuGroupId);
        menuRequest.setMenuProducts(menuProducts);
        return menuRequest;
    }

    private MenuProduct createMenuProductRequest(final Long productId, final long quantity) {
        final MenuProduct menuProductRequest = new MenuProduct();
        menuProductRequest.setProductId(productId);
        menuProductRequest.setQuantity(quantity);
        return menuProductRequest;
    }
}
