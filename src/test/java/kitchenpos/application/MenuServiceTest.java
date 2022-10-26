package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @BeforeEach
    void setup() {
        productDao.save(상품_후라이드());
        menuProductDao.save(메뉴상품_후라이드());
    }

    @DisplayName("특정 메뉴를 추가할 시 메뉴 목록에 추가된다.")
    @Test
    void createAndList() {
        Menu 메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨());

        List<Menu> 메뉴_목록 = menuService.list();

        검증_필드비교_값포함(assertThat(메뉴_목록), 메뉴_후라이드치킨);
    }

    @DisplayName("여러 메뉴를 추가할 시 메뉴 목록에 추가된다.")
    @Test
    void createAndList_multi() {
        Menu 메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨());
        Menu 메뉴_후라이드치킨2 = menuService.create(메뉴_후라이드치킨());

        List<Menu> 메뉴_목록 = menuService.list();

        검증_필드비교_동일_목록(메뉴_목록, List.of(메뉴_후라이드치킨, 메뉴_후라이드치킨2));
    }

    @DisplayName("메뉴 그룹은 DB에 등록되어야 한다.")
    @Test
    void createAndList_invalidMenuGroup() {
        long 잘못된_메뉴그룹_ID = 100L;
        Menu 메뉴_후라이드치킨 = new Menu(1L, "후라이드", BigDecimal.valueOf(10000),
                잘못된_메뉴그룹_ID, List.of(메뉴상품_후라이드()));

        assertThatThrownBy(() -> menuService.create(메뉴_후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹은 DB에 등록되어야 한다.");
    }

    @DisplayName("메뉴 속 상품들은 모두 DB에 등록되어야 한다.")
    @Test
    void createAndList_invalidProduct() {
        long 잘못된_상품_id = 100L;
        Menu 메뉴_치킨그룹 = 메뉴_치킨그룹(new MenuProduct(1L, 1L, 잘못된_상품_id, 10));

        assertThatThrownBy(() -> menuService.create(메뉴_치킨그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 속 상품들은 모두 DB에 등록되어야 한다");
    }

    @DisplayName("메뉴 가격은 내부 모든 상품가격보다 낮아야 한다.")
    @Test
    void createAndList_invalidDiscount() {
        BigDecimal 너무비싼_가격 = BigDecimal.valueOf(100000);
        Menu 메뉴_후라이드치킨 = new Menu(1L, "후라이드", 너무비싼_가격,
                1L, List.of(메뉴상품_후라이드()));

        assertThatThrownBy(() -> menuService.create(메뉴_후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 내부 모든 상품가격보다 낮아야 한다.");
    }
}
