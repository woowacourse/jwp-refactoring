package kitchenpos.menu.application;

import static kitchenpos.TestObjectFactory.createMenuGroup;
import static kitchenpos.TestObjectFactory.createMenuProductRequest;
import static kitchenpos.TestObjectFactory.createMenuRequest;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu_group.repository.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("메뉴 추가")
    @Test
    void create() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(createMenuGroup("강정메뉴"));

        Product savedProduct = productRepository.save(createProduct(18_000));
        MenuProductRequest menuProduct = createMenuProductRequest(savedProduct);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);

        MenuRequest request = createMenuRequest(18_000, savedMenuGroup, menuProducts);

        MenuResponse savedMenu = menuService.create(request);

        assertAll(
            () -> assertThat(savedMenu.getId()).isNotNull(),
            () -> assertThat(savedMenu.getMenuProducts().get(0).getSeq()).isNotNull()
        );
    }

    @DisplayName("[예외] 존재하지 않는 메뉴 그룹에 속한 메뉴 추가")
    @Test
    void create_Fail_With_NotExistMenuGroup() {
        Product savedProduct = productRepository.save(createProduct(18_000));
        MenuProductRequest menuProduct = createMenuProductRequest(savedProduct);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);

        MenuRequest request = MenuRequest.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .menuGroupId(1000L)
            .menuProducts(menuProducts)
            .build();

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 상품을 포함한 메뉴 추가")
    @Test
    void create_Fail_With_NotExistProduct() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(createMenuGroup("강정메뉴"));

        Product notSavedProduct = Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .build();
        MenuProductRequest menuProduct = createMenuProductRequest(notSavedProduct);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);

        MenuRequest request = createMenuRequest(18_000, savedMenuGroup, menuProducts);

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 전체 조회")
    @Test
    void list() {
        MenuGroup savedMenuGroup = menuGroupRepository.save(createMenuGroup("강정메뉴"));

        Product savedProduct = productRepository.save(createProduct(18_000));
        MenuProductRequest menuProduct = createMenuProductRequest(savedProduct);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);

        MenuRequest request = createMenuRequest(18_000, savedMenuGroup, menuProducts);

        menuService.create(request);
        menuService.create(request);

        List<MenuResponse> list = menuService.list();

        assertThat(list).hasSize(2);
    }
}