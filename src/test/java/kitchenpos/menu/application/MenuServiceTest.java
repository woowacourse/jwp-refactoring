package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        MenuRequest request = createMenuRequest();

        // when
        MenuResponse savedMenu = menuService.create(request);

        // then
        assertMenuWithRequest(request, savedMenu);
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
        return createMenuRequest(48000);
    }

    private MenuRequest createMenuRequest(int priceValue) {
        MenuGroupRequest menuGroup = new MenuGroupRequest(NO_ID, "세마리메뉴");
        Long menuGroupId = menuGroupService.create(menuGroup).getId();

        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        Long productId = productService.create(product).getId();

        return new MenuRequest(NO_ID, "후라이드+후라이드+후라이드", new BigDecimal(priceValue), menuGroupId,
            List.of(new MenuProductRequest(NO_ID, NO_ID, productId, 3)));
    }

    private void assertMenuWithRequest(final MenuRequest request, final MenuResponse response) {
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getPrice().compareTo(request.getPrice())).isEqualTo(0);
        assertThat(response.getMenuProducts()).isNotNull();
    }

    private void assertMenuResponse(final MenuResponse actual, final MenuResponse expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
        assertThat(actual.getPrice().compareTo(expected.getPrice())).isEqualTo(0);
    }
}
