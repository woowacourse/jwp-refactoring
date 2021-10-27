package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@DisplayName("MenuService 단위 테스트")
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

    private Product 후라이드치킨정보;
    private Product 양념치킨정보;
    private MenuProduct 후라이드치킨;
    private MenuProduct 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨정보 = new Product(1L, "후라이드 치킨", 16000);
        양념치킨정보 = new Product(2L, "양념 치킨", 16000);
        후라이드치킨 = new MenuProduct(1L, 1);
        양념치킨 = new MenuProduct(2L, 1);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다")
    void create() {
        // given
        Menu 양념반_후라이드반 = new Menu("양념 반 + 후라이드 반", 30000, 1L, Arrays.asList(후라이드치킨, 양념치킨));
        Menu expected = new Menu(1L, "양념 반 + 후라이드 반", 30000, 1L, Arrays.asList(후라이드치킨, 양념치킨));
        given(menuGroupDao.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(true);
        given(productDao.findById(후라이드치킨.getProductId())).willReturn(Optional.of(후라이드치킨정보));
        given(productDao.findById(양념치킨.getProductId())).willReturn(Optional.of(양념치킨정보));
        given(menuDao.save(양념반_후라이드반)).willReturn(expected);
        MenuProduct expected_후라이드치킨 = new MenuProduct(1L, expected.getId(), 1L, 1);
        MenuProduct expected_양념치킨 = new MenuProduct(2L, expected.getId(), 2L, 1);
        given(menuProductDao.save(후라이드치킨)).willReturn(expected_후라이드치킨);
        given(menuProductDao.save(양념치킨)).willReturn(expected_양념치킨);

        // when
        Menu actual = menuService.create(양념반_후라이드반);

        // then
        assertEquals(expected, actual);
        assertEquals(2, actual.getMenuProducts().size());
    }

    @Test
    @DisplayName("메뉴의 가격이 null이면 메뉴를 등록할 수 없다.")
    void createWrongPriceNull() {
        // given
        Menu 양념반_후라이드반 = new Menu("양념 반 + 후라이드 반", 1L, Arrays.asList(후라이드치킨, 양념치킨));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    @DisplayName("메뉴의 가격이 음수면 메뉴를 등록할 수 없다.")
    void createWrongPriceUnderZero(int price) {
        // given
        Menu 양념반_후라이드반 = new Menu("양념 반 + 후라이드 반", price, 1L, Arrays.asList(후라이드치킨, 양념치킨));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴를 구성하는 실제 제품들을 단품으로 주문하였을 때의 가격 합보다 크면 메뉴를 등록할 수 없다.")
    void createWrongPriceSumOfProducts() {
        // given
        Menu 양념반_후라이드반 = new Menu("양념 반 + 후라이드 반", 32001, 1L, Arrays.asList(후라이드치킨, 양념치킨));
        given(menuGroupDao.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(true);
        given(productDao.findById(후라이드치킨.getProductId())).willReturn(Optional.of(후라이드치킨정보));
        given(productDao.findById(양념치킨.getProductId())).willReturn(Optional.of(양념치킨정보));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면 메뉴를 등록할 수 없다.")
    void createWrongMenuGroupNotExist() {
        // given
        Menu 양념반_후라이드반 = new Menu("양념 반 + 후라이드 반", 30000, 1L, Arrays.asList(후라이드치킨, 양념치킨));
        given(menuGroupDao.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(false);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("메뉴 그룹이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("목록에 포함된 데이터들이 존재하지 않으면 메뉴를 등록할 수 없다.")
    void createWrongProductNotExist() {
        // given
        Menu 양념반_후라이드반 = new Menu("양념 반 + 후라이드 반", 30000, 1L, Arrays.asList(후라이드치킨, 양념치킨));
        given(menuGroupDao.existsById(양념반_후라이드반.getMenuGroupId())).willReturn(true);
        given(productDao.findById(후라이드치킨.getProductId())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(양념반_후라이드반));
        assertEquals("상품이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("전체 메뉴를 조회할 수 있다")
    void list() {
        // given
        MenuProduct 간장치킨 = new MenuProduct(3L, 1);
        Menu 양념반_후라이드반 = new Menu(1L, "양념 반 + 후라이드 반", 30000, 1L, Arrays.asList(후라이드치킨, 양념치킨));
        Menu 간장반_후라이드반 = new Menu(2L, "간장 반 + 후라이드 반", 30000, 2L, Arrays.asList(후라이드치킨, 간장치킨));
        given(menuDao.findAll()).willReturn(Arrays.asList(양념반_후라이드반, 간장반_후라이드반));

        MenuProduct expected_후라이드치킨_menu1 = new MenuProduct(1L, 양념반_후라이드반.getId(), 1L, 1);
        MenuProduct expected_양념치킨 = new MenuProduct(2L, 양념반_후라이드반.getId(), 2L, 1);
        MenuProduct expected_후라이드치킨_menu2 = new MenuProduct(3L, 간장반_후라이드반.getId(), 1L, 1);
        MenuProduct expected_간장치킨 = new MenuProduct(4L, 간장반_후라이드반.getId(), 3L, 1);
        given(menuProductDao.findAllByMenuId(양념반_후라이드반.getId())).willReturn(Arrays.asList(expected_후라이드치킨_menu1, expected_양념치킨));
        given(menuProductDao.findAllByMenuId(간장반_후라이드반.getId())).willReturn(Arrays.asList(expected_후라이드치킨_menu2, expected_간장치킨));

        // when
        List<Menu> actual = menuService.list();

        // then
        assertEquals(2, actual.size());
        assertEquals(양념반_후라이드반, actual.get(0));
        assertEquals(간장반_후라이드반, actual.get(1));
        assertEquals(2, 양념반_후라이드반.getMenuProducts().size());
        assertEquals(2, 간장반_후라이드반.getMenuProducts().size());
    }
}
