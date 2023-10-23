package kitchenpos.domain.menu;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.support.money.Money;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class MenuValidatorTest {

    @Autowired
    private MenuValidator sut;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 존재하지_않는_메뉴_그룹이라면_예외를_던진다() {
        // given
        Menu menu = new Menu("치즈피자", Money.valueOf(-1), 1L, List.of());

        // expect
        assertThatThrownBy(() -> sut.validate(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴 그룹 아이디입니다.");
    }

    @Test
    void 메뉴의_가격이_0원_미만인_경우_예외를_던진다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
        Menu menu = new Menu("치즈피자", Money.valueOf(-1), menuGroup.getId(), List.of());

        // expect
        assertThatThrownBy(() -> sut.validate(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 메뉴의_가격이_메뉴_상품들의_금액의_합보다_큰_경우_예외를_던진다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
        Product product = 상품("치즈 피자", 8900L);
        MenuProduct menuProduct = 메뉴_상품(product, 1L);
        Menu menu = new Menu("치즈피자", Money.valueOf(8901L), menuGroup.getId(), List.of(menuProduct));

        // expect
        assertThatThrownBy(() -> sut.validate(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품들의 금액의 합보다 클 수 없습니다.");
    }
}
