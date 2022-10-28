package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
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
        final MenuGroup menuGroup = menuGroupRepository.save(createMenuGroup("추천메뉴"));
        validMenuGroupId = menuGroup.getId();
        final Product product = productRepository.save(createProduct("후라이드", 16_000L));
        validProductId = product.getId();
    }

    @DisplayName("메뉴를 저장한다.")
    @Test
    void create() {
        // given
        final MenuProductRequest menuProductRequest = createMenuProductRequest(validProductId, 3);
        final MenuRequest menuRequest = createMenuRequest("후라후라후라", 19_000L, validMenuGroupId,
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

    @DisplayName("메뉴의 가격이 0보다 작은 경우 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceNotPositive() {
        // given
        final MenuProductRequest menuProductRequest = createMenuProductRequest(validProductId, 3);
        final MenuRequest menuRequest = createMenuRequest("후라후라후라", -1L, validMenuGroupId, List.of(menuProductRequest));

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
        final MenuProductRequest menuProductRequest = createMenuProductRequest(validProductId, 3);
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
        final MenuRequest menuRequest = createMenuRequest("후라후라", 19_000L, validMenuGroupId,
                List.of(menuProductRequest));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품 총액보다 크면 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceMoreExpensiveThanTotalMenuProduct() {
        // given
        final MenuProductRequest menuProductRequest = createMenuProductRequest(validProductId, 3);
        final MenuRequest menuRequest = createMenuRequest("후라후라후라", 50_000L, validMenuGroupId,
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
        menuRepository.save(createMenu("후라후라후라", 19_000L, validMenuGroupId));

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
