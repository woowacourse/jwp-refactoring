package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

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

    private MenuGroup savedMenuGroup;
    private Menu savedMenu;
    private Product savedProduct;
    private MenuProduct savedMenuProduct;

    @BeforeEach
    void setUp() {
        savedMenuGroup = new MenuGroup(1L, "추천메뉴");

        savedMenu = new Menu();
        savedMenu.setId(1L);
        savedMenu.setName("후라이드");
        savedMenu.setPrice(BigDecimal.valueOf(19000));
        savedMenu.setMenuGroupId(savedMenuGroup.getId());

        savedProduct = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));

        savedMenuProduct = new MenuProduct();
        savedMenuProduct.setSeq(1L);
        savedMenuProduct.setMenuId(savedMenu.getId());
        savedMenuProduct.setProductId(savedProduct.getId());
        savedMenuProduct.setQuantity(2L);
    }

    @Test
    void 메뉴이름_가격_메뉴그룹ID_메뉴상품리스트를_받아서_메뉴_정보를_등록할_수_있다() {
        //given
        MenuProduct menuProductRequest = new MenuProduct();
        menuProductRequest.setProductId(1L);
        menuProductRequest.setQuantity(2L);

        Menu menuRequest = new Menu();
        menuRequest.setName("후라이드");
        menuRequest.setPrice(BigDecimal.valueOf(19000));
        menuRequest.setMenuGroupId(1L);
        menuRequest.setMenuProducts(List.of(menuProductRequest));

        given(menuGroupDao.existsById(savedMenuGroup.getId())).willReturn(true);
        given(productDao.findById(savedProduct.getId())).willReturn(Optional.of(savedProduct));
        given(menuDao.save(any(Menu.class))).willReturn(savedMenu);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(savedMenuProduct);
        
        //when
        Menu response = menuService.create(menuRequest);
        
        //then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo(savedMenu.getName());
        assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(19000));
        assertThat(response.getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
        assertThat(response.getMenuProducts().get(0).getMenuId()).isEqualTo(savedMenu.getId());
        assertThat(response.getMenuProducts().get(0).getProductId()).isEqualTo(savedProduct.getId());
    }

    @Test
    void 메뉴_가격이_입력되지_않으면_예외처리한다() {
        //given
        MenuProduct menuProductRequest = new MenuProduct();
        menuProductRequest.setProductId(1L);
        menuProductRequest.setQuantity(2L);

        Menu menuRequest = new Menu();
        menuRequest.setName("후라이드");
        menuRequest.setMenuGroupId(1L);
        menuRequest.setMenuProducts(List.of(menuProductRequest));

        //when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_0원_미만으로_입력되면_예외처리한다() {
        //given
        MenuProduct menuProductRequest = new MenuProduct();
        menuProductRequest.setProductId(1L);
        menuProductRequest.setQuantity(2L);

        Menu menuRequest = new Menu();
        menuRequest.setName("후라이드");
        menuRequest.setPrice(BigDecimal.valueOf(0));
        menuRequest.setMenuGroupId(1L);
        menuRequest.setMenuProducts(List.of(menuProductRequest));

        //when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹_ID가_존재하지_않는_것이면_예외처리한다() {
        //given
        MenuProduct menuProductRequest = new MenuProduct();
        menuProductRequest.setProductId(1L);
        menuProductRequest.setQuantity(2L);

        Menu menuRequest = new Menu();
        menuRequest.setName("후라이드");
        menuRequest.setPrice(BigDecimal.valueOf(19000));
        menuRequest.setMenuGroupId(-1L);
        menuRequest.setMenuProducts(List.of(menuProductRequest));

        given(menuGroupDao.existsById(anyLong())).willReturn(false);

        //when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 각_메뉴_상품들의_가격과_수량을_곱한_값의_합보다_메뉴_가격이_높으면_예외처리한다() {
        //given
        MenuProduct menuProductRequest = new MenuProduct();
        menuProductRequest.setProductId(1L);
        menuProductRequest.setQuantity(2L);

        Menu menuRequest = new Menu();
        menuRequest.setName("후라이드");
        menuRequest.setPrice(BigDecimal.valueOf(50000));
        menuRequest.setMenuGroupId(1L);
        menuRequest.setMenuProducts(List.of(menuProductRequest));

        given(menuGroupDao.existsById(savedMenuGroup.getId())).willReturn(true);
        given(productDao.findById(savedProduct.getId())).willReturn(Optional.of(savedProduct));

        //when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록된_전체_메뉴_정보를_조회할_수_있다() {
        //given
        given(menuDao.findAll()).willReturn(List.of(savedMenu));
        given(menuProductDao.findAllByMenuId(savedMenu.getId())).willReturn(List.of(savedMenuProduct));

        //when
        List<Menu> result = menuService.list();

        //then
        assertThat(result).hasSize(1);
    }
}
