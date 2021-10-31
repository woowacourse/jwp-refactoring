package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
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

    private Product 후라이드_치킨;
    private Product 양념_치킨;
    private Product 간장_치킨;
    private Product 깐풍_치킨;
    private Product 마늘_치킨;
    private Product 모짜_치즈볼;
    private Product 크림_치즈볼;
    private Product 콜라_1_25L;
    private Product 맥주_500cc;
    private Product 치킨윙;

    private MenuProduct 후라이드_치킨_구성;
    private MenuProduct 양념_치킨_구성;
    private MenuProduct 간장_치킨_구성;
    private MenuProduct 깐풍_치킨_구성;
    private MenuProduct 마늘_치킨_구성;
    private MenuProduct 모짜_치즈볼_구성;
    private MenuProduct 크림_치즈볼_구성;
    private MenuProduct 콜라_1_25L_구성;
    private MenuProduct 맥주_500cc_구성;
    private MenuProduct 치킨윙_4pc_구성;
    private MenuProduct 치킨윙_12pc_구성;

    private MenuGroup 치킨류;
    private MenuGroup 세트류;
    private MenuGroup 사이드류;
    private MenuGroup 주류;

    @BeforeEach
    void setUp() {
        후라이드_치킨 = productConstructor(1L, "후라이드 치킨", new BigDecimal(15000));
        양념_치킨 = productConstructor(2L, "양념 치킨", new BigDecimal(16000));
        간장_치킨 = productConstructor(3L, "간장 치킨", new BigDecimal(17000));
        깐풍_치킨 = productConstructor(4L, "깐풍 치킨", new BigDecimal(20000));
        마늘_치킨 = productConstructor(5L, "마늘 치킨", new BigDecimal(18000));
        모짜_치즈볼 = productConstructor(6L, "모짜_치즈볼", new BigDecimal(1000));
        크림_치즈볼 = productConstructor(7L, "크림_치즈볼", new BigDecimal(1500));
        콜라_1_25L = productConstructor(8L, "콜라 1.25L", new BigDecimal(2500));
        맥주_500cc = productConstructor(9L, "맥주 500cc", new BigDecimal(4000));
        치킨윙 = productConstructor(10L, "치킨윙", new BigDecimal(1200));

        후라이드_치킨_구성 = menuProductConstructor(1L, null, 1L, 1);
        양념_치킨_구성 = menuProductConstructor(2L, null, 2L, 1);
        간장_치킨_구성 = menuProductConstructor(3L, null, 3L, 1);
        깐풍_치킨_구성 = menuProductConstructor(4L, null, 4L, 1);
        마늘_치킨_구성 = menuProductConstructor(5L, null, 5L, 1);
        모짜_치즈볼_구성 = menuProductConstructor(6L, null, 6L, 5);
        크림_치즈볼_구성 = menuProductConstructor(7L, null, 7L, 3);
        콜라_1_25L_구성 = menuProductConstructor(8L, null, 8L, 1);
        맥주_500cc_구성 = menuProductConstructor(9L, null, 9L, 1);
        치킨윙_4pc_구성 = menuProductConstructor(10L, null, 10L, 4);
        치킨윙_12pc_구성 = menuProductConstructor(11L, null, 10L, 12);

        치킨류 = menuGroupConstructor(1L, "치킨류");
        세트류 = menuGroupConstructor(2L, "세트류");
        사이드류 = menuGroupConstructor(3L, "사이드류");
        주류 = menuGroupConstructor(4L, "주류");
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        Menu menu = menuConstructor("단짠단짠 치킨 패밀리 세트", 23000, 1L, Arrays.asList(양념_치킨_구성, 치킨윙_4pc_구성, 모짜_치즈볼_구성));
        Menu expected = menuConstructor(1L, "단짠단짠 치킨 패밀리 세트", new BigDecimal(23000), 1L, Arrays.asList(
            menuProductSetMenuId(양념_치킨_구성, 1L),
            menuProductSetMenuId(치킨윙_4pc_구성, 1L),
            menuProductSetMenuId(모짜_치즈볼_구성, 1L)
        ));

        //when
        when(menuDao.save(menu)).thenAnswer(invocation -> {
            Menu argument = invocation.getArgument(0);
            MenuProduct menuProduct1 = argument.getMenuProducts().get(0);
            menuProduct1.setMenuId(1L);
            MenuProduct menuProduct2 = argument.getMenuProducts().get(1);
            menuProduct2.setMenuId(1L);
            MenuProduct menuProduct3 = argument.getMenuProducts().get(2);
            menuProduct3.setMenuId(1L);

            return menuConstructor(1L, argument.getName(), argument.getPrice(), argument.getMenuGroupId(),
                Arrays.asList(menuProduct1, menuProduct2, menuProduct3));
        });
        Menu actual = menuDao.save(menu);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("메뉴 구성가가 단품의 가격의 합보다 크면 예외가 발생한다.")
    @Test
    void createWhenExpensiveMenu() {
        Menu menu = menuConstructor(3L, "깐풍 치킨 세트", new BigDecimal(23000), 3L, Arrays.asList(
            menuProductSetMenuId(깐풍_치킨_구성, 3L),
            menuProductSetMenuId(콜라_1_25L_구성, 3L)
        ));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(깐풍_치킨.getId())).willReturn(Optional.of(깐풍_치킨));
        given(productDao.findById(콜라_1_25L.getId())).willReturn(Optional.of(콜라_1_25L));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 그룹을 적용한 메뉴를 등록할 때 예외가 발생한다.")
    @Test
    void createWhenInputNotExistMenuGroup() {
        //given
        Long menuGroupId = 5L;
        given(menuGroupDao.existsById(menuGroupId)).willReturn(false);
        Menu menu = menuConstructor(10L, "핫 후라이드 치킨", new BigDecimal(17000), menuGroupId, Collections.singletonList(후라이드_치킨_구성));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 상품을 적용한 메뉴를 등록할 때 예외가 발생한다.")
    @Test
    void createWhenInputNotExistProduct() {
        //given
        Long menuGroupId = 5L;
        given(menuGroupDao.existsById(menuGroupId)).willReturn(true);
        given(productDao.findById(후라이드_치킨.getId())).willReturn(Optional.empty());
        Menu menu = menuConstructor(10L, "핫 후라이드 치킨", new BigDecimal(17000), menuGroupId, Collections.singletonList(후라이드_치킨_구성));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 리스트를 조회할 수 있다.")
    @Test
    void readAll() {
        //given
        List<Menu> expected = Arrays.asList(
            menuConstructor(1L, "마늘 치킨", new BigDecimal(18000), 치킨류.getId(), Collections.singletonList(마늘_치킨_구성)),
            menuConstructor(2L, "단짠단짠 치킨 패밀리 세트", new BigDecimal(23000), 세트류.getId(), Arrays.asList(양념_치킨_구성, 치킨윙_4pc_구성, 모짜_치즈볼_구성)),
            menuConstructor(3L, "후라이드 & 양념 두 마리 치킨 세트", new BigDecimal(30000), 세트류.getId(), Arrays.asList(후라이드_치킨_구성, 양념_치킨_구성, 콜라_1_25L_구성)),
            menuConstructor(4L, "혼술 맥주 안주 세트", new BigDecimal(14000), 사이드류.getId(), Arrays.asList(모짜_치즈볼_구성, 크림_치즈볼_구성, 맥주_500cc_구성, 치킨윙_4pc_구성))
        );
        given(menuService.list()).willReturn(expected);

        //when
        List<Menu> actual = menuService.list();

        //then
        assertEquals(actual, expected);
    }

    private MenuProduct menuProductSetMenuId(final MenuProduct menuProduct, final Long menuId) {
        menuProduct.setMenuId(menuId);
        return menuProduct;
    }

    private Product productConstructor(final Long id, final String name, final BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    private Menu menuConstructor(final String name, final int price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return menuConstructor(null, name, new BigDecimal(price), menuGroupId, menuProducts);
    }

    private Menu menuConstructor(final Long id, final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    private MenuProduct menuProductConstructor(final Long seq, final Long menuId, final Long productId, final long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    private MenuGroup menuGroupConstructor(final Long id, final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);

        return menuGroup;
    }
}
