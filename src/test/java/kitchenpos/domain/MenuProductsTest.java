package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dto.menu.MenuProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    @DisplayName("from")
    void from() {
        Menu menu = new Menu(1L, "치킨세트", new Price(BigDecimal.valueOf(1_900)), 1L);
        List<MenuProductRequest> menuProductRequestList = new ArrayList<>();

        menuProductRequestList.add(new MenuProductRequest(1L, 2));
        menuProductRequestList.add(new MenuProductRequest(2L, 1));
        menuProductRequestList.add(new MenuProductRequest(3L, 5));

        assertThat(MenuProducts.from(menuProductRequestList, menu))
            .isInstanceOf(MenuProducts.class);
    }

    @Test
    @DisplayName("from - menu id가 전달되지 않았을 경우 예외처리")
    void from_IfMenuIdIsEmpty_ThrowException() {
        Menu menu = new Menu("치킨세트", new Price(BigDecimal.valueOf(1_900)), 1L);
        List<MenuProductRequest> menuProductRequestList = new ArrayList<>();

        menuProductRequestList.add(new MenuProductRequest(1L, 2));
        menuProductRequestList.add(new MenuProductRequest(2L, 1));
        menuProductRequestList.add(new MenuProductRequest(3L, 5));

        assertThatThrownBy(() -> MenuProducts.from(menuProductRequestList, menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("from - menuProductRequests 중 잘못된 productId 또는 quantity 가 있는 경우 예외처리")
    void from_IfMenuProductsIsWrong_ThrowException() {
        Menu menu = new Menu("치킨세트", new Price(BigDecimal.valueOf(1_900)), 1L);
        List<MenuProductRequest> menuProductRequestList = new ArrayList<>();

        menuProductRequestList.add(new MenuProductRequest(null, 1));
        menuProductRequestList.add(new MenuProductRequest(1L, -1));
        menuProductRequestList.add(new MenuProductRequest(3L, 0));

        assertThatThrownBy(() -> MenuProducts.from(menuProductRequestList, menu))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
