package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.exception.MenuServiceException.PriceMoreThanProductsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void init_success() {
        MenuProduct wooga1 = new MenuProduct(new Menu(), Product.of("wooga", BigDecimal.valueOf(1000)), 10);
        MenuProduct wooga2 = new MenuProduct(new Menu(), Product.of("wooga", BigDecimal.valueOf(5000)), 3);

        Menu woogas = Menu.of("woogas", Price.from(BigDecimal.valueOf(10000)), new MenuGroup(),
                List.of(wooga1, wooga2));

        assertThat(woogas.getName()).isEqualTo("woogas");
    }


    @Test
    @DisplayName("메뉴를 생성할 때 가격의 총합이 메뉴 상품들의 총합보다 작아야한다.")
    void init_fail() {
        MenuProduct wooga1 = new MenuProduct(new Menu(), Product.of("wooga", BigDecimal.valueOf(1000)), 10);
        MenuProduct wooga2 = new MenuProduct(new Menu(), Product.of("wooga", BigDecimal.valueOf(5000)), 3);

        assertThatThrownBy(() ->
                Menu.of("woogas", Price.from(BigDecimal.valueOf(25001)), new MenuGroup(), List.of(wooga1, wooga2)))
                .isInstanceOf(PriceMoreThanProductsException.class);
    }
}
