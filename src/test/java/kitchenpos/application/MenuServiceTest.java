package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class MenuServiceTest extends MockServiceTest {

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

    private Menu firstMenu;
    private Product firstProduct;
    private Product secondProduct;
    private MenuProduct firstMenuProduct;
    private MenuProduct secondMenuProduct;

    @BeforeEach
    void setUp() {
        firstMenu = new Menu();
        firstMenu.setId(1L);
        firstMenu.setName("불고기 한 상 세트");
        firstMenu.setPrice(BigDecimal.valueOf(45000L));
        firstMenu.setMenuGroupId(1L);

        firstProduct = new Product();
        firstProduct.setId(1L);
        firstProduct.setName("pizza");
        firstProduct.setPrice(BigDecimal.valueOf(10000L));

        secondProduct = new Product();
        secondProduct.setId(2L);
        secondProduct.setName("chicken");
        secondProduct.setPrice(BigDecimal.valueOf(25000L));

        firstMenuProduct = new MenuProduct();
        firstMenuProduct.setSeq(1L);
        firstMenuProduct.setMenuId(firstMenu.getMenuGroupId());
        firstMenuProduct.setProductId(firstProduct.getId());
        firstMenuProduct.setQuantity(2L);

        secondMenuProduct = new MenuProduct();
        secondMenuProduct.setSeq(2L);
        secondMenuProduct.setMenuId(firstMenu.getMenuGroupId());
        secondMenuProduct.setProductId(secondProduct.getId());
        secondMenuProduct.setQuantity(1L);
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        List<Menu> expected = List.of(firstMenu);
        MenuProduct expectedFirstMenuProduct = firstMenuProduct;
        MenuProduct expectedSecondMenuProduct = secondMenuProduct;
        firstMenu.setMenuProducts(
                List.of(expectedFirstMenuProduct, expectedSecondMenuProduct)
        );

        Menu mockReturnMenu = new Menu();
        mockReturnMenu.setId(firstMenu.getId());
        mockReturnMenu.setName(firstMenu.getName());
        mockReturnMenu.setPrice(firstMenu.getPrice());
        mockReturnMenu.setMenuGroupId(firstMenu.getMenuGroupId());

        BDDMockito.given(menuDao.findAll())
                .willReturn(List.of(mockReturnMenu));

        MenuProduct mockFirstReturnMenuProduct = new MenuProduct();
        mockFirstReturnMenuProduct.setSeq(expectedFirstMenuProduct.getSeq());
        mockFirstReturnMenuProduct.setMenuId(expectedFirstMenuProduct.getMenuId());
        mockFirstReturnMenuProduct.setProductId(expectedFirstMenuProduct.getProductId());
        mockFirstReturnMenuProduct.setQuantity(expectedFirstMenuProduct.getQuantity());

        MenuProduct mockSecondReturnMenuProduct = new MenuProduct();
        mockSecondReturnMenuProduct.setSeq(expectedSecondMenuProduct.getSeq());
        mockSecondReturnMenuProduct.setMenuId(expectedSecondMenuProduct.getMenuId());
        mockSecondReturnMenuProduct.setProductId(expectedSecondMenuProduct.getProductId());
        mockSecondReturnMenuProduct.setQuantity(expectedSecondMenuProduct.getQuantity());

        BDDMockito.given(menuProductDao.findAllByMenuId(mockReturnMenu.getId()))
                .willReturn(List.of(
                                mockFirstReturnMenuProduct,
                                mockSecondReturnMenuProduct
                        )
                );

        // when
        List<Menu> actual = menuService.list();

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 메뉴를_추가한다() {
        // given
        Menu expected = firstMenu;
        expected.setMenuProducts(List.of(firstMenuProduct, secondMenuProduct));

        Menu menu = new Menu();
        menu.setName(firstMenu.getName());
        menu.setPrice(firstMenu.getPrice());
        menu.setMenuGroupId(firstMenu.getMenuGroupId());

        MenuProduct firstMenuProduct = new MenuProduct();
        firstMenuProduct.setProductId(this.firstMenuProduct.getProductId());
        firstMenuProduct.setQuantity(this.firstMenuProduct.getQuantity());

        MenuProduct secondMenuProduct = new MenuProduct();
        secondMenuProduct.setProductId(this.secondMenuProduct.getProductId());
        secondMenuProduct.setQuantity(this.secondMenuProduct.getQuantity());

        menu.setMenuProducts(List.of(firstMenuProduct, secondMenuProduct));

        BDDMockito.given(menuGroupDao.existsById(menu.getMenuGroupId()))
                .willReturn(true);

        BDDMockito.given(productDao.findById(firstMenuProduct.getProductId()))
                .willReturn(Optional.of(firstProduct));
        BDDMockito.given(productDao.findById(secondMenuProduct.getProductId()))
                .willReturn(Optional.of(secondProduct));

        Menu savedMenu = new Menu();
        savedMenu.setId(1L);
        savedMenu.setName(menu.getName());
        savedMenu.setPrice(menu.getPrice());
        savedMenu.setMenuGroupId(menu.getMenuGroupId());

        BDDMockito.given(menuDao.save(menu))
                .willReturn(savedMenu);

        BDDMockito.given(menuProductDao.save(firstMenuProduct))
                .willReturn(this.firstMenuProduct);
        BDDMockito.given(menuProductDao.save(secondMenuProduct))
                .willReturn(this.secondMenuProduct);

        // when
        Menu actual = menuService.create(menu);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 메뉴를_추가할_때_메뉴가격이_null_이면_예외를_던진다() {
        // given
        Menu menu = new Menu();
        menu.setPrice(null);

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        BDDMockito.then(menuGroupDao)
                .should(BDDMockito.times(0))
                .existsById(BDDMockito.anyLong());
    }

    @Test
    void 메뉴를_추가할_때_메뉴가격이_음수이면_예외를_던진다() {
        // given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1000L));

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        BDDMockito.then(menuGroupDao)
                .should(BDDMockito.times(0))
                .existsById(BDDMockito.anyLong());
    }

    @Test
    void 메뉴를_추가할_때_메뉴가격이_상품가격_곱하기_상품수량의_합보다_크면_예외를_던진다() {
        // given
        Menu menu = firstMenu;
        menu.setMenuProducts(List.of(firstMenuProduct, secondMenuProduct));
        BigDecimal firstMenuPrice = firstProduct.getPrice().multiply(BigDecimal.valueOf(firstMenuProduct.getQuantity()));
        BigDecimal secondMenuPrice = secondProduct.getPrice().multiply(BigDecimal.valueOf(secondMenuProduct.getQuantity()));

        BigDecimal moreMenuPrice = firstMenuPrice.add(secondMenuPrice).add(BigDecimal.valueOf(1000L));
        menu.setPrice(moreMenuPrice);

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        BDDMockito.then(menuDao)
                .should(BDDMockito.times(0))
                .save(menu);
    }

    @Test
    void 메뉴를_추가할_때_메뉴_안의_상품이_존재하지_않으면_예외를_던진다() {
        // given
        Menu menu = firstMenu;
        menu.setMenuProducts(List.of(firstMenuProduct, secondMenuProduct));
        BigDecimal firstMenuPrice = firstProduct.getPrice().multiply(BigDecimal.valueOf(firstMenuProduct.getQuantity()));
        BigDecimal secondMenuPrice = secondProduct.getPrice().multiply(BigDecimal.valueOf(secondMenuProduct.getQuantity()));

        BigDecimal moreMenuPrice = firstMenuPrice.add(secondMenuPrice).add(BigDecimal.valueOf(1000L));
        menu.setPrice(moreMenuPrice);

        BDDMockito.given(menuGroupDao.existsById(menu.getMenuGroupId()))
                .willReturn(true);
        BDDMockito.given(productDao.findById(firstMenuProduct.getProductId()))
                .willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        BDDMockito.then(productDao)
                .should(BDDMockito.times(1))
                .findById(BDDMockito.anyLong());
    }

    @Test
    void 메뉴를_추가할_때_메뉴그룹이_존재하지_않으면_예외를_던진다() {
        // given
        Menu menu = firstMenu;
        menu.setMenuProducts(List.of(firstMenuProduct, secondMenuProduct));
        BigDecimal firstMenuPrice = firstProduct.getPrice().multiply(BigDecimal.valueOf(firstMenuProduct.getQuantity()));
        BigDecimal secondMenuPrice = secondProduct.getPrice().multiply(BigDecimal.valueOf(secondMenuProduct.getQuantity()));

        BigDecimal moreMenuPrice = firstMenuPrice.add(secondMenuPrice).add(BigDecimal.valueOf(1000L));
        menu.setPrice(moreMenuPrice);

        BDDMockito.given(menuGroupDao.existsById(menu.getMenuGroupId()))
                .willReturn(false);

        // when, then
        Assertions.assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        BDDMockito.then(productDao)
                .should(BDDMockito.times(0))
                .findById(BDDMockito.anyLong());
    }
}
