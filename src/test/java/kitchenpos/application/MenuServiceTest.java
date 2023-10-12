package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.supports.MenuFixture;
import kitchenpos.supports.MenuProductFixture;
import kitchenpos.supports.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    ProductDao productDao;

    @InjectMocks
    MenuService menuService;

    @Nested
    class 메뉴_생성 {

        @Test
        void 메뉴의_가격은_null일_수_없다() {
            // given
            BigDecimal price = null;
            Menu menu = MenuFixture.fixture().price(price).build();

            // when
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않는 메뉴 가격입니다.");
        }

        @Test
        void 메뉴의_가격은_음수일_수_없다() {
            // given
            int price = -1000;
            Menu menu = MenuFixture.fixture().price(price).build();

            // when
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않는 메뉴 가격입니다.");
        }

        @Test
        void 메뉴_그룹은_DB에_존재해야한다() {
            // given
            Long menuGroupId = 1L;
            Menu menu = MenuFixture.fixture().menuGroupId(menuGroupId).build();

            given(menuGroupDao.existsById(menuGroupId))
                .willReturn(false);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴 그룹입니다.");
        }

        @Test
        void 메뉴_상품의_상품은_모두_DB에_존재해야한다() {
            // given
            List<MenuProduct> menuProducts = List.of(
                MenuProductFixture.fixture().productId(1L).build(),
                MenuProductFixture.fixture().productId(2L).build()
            );

            Long menuGroupId = 1L;
            Menu menu = MenuFixture.fixture().menuProducts(menuProducts).build();
            given(menuGroupDao.existsById(menuGroupId))
                .willReturn(true);

            given(productDao.findById(1L))
                .willReturn(Optional.of(ProductFixture.fixture().id(1L).build()));
            given(productDao.findById(2L))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
        }

        @Test
        void 메뉴_가격이_상품의_가격_총합보다_크면_예외() {
            // given
            int menuPrice = 13000;
            Long menuGroupId = 1L;

            Product product1 = ProductFixture.fixture().id(1L).price(2000).build();
            Product product2 = ProductFixture.fixture().id(2L).price(3000).build();
            List<MenuProduct> menuProducts = List.of(
                MenuProductFixture.fixture().productId(product1.getId()).quantity(3L).build(),
                MenuProductFixture.fixture().productId(product2.getId()).quantity(2L).build()
            );
            Menu menu = MenuFixture.fixture().price(menuPrice).menuGroupId(menuGroupId).menuProducts(menuProducts)
                .build();

            given(menuGroupDao.existsById(menuGroupId))
                .willReturn(true);
            given(productDao.findById(product1.getId()))
                .willReturn(Optional.of(product1));
            given(productDao.findById(product2.getId()))
                .willReturn(Optional.of(product2));

            // when
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 상품의 가격 총 합보다 작아야합니다.");
        }

        @Test
        void 이름_가격_메뉴그룹id_메뉴상품들을_입력받아_생성한다() {
            // given
            String name = "치킨 콤보 세트";
            int menuPrice = 12_000;
            Long menuGroupId = 1L;
            Product product1 = ProductFixture.fixture().id(1L).price(2000).build();
            Product product2 = ProductFixture.fixture().id(2L).price(3000).build();
            List<MenuProductFixture> menuProductFixtures = List.of(
                MenuProductFixture.fixture().productId(product1.getId()).quantity(3L),
                MenuProductFixture.fixture().productId(product2.getId()).quantity(2L)
            );
            List<MenuProduct> menuProducts = List.of(
                menuProductFixtures.get(0).build(),
                menuProductFixtures.get(1).build());
            MenuFixture menuFixture = MenuFixture.fixture().price(menuPrice).name(name).menuGroupId(menuGroupId)
                .menuProducts(menuProducts);
            Menu menu = menuFixture.build();
            Long menuId = 1L;
            Menu savedMenu = menuFixture.id(menuId).build();
            List<MenuProduct> savedMenuProducts = List.of(
                menuProductFixtures.get(0).menuId(menuId).build(),
                menuProductFixtures.get(1).menuId(menuId).build()
            );

            given(menuGroupDao.existsById(menuGroupId))
                .willReturn(true);
            given(productDao.findById(product1.getId()))
                .willReturn(Optional.of(product1));
            given(productDao.findById(product2.getId()))
                .willReturn(Optional.of(product2));
            given(menuDao.save(menu))
                .willReturn(savedMenu);
            given(menuProductDao.save(menuProducts.get(0)))
                .willReturn(savedMenuProducts.get(0));
            given(menuProductDao.save(menuProducts.get(1)))
                .willReturn(savedMenuProducts.get(1));

            // when
            Menu actual = menuService.create(menu);

            // then
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(menuFixture.id(menuId).menuProducts(savedMenuProducts).build());
        }
    }

    @Nested
    class 메뉴_전체_조회 {

        @Test
        void 전체_메뉴를_조회한다() {
            // given
            List<Menu> menus = List.of(
                MenuFixture.fixture().id(1L).build(),
                MenuFixture.fixture().id(2L).build()
            );
            List<List<MenuProduct>> menuProducts = List.of(
                List.of(
                    MenuProductFixture.fixture().seq(1L).menuId(1L).build(),
                    MenuProductFixture.fixture().seq(2L).menuId(1L).build()
                ),
                List.of(
                    MenuProductFixture.fixture().seq(3L).menuId(2L).build(),
                    MenuProductFixture.fixture().seq(4L).menuId(2L).build()
                )
            );

            given(menuDao.findAll())
                .willReturn(menus);
            given(menuProductDao.findAllByMenuId(menus.get(0).getId()))
                .willReturn(menuProducts.get(0));
            given(menuProductDao.findAllByMenuId(menus.get(1).getId()))
                .willReturn(menuProducts.get(1));

            // when
            List<Menu> actual = menuService.list();

            // then
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(
                    List.of(
                        MenuFixture.fixture().id(1L).menuProducts(menuProducts.get(0)).build(),
                        MenuFixture.fixture().id(2L).menuProducts(menuProducts.get(1)).build()
                    )
                );
        }
    }
}
