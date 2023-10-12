package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
            final MenuRequest request1 = new MenuRequest("productName", null, 1L, Collections.emptyList());
            assertThatThrownBy(() -> menuService.create(request1)).isInstanceOf(IllegalArgumentException.class);

            final MenuRequest request2 = new MenuRequest("productName", new BigDecimal("-1"), 1L, Collections.emptyList());
            assertThatThrownBy(() -> menuService.create(request2)).isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("menuProduct가 존재하지 않으면 예외가 발생한다.")
        void menuProductNotExist() {
            final MenuRequest request = new MenuRequest("productName", new BigDecimal("1000"), 999L, Collections.emptyList());

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("총 가격이 MenuProducts의 총 합계 금액보다 크면 예외가 발생한다.")
        void comparePriceAndSum() {
            // given
            final Product product = new Product(1L, "product", BigDecimal.TEN);

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(anyLong())).willReturn(Optional.of(product));

            final List<MenuProductRequest> menuProductRequests = List.of(
                    new MenuProductRequest(1L, 1),
                    new MenuProductRequest(2L, 2),
                    new MenuProductRequest(3L, 1)
            );
            final MenuRequest request = new MenuRequest("productName", new BigDecimal("1000"), 1L, menuProductRequests);

            // when, then
            assertThatThrownBy(() -> menuService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("정상적인 메뉴 등록을 진행한다.")
        void createMenu() {
            // given
            final Product product = new Product(1L, "product", BigDecimal.valueOf(10_000));
            final Menu menu = new Menu("menu", BigDecimal.TEN, 1L);
            final List<MenuProductRequest> menuProductRequests = List.of(
                    new MenuProductRequest(1L, 1),
                    new MenuProductRequest(2L, 2),
                    new MenuProductRequest(3L, 1)
            );
            final MenuRequest request = new MenuRequest("productName", new BigDecimal("35000"), 1L, menuProductRequests);

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(anyLong())).willReturn(Optional.of(product));
            given(menuDao.save(any())).willReturn(menu);

            // when
            final Menu savedMenu = menuService.create(request);

            // then
            assertAll(
                    () -> verify(menuDao, times(1)).save(any()),
                    () -> verify(menuProductDao, times(3)).save(any())
            );
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
