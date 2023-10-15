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
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
            // given
            final MenuRequest request1 = new MenuRequest("productName", null, 1L, Collections.emptyList());
            final MenuRequest request2 = new MenuRequest("productName", new BigDecimal("-1"), 1L, Collections.emptyList());

            // when, then
            assertSoftly(softly -> {
                assertThatThrownBy(() -> menuService.create(request1)).isInstanceOf(IllegalArgumentException.class);
                assertThatThrownBy(() -> menuService.create(request2)).isInstanceOf(IllegalArgumentException.class);
            });
        }

        @Test
        @DisplayName("menuProduct가 존재하지 않으면 예외가 발생한다.")
        void menuProductNotExist() {
            // given
            final MenuRequest request = mock(MenuRequest.class);
            given(request.getPrice()).willReturn(BigDecimal.valueOf(10000));
            given(request.getMenuGroupId()).willReturn(1L);
            given(menuGroupDao.existsById(anyLong())).willReturn(false);

            // when, then
            assertThatThrownBy(() -> menuService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("사용자가 요청한 가격이 서버에서 측정한 상품의 총 합계 가격보다 크다면 예외가 발생한다.")
        void comparePriceAndSum() {
            // given
            final List<MenuProductRequest> menuProductRequests = List.of(
                    new MenuProductRequest(1L, 1),
                    new MenuProductRequest(2L, 1),
                    new MenuProductRequest(3L, 1)
            );
            final Product product1 = new Product("product1", BigDecimal.valueOf(100));
            final Product product2 = new Product("product2", BigDecimal.valueOf(200));
            final Product product3 = new Product("product3", BigDecimal.valueOf(300));

            final MenuRequest request = mock(MenuRequest.class);
            given(request.getPrice()).willReturn(BigDecimal.valueOf(700));
            given(request.getMenuProducts()).willReturn(menuProductRequests);
            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(1L)).willReturn(Optional.of(product1));
            given(productDao.findById(2L)).willReturn(Optional.of(product2));
            given(productDao.findById(3L)).willReturn(Optional.of(product3));

            // when, then
            assertThatThrownBy(() -> menuService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴를 생성한다.")
        void createMenu() {
            // given
            final List<MenuProductRequest> menuProductRequests = List.of(
                    new MenuProductRequest(1L, 1),
                    new MenuProductRequest(2L, 1),
                    new MenuProductRequest(3L, 1)
            );
            final Product product1 = new Product("product1", BigDecimal.valueOf(100));
            final Product product2 = new Product("product2", BigDecimal.valueOf(200));
            final Product product3 = new Product("product3", BigDecimal.valueOf(300));
            final MenuRequest request = new MenuRequest("productName", BigDecimal.valueOf(600), 1L, menuProductRequests);

            final Menu menu = new Menu(1L, request.getName(), request.getPrice(), request.getMenuGroupId());

            given(menuGroupDao.existsById(anyLong())).willReturn(true);
            given(productDao.findById(1L)).willReturn(Optional.of(product1));
            given(productDao.findById(2L)).willReturn(Optional.of(product2));
            given(productDao.findById(3L)).willReturn(Optional.of(product3));
            given(menuDao.save(any())).willReturn(menu);

            final MenuProduct menuProduct1 = new MenuProduct(1L, 1L, 1L, 1);
            final MenuProduct menuProduct2 = new MenuProduct(2L, 1L, 2L, 1);
            final MenuProduct menuProduct3 = new MenuProduct(3L, 1L, 3L, 1);
            when(menuProductDao.save(any()))
                    .thenReturn(menuProduct1)
                    .thenReturn(menuProduct2)
                    .thenReturn(menuProduct3);

            // when
            final Menu savedMenu = menuService.create(request);

            // then
            assertSoftly(softly -> {
                verify(menuDao, times(1)).save(any());
                verify(menuProductDao, times(3)).save(any());
                assertThat(savedMenu).usingRecursiveComparison().isEqualTo(menu);
                assertThat(savedMenu).extracting("menuProducts")
                        .usingRecursiveComparison().isEqualTo(List.of(menuProduct1, menuProduct2, menuProduct3));
            });
        }
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        final Menu menu = new Menu(1L, "productName", new BigDecimal("35000"), 1L);
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 3L, 1L);
        given(menuDao.findAll()).willReturn(List.of(menu));
        given(menuProductDao.findAllByMenuId(anyLong())).willReturn(List.of(menuProduct));

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertSoftly(softly -> {
            assertThat(menus).hasSize(1);
            assertThat(menus.get(0)).usingRecursiveComparison().isEqualTo(menu);
            assertThat(menus.get(0)).extracting("menuProducts")
                    .usingRecursiveComparison().isEqualTo(List.of(menuProduct));
        });
    }
}
