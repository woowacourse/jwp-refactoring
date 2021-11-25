package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroup 치킨세트;
    private Long 마늘_Id;
    private Long 치킨_Id;
    private List<MenuProduct> 상품들;

    @BeforeEach
    void setup() {
        id = 1L;
        name = "마늘치킨";
        price = BigDecimal.valueOf(4000);
        치킨세트 = MenuGroup.create(1L, "치킨세트");
        마늘_Id = 1L;
        치킨_Id = 2L;
        상품들 = Arrays.asList(
                MenuProduct.create(마늘_Id, 1L),
                MenuProduct.create(치킨_Id, 1L)
        );
    }

    @Test
    @DisplayName("Menu 생성 성공 테스트")
    public void createTest() throws Exception {
        //when
        Menu actual = Menu.create(id, name, price, 치킨세트.getId(), 상품들);

        //then
        assertEquals(actual.getId(), id);
        assertEquals(actual.getName(), name);
        assertEquals(actual.getPrice(), price);
    }

    @Test
    @DisplayName("Menu 생성 시 메뉴 Price가 0보다 작거나 null일 때 에러 발생 테스트")
    public void validatesPriceTest_WithNullPriceOrNegativePrice() throws Exception {
        //given
        price = null;

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Menu.create(id, name, price, 치킨세트.getId(), 상품들);
        }).withMessage("Menu의 price는 null이 아니거나 0보다 커야합니다.");
    }
}
