package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import org.junit.jupiter.api.BeforeEach;
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
    private MenuProduct menuProduct;
    private List<MenuProduct> menuProducts;
    private MenuGroup menuGroup;
    private Product product;
    private Menu menu;
    private List<Menu> menus;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup(1L, "한마리치킨");
        menu = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000.00), menuGroup);
        product = new Product(1L, "후라이드", BigDecimal.valueOf(16000.00));
        menuProduct = new MenuProduct(1L, menu, product, 1L);

        menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        menu.addMenuProducts(menuProducts);

        menus = new ArrayList<>();
        menus.add(menu);
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        given(menuGroupRepository.findById(anyLong()))
            .willReturn(Optional.of(menuGroup));
        given(menuProductDao.findById(anyLong()))
            .willReturn(Optional.of(menuProduct));
        given(productDao.findById(anyLong()))
            .willReturn(Optional.of(product));
        given(menuRepository.save(any(Menu.class)))
            .willReturn(menu);
        given(menuProductDao.save(any(MenuProduct.class)))
            .willReturn(menuProduct);

        MenuRequest menuRequest = new MenuRequest("후라이드치킨", 16000.00, menuGroup.getId(),
            Collections.singletonList(1L));

        menuService.create(menuRequest);

        verify(menuRepository).save(any(Menu.class));
    }


    @DisplayName("메뉴 불러오기")
    @Test
    void list() {
        Menu otherMenu = new Menu(2L, "양념치킨", BigDecimal.valueOf(17000.00), menuGroup);
        menus.add(otherMenu);

        given(menuRepository.findAll())
            .willReturn(menus);
        menuService.list();

        verify(menuRepository).findAll();
        verify(menuProductDao, times(2)).findAllByMenuId(anyLong());
    }
}
