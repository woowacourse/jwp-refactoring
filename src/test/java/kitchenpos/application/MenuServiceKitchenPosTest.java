package kitchenpos.application;

import kitchenpos.KitchenPosTestFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceKitchenPosTest extends KitchenPosTestFixture {

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

    private MenuProduct menuProduct;
    private Product product;
    private Menu menu;

    @BeforeEach
    void setUp() {
        product = 상품을_저장한다(
                1L,
                "강정치킨",
                BigDecimal.valueOf(17000)
        );
        menuProduct = 메뉴_상품을_저장한다(
                1L,
                null,
                product.getId(),
                2L
        );
        menu = 메뉴를_저장한다(
                null,
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1L,
                Collections.singletonList(menuProduct)
        );
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

        Menu expected = 메뉴를_저장한다(1L, menu);
        given(menuDao.save(menu)).willReturn(expected);

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu).isEqualTo(expected);
        verify(menuGroupDao, times(1)).existsById(menu.getMenuGroupId());
        verify(productDao, times(1)).findById(menuProduct.getProductId());
        verify(menuDao, times(1)).save(menu);
    }

    @DisplayName("메뉴를 조회할 수 있다.")
    @Test
    void list() {
        // given
        Menu savedMenu = 메뉴를_저장한다(1L, menu);

        given(menuDao.findAll()).willReturn(Collections.singletonList(savedMenu));
        given(menuProductDao.findAllByMenuId(savedMenu.getId())).willReturn(Collections.singletonList(menuProduct));

        // when
        List<Menu> findMenus = menuService.list();

        // then
        assertThat(findMenus).isEqualTo(Collections.singletonList(savedMenu));
        verify(menuDao, times(1)).findAll();
        verify(menuProductDao, times(1)).findAllByMenuId(savedMenu.getId());
    }

    @DisplayName("메뉴 가격은 0원 이상이어야한다.")
    @Test
    void validateMenuNameLength() {
        // when
        menu.setPrice(BigDecimal.valueOf(-1));

        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 제품 양은 0 이상이어야한다.")
    @Test
    void validateMenuProduct() {
        // when
        menu.getMenuProducts().get(0).setQuantity(-1);

        // then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }
}