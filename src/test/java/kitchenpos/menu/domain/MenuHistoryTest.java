package kitchenpos.menu.domain;

import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.menu.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

class MenuHistoryTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        assertThatCode(() -> new MenuHistory(
                new Name("테스트용 기록 메뉴명"),
                Price.from("10000"),
                new MenuProductHistories()
        )).doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 메뉴를 통해서 메뉴 기록을 생성한다.")
    @Test
    void success_create_menu() {
        // given
        final Product product = new Product(new Name("테스트용 상품명"), Price.from("10000"));
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));

        final MenuProduct menuProduct = new MenuProduct(product, new Quantity(10));
        final Menu expectedMenu = Menu.withEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup);
        expectedMenu.addMenuProducts(List.of(menuProduct));

        // when
        final MenuHistory actual = MenuHistory.from(expectedMenu);
        final MenuProductHistories expectedMenuProductHistories = MenuProductHistories.from(new MenuProducts(List.of(
                menuProduct
        )));

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNull();
            softly.assertThat(actual.getName()).isEqualTo(expectedMenu.getName());
            softly.assertThat(actual.getPrice()).isEqualTo(expectedMenu.getPrice());
            softly.assertThat(actual.getOrderProductHistories())
                    .usingRecursiveComparison()
                    .isEqualTo(expectedMenuProductHistories);
        });
    }
}
