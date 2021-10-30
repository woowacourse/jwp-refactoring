package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTestWithProfiles
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    private Product product;
    private MenuGroup menuGroup;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        product = productService.create(new Product("product", BigDecimal.valueOf(1000)));
        menuGroup = menuGroupService.create(new MenuGroup("menuGroup"));
    }


    @Test
    @DisplayName("메뉴 정상 등록 :: 메뉴 품목의 합과 동일")
    void createPriceEqualToSumOfMenuProduct() {
        menuProduct = new MenuProduct(product.getId(), 3);

        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));

        Menu input = new Menu("menu", sumOfMenuProduct, menuGroup.getId(),
                Collections.singletonList(menuProduct));

        Menu saved = menuService.create(input);

        assertNotNull(saved.getId());
        assertThat(saved.getPrice()).isEqualByComparingTo(sumOfMenuProduct);
    }

    @Test
    @DisplayName("메뉴 정상 등록 :: 메뉴 품목의 합에 일부 할인")
    void createPriceWithDiscount() {
        menuProduct = new MenuProduct(product.getId(), 3);

        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
        BigDecimal price = sumOfMenuProduct.subtract(BigDecimal.valueOf(1000));

        Menu input = new Menu("menu", price, menuGroup.getId(),
                Collections.singletonList(menuProduct));

        Menu saved = menuService.create(input);

        assertNotNull(saved.getId());
        assertThat(saved.getPrice()).isEqualByComparingTo(price);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 메뉴 가격 null")
    void createWithNullPrice() {
        menuProduct = new MenuProduct(product.getId(), 3);

        Menu input = new Menu("menu", null, menuGroup.getId(),
                Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 메뉴 가격 음수")
    void createWithNegativePrice() {
        menuProduct = new MenuProduct(product.getId(), 3);

        Menu input = new Menu("menu", new BigDecimal(-1000), menuGroup.getId(),
                Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 메뉴 가격이 메뉴 품목 가격의 합보다 비싼 경우")
    void createWithExpensivePrice() {
        menuProduct = new MenuProduct(product.getId(), 3);

        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
        BigDecimal expensivePrice = sumOfMenuProduct.add(BigDecimal.valueOf(1000));

        Menu input = new Menu("menu", expensivePrice, menuGroup.getId(),
                Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 존재하지 않는 메뉴 그룹")
    void createWithNotExistingMenuGroup() {
        menuProduct = new MenuProduct(product.getId(), 3);
        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));

        Long notExistingMenuGroupId = Long.MAX_VALUE;

        Menu input = new Menu("menu", sumOfMenuProduct, notExistingMenuGroupId,
                Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 존재하지 않는 상품을 메뉴 그룹에 포함")
    void createWithMenuGroupWithNotExistingProduct() {
        Long notExistingProductId = Long.MAX_VALUE;

        menuProduct = new MenuProduct(notExistingProductId, 3);
        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));

        Menu input = new Menu("menu", sumOfMenuProduct, menuGroup.getId(),
                Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 품목 정상 조회")
    void searchMenuList() {
        menuProduct = new MenuProduct(product.getId(), 3);
        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
        Menu input = new Menu("menu", sumOfMenuProduct, menuGroup.getId(),
                Collections.singletonList(menuProduct));
        menuService.create(input);

        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(1);
        assertThat(menus).allMatch(menu -> !menu.getMenuProducts().isEmpty());
    }
}
