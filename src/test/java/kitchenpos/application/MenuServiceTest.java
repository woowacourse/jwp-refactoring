package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

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

    @Test
    @DisplayName("메뉴 생성")
    void create() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(1000));
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(10);
        menu.setMenuProducts(Collections.singletonList(menuProduct));
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1000));

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(product));
        given(menuDao.save(any(Menu.class))).willReturn(menu);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(menuProduct);

        Menu result = menuService.create(menu);

        assertThat(result).isNotNull();
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(1L),
            () -> assertThat(result.getMenuGroupId()).isEqualTo(1L),
            () -> assertThat(result.getMenuProducts()).hasSize(1)
        );
    }

    @Test
    @DisplayName("가격이 null이거나 0미만인 경우")
    void invalidPrice() {
        Menu menu = new Menu();
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);

        menu.setPrice(BigDecimal.valueOf(-10));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 속한 상품 금액의 합이 메뉴의 가격보다 작은 경우")
    void sumOfMenuProductPrice() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(1000));
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(10);
        menu.setMenuProducts(Collections.singletonList(menuProduct));
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(10));

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(product));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹에 존재하지 않는 경우")
    void doesNotExistInMenuGroup() {
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1000));

        given(menuGroupDao.existsById(any())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 상품이 존재하지 않는 경우")
    void productDoesNotExist() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuProducts(Collections.singletonList(new MenuProduct()));

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
