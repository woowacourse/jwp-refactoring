package kitchenpos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;

import static kitchenpos.MenuProductFixture.양념치킨_한마리_메뉴상품;
import static kitchenpos.MenuProductFixture.후라이드치킨_한마리_메뉴상품;
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
                1L,
                Arrays.asList(후라이드치킨_한마리_메뉴상품, 양념치킨_한마리_메뉴상품)));
    }

    @Test
    @DisplayName("메뉴의 가격이 null이면 메뉴를 생성할 수 없다.")
    void nullPrice() {
        // when & then
        assertThatThrownBy(() -> new Menu(
                "양념 반 + 후라이드 반",
                null,
                1L,
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
                1L,
                Arrays.asList(후라이드치킨_한마리_메뉴상품, 양념치킨_한마리_메뉴상품))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.");
    }
}
