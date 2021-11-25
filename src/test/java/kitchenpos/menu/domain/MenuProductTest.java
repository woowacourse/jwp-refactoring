package kitchenpos.menu.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductTest {

    @Test
    @DisplayName("MenuProduct 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Product 마늘치킨 = Product.create(1L, "마늘치킨", BigDecimal.valueOf(1000));
        Long quantity = 3L;

        //when
        MenuProduct actual = MenuProduct.create(마늘치킨.getId(), quantity);

        //then
        assertEquals(actual.getProductId(), 마늘치킨.getId());
        assertEquals(actual.getQuantity(), quantity);
    }
}
