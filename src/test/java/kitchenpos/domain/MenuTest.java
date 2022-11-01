package kitchenpos.domain;

import static kitchenpos.DomainFixture.createMenu;
import static kitchenpos.DomainFixture.메뉴그룹1;
import static kitchenpos.DomainFixture.양념치킨;
import static kitchenpos.DomainFixture.피자;
import static kitchenpos.DomainFixture.후라이드치킨;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @Test
    @DisplayName("메뉴의 가격이 0원 미만이면 예외가 발생한다.")
    void menu_invalidPrice_throwException() {
        // when & then
        assertThatThrownBy(() -> createMenu("양념치킨메뉴", -1, 메뉴그룹1, 피자))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴 상품들 가격의 합보다 클 경우 예외가 발생한다.")
    void menu_overProductsPrice_throwException() {
        // given
        final Menu menu = createMenu("치킨피자메뉴", 30_000, 메뉴그룹1, 후라이드치킨, 양념치킨);

        // when & then
        assertThatThrownBy(() -> menu.compareToTotalMenuPrice(후라이드치킨.getPrice().add(양념치킨.getPrice())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
