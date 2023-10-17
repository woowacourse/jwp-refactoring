package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.application.fixture.MenuGroupFixture.western;
import static kitchenpos.application.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.application.fixture.ProductFixture.noodle;
import static kitchenpos.application.fixture.ProductFixture.potato;
import static kitchenpos.application.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    ProductDao productDao;

    @Nested
    class Create {

        @Test
        void 메뉴를_생성한다() {
            // given
            final Product noodle = noodle();
            final Product potato = potato();
            final MenuProduct wooDong = menuProduct(noodle, 1);
            final MenuProduct frenchFries = menuProduct(potato, 1);
            final Menu expected = new Menu("우동세트", BigDecimal.valueOf(9000), western(), List.of(wooDong, frenchFries));

            given(menuGroupDao.existsById(any())).willReturn(true);
            given(productDao.findById(any()))
                    .willReturn(Optional.ofNullable(noodle))
                    .willReturn(Optional.ofNullable(potato));

            final Menu spyExpected = spy(new Menu(expected.getName(), expected.getPrice(), expected.getMenuGroup(), new ArrayList<>()));
            given(menuDao.save(expected)).willReturn(spyExpected);

            final MenuProduct menuProduct1 = new MenuProduct(1L, expected, wooDong.getProduct(), frenchFries.getQuantity());
            final MenuProduct menuProduct2 = new MenuProduct(2L, expected, frenchFries.getProduct(), frenchFries.getQuantity());
            given(menuProductDao.save(any(MenuProduct.class)))
                    .willReturn(menuProduct1)
                    .willReturn(menuProduct2);

            // when
            final Menu actual = menuService.create(expected);

            // then
            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(spyExpected);
        }

        @Test
        void 메뉴_가격이_0보다_작으면_생성할_수_없다() {
            // given
            final MenuProduct wooDong = menuProduct(noodle(), 1);
            final MenuProduct frenchFries = menuProduct(potato(), 1);

            final BigDecimal underZeroPrice = BigDecimal.valueOf(-1);
            final Menu expected = new Menu("우동세트", underZeroPrice, western(), List.of(wooDong, frenchFries));

            // when, then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격이_null이면_생성할_수_없다() {
            // given
            final MenuProduct wooDong = menuProduct(noodle(), 1);
            final MenuProduct frenchFries = menuProduct(potato(), 1);

            BigDecimal nullPrice = null;
            final Menu expected = new Menu("우동세트", nullPrice, western(), List.of(wooDong, frenchFries));

            // when, then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_없으면_메뉴를_생성할_수_없다() {
            // given
            final MenuProduct wooDong = menuProduct(noodle(), 1);
            final MenuProduct frenchFries = menuProduct(potato(), 1);

            final MenuGroup noneExistedMenuGroup = menuGroup("noneExistedMenuGroupId");
            final Menu expected = new Menu("우동세트", BigDecimal.valueOf(9000), noneExistedMenuGroup, List.of(wooDong, frenchFries));

            given(menuGroupDao.existsById(any())).willReturn(false);

            // when, then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품이_저장되어_있지_않으면_메뉴를_생성할_수_없다() {
            // given
            final Product noneExistedProduct = product("noneExistedProduct", BigDecimal.valueOf(-1));
            final MenuProduct wooDong = menuProduct(noneExistedProduct, 1);
            final MenuProduct frenchFries = menuProduct(potato(), 1);
            final Menu expected = new Menu("우동세트", BigDecimal.valueOf(9000), western(), List.of(wooDong, frenchFries));

            given(menuGroupDao.existsById(any())).willReturn(true);
            given(productDao.findById(noneExistedProduct.getId())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격이_총_합산_가격보다_크면_메뉴를_만들_수_없다() {
            // given
            final Product noodle = noodle();
            final MenuProduct wooDong = menuProduct(noodle, 1);
            final Product potato = potato();
            final MenuProduct frenchFries = menuProduct(potato, 1);

            BigDecimal overSumOfProductPrice = noodle.getPrice()
                    .add(potato.getPrice())
                    .add(BigDecimal.valueOf(1000));
            final Menu expected = new Menu("우동세트", overSumOfProductPrice, western(), List.of(wooDong, frenchFries));

            given(menuGroupDao.existsById(any())).willReturn(true);
            given(productDao.findById(any()))
                    .willReturn(Optional.ofNullable(noodle))
                    .willReturn(Optional.ofNullable(potato));

            // when, then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FindAll {

        @Test
        void 메뉴를_전체_조회할_수_있다() {
            // given
            final MenuProduct wooDong = menuProduct(noodle(), 1);
            final MenuProduct frenchFries = menuProduct(potato(), 1);

            final Menu expected = new Menu("우동세트", BigDecimal.valueOf(9000), western(), new ArrayList<>());
            final Menu spyExpected = spy(expected);

            given(menuDao.findAll()).willReturn(List.of(spyExpected));
            given(spyExpected.getId()).willReturn(1L);

            given(menuProductDao.findAllByMenuId(anyLong())).willReturn(List.of(wooDong, frenchFries));

            // when
            final List<Menu> actual = menuService.list();

            // then
            assertThat(actual)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(spyExpected);
        }
    }
}
