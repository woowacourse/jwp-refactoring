package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.support.domain.MenuTestSupport;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    MenuService target;

    @DisplayName("메뉴를 생성하면 DB에 저장해서 반환한다.")
    @Test
    void create() {
        //given
        final Menu menu = MenuTestSupport.builder().build();

        //when

        //then
        Assertions.assertDoesNotThrow(() -> target.create(menu));
    }

    @DisplayName("메뉴의 가격이 음수이면 예외처리한다.")
    @Test
    void create_fail_price_minus() {
        //given
        final BigDecimal price = new BigDecimal("-1");
        final Menu menu = MenuTestSupport.builder().price(price).build();

        //when

        //then
        assertThatThrownBy(() -> target.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 메뉴 그룹에 포함되지 않으면 예외처리한다.")
    @Test
    void create_fail_not_in_menuGroup() {
        //given
        final Menu menu = MenuTestSupport.builder().menuGroupId(null).build();

        //when

        //then
        assertThatThrownBy(() -> target.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("입력받은 메뉴의 가격은 메뉴 상품들의 가격보다 비싸면 안된다.")
    @Test
    void create_fail_price_greaterThan_product_price() {
        //given
        final BigDecimal price = new BigDecimal(Long.MAX_VALUE);
        final Menu menu = MenuTestSupport.builder().price(price).build();

        //when

        //then
        assertThatThrownBy(() -> target.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 모든 메뉴를 조회한다.")
    @Test
    void list() {
        //given
        final Menu menu1 = target.create(MenuTestSupport.builder().build());
        final Menu menu2 = target.create(MenuTestSupport.builder().build());

        //when
        final List<Menu> result = target.list();

        //then
        assertThat(result)
                .extracting(Menu::getName)
                .contains(menu1.getName(), menu2.getName());
    }
}
