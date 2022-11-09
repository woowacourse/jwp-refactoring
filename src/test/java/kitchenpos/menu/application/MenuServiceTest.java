package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.common.ServiceTest;
import kitchenpos.menu.application.request.MenuGroupRequest;
import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.product.application.request.ProductCreateRequest;

public class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        MenuRequest request = createMenuRequest(48000, 3, product);

        // when
        MenuResponse savedMenu = menuService.create(request);

        // then
        assertMenuWithRequest(request, savedMenu);
    }

    @Test
    @DisplayName("가격이 음수이면 예외를 던진다.")
    void create_price_negative() {
        // given
        MenuRequest request = createMenuRequest(-1);

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {48000, 47999})
    @DisplayName("가격은 상품가격의 합보다 작거나 같아야 한다.")
    void validatePriceUnderProductsSum(int priceValue) {
        // given
        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        MenuRequest request = createMenuRequest(priceValue, 3, product);

        // when, then
        Assertions.assertDoesNotThrow(
            () -> menuService.create(request));
    }

    @Test
    @DisplayName("가격이 상품 가격의 합보다 크면 예외를 던진다.")
    void validatePriceUnderProductsSum_exception() {
        // given
        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        MenuRequest request = createMenuRequest(48001, 3, product);

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("price must be equal to or less than the sum of product prices");
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        MenuResponse savedMenu = menuService.create(createMenuRequest());

        // when
        List<MenuResponse> result = menuService.list();

        // then
        assertMenuResponse(savedMenu, result.get(0));
    }

    private MenuRequest createMenuRequest() {
        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        return createMenuRequest(48000, 3, product);
    }

    private MenuRequest createMenuRequest(final int priceValue) {
        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        return createMenuRequest(priceValue, 3, product);
    }

    private MenuRequest createMenuRequest(final int priceValue, final int quantity,
        final ProductCreateRequest productCreateRequest) {
        MenuGroupRequest menuGroup = new MenuGroupRequest(NO_ID, "세마리메뉴");
        Long menuGroupId = menuGroupService.create(menuGroup).getId();

        Long productId = productService.create(productCreateRequest).getId();

        return new MenuRequest(NO_ID, "후라이드+후라이드+후라이드", new BigDecimal(priceValue), menuGroupId,
            List.of(new MenuProductRequest(NO_ID, NO_ID, productId, quantity)));
    }

    private void assertMenuWithRequest(final MenuRequest request, final MenuResponse response) {
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getPrice().compareTo(request.getPrice())).isEqualTo(0);
    }

    private void assertMenuResponse(final MenuResponse actual, final MenuResponse expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
        assertThat(actual.getPrice().compareTo(expected.getPrice())).isEqualTo(0);
    }
}
