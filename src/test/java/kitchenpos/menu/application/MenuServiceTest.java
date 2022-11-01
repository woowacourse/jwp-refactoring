package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.ui.request.MenuGroupRequest;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.product.ui.request.ProductCreateRequest;

public class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        MenuRequest request = createMenuRequest();

        // when
        Menu savedMenu = menuService.create(request);

        // then
        assertMenu(request, savedMenu);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        Menu savedMenu = menuService.create(createMenuRequest());

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).contains(savedMenu);
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

    private void assertMenu(final MenuRequest request, final Menu savedMenu) {
        assertThat(savedMenu.getName()).isEqualTo(request.getName());
        assertThat(savedMenu.getPrice()).isCloseTo(request.getPrice(), Percentage.withPercentage(0.0001));
        assertThat(savedMenu.getMenuProducts()).isNotNull();
    }
}
