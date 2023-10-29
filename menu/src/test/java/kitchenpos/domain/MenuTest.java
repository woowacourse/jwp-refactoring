package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    private MenuGroup 두마리메뉴;

    private MenuProduct 후라이드_한마리;
    private MenuProduct 양념치킨_한마리;

    @BeforeEach
    void setUp() {
        두마리메뉴 = MenuGroup.create("두마리메뉴");

        Product 후라이드 = Product.create("후라이드", BigDecimal.valueOf(16000));
        Product 양념치킨 = Product.create("양념치킨", BigDecimal.valueOf(16000));

        Menu menu = Menu.create("두마리메뉴 - 후1양1", BigDecimal.valueOf(32000L), this.두마리메뉴);

        후라이드_한마리 = MenuProduct.create(menu, 후라이드, 1L);
        양념치킨_한마리 = MenuProduct.create(menu, 양념치킨, 1L);
    }

    @DisplayName("메뉴 생성 시, 이름이 비어있으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "  "})
    void menu_FailWithBlankName(String blankName) {
        // when & then
        assertThatThrownBy(() -> Menu.create(blankName, BigDecimal.ZERO, 두마리메뉴))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴명이 비어있습니다.");
    }

    @DisplayName("메뉴 생성 시, 가격이 비어있으면 예외가 발생한다.")
    @Test
    void menu_FailWithNullPrice() {
        // when & then
        assertThatThrownBy(() -> Menu.create("메뉴", null, 두마리메뉴))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격이 비어있습니다.");
    }

    @DisplayName("메뉴 생성 시, 가격이 0원 미이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L})
    void menu_FailWithBlankName(Long invalidPrice) {
        // when & then
        assertThatThrownBy(() -> Menu.create("메뉴명", BigDecimal.valueOf(invalidPrice), 두마리메뉴))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void menu() {
        // then
        assertDoesNotThrow(() -> Menu.create("메뉴명", BigDecimal.ZERO, 두마리메뉴));
    }

    @DisplayName("메뉴에 상품을 추가 시, 메뉴 가격이 상품 가격의 합보다 크면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {33000L, 34000L})
    void updateMenuProducts_FailWithInvalidPrice(Long invalidPrice) {
        // given
        Menu menu = Menu.create("메뉴명", BigDecimal.valueOf(invalidPrice), 두마리메뉴);

        // when & hen
        assertThatThrownBy(() -> menu.updateMenuProducts(List.of(후라이드_한마리, 양념치킨_한마리)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 금액의 합계는 각 상품들의 금액 합계보다 클 수 없습니다.");
    }

    @DisplayName("메뉴에 상품을 추가 시, 메뉴 가격이 상품 가격의 합보다 작으면 정상적으로 추가된다.")
    @ParameterizedTest
    @ValueSource(longs = {32000L, 30000L})
    void updateMenuProducts(Long validPrice) {
        // given
        Menu menu = Menu.create("메뉴명", BigDecimal.valueOf(validPrice), 두마리메뉴);

        // when & then
        assertDoesNotThrow(() -> menu.updateMenuProducts(List.of(후라이드_한마리, 양념치킨_한마리)));
    }
}
