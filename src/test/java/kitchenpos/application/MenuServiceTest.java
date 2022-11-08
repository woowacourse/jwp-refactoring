package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.application.request.MenuRequest;
import kitchenpos.application.request.MenuRequest.MenuProductRequest;
import kitchenpos.application.response.MenuResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    void create() {
        MenuProductRequest menuProduct = new MenuProductRequest(1L, 10);
        MenuRequest request = new MenuRequest( "name", BigDecimal.valueOf(1000), 1L, List.of(menuProduct));

        MenuResponse response = menuService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    void createThrowExceptionWhenNotExistProduct() {
        MenuProductRequest menuProduct = new MenuProductRequest(0L, 10);
        MenuRequest request = new MenuRequest("name", BigDecimal.valueOf(100), 1L, List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 product입니다.");
    }

    @Test
    void createThrowExceptionWhenNotExistMenuId() {
        MenuProductRequest menuProduct = new MenuProductRequest( 1L, 10);
        MenuRequest request = new MenuRequest("name", BigDecimal.valueOf(100), 0L, List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("menuGroup이 존재하지 않습니다.");
    }

    @Test
    void list() {
        List<MenuResponse> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(6);
    }
}
