package kitchenpos.application;

import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

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
        Menu menu = new Menu();
        BigDecimal price = BigDecimal.valueOf(1000);
        menu.setPrice(price);
        menu.setMenuGroupId(1L);

        Product product = new Product();
        long productId = 1L;
        product.setId(productId);
        product.setPrice(BigDecimal.valueOf(1000));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1);

        menu.setMenuProducts(Arrays.asList(menuProduct));

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));
        given(menuRepository.save(any(Menu.class)))
                .willReturn(menu);
        given(menuProductRepository.save(any(MenuProduct.class)))
                .willReturn(menuProduct);


        // when
        Menu actual = menuService.create(menu);

        // then
        assertThat(actual).isEqualTo(menu);
    }

    @Test
    @DisplayName("가격이 null이라면 예외가 발생한다.")
    void createFailWhenPriceIsNull() {
        // given
        Menu menu = new Menu();

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 음수라면 예외가 발생한다.")
    void createFailWhenPriceIsNegativeNumber() {
        // given
        Menu menu = new Menu();
        BigDecimal price = BigDecimal.valueOf(-1);
        menu.setPrice(price);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹을 찾을 수 없다면 예외가 발생한다.")
    void createFailWhenCannotFindMenuGroup() {
        // given
        Menu menu = new Menu();
        BigDecimal price = BigDecimal.valueOf(1000L);
        menu.setPrice(price);
        menu.setMenuGroupId(1L);

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품을 찾을 수 없다면 예외가 발생한다.")
    void createFailWhenCannotFindProduct() {
        // given
        Menu menu = new Menu();
        BigDecimal price = BigDecimal.valueOf(1000);
        menu.setPrice(price);
        menu.setMenuGroupId(1L);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);

        menu.setMenuProducts(Arrays.asList(menuProduct));

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({"2000, 1000, 1", "5000, 1000, 3"})
    @DisplayName("메뉴의 가격이 상품 가격의 총합보다 크다면 예외가 발생한다.")
    void createFailWhenMenuPriceLowerThanAllProduct(int menuPrice, int productPrice, int productQuantity) {
        // given
        Menu menu = new Menu();
        BigDecimal price = BigDecimal.valueOf(menuPrice);
        menu.setPrice(price);
        menu.setMenuGroupId(1L);

        Product product = new Product();
        long productId = 1L;
        product.setId(productId);
        product.setPrice(BigDecimal.valueOf(productPrice));

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(productQuantity);

        menu.setMenuProducts(Arrays.asList(menuProduct));

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 불러올 수 있다.")
    void list() {
        //given
        Menu menu1 = new Menu();
        Menu menu2 = new Menu();

        List<Menu> expected = Arrays.asList(menu1, menu2);
        given(menuRepository.findAll())
                .willReturn(expected);

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
