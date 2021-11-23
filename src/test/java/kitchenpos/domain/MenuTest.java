package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @Test
    @DisplayName("Menu 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Long id = 1L;
        String name = "마늘치킨";
        BigDecimal price = BigDecimal.valueOf(4000);
        MenuGroup 치킨세트 = MenuGroup.create("치킨세트");
        List<MenuProduct> 상품들 = Arrays.asList(
                MenuProduct.create(Product.create("마늘", BigDecimal.valueOf(1000)), 1L),
                MenuProduct.create(Product.create("치킨", BigDecimal.valueOf(3000)), 1L)
        );

        //when
        Menu actual = Menu.create(id, name, price, 치킨세트, 상품들);

        //then
        assertEquals(actual.getId(), id);
        assertEquals(actual.getName(), name);
        assertEquals(actual.getPrice(), price);
    }

    @Test
    @DisplayName("Menu 생성 시 메뉴 Price가 0보다 작거나 null일 때 에러 발생 테스트")
    public void validatesPriceTest_WithNullPriceOrNegativePrice() throws Exception {
        //given
        Long id = 1L;
        String name = "마늘치킨";
        BigDecimal price = null;
        MenuGroup 치킨세트 = MenuGroup.create("치킨세트");
        List<MenuProduct> 상품들 = Arrays.asList(
                MenuProduct.create(Product.create("마늘", BigDecimal.valueOf(1000)), 1L),
                MenuProduct.create(Product.create("치킨", BigDecimal.valueOf(3000)), 1L)
        );

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Menu.create(id, name, price, 치킨세트, 상품들);
        }).withMessage("Menu의 price는 null이 아니거나 0보다 커야합니다.");
    }

    @Test
    @DisplayName("Menu 생성 시 메뉴 Price가 상품들의 총합 가격보다 높을 때 에러 발생 테스트")
    public void validatesPriceTest_MenuPriceBiggerThanSumOfProductPrice() throws Exception {
        //given
        Long id = 1L;
        String name = "마늘치킨";
        BigDecimal price = BigDecimal.valueOf(5000);
        MenuGroup 치킨세트 = MenuGroup.create("치킨세트");
        List<MenuProduct> 상품들 = Arrays.asList(
                MenuProduct.create(Product.create("마늘", BigDecimal.valueOf(1000)), 1L),
                MenuProduct.create(Product.create("치킨", BigDecimal.valueOf(3000)), 1L)
        );

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Menu.create(id, name, price, 치킨세트, 상품들);
        }).withMessage("상품들의 총합 가격보다 메뉴의 가격이 높으면 안됩니다.");
    }
}
