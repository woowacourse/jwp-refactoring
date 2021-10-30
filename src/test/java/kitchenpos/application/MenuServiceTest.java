package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.menu.MenuPriceOverThanProductsException;
import kitchenpos.exception.menu.NoSuchMenuGroupException;
import kitchenpos.exception.product.NoSuchProductException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
class MenuServiceTest {
    @MockBean
    private MenuRepository menuRepository;

    @MockBean
    private MenuGroupRepository menuGroupRepository;

    @MockBean
    private MenuProductRepository menuProductRepository;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    MenuService menuService;

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void create() {
        // given
        long productId = 1L;
        long menuProductQuantity = 1;
        MenuProductRequest menuProductRequest = new MenuProductRequest(productId, menuProductQuantity);

        String menuName = "menuName";
        Long menuPrice = 1000L;
        Long menuGroupId = 1L;
        MenuRequest menuRequest = new MenuRequest(menuName, menuPrice, menuGroupId, Arrays.asList(menuProductRequest));

        Product product = new Product(productId, "productName", 1000L);

        MenuGroup menuGroup = new MenuGroup(menuGroupId, "menuGroupName");

        MenuProduct menuProduct = new MenuProduct(product, menuProductQuantity);
        Menu savedMenu = new Menu(menuName, menuPrice, menuGroup, Arrays.asList(menuProduct));

        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(menuGroup));
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));
        given(menuRepository.save(any(Menu.class)))
                .willReturn(savedMenu);
        given(menuProductRepository.save(any(MenuProduct.class)))
                .willReturn(menuProduct);

        // when
        MenuResponse expected = MenuResponse.from(savedMenu);
        MenuResponse actual = menuService.create(menuRequest);

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("메뉴 그룹을 찾을 수 없다면 예외가 발생한다.")
    void createFailWhenCannotFindMenuGroup() {
        // given
        MenuRequest menuRequest = new MenuRequest("name", 1000L, 1L, mock(List.class));

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NoSuchMenuGroupException.class);
    }

    @Test
    @DisplayName("상품을 찾을 수 없다면 예외가 발생한다.")
    void createFailWhenCannotFindProduct() {
        // given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
        MenuRequest menuRequest = new MenuRequest("name", 1000L, 1L, Arrays.asList(menuProductRequest));

        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(mock(MenuGroup.class)));
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(NoSuchProductException.class);
    }

    @ParameterizedTest
    @CsvSource({"2000, 1000, 1", "5000, 1000, 3"})
    @DisplayName("메뉴의 가격이 메뉴 상품 가격의 총합보다 크다면 예외가 발생한다.")
    void createFailWhenMenuPriceLowerThanAllProduct(Long menuPrice, Long productPrice, Long productQuantity) {
        // given
        String menuName = "name";

        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, productQuantity);
        MenuRequest menuRequest = new MenuRequest(menuName, menuPrice, 1L, Arrays.asList(menuProductRequest));

        Product product = new Product("productName", productPrice);
        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(mock(MenuGroup.class)));
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(MenuPriceOverThanProductsException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 불러올 수 있다.")
    void list() {
        //given
        Product product = new Product("product1", 10000L);

        MenuProduct menuProduct1 = new MenuProduct(product, 1L);
        MenuProduct menuProduct2 = new MenuProduct(product, 2L);

        MenuGroup menuGroup = new MenuGroup("menuGroup");

        Menu menu1 = new Menu("menuName", 10000L, menuGroup, Arrays.asList(menuProduct1));
        Menu menu2 = new Menu("menuName", 20000L, menuGroup, Arrays.asList(menuProduct2));

        List<Menu> savedMenu = Arrays.asList(menu1, menu2);
        given(menuRepository.findAll())
                .willReturn(savedMenu);

        List<MenuResponse> expected = savedMenu.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
