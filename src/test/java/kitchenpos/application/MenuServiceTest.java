package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("떡볶이");
        product1.setPrice(new BigDecimal(3500));
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("순대");
        product2.setPrice(new BigDecimal(4000));
        Product product3 = new Product();
        product3.setId(3L);
        product3.setName("튀김");
        product3.setPrice(new BigDecimal(4000));

        given(productDao.findById(1L))
                .willReturn(Optional.of(product1));
        given(productDao.findById(2L))
                .willReturn(Optional.of(product2));
        given(productDao.findById(3L))
                .willReturn(Optional.of(product3));

        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setSeq(1L);
        menuProduct1.setMenuId(1L);
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1L);
        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct1.setSeq(1L);
        menuProduct1.setMenuId(1L);
        menuProduct2.setProductId(2L);
        menuProduct2.setQuantity(1L);
        MenuProduct menuProduct3 = new MenuProduct();
        menuProduct1.setSeq(1L);
        menuProduct1.setMenuId(1L);
        menuProduct3.setProductId(3L);
        menuProduct3.setQuantity(1L);

        given(menuProductDao.save(menuProduct1))
                .willReturn(menuProduct1);
        given(menuProductDao.save(menuProduct2))
                .willReturn(menuProduct2);
        given(menuProductDao.save(menuProduct3))
                .willReturn(menuProduct3);

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("떡순튀");
        menu.setPrice(new BigDecimal(11000));
        menu.setMenuProducts(List.of(menuProduct1, menuProduct2, menuProduct3));

        given(menuGroupDao.existsById(menu.getMenuGroupId()))
                .willReturn(true);
        given(menuDao.save(menu))
                .willReturn(menu);
        //when
        Menu actual = menuService.create(menu);
        //then
        assertThat(actual).isEqualTo(menu);

        verify(menuGroupDao, times(1)).existsById(menu.getMenuGroupId());
        verify(productDao, times(3)).findById(anyLong());
        verify(menuDao, times(1)).save(menu);
        verify(menuProductDao, times(3)).save(any());
    }

    @DisplayName("메뉴 등록 실패 - 올바르지 않은 가격")
    @Test
    void createFailInvalidPrice() {
        //given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(-1000));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 null 또는 음수가 될 수 없습니다.");
    }

    @DisplayName("메뉴 등록 실패 - 메뉴 그룹이 존재하지 않을 경우")
    @Test
    void createFailNotExistMenuGroup() {
        //given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1000));

        given(menuGroupDao.existsById(menu.getMenuGroupId()))
                .willReturn(false);
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @DisplayName("메뉴 등록 실패 - 상품이 존재하지 않을 경우")
    @Test
    void createFailNotExistProduct() {
        //given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(1000));
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menu.setMenuProducts(List.of(menuProduct));

        given(menuGroupDao.existsById(menu.getMenuGroupId()))
                .willReturn(true);
        given(productDao.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품 ID 입니다.");
    }

    @DisplayName("메뉴 등록 실패 - 단일 상품가격의 합보다 메뉴의 가격이 클 경우")
    @Test
    void createFailInvalidMenuPrice() {
        //given
        Menu menu = new Menu();
        menu.setPrice(new BigDecimal(10000));
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1L);
        menu.setMenuProducts(List.of(menuProduct));
        Product product = new Product();
        product.setPrice(new BigDecimal(9999));

        given(menuGroupDao.existsById(menu.getMenuGroupId()))
                .willReturn(true);
        given(productDao.findById(anyLong()))
                .willReturn(Optional.of(product));
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 단일 상품의 가격 합보다 작아야합니다.");
    }

    @DisplayName("메뉴 목록을 불러온다.")
    @Test
    void list() {
        //given
        Menu menu1 = new Menu();
        Menu menu2 = new Menu();
        List<Menu> expected = List.of(menu1, menu2);
        given(menuDao.findAll())
                .willReturn(expected);
        //when
        List<Menu> actual = menuService.list();
        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(actual).hasSize(2);

        verify(menuDao, times(1)).findAll();
    }
}