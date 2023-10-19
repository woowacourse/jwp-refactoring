package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Test
    @DisplayName("메뉴를 성공적으로 생성한다")
    void testCreateSuccess() {
        //given
        final Product product = new Product(1L, "testProduct", BigDecimal.valueOf(1000));
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        final MenuGroup menuGroup = new MenuGroup(1L, "testMenuGroup");
        final List<MenuProduct> menuProducts = List.of(menuProduct);
        final Menu menu = new Menu("testMenu", product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())),
                menuGroup.getId(), menuProducts);

        final List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                .map(it -> new MenuProductRequest(1L, 2))
                .collect(Collectors.toList());
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest("testMenu", product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())),
                menuGroup.getId(), menuProductRequests);

        when(menuGroupDao.existsById(any()))
                .thenReturn(true);
        when(productDao.findById(any()))
                .thenReturn(Optional.of(product));
        when(menuDao.save(any()))
                .thenReturn(new Menu(1L, menu.getName(), menu.getPrice(), menu.getMenuGroupId(), null));
        when(menuProductDao.save(any()))
                .thenReturn(menuProduct);

        //when
        final Menu result = menuService.create(menuCreateRequest);

        //then
        final Menu expected = new Menu(1L, "testMenu", product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())),
                menuGroup.getId(), List.of(menuProduct));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("메뉴 생성 시 가격이 null일 경우 예외가 발생한다")
    void testCreateWhenMenuPriceIsNullFailure() {
        //given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        final MenuGroup menuGroup = new MenuGroup(1L, "testMenuGroup");
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                .map(it -> new MenuProductRequest(1L, 2))
                .collect(Collectors.toList());
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest("testMenu", null,
                menuGroup.getId(), menuProductRequests);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 가격이 0보다 작을 경우 예외가 발생한다")
    void testCreateWhenMenuPriceIsLowerThanZeroFailure() {
        //given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        final MenuGroup menuGroup = new MenuGroup(1L, "testMenuGroup");
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                .map(it -> new MenuProductRequest(1L, 2))
                .collect(Collectors.toList());
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest("testMenu", BigDecimal.valueOf(-1),
                menuGroup.getId(), menuProductRequests);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 메뉴 그룹이 존재하지 않을 경우 예외가 발생한다")
    void testCreateWhenMenuGroupNotExistFailure() {
        //given
        final Product product = new Product(1L, "testProduct", BigDecimal.valueOf(1000));
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        final MenuGroup menuGroup = new MenuGroup(1L, "testMenuGroup");
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                .map(it -> new MenuProductRequest(1L, 2))
                .collect(Collectors.toList());
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest("testMenu", product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())),
                menuGroup.getId(), menuProductRequests);

        when(menuGroupDao.existsById(any()))
                .thenReturn(false);

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시 제품이 존재하지 않을 경우 예외가 발생한다")
    void testCreateWhenProductNotFoundFailure() {
        //given
        final Product product = new Product(1L, "testProduct", BigDecimal.valueOf(1000));
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        final MenuGroup menuGroup = new MenuGroup(1L, "testMenuGroup");
        final List<MenuProduct> menuProducts = List.of(menuProduct);

        final List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                .map(it -> new MenuProductRequest(1L, 2))
                .collect(Collectors.toList());
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest("testMenu", product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())),
                menuGroup.getId(), menuProductRequests);

        when(menuGroupDao.existsById(any()))
                .thenReturn(true);
        when(productDao.findById(any()))
                .thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {2001, 2002, 100000000})
    void testWhenPriceMoreThanProductSumFailure(final int price) {
        //given
        final Product product = new Product(1L, "testProduct", BigDecimal.valueOf(1000));
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        final MenuGroup menuGroup = new MenuGroup(1L, "testMenuGroup");
        final List<MenuProduct> menuProducts = List.of(menuProduct);


        final List<MenuProductRequest> menuProductRequests = menuProducts.stream()
                .map(it -> new MenuProductRequest(1L, 2))
                .collect(Collectors.toList());
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest("testMenu", BigDecimal.valueOf(price),
                menuGroup.getId(), menuProductRequests);

        when(menuGroupDao.existsById(any()))
                .thenReturn(true);
        when(productDao.findById(any()))
                .thenReturn(Optional.of(product));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("리스트를 성공적으로 조회한다")
    void testListSuccess() {
        //given
        final Menu menu1 = new Menu(1L, "testMenu1", BigDecimal.valueOf(2000),
                1L, null);
        final Menu menu2 = new Menu(2L, "testMenu2", BigDecimal.valueOf(2000),
                1L, null);
        final Menu menu3 = new Menu(3L, "testMenu3", BigDecimal.valueOf(2000),
                1L, null);

        final MenuProduct menuProduct1 = new MenuProduct(1L, 1L, 1L, 1L);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 2L, 2L, 2L);
        final MenuProduct menuProduct3 = new MenuProduct(3L, 3L, 3L, 3L);


        when(menuDao.findAll())
                .thenReturn(List.of(menu1, menu2, menu3));
        when(menuProductDao.findAllByMenuId(any()))
                .thenReturn(List.of(menuProduct1));
        when(menuProductDao.findAllByMenuId(any()))
                .thenReturn(List.of(menuProduct2));
        when(menuProductDao.findAllByMenuId(any()))
                .thenReturn(List.of(menuProduct3));

        //when
        final List<Menu> result = menuService.list();

        //then
        menu1.setMenuProducts(List.of(menuProduct1));
        menu2.setMenuProducts(List.of(menuProduct2));
        menu3.setMenuProducts(List.of(menuProduct3));
        assertThat(result).isEqualTo(List.of(menu1, menu2, menu3));
    }
}
