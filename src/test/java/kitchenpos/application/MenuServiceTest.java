package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
        productDao.save(Fixtures.상품_후라이드());
        menuProductDao.save(Fixtures.메뉴상품_후라이드());
    }

    @DisplayName("특정 메뉴를 추가할 시 메뉴 목록에 추가된다.")
    @Test
    void createAndList() {
        Menu 메뉴_후라이드치킨 = Fixtures.메뉴_후라이드치킨();

        Menu saved = menuService.create(메뉴_후라이드치킨);

        assertThat(menuService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }

    @DisplayName("메뉴 그룹은 DB에 등록되어야 한다.")
    @Test
    void createAndList_invalidMenuGroup() {
        MenuProduct menuProduct = Fixtures.메뉴상품_후라이드();
        Menu wrong = new Menu(1L, "후라이드", BigDecimal.valueOf(10000),
                100L,
                List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(wrong))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹은 DB에 등록되어야 한다.");
    }

    @DisplayName("메뉴 속 상품들은 모두 DB에 등록되어야 한다.")
    @Test
    void createAndList_invalidProduct() {
        Product product = new Product(100L, "wrong", BigDecimal.valueOf(10000));
        MenuProduct menuProduct = new MenuProduct(1L, 1L, product.getId(), 10);
        Menu wrong = new Menu(1L, "후라이드", BigDecimal.valueOf(10000),
                1L,
                List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(wrong))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 속 상품들은 모두 DB에 등록되어야 한다");
    }

    @DisplayName("메뉴 가격은 0 이상이어야 한다.")
    @Test
    void createAndList_invalidPrice() {
        MenuProduct menuProduct = Fixtures.메뉴상품_후라이드();

        assertThatThrownBy(() -> new Menu(1L, "후라이드", BigDecimal.valueOf(-10000),
                1L,
                List.of(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 0 이상이어야 한다.");
    }

    @DisplayName("메뉴 가격은 내부 모든 상품가격보다 낮아야 한다.")
    @Test
    void createAndList_invalidDiscount() {
        MenuProduct menuProduct = Fixtures.메뉴상품_후라이드();
        Menu wrong = new Menu(1L, "후라이드", BigDecimal.valueOf(100000),
                1L,
                List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(wrong))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 내부 모든 상품가격보다 낮아야 한다.");
    }
}
