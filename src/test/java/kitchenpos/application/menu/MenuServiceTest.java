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

    private static final Double BASIC_PRICE = 1000.0d;
    private static final Double CHEAPER_PRICE = 800.0d;
    private static final Double MINUS_PRICE = -100.0d;
    private static final Integer BASIC_SIZE = 1;
    private static final Long BASIC_MENU_ID = 1L;
    private static final Long BASIC_MENU_GROUP_ID = 1L;
    private static final Long BASIC_PRODUCT_ID = 1L;
    private static final Long BASIC_QUANTITY = 1L;
    private static final Long BASIC_SEQUENCE_NUMBER = 1L;
    private static final String BASIC_MENU_NAME = "신상품 메뉴";
    private static final String BASIC_PRODUCT_NAME = "신상품";

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
        standardMenuProduct.setSeq(BASIC_SEQUENCE_NUMBER);
        standardMenuProduct.setMenuId(BASIC_MENU_ID);
        standardMenuProduct.setProductId(BASIC_PRODUCT_ID);
        standardMenuProduct.setQuantity(BASIC_QUANTITY);
        standardMenuProducts = new LinkedList<>();
        standardMenuProducts.add(standardMenuProduct);

        standardMenu = new Menu();
        standardMenu.setId(BASIC_MENU_ID);
        standardMenu.setName(BASIC_MENU_NAME);
        standardMenu.setPrice(BigDecimal.valueOf(BASIC_PRICE));
        standardMenu.setMenuGroupId(BASIC_MENU_GROUP_ID);
        standardMenu.setMenuProducts(standardMenuProducts);
        standardMenuRegistry = new LinkedList<>();
        standardMenuRegistry.add(standardMenu);
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void getMenu() {
        //given
        given(menuDao.findAll()).willReturn(standardMenuRegistry);
        given(menuProductDao.findAllByMenuId(BASIC_MENU_ID)).willReturn(standardMenuProducts);

        //when
        List<Menu> menuRegistry = menuService.list();

        //then
        assertThat(menuRegistry.size()).isEqualTo(1);
    }

    @DisplayName("메뉴의 가격은 0 미만이 되선 안 된다.")
    @Test
    void createMenuWithMinusPrice() {
        //given
        standardMenu.setPrice(BigDecimal.valueOf(MINUS_PRICE));

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
        //given
        Product product = new Product();
        product.setId(BASIC_PRODUCT_ID);
        product.setName(BASIC_PRODUCT_NAME);
        product.setPrice(BigDecimal.valueOf(CHEAPER_PRICE));

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
        product.setId(BASIC_PRODUCT_ID);
        product.setName(BASIC_PRODUCT_NAME);
        product.setPrice(BigDecimal.valueOf(BASIC_PRICE));

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
            () -> assertThat(menu.getId()).isEqualTo(BASIC_MENU_ID),
            () -> assertThat(menu.getMenuGroupId()).isEqualTo(BASIC_MENU_GROUP_ID),
            () -> assertThat(menu.getName()).isEqualTo(BASIC_MENU_NAME),
            () -> assertThat(menu.getMenuProducts().size()).isEqualTo(BASIC_SIZE),
            () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(BASIC_PRICE))
        );
    }

}