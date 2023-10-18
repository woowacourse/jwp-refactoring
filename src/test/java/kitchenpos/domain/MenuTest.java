package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.exception.MenuException.PriceMoreThanProductsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void init_success() {
        MenuProduct wuga1 = new MenuProduct(new Menu(), Product.of("wuga", BigDecimal.valueOf(1000)), 10);
        MenuProduct wuga2 = new MenuProduct(new Menu(), Product.of("wuga", BigDecimal.valueOf(5000)), 3);

        Menu wugas = Menu.of("wugas", Price.from(BigDecimal.valueOf(10000)), new MenuGroup(),
                List.of(wuga1, wuga2));

        assertThat(wugas.getName()).isEqualTo("wugas");
    }


    @Test
    @DisplayName("메뉴를 생성할 때 가격의 총합이 메뉴 상품들의 총합보다 작아야한다.")
    void init_fail() {
        MenuProduct wuga1 = new MenuProduct(new Menu(), Product.of("wuga", BigDecimal.valueOf(1000)), 10);
        MenuProduct wuga2 = new MenuProduct(new Menu(), Product.of("wuga", BigDecimal.valueOf(5000)), 3);

        assertThatThrownBy(() ->
                Menu.of("wugas", Price.from(BigDecimal.valueOf(25001)), new MenuGroup(), List.of(wuga1, wuga2)))
                .isInstanceOf(PriceMoreThanProductsException.class);
    }
}
