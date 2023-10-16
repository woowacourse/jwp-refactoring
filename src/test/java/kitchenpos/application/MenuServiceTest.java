package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
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
            final Product noodle = new Product("국수", BigDecimal.valueOf(6000));
            final Product potato = new Product("감자", BigDecimal.valueOf(3000));
            final MenuProduct wooDong = new MenuProduct(1L, 1);
            final MenuProduct frenchFries = new MenuProduct(2L, 1);
            final Menu expected = new Menu("우동세트", BigDecimal.valueOf(9000), 1L, List.of(wooDong, frenchFries));

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(1L)).willReturn(Optional.ofNullable(noodle));
            given(productDao.findById(2L)).willReturn(Optional.ofNullable(potato));

            final Menu spyExpected = spy(expected);
            given(menuDao.save(expected)).willReturn(spyExpected);
            final long menuId = 1L;
            given(spyExpected.getId()).willReturn(menuId);

            final MenuProduct menuProduct1 = new MenuProduct(1L, menuId, frenchFries.getProductId(), frenchFries.getQuantity());
            final MenuProduct menuProduct2 = new MenuProduct(2L, menuId, frenchFries.getProductId(), frenchFries.getQuantity());
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
            final MenuProduct wooDong = new MenuProduct(1L, 1);
            final MenuProduct frenchFries = new MenuProduct(2L, 1);

            final BigDecimal underZeroPrice = BigDecimal.valueOf(-1);
            final Menu expected = new Menu("우동세트", underZeroPrice, 1L, List.of(wooDong, frenchFries));

            // when, then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격이_null이면_생성할_수_없다() {
            // given
            final MenuProduct wooDong = new MenuProduct(1L, 1);
            final MenuProduct frenchFries = new MenuProduct(2L, 1);

            BigDecimal nullPrice = null;
            final Menu expected = new Menu("우동세트", nullPrice, 1L, List.of(wooDong, frenchFries));

            // when, then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_없으면_메뉴를_생성할_수_없다() {
            // given
            final MenuProduct wooDong = new MenuProduct(1L, 1);
            final MenuProduct frenchFries = new MenuProduct(2L, 1);

            final long noneExistedMenuGroupId = -1L;
            final Menu expected = new Menu("우동세트", BigDecimal.valueOf(9000), noneExistedMenuGroupId, List.of(wooDong, frenchFries));

            given(menuGroupDao.existsById(noneExistedMenuGroupId)).willReturn(false);

            // when, then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품이_저장되어_있지_않으면_메뉴를_생성할_수_없다() {
            // given
            long noneExistedMenuProductId = -1;
            final MenuProduct wooDong = new MenuProduct(noneExistedMenuProductId, 1);
            final MenuProduct frenchFries = new MenuProduct(2L, 1);
            final Menu expected = new Menu("우동세트", BigDecimal.valueOf(9000), 1L, List.of(wooDong, frenchFries));

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(noneExistedMenuProductId)).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격이_총_합산_가격보다_크면_메뉴를_만들_수_없다() {
            // given
            final Product noodle = new Product("국수", BigDecimal.valueOf(6000));
            final Product potato = new Product("감자", BigDecimal.valueOf(3000));
            final MenuProduct wooDong = new MenuProduct(1L, 1);
            final MenuProduct frenchFries = new MenuProduct(2L, 1);

            BigDecimal overSumOfProductPrice = noodle.getPrice()
                    .add(potato.getPrice())
                    .add(BigDecimal.valueOf(1000));
            final Menu expected = new Menu("우동세트", overSumOfProductPrice, 1L, List.of(wooDong, frenchFries));

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(1L)).willReturn(Optional.ofNullable(noodle));
            given(productDao.findById(2L)).willReturn(Optional.ofNullable(potato));

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
            final MenuProduct wooDong = new MenuProduct(1L, 1);
            final MenuProduct frenchFries = new MenuProduct(2L, 1);

            final Menu menu = new Menu("우동세트", BigDecimal.valueOf(9000), 1L, List.of(wooDong, frenchFries));
            menu.setId(1L);

            given(menuDao.findAll()).willReturn(List.of(menu));
            given(menuProductDao.findAllByMenuId(anyLong())).willReturn(List.of(wooDong, frenchFries));

            // when
            final List<Menu> actual = menuService.list();

            // then
            assertAll(
                    () -> assertThat(actual).containsExactly(menu),
                    () -> assertThat(actual.get(0).getMenuProducts()).containsExactly(wooDong, frenchFries)
            );
        }
    }
}
