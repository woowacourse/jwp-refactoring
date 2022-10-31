package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuProductsTest {

    @DisplayName("메뉴 가격이 메뉴 상품 목록 전체 가격보다 큰 경우")
    @ParameterizedTest
    @CsvSource(value = {"7000:false", "7001:true"}, delimiter = ':')
    void isOverThanTotalPrice(long menuPrice, boolean result) {
        List<MenuProduct> menuProductList = List.of(
                new MenuProduct(1L,1L), new MenuProduct(2L, 3L));
        Map<Long, Long> groupedPriceByProduct = Map.of(1L, 1000L, 2L, 2000L, 3L, 3000L);

        MenuProducts menuProducts = new MenuProducts(menuProductList);
        boolean overThanTotalPrice = menuProducts.isOverThanTotalPrice(groupedPriceByProduct, menuPrice);

        assertThat(overThanTotalPrice).isEqualTo(result);
    }
}
