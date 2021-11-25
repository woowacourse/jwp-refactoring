package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuPrices;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuPrices 단위 테스트")
class MenuPricesTest {

    @DisplayName("모든 MenuPrice들의 값을 더한 결과를 반환한다.")
    @Test
    void sumAll() {
        // given
        MenuPrice menuPrice1 = new MenuPrice(BigDecimal.valueOf(3_000));
        MenuPrice menuPrice2 = new MenuPrice(BigDecimal.valueOf(4_000));
        MenuPrice menuPrice3 = new MenuPrice(BigDecimal.valueOf(5_000));

        MenuPrices menuPrices = new MenuPrices(Arrays.asList(menuPrice1, menuPrice2, menuPrice3));

        // when
        MenuPrice totalMenuPrice = menuPrices.sumAll();
        MenuPrice expectedMenuPrice = new MenuPrice(menuPrice1.getValue().add(menuPrice2.getValue()).add(menuPrice3.getValue()));

        // then
        assertThat(totalMenuPrice).isEqualTo(expectedMenuPrice);
    }
}
