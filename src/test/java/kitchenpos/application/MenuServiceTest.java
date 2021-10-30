package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.ServiceTest;
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
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ServiceTest
class MenuServiceTest {

    @Mock
    private MenuDao mockMenuDao;

    @Mock
    private MenuGroupDao mockMenuGroupDao;

    @Mock
    private MenuProductDao mockMenuProductDao;

    @Mock
    private ProductDao mockProductDao;

    @InjectMocks
    private MenuService menuService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = createMenu();
        when(mockMenuDao.save(any())).then(AdditionalAnswers.returnsFirstArg());
        when(mockMenuGroupDao.existsById(any())).thenReturn(true);
        when(mockProductDao.findById(any())).thenReturn(Optional.of(createProduct()));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        Menu savedMenu = menuService.create(menu);
        assertThat(savedMenu).isEqualTo(menu);
    }

    @DisplayName("메뉴 가격은 음수일 수 없다.")
    @Test
    void createWithInvalidPrice1() {
        menu.setPrice(BigDecimal.valueOf(-1));
        assertThatThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 상품 가격의 합보다 큰 가격으로 메뉴를 생성할 수 없다.")
    @Test
    void createWithInvalidPrice2() {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            Product product = mockProductDao.findById(menuProduct.getProductId()).get();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        menu.setPrice(sum.add(BigDecimal.ONE));
        assertThatThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하지 않는 메뉴 그룹에 메뉴를 생성할 수 없다.")
    @Test
    void createWithInvalidMenuGroup() {
        when(mockMenuGroupDao.existsById(any())).thenReturn(false);
        assertThatThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 목록을 출력한다.")
    @Test
    void list() {
        when(mockMenuDao.findAll()).thenReturn(Collections.singletonList(menu));
        when(mockMenuProductDao.findAllByMenuId(any())).thenReturn(menu.getMenuProducts());
        List<Menu> list = menuService.list();
        assertAll(
                () -> assertThat(list).hasSize(1),
                () -> assertThat(list).contains(menu)
        );
    }
}
