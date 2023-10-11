package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    MenuService menuService;
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @Nested
    class CreateTest {

        @Test
        @DisplayName("가격이 null 혹은 음수값이면 예외가 발생한다.")
        void priceNullOrNegative() {
            assertThatThrownBy(() -> menuService.create(new Menu(1L, "productName", null, 1L)))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> menuService.create(new Menu(1L, "productName", new BigDecimal("-1"), 1L)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 메뉴 그룹이면 예외가 발생한다.")
        void groupValidation() {
            assertThatThrownBy(() -> menuService.create(new Menu(1L, "productName", new BigDecimal("1000"), 9999L)))
                    .isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("menuProduct가 존재하지 않으면 예외가 발생한다.")
        void menuProductNotExist() {
            assertThatThrownBy(() -> menuService.create(new Menu(1L, "productName", new BigDecimal("1000"), 9999L)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("총 가격이 MenuProducts의 총 합계 금액보다 크면 예외가 발생한다.")
        void comparePriceAndSum() {
            final Product product = new Product(1L, "product", BigDecimal.TEN);
            final MenuProduct menuProduct1 = mock(MenuProduct.class);
            final MenuProduct menuProduct2 = mock(MenuProduct.class);
            final MenuProduct menuProduct3 = mock(MenuProduct.class);
            final List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2, menuProduct3);

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(anyLong())).willReturn(Optional.of(product));
            given(menuProduct1.getQuantity()).willReturn(1L);
            given(menuProduct2.getQuantity()).willReturn(2L);
            given(menuProduct3.getQuantity()).willReturn(1L);

            assertThatThrownBy(() -> menuService.create(new Menu(1L, "productName", new BigDecimal("1000"), 1L, menuProducts)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("정상적인 메뉴 등록을 진행한다.")
        void createMenu() {
            // given
            final Product product = new Product(1L, "product", BigDecimal.valueOf(10_000));
            final MenuProduct menuProduct1 = mock(MenuProduct.class);
            final MenuProduct menuProduct2 = mock(MenuProduct.class);
            final MenuProduct menuProduct3 = mock(MenuProduct.class);
            final Menu menu = mock(Menu.class);
            final List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2, menuProduct3);

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(anyLong())).willReturn(Optional.of(product));
            given(menuProduct1.getQuantity()).willReturn(1L);
            given(menuProduct2.getQuantity()).willReturn(2L);
            given(menuProduct3.getQuantity()).willReturn(1L);
            given(menuDao.save(any())).willReturn(menu);
            given(menuProductDao.save(menuProduct1)).willReturn(new MenuProduct(1L, 1L, 1L, 1L));
            given(menuProductDao.save(menuProduct2)).willReturn(new MenuProduct(1L, 1L, 2L, 2L));
            given(menuProductDao.save(menuProduct3)).willReturn(new MenuProduct(1L, 1L, 3L, 1L));

            // when
            final Menu savedMenu = menuService.create(new Menu(1L, "productName", new BigDecimal("35000"), 1L, menuProducts));

            // then
            assertThat(savedMenu).isEqualTo(menu);
        }
    }

    @Test
    void list() {
        // given
        final Menu menu = new Menu(1L, "productName", new BigDecimal("35000"), 1L);
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 3L, 1L);
        given(menuDao.findAll()).willReturn(List.of(menu));
        given(menuProductDao.findAllByMenuId(anyLong())).willReturn(List.of(menuProduct));

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menus.get(0)).isEqualTo(menu),
                () -> assertThat(menus.get(0).getMenuProducts()).hasSize(1),
                () -> assertThat(menus.get(0).getMenuProducts().get(0)).isEqualTo(menuProduct)
        );
    }
}
