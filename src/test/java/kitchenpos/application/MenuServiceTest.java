//package kitchenpos.application;
//
//import kitchenpos.dao.MenuDao;
//import kitchenpos.dao.MenuGroupDao;
//import kitchenpos.dao.MenuProductDao;
//import kitchenpos.dao.ProductDao;
//import kitchenpos.domain.Menu;
//import kitchenpos.domain.MenuProduct;
//import kitchenpos.domain.Product;
//import kitchenpos.fixture.MenuFixture;
//import kitchenpos.fixture.MenuProductFixture;
//import kitchenpos.fixture.ProductFixture;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.BDDMockito.given;
//
//
//@DisplayName("MenuService 테스트")
//@ExtendWith(MockitoExtension.class)
//public class MenuServiceTest {
//
//    @Mock
//    private MenuDao menuDao;
//    @Mock
//    private MenuGroupDao menuGroupDao;
//    @Mock
//    private MenuProductDao menuProductDao;
//    @Mock
//    private ProductDao productDao;
//
//    @InjectMocks
//    private MenuService menuService;
//
//    private final ProductFixture productFixture = new ProductFixture();
//    private final MenuProductFixture menuProductFixture = new MenuProductFixture();
//    private final MenuFixture menuFixture = new MenuFixture();
//
//    private Product 상품1;
//    private Product 상품2;
//    private MenuProduct 메뉴_상품1;
//    private MenuProduct 메뉴_상품2;
//    private List<MenuProduct> 메뉴_상품들;
//
//    @BeforeEach
//    void setup() {
//        상품1 = productFixture.상품_생성(1L, "상품1", BigDecimal.valueOf(1000));
//        상품2 = productFixture.상품_생성(2L, "상품2", BigDecimal.valueOf(2000));
//        메뉴_상품1 = menuProductFixture.메뉴_상품_생성(상품1.getId(), 1);
//        메뉴_상품2 = menuProductFixture.메뉴_상품_생성(상품2.getId(), 1);
//        메뉴_상품들 = menuProductFixture.메뉴_상품_리스트_생성(메뉴_상품1, 메뉴_상품2);
//    }
//
//    @Test
//    @DisplayName("메뉴 생성 테스트 - 성공")
//    public void createTest() throws Exception {
//        // given
//        Menu 메뉴1 = menuFixture.메뉴_생성("메뉴1", BigDecimal.valueOf(1000), 1L, 메뉴_상품들);
//        Menu expected = menuFixture.메뉴_생성(1L, "메뉴1", BigDecimal.valueOf(1000), 1L, 메뉴_상품들);
//        given(menuGroupDao.existsById(메뉴1.getMenuGroupId())).willReturn(true);
//        given(productDao.findById(메뉴_상품1.getProductId())).willReturn(Optional.of(상품1));
//        given(productDao.findById(메뉴_상품2.getProductId())).willReturn(Optional.of(상품2));
//        given(menuDao.save(메뉴1)).willReturn(expected);
//
//        MenuProduct expected_메뉴1_상품1 = menuProductFixture.메뉴_상품_생성(1L, expected.getId(), 1L, 1);
//        MenuProduct expected_메뉴1_상품2 = menuProductFixture.메뉴_상품_생성(2L, expected.getId(), 2L, 1);
//        given(menuProductDao.save(메뉴_상품1)).willReturn(expected_메뉴1_상품1);
//        given(menuProductDao.save(메뉴_상품2)).willReturn(expected_메뉴1_상품2);
//
//        // when
//        Menu actual = menuService.create(메뉴1);
//
//        // then
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @DisplayName("모든 메뉴 조회 테스트 - 성공")
//    public void listTest() throws Exception {
//        // given
//        Menu 메뉴1 = menuFixture.메뉴_생성(1L, "메뉴1", BigDecimal.valueOf(1000), 1L, 메뉴_상품들);
//        Menu 메뉴2 = menuFixture.메뉴_생성(2L, "메뉴2", BigDecimal.valueOf(1000), 1L, 메뉴_상품들);
//        given(menuDao.findAll()).willReturn(Arrays.asList(메뉴1, 메뉴2));
//
//        MenuProduct expected_메뉴1_상품1 = menuProductFixture.메뉴_상품_생성(1L, 메뉴1.getId(), 1L, 1);
//        MenuProduct expected_메뉴1_상품2 = menuProductFixture.메뉴_상품_생성(2L, 메뉴2.getId(), 2L, 1);
//        given(menuProductDao.findAllByMenuId(메뉴1.getId()))
//                .willReturn(Collections.singletonList(expected_메뉴1_상품1));
//        given(menuProductDao.findAllByMenuId(메뉴2.getId()))
//                .willReturn(Collections.singletonList(expected_메뉴1_상품2));
//
//        // when
//        List<Menu> actual = menuService.list();
//
//        // then
//        assertEquals(2, actual.size());
//        assertEquals(메뉴1, actual.get(0));
//    }
//}
