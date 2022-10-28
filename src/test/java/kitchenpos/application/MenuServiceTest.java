package kitchenpos.application;

import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.domain.Menu;
import kitchenpos.exception.MenuPriceException;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.ui.dto.MenuProductDto;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {

    @Test
    void 메뉴를_생성한다() {
        MenuCreateRequest menuCreateRequest
                = new MenuCreateRequest("", BigDecimal.valueOf(0), 1L, Collections.emptyList());

        Menu savedMenu = menuService.create(menuCreateRequest);

        assertThat(menuDao.findById(savedMenu.getId())).isPresent();
    }

    @Test
    void 메뉴를_생성할때_price_예외를_발생한다() {
        MenuCreateRequest menuCreateRequest
                = new MenuCreateRequest("", BigDecimal.valueOf(-1), 1L, Collections.emptyList());

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(MenuPriceException.class);
    }

    @Test
    void 메뉴를_생성할때_메뉴그룹아이디_예외를_발생한다() {
        MenuCreateRequest menuCreateRequest
                = new MenuCreateRequest("", BigDecimal.valueOf(0), 0L, Collections.emptyList());

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(NotFoundMenuGroupException.class);
    }

    @Test
    void 메뉴를_생성할때_product총가격보다_menu가격이높으면_예외를_발생한다() {
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("", BigDecimal.valueOf(MAX_VALUE), 1L,
                Collections.singletonList(new MenuProductDto(1L, 1)));

        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(MenuPriceException.class);
    }

    @Test
    void 메뉴_리스트를_반환한다() {
        Menu menu = new Menu("", BigDecimal.valueOf(0), 1L, Collections.emptyList());

        int beforeSize = menuService.list().size();
        menuDao.save(menu);

        assertThat(menuService.list().size()).isEqualTo(beforeSize + 1);
    }
}
