package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.Fixture.Fixture.menuFixture;
import static kitchenpos.Fixture.Fixture.menuGroupFixture;
import static kitchenpos.Fixture.Fixture.menuProductFixture;
import static kitchenpos.Fixture.Fixture.productFixture;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;
    private Product product;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        this.menuGroup = menuGroupDao.save(menuGroupFixture("메뉴 그룹1"));
        this.product = productDao.save(productFixture("상품1", new BigDecimal(10000)));
        this.menuProduct = menuProductFixture(null, product.getId(), 4);
    }

    @Nested
    class 몌뉴등록 {
        @Test
        void 메뉴를_등록한다() {
            MenuGroup menuGroup = menuGroupDao.save(menuGroupFixture("메뉴 그룹1"));
            Product product = productDao.save(productFixture("productName", BigDecimal.valueOf(10000L)));
            MenuProduct menuProduct = menuProductFixture(null, product.getId(), 4);

            Menu menu = menuFixture("메뉴", new BigDecimal("30000.00"), menuGroup.getId(), List.of(menuProduct));
            Menu savedMenu = menuService.create(menu);

            assertSoftly(softly -> {
                softly.assertThat(savedMenu.getId()).isNotNull();
                softly.assertThat(savedMenu.getPrice()).isEqualByComparingTo(menu.getPrice());
                softly.assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "menuProducts")
                        .isEqualTo(menu);
                softly.assertThat(savedMenu.getMenuProducts()).hasSize(1)
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("seq")
                        .containsOnly(menuProduct);
            });
        }

        @Test
        void 메뉴_가격이_0원_미만이면_등록할_수_없다() {
            Menu menu = menuFixture("메뉴", new BigDecimal(-1), menuGroup.getId(), List.of(menuProduct));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격은 0 이상이어야 합니다.");
        }

        @Test
        void 포함될_메뉴_그룹이_존재하지_않으면_등록할_수_없다() {
            long notExistMenuGroupId = 100000L;
            Menu menu = menuFixture("메뉴", new BigDecimal("30000.00"), notExistMenuGroupId, List.of(menuProduct));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴그룹입니다. 메뉴를 등록할 수 없습니다.");
        }

        @Test
        void 메뉴_상품이_존재하지_않으면_등록할_수_없다() {
            MenuProduct notExistMenuProduct = menuProductFixture(null, Long.MIN_VALUE, 1);
            Menu menu = menuFixture("메뉴", new BigDecimal("30000.00"), menuGroup.getId(), List.of(notExistMenuProduct));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 메뉴상품입니다. 메뉴를 등록할 수 없습니다.");
        }

        @Test
        void 메뉴의_가격이_메뉴_상품들의_가격_합보다_비싸면_등록할_수_없다() {
            Product product1 = productFixture("상품2", new BigDecimal(10001));
            MenuProduct menuProduct1 = menuProductFixture(null, product1.getId(), 1);
            Menu menu = menuFixture("메뉴", new BigDecimal("50000.00"), menuGroup.getId(), List.of(menuProduct, menuProduct1));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴의_목록을_조회할_수_있다() {
        Menu menu1 = menuService.create(menuFixture("메뉴1", new BigDecimal("30000.00"), menuGroup.getId(), List.of(menuProduct)));
        Menu menu2 = menuService.create(menuFixture("메뉴2", new BigDecimal("30000.00"), menuGroup.getId(), List.of(menuProduct)));

        List<Menu> menuList = menuService.list();

        assertThat(menuList).hasSize(2)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("price", "menuProducts")
                .containsExactly(menu1, menu2);
    }
}