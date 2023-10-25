package kitchenpos.application;

import kitchenpos.domain.Price;
import kitchenpos.fixture.MenuFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.fixture.MenuFixture.후라이드_두마리;
import static kitchenpos.fixture.MenuFixture.후라이드_두마리_REQUEST;
import static kitchenpos.fixture.ProductFixture.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @Transactional
    void 모든_메뉴들을_가져온다() {
        assertThat(menuService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(MenuFixture.listAllInDatabase());
    }

    @Test
    void 메뉴_생성시_가격은_비워둘_수_없다() {
        var menu = 후라이드_두마리_REQUEST();
        menu.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성시_가격은_0보다_작아선_안된다() {
        var menu = 후라이드_두마리_REQUEST();
        menu.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성시_기존_메뉴상품을_사용해야_한다() {
        var menu = 후라이드_두마리_REQUEST();
        menu.setMenuProducts(Collections.emptyList());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성시_메뉴_상품들의_가격_총합은_상품들의_원래_가격_총합보다_커선_안된다() {
        var menu = 후라이드_두마리_REQUEST();
        var menuProduct = menu.getMenuProducts().get(0);
        Price sum = 후라이드().getPrice().multiply(menuProduct.getQuantity());

        menu.setPrice(sum.add(new Price(1)).getAmount());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성시_기존_메뉴_그룹을_활용해야_한다() {
        var menu = 후라이드_두마리_REQUEST();
        menu.setMenuGroupId(-1L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_생성시_저장한_메뉴를_반환한다() {
        var menu = 후라이드_두마리_REQUEST();

        assertThat(menuService.create(menu))
                .usingRecursiveComparison()
                .ignoringFields("id", "menuProducts.seq")
                .isEqualTo(후라이드_두마리());
    }
}
