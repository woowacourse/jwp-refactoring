package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
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
        Long menuGroupId = 1L;
        MenuGroup menuGroup = new MenuGroup(1L, "menuGroupName");

        String menuName = "menuName";
        Long menuPrice = 1000L;
        Menu expected = new Menu(menuName, menuPrice, menuGroup);

        long productId = 1L;
        Product product = new Product(productId, "productName", 1000L);

        long menuProductQuantity = 1;
        MenuProduct menuProduct = new MenuProduct(expected, product, menuProductQuantity);

        given(menuGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(menuGroup));
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));
        given(menuRepository.save(any(Menu.class)))
                .willReturn(expected);
        given(menuProductRepository.save(any(MenuProduct.class)))
                .willReturn(menuProduct);

        MenuProductRequest menuProductRequest = new MenuProductRequest(productId, menuProductQuantity);
        MenuRequest menuRequest = new MenuRequest(menuName, menuPrice.longValue(), menuGroupId, Arrays.asList(menuProductRequest));

        // when
        Menu actual = menuService.create(menuRequest);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("가격이 null이라면 예외가 발생한다.")
    void createFailWhenPriceIsNull() {
        // given
        MenuRequest menuRequest = new MenuRequest("name", null, 1L, mock(List.class));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 음수라면 예외가 발생한다.")
    void createFailWhenPriceIsNegativeNumber() {
        // given
        MenuRequest menuRequest = new MenuRequest("name", -1L, 1L, mock(List.class));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
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
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품을 찾을 수 없다면 예외가 발생한다.")
    void createFailWhenCannotFindProduct() {
        // given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
        MenuRequest menuRequest = new MenuRequest("name", 1000L, 1L, Arrays.asList(menuProductRequest));

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({"2000, 1000, 1", "5000, 1000, 3"})
    @DisplayName("메뉴의 가격이 상품 가격의 총합보다 크다면 예외가 발생한다.")
    void createFailWhenMenuPriceLowerThanAllProduct(Long menuPrice, Long productPrice, Long productQuantity) {
        // given
        String menuName = "name";
        MenuGroup menuGroup = new MenuGroup(1L, "menuGroupName");
        Menu expected = new Menu(menuName, menuPrice, menuGroup);

        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, productQuantity);
        MenuRequest menuRequest = new MenuRequest(menuName, menuPrice, 1L, Arrays.asList(menuProductRequest));

        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);
        given(menuRepository.save(any(Menu.class)))
                .willReturn(expected);

        Product product = new Product("name", productPrice);
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.of(product));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
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
