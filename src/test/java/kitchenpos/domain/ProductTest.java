package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("Product 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Long id = 1L;
        String name = "치킨";
        BigDecimal price = BigDecimal.valueOf(5000);

        //when
        Product product = Product.create(id, name, price);

        //then
        assertEquals(product.getId(), id);
        assertEquals(product.getName(), name);
        assertEquals(product.getPrice(), price);
    }

    @Test
    @DisplayName("Product 생성 시 가격이 null일 때 예외 테스트")
    public void validatePriceTest_WithNull() throws Exception {
        //given
        Long id = 1L;
        String name = "치킨";
        BigDecimal price = null;

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Product.create(id, name, price);
        }).withMessage("가격이 존재하거나 0보다 커야합니다.");
    }

    @Test
    @DisplayName("Product 생성 시 가격이 음수일 때 예외 테스트")
    public void validatePriceTest_WithNegative() throws Exception {
        //given
        Long id = 1L;
        String name = "치킨";
        BigDecimal price = BigDecimal.valueOf(-1000);

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Product.create(id, name, price);
        }).withMessage("가격이 존재하거나 0보다 커야합니다.");
    }

    @Test
    @DisplayName("상품 가격에 인자로 받은 개수를 곱한 총 가격 반환 테스트")
    public void calculatePriceTest() throws Exception {
        //given
        Long id = 1L;
        String name = "치킨";
        BigDecimal price = BigDecimal.valueOf(5000);
        Product product = Product.create(id, name, price);

        //when
        BigDecimal totalPrice = product.calculatePrice(10L);

        //then
        assertEquals(totalPrice, BigDecimal.valueOf(50000));
    }
}
