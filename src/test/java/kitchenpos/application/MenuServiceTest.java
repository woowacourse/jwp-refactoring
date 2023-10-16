package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.fake.FakeMenuDao;
import kitchenpos.fake.FakeMenuGroupDao;
import kitchenpos.fake.FakeMenuProductDao;
import kitchenpos.fake.FakeProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceTest {

    private MenuGroupDao menuGroupDao = new FakeMenuGroupDao();
    private MenuProductDao menuProductDao = new FakeMenuProductDao();
    private MenuDao menuDao = new FakeMenuDao();
    private ProductDao productDao = new FakeProductDao();
    private MenuService menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);


    private MenuProduct menuProduct;
    private MenuGroup menuGroup;
    private Product product;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
        product = productDao.save(new Product(null, "상품1", BigDecimal.valueOf(1000)));
        menuProduct = menuProductDao.save(new MenuProduct(null, 1L, 1L, 1L));
    }

    @Test
    void 메뉴를_저장한다() {
        Menu menu = menuService.create(new Menu(1L, "메뉴1", BigDecimal.valueOf(1000), 1L, List.of(menuProduct)));

        assertThat(menuDao.findById(menu.getId()).get()).usingRecursiveComparison()
                .isEqualTo(menu);
    }

    @Test
    void 메뉴_전체_조회를_한다() {
        menuService.create(new Menu(1L, "메뉴1", BigDecimal.valueOf(1000), 1L, List.of(menuProduct)));
        menuService.create(new Menu(2L, "메뉴2", BigDecimal.valueOf(1000), 1L, List.of(menuProduct)));

        List<Menu> result = menuService.list();
        assertThat(result).hasSize(2);
    }

    @Nested
    class 메뉴를_생성할_때 {
        @Test
        void 가격은_0_미만_일_수_없다() {
            assertThatThrownBy(() -> menuService.create(new Menu(1L, "메뉴1", BigDecimal.valueOf(-1), 1L, List.of(menuProduct))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격은_null_일_수_없다() {
            assertThatThrownBy(() -> menuService.create(new Menu(1L, "메뉴1", null, 1L, List.of(menuProduct))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴그룹에_만들_수_없다() {
            assertThatThrownBy(() -> menuService.create(new Menu(1L, "메뉴1", BigDecimal.valueOf(1000), 2L, List.of(menuProduct))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_항목의_합보다_비싸면_생성할_수_없다() {
            assertThatThrownBy(() -> menuService.create(new Menu(1L, "메뉴1", BigDecimal.valueOf(2000), 1L, List.of(menuProduct))))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
