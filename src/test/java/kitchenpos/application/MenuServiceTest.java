package kitchenpos.application;


import static kitchenpos.Fixtures.메뉴_치킨그룹;
import static kitchenpos.Fixtures.메뉴_후라이드치킨;
import static kitchenpos.Fixtures.메뉴상품_후라이드;
import static kitchenpos.Fixtures.상품_후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuProductDao menuProductDao;

    @BeforeEach
    void setup() {
        productDao.save(상품_후라이드());
        menuProductDao.save(메뉴상품_후라이드());
    }

    @DisplayName("특정 메뉴를 추가할 시 메뉴 목록에 추가된다.")
    @Test
    void createAndList() {
        Menu 메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨());

        assertThat(menuService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(메뉴_후라이드치킨);
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

    @DisplayName("메뉴 가격은 0 이상이어야 한다.")
    @Test
    void createAndList_invalidPrice() {
        BigDecimal 음수_가격 = BigDecimal.valueOf(-10000);

        assertThatThrownBy(() -> new Menu(1L, "후라이드", 음수_가격,
                1L, List.of(메뉴상품_후라이드())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 0 이상이어야 한다.");
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
