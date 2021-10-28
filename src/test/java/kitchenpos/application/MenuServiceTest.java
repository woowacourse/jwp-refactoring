package kitchenpos.application;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
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

    private Menu menu;

    private MenuProduct menuProduct;

    private Product product;

    @BeforeEach
    void setUp() {
        menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(1);

        menu = new Menu();
        menu.setId(1L);
        menu.setName("메뉴");
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuProducts(Arrays.asList(menuProduct));

        product = new Product();
        product.setPrice(BigDecimal.valueOf(1100));
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        // given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(product));
        given(menuDao.save(any())).willReturn(menu);
        given(menuProductDao.save(any())).willReturn(menuProduct);

        // when, then
        assertDoesNotThrow(() -> menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴의 가격이 null이면 예외를 발생시킨다.")
    void throwExceptionWhenNullPrice() {
        // given
        menu.setPrice(null);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴의 가격이 음수이면 예외를 발생시킨다.")
    void throwExceptionWhenNegativePrice() {
        // given
        menu.setPrice(BigDecimal.valueOf(-1000));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹에 속하는 경우 예외를 발생시킨다.")
    void throwExceptionWhenMenuGroupNotExists() {
        // given
        given(menuGroupDao.existsById(any())).willReturn(false);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @Test
    @DisplayName("메뉴의 가격이 각각의 메뉴 상품 가격의 총합보다 클 경우 예외를 발생시킨다.")
    void throwExceptionWhenPriceMoreExpensiveThanSum() {
        // given
        product.setPrice(BigDecimal.valueOf(900));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.of(product));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
    }

    @Test
    @DisplayName("모든 메뉴를 조회한다.")
    void findAllMenu() {
        // given
        given(menuDao.findAll()).willReturn(Collections.singletonList(menu));
        given(menuProductDao.findAllByMenuId(any())).willReturn(Collections.singletonList(menuProduct));

        // when, then
        assertDoesNotThrow(() -> menuService.list());
    }
}