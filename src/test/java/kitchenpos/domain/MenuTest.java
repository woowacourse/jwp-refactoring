package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Menu 단위 테스트")
class MenuTest {
    // given
    private final String name = "양념 반 + 후라이드 반";
    private final BigDecimal price = BigDecimal.valueOf(30000);
    private final Long menuGroupId = 1L;
    private final Product 후라이드치킨_정보 = new Product(1L, "후라이드 치킨", 16000);
    private final Product 양념치킨_정보 = new Product(2L, "양념 치킨", 16000);
    private final MenuProduct 후라이드치킨 = new MenuProduct(후라이드치킨_정보, 1);
    private final MenuProduct 양념치킨 = new MenuProduct(양념치킨_정보, 1);
    private final List<MenuProduct> menuProducts = Arrays.asList(후라이드치킨, 양념치킨);

    @Test
    @DisplayName("메뉴의 이름, 가격, 메뉴 그룹, 메뉴에 포함된 제품이 조건을 만족한다면 메뉴를 생성한다.")
    void create() {
        // when & then
        assertDoesNotThrow(() -> new Menu(name, price, menuGroupId, menuProducts));
    }

    @Test
    @DisplayName("메뉴의 가격이 null이면 메뉴를 생성할 수 없다.")
    void nullPrice() {
        // when & then
        assertThatThrownBy(() -> new Menu(name, null, menuGroupId, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴의 가격이 음수면 메뉴를 생성할 수 없다.")
    void minusPrice() {
        // when & then
        assertThatThrownBy(() -> new Menu(name, BigDecimal.valueOf(-1), menuGroupId, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.");
    }

//    @Test
//    @DisplayName("메뉴의 가격이 메뉴를 구성하는 실제 제품들을 단품으로 주문하였을 때의 가격 합보다 크면 메뉴를 생성할 수 없다.")
//    void expensivePrice() {
//        // when & then
//        assertThatThrownBy(() -> new Menu(name, BigDecimal.valueOf(50000), menuGroupId, menuProducts))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다.");
//    }
}
