package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.MenuFixture.createMenu;
import static kitchenpos.MenuFixture.createMenuProduct;
import static kitchenpos.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private MenuProductDao menuProductDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        Menu menu1 = createMenu();
        Menu menu2 = createMenu();
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu1, menu2));

        List<Menu> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(menu1, menu2)
        );
    }

    @DisplayName("메뉴 생성은")
    @Nested
    class Create {

        private Menu menu;
        private Product product;

        @BeforeEach
        void beforeAll() {
            menu = createMenu();
            product = createProduct();
        }

        @DisplayName("가격이 0원 미만일 경우 생성할 수 없다.")
        @Test
        void createExceptionIfPriceZero() {
            menu.setPrice(BigDecimal.valueOf(-1000));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 그룹에 속한 경우 생성할 수 없다.")
        @Test
        void createExceptionIfNotExistGroup() {
            when(menuGroupDao.existsById(any())).thenReturn(false);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 상품을 포함한 경우 생성할 수 없다.")
        @Test
        void createExceptionIfNotExistProduct() {
            menu.setMenuProducts(Arrays.asList(createMenuProduct(product), createMenuProduct(product)));
            when(menuGroupDao.existsById(any())).thenReturn(true);
            when(productDao.findById(any())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격이 메뉴 상품 가격의 총합보다 클 수 없다.")
        @Test
        void createExceptionIfExceedPrice() {
            product.setId(1L);
            product.setPrice(BigDecimal.ONE);
            menu.setPrice(BigDecimal.TEN);
            menu.setMenuProducts(Arrays.asList(createMenuProduct(product, 1), createMenuProduct(product, 2)));
            when(menuGroupDao.existsById(any())).thenReturn(true);
            when(productDao.findById(any())).thenReturn(Optional.of(product));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 생성할 수 있다.")
        @Test
        void create() {
            product.setId(1L);
            product.setPrice(BigDecimal.TEN);
            menu.setPrice(BigDecimal.ONE);
            menu.setMenuProducts(Arrays.asList(createMenuProduct(product, 1), createMenuProduct(product, 2)));
            when(menuGroupDao.existsById(any())).thenReturn(true);
            when(productDao.findById(any())).thenReturn(Optional.of(product));
            when(menuDao.save(any())).thenReturn(createMenu(1L));
            when(menuProductDao.save(any())).thenReturn(createMenuProduct(product, 1));

            assertDoesNotThrow(() -> menuService.create(menu));
        }
    }
}
