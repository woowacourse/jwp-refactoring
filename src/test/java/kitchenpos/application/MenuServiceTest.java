package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTestWithProfiles
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;

    private Product product;
    private MenuGroup menuGroup;
    private MenuProductRequest menuProductRequest;

    @BeforeEach
    void setUp() {
        product = productService.create(new Product("product", BigDecimal.valueOf(1000)));
        menuGroup = menuGroupService.create(new MenuGroup("menuGroup"));
        menuProductRequest = new MenuProductRequest(product.getId(), 3L);
    }


    @Test
    @DisplayName("메뉴 정상 등록 :: 메뉴 품목의 합과 동일")
    void createPriceEqualToSumOfMenuProduct() {
        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));

        MenuRequest input = new MenuRequest("menu", sumOfMenuProduct, menuGroup.getId(),
                Collections.singletonList(menuProductRequest));

        Menu saved = menuService.create(input);

        assertNotNull(saved.getId());
        assertThat(saved.getMenuProducts().toList()).allMatch(it -> Objects.nonNull(it.getSeq()));
        assertThat(saved.getPrice()).isEqualByComparingTo(sumOfMenuProduct);
    }

    @Test
    @DisplayName("메뉴 정상 등록 :: 메뉴 품목의 합에 일부 할인")
    void createPriceWithDiscount() {
        menuProductRequest = new MenuProductRequest(product.getId(), 3L);

        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
        BigDecimal price = sumOfMenuProduct.subtract(BigDecimal.valueOf(1000));

        MenuRequest input = new MenuRequest("menu", price, menuGroup.getId(),
                Collections.singletonList(menuProductRequest));

        Menu saved = menuService.create(input);

        assertNotNull(saved.getId());
        assertThat(saved.getPrice()).isEqualByComparingTo(price);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 메뉴 가격 null")
    void createWithNullPrice() {
        MenuRequest input = new MenuRequest("menu", null, menuGroup.getId(),
                Collections.singletonList(menuProductRequest));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 메뉴 가격 음수")
    void createWithNegativePrice() {
        MenuRequest input = new MenuRequest("menu", new BigDecimal(-1000), menuGroup.getId(),
                Collections.singletonList(menuProductRequest));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 메뉴 가격이 메뉴 품목 가격의 합보다 비싼 경우")
    void createWithExpensivePrice() {
        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
        BigDecimal expensivePrice = sumOfMenuProduct.add(BigDecimal.valueOf(1000));

        MenuRequest input = new MenuRequest("menu", expensivePrice, menuGroup.getId(),
                Collections.singletonList(menuProductRequest));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 존재하지 않는 메뉴 그룹")
    void createWithNotExistingMenuGroup() {
        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));

        Long notExistingMenuGroupId = Long.MAX_VALUE;

        MenuRequest input = new MenuRequest("menu", sumOfMenuProduct, notExistingMenuGroupId,
                Collections.singletonList(menuProductRequest));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 실패 :: 존재하지 않는 상품을 메뉴 그룹에 포함")
    void createWithMenuGroupWithNotExistingProduct() {
        Long notExistingProductId = Long.MAX_VALUE;

        menuProductRequest = new MenuProductRequest(notExistingProductId, 3L);
        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));

        MenuRequest input = new MenuRequest("menu", sumOfMenuProduct, menuGroup.getId(),
                Collections.singletonList(menuProductRequest));

        assertThatThrownBy(() -> menuService.create(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 품목 정상 조회")
    void searchMenuList() {
        BigDecimal sumOfMenuProduct = product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
        MenuRequest input = new MenuRequest("menu", sumOfMenuProduct, menuGroup.getId(),
                Collections.singletonList(menuProductRequest));
        menuService.create(input);

        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(1);
        assertThat(menus).allMatch(menu -> !menu.getMenuProducts().toList().isEmpty());
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        menuGroupRepository.deleteAllInBatch();
    }
}
