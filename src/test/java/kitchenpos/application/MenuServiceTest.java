package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;


    @Nested
    class 메뉴를_등록할_때 {

        @Test
        void success() {
            // given
            Product savedProduct = productDao.save(ProductFixture.create("후라이드", 2000));
            MenuProduct menuProduct = MenuProductFixture.create(savedProduct.getId());
            MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.create("치킨"));

            Menu menu = MenuFixture.create(
                    "후라이드 치킨",
                    1000,
                    savedMenuGroup.getId(),
                    List.of(menuProduct)
            );

            // when
            Menu result = menuService.create(menu);

            // then
            assertThat(result.getName()).isEqualTo(menu.getName());
            assertThat(result.getPrice()).isEqualByComparingTo(menu.getPrice());
        }

        @Test
        void 가격_정보가_없거나_0보다_작은_경우_실패() {
            // given
            Product savedProduct = productDao.save(ProductFixture.create("후라이드", 2000));
            MenuProduct menuProduct = MenuProductFixture.create(savedProduct.getId());
            MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.create("치킨"));

            Menu menu = MenuFixture.create(
                    "후라이드 치킨",
                    -10,
                    savedMenuGroup.getId(),
                    List.of(menuProduct)
            );

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 등록되지_않은_메뉴그룹에_속할_시_실패() {
            // given
            Product savedProduct = productDao.save(ProductFixture.create("후라이드", 2000));
            MenuProduct menuProduct = MenuProductFixture.create(savedProduct.getId());
            MenuGroup savedMenuGroup = MenuGroupFixture.create("치킨");

            Menu menu = MenuFixture.create(
                    "후라이드 치킨",
                    1000,
                    savedMenuGroup.getId(),
                    List.of(menuProduct)
            );

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_메뉴_내부의_상품의_총_합계_가격보다_크면_실패() {
            // given
            Product savedProduct = productDao.save(ProductFixture.create("후라이드", 2000));
            MenuProduct menuProduct = MenuProductFixture.create(savedProduct.getId());
            MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.create("치킨"));

            Menu menu = MenuFixture.create(
                    "후라이드 치킨",
                    3000,
                    savedMenuGroup.getId(),
                    List.of(menuProduct)
            );

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 메뉴_목록_조회() {
        // given
        Product savedProduct = productDao.save(ProductFixture.create("후라이드", 2000));
        MenuProduct menuProduct = MenuProductFixture.create(savedProduct.getId());
        MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroupFixture.create("치킨"));

        Menu menu = MenuFixture.create(
                "후라이드 치킨",
                1000,
                savedMenuGroup.getId(),
                List.of(menuProduct)
        );
        Menu savedMenu = menuService.create(menu);

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(List.of(savedMenu));
    }
}
