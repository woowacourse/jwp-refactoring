package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.annotation.MockTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@MockTest
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Nested
    class 메뉴_저장 {

        @Test
        void 요청된_메뉴의_가격_값이_null이면_예외를_발생한다() {
            // given
            Menu menu = new Menu();
            menu.setPrice(null);

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_메뉴의_가격_값이_0_미만이면_예외를_발생한다() {
            // given
            Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(-1));

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_메뉴의_메뉴_그룹이_저장되어_있지_않으면_예외를_발생한다() {
            // given
            Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(1000));
            menu.setMenuGroupId(10L);

            when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(false);

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_메뉴의_메뉴_상품이_저장되어_있지_않으면_예외를_발생한다() {
            // given
            Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(1000));
            menu.setMenuGroupId(10L);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(1L);
            menu.setMenuProducts(List.of(menuProduct));

            when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
            when(productDao.findById(menuProduct.getProductId())).thenReturn(Optional.empty());

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_메뉴의_가격이_메뉴_상품_가격의_총합보다_크면_예외를_발생한다() {
            // given
            Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(1000));
            menu.setMenuGroupId(10L);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(1L);
            menuProduct.setQuantity(1);
            menu.setMenuProducts(List.of(menuProduct));

            Product product = new Product();
            product.setPrice(BigDecimal.valueOf(1));

            when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
            when(productDao.findById(menuProduct.getProductId())).thenReturn(Optional.of(product));

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴를_저장하고_메뉴_상품에_메뉴_ID를_할당한다() {
            // given
            Menu menu = new Menu();
            menu.setId(1L);
            menu.setPrice(BigDecimal.valueOf(1000));
            menu.setMenuGroupId(10L);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(1L);
            menuProduct.setQuantity(100);
            menu.setMenuProducts(List.of(menuProduct));

            Product product = new Product();
            product.setPrice(BigDecimal.valueOf(1_000_000));

            when(menuGroupDao.existsById(menu.getMenuGroupId())).thenReturn(true);
            when(productDao.findById(menuProduct.getProductId())).thenReturn(Optional.of(product));

            when(menuDao.save(any(Menu.class))).thenReturn(menu);

            // when
            menuService.create(menu);

            // then
            assertThat(menuProduct.getMenuId()).isEqualTo(menu.getId());
        }
    }

    @Nested
    class 모든_메뉴_조회 {

        @Test
        void 모든_메뉴를_조회한다() {
            // when
            menuService.list();

            // then
            verify(menuDao).findAll();
        }
    }
}
