package kitchenpos.domain;

import static kitchenpos.DomainFixture.createMenu;
import static kitchenpos.DomainFixture.메뉴그룹1;
import static kitchenpos.DomainFixture.피자;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴의 가격이 0원 미만이면 예외가 발생한다.")
    void Menu() {
        // when & then
        assertThatThrownBy(() -> createMenu("양념치킨메뉴", -1, 메뉴그룹1, 피자))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
