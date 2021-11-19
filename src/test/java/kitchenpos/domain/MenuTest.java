package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import static kitchenpos.fixture.MenuGroupFixture.추천메뉴;
import static kitchenpos.fixture.MenuProductFixture.양념치킨_한마리_메뉴상품;
import static kitchenpos.fixture.MenuProductFixture.후라이드치킨_한마리_메뉴상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Menu 단위 테스트")
class MenuTest {

    @Test
    @DisplayName("메뉴의 이름, 가격, 메뉴 그룹, 메뉴에 포함된 제품이 조건을 만족한다면 메뉴를 생성한다.")
    void create() {
        // when & then
        assertDoesNotThrow(() -> new Menu(
                "양념 반 + 후라이드 반",
                BigDecimal.valueOf(30000),
                추천메뉴,
                Arrays.asList(후라이드치킨_한마리_메뉴상품, 양념치킨_한마리_메뉴상품)));
    }

    @Test
    @DisplayName("메뉴의 가격이 null이면 메뉴를 생성할 수 없다.")
    void nullPrice() {
        // when & then
        assertThatThrownBy(() -> new Menu(
                "양념 반 + 후라이드 반",
                null,
                추천메뉴,
                Arrays.asList(후라이드치킨_한마리_메뉴상품, 양념치킨_한마리_메뉴상품))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴의 가격이 음수면 메뉴를 생성할 수 없다.")
    void minusPrice() {
        // when & then
        assertThatThrownBy(() -> new Menu(
                "양념 반 + 후라이드 반",
                BigDecimal.valueOf(-1),
                추천메뉴,
                Arrays.asList(후라이드치킨_한마리_메뉴상품, 양념치킨_한마리_메뉴상품))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴를 구성하는 실제 제품들을 단품으로 주문하였을 때의 가격 합보다 크면 메뉴를 생성할 수 없다.")
    void addMenuProductsExpensivePrice() {
        // given
        Menu menu = new Menu(
                "양념 반 + 후라이드 반",
                BigDecimal.valueOf(34001),
                추천메뉴);
        List<MenuProduct> menuProducts = Arrays.asList(후라이드치킨_한마리_메뉴상품, 양념치킨_한마리_메뉴상품); // 후라이드 치킨 17000원, 양념 치킨 17000원

        // when & then
        assertThatThrownBy(() -> menu.addMenuProducts(menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다.");
    }
}
