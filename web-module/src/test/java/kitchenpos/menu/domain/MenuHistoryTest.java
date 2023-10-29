package kitchenpos.menu.domain;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.order.domain.MenuHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuHistoryTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        assertThatCode(() -> new MenuHistory(
                1L,
                new Name("테스트용 기록 메뉴명"),
                Price.from("10000")
        )).doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 메뉴를 통해서 메뉴 기록을 생성한다.")
    @Test
    void success_create_menu() {
        // given
        final Product product = new Product(new Name("테스트용 상품명"), Price.from("10000"));
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));

        final MenuProduct menuProduct = MenuProduct.withoutMenu(product, new Quantity(10));
        final Menu expectedMenu = Menu.withEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup);
        expectedMenu.addMenuProducts(List.of(menuProduct));

        // when
        final MenuHistory actual = MenuHistory.of(1L, expectedMenu.getName(), expectedMenu.getPrice());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNull();
            softly.assertThat(actual.getName()).isEqualTo(expectedMenu.getName());
            softly.assertThat(actual.getPrice()).isEqualTo(expectedMenu.getPrice());
        });
    }
}
