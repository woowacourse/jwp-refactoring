package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Menu 메뉴는 이름, 가격, 메뉴 그룹, 메뉴 상품 리스트를 가지고 있습니다.
 * <p>
 * create (메뉴를 생성할 수 있습니다.)
 * <p>
 * price 가 필수값입니다. price > 0 조건을 만족해야합니다. 존재하는 menu group 이어야 합니다. menu 가 가지고 있는 product 가 존재하는 상품이어야합니다. findAll (메뉴
 * 전체를 조회합니다.)
 */

@SpringBootTest
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴의_가격이_0이하면_예외를_반환한다() {
        // given
        final Menu 세트A = new Menu("세트A", BigDecimal.ZERO, 1L, null);

        // when then
        assertThatThrownBy(() -> menuService.create(세트A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_Null_이면_예외를_반환한다() {
        // given
        final Menu 세트A = new Menu("세트A", null, 1L, null);

        // when then
        assertThatThrownBy(() -> menuService.create(세트A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴그룹이_존재하지_않으면_예외를_반환한다() {
        //given
        final Menu 세트A = new Menu("세트A", BigDecimal.valueOf(1000), 1L, null);
        메뉴그룹에서_없는_메뉴로_세팅한다();

        // when then
        assertThatThrownBy(() -> menuService.create(세트A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴가_존재하지_않으면_예외를_반환한다() {
        //given
        final Menu 세트A = get세트A();
        메뉴그룹에서_있는_메뉴로_세팅한다();
        없는_상품으로_세팅한다();

        assertThatThrownBy(() -> menuService.create(세트A))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
