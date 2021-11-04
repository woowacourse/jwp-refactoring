package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.MenuService;
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

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private List<Menu> standardMenuRegistry;
    private Menu standardMenu;
    private List<MenuProduct> standardMenuProducts;
    private MenuProduct standardMenuProduct;

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

    @BeforeEach
    void setUp() {
        standardMenuProduct = new MenuProduct();
        standardMenuProduct.setSeq(1L);
        standardMenuProduct.setMenuId(1L);
        standardMenuProduct.setProductId(1L);
        standardMenuProduct.setQuantity(1L);
        standardMenuProducts = new LinkedList<>();
        standardMenuProducts.add(standardMenuProduct);

        standardMenu = new Menu();
        standardMenu.setId(1L);
        standardMenu.setName("신상품");
        standardMenu.setPrice(BigDecimal.valueOf(1000.0d));
        standardMenu.setMenuGroupId(1L);
        standardMenu.setMenuProducts(standardMenuProducts);
        standardMenuRegistry = new LinkedList<>();
        standardMenuRegistry.add(standardMenu);
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void getMenu() {
        //given
        given(menuDao.findAll()).willReturn(standardMenuRegistry);
        given(menuProductDao.findAllByMenuId(1L)).willReturn(standardMenuProducts);

        //when
        List<Menu> menuRegistry = menuService.list();

        //then
        assertThat(menuRegistry.size()).isEqualTo(1);
    }

    @DisplayName("메뉴의 가격은 0 미만이 되선 안 된다.")
    @Test
    void createMenuWithMinusPrice() {
        //given
        standardMenu.setPrice(BigDecimal.valueOf(-1000.0d));

        //when

        //then
        assertThatThrownBy(() -> menuService.create(standardMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹의 번호가 메뉴 그룹에 존재해야만 한다.")
    @Test
    void createMenuWithNonExistedMenuGroupId() {
        //given
        Long supposeNonExistedMenuGroupId = standardMenu.getMenuGroupId();
        given(menuGroupDao.existsById(supposeNonExistedMenuGroupId)).willReturn(false);

        //when

        //then
        assertThatThrownBy(() -> menuService.create(standardMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 상품을 가져와선 안 된다.")
    @Test
    void createMenuWithNonExistedProductId() {
        //given
        Long supposeExistedMenuGroupId = standardMenu.getMenuGroupId();
        Long supposedNonExistedProductId = standardMenuProduct.getProductId();
        given(menuGroupDao.existsById(supposeExistedMenuGroupId)).willReturn(true);
        given(productDao.findById(supposedNonExistedProductId)).willThrow(
            IllegalArgumentException.class);

        //when

        //then
        assertThatThrownBy(() -> menuService.create(standardMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 상품보다 비싸선 안 된다.")
    @Test
    void createMenuWithMoreExpensivePrice() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(800.0d));

        Long supposeExistedMenuGroupId = standardMenu.getMenuGroupId();
        Long supposedExistedProductId = standardMenuProduct.getProductId();
        given(menuGroupDao.existsById(supposeExistedMenuGroupId)).willReturn(true);
        given(productDao.findById(supposedExistedProductId)).willReturn(Optional.of(product));

        //when

        //then
        assertThatThrownBy(() -> menuService.create(standardMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 추가한다.")
    @Test
    void createMenu() {
        //given
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(1200.0d));

        Long supposeExistedMenuGroupId = standardMenu.getMenuGroupId();
        Long supposedExistedProductId = standardMenuProduct.getProductId();
        given(menuGroupDao.existsById(supposeExistedMenuGroupId)).willReturn(true);
        given(productDao.findById(supposedExistedProductId)).willReturn(Optional.of(product));
        given(menuDao.save(standardMenu)).willReturn(standardMenu);
        given(menuProductDao.save(standardMenuProduct)).willReturn(standardMenuProduct);

        //when
        Menu menu = menuService.create(standardMenu);

        //then
        assertAll(
            () -> assertThat(menu.getId()).isEqualTo(1L),
            () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L),
            () -> assertThat(menu.getName()).isEqualTo("신상품"),
            () -> assertThat(menu.getMenuProducts().size()).isEqualTo(1),
            () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(1000.0d))
        );
    }

}