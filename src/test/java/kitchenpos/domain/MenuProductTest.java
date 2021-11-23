package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductTest {

    @Test
    @DisplayName("MenuProduct 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Product 마늘치킨 = Product.create("마늘치킨", BigDecimal.valueOf(1000));
        Long quantity = 3L;

        //when
        MenuProduct actual = MenuProduct.create(마늘치킨, quantity);

        //then
        assertEquals(actual.getProduct(), 마늘치킨);
        assertEquals(actual.getQuantity(), quantity);
    }

    @Test
    @DisplayName("totalPrice 계산 테스트")
    public void totalPriceTest() throws Exception {
        //given
        Product 마늘치킨 = Product.create("마늘치킨", BigDecimal.valueOf(1000));
        Long quantity = 3L;
        MenuProduct actual = MenuProduct.create(마늘치킨, quantity);

        //when
        BigDecimal totalPrice = actual.totalPrice();

        //then
        assertEquals(totalPrice, BigDecimal.valueOf(3000));
    }
}
