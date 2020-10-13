package kitchenpos.application;

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
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("메뉴 추가")
    @Test
    void create() {
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup("강정메뉴"));

        Product savedProduct = productDao.save(createProduct(18_000));
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
        MenuGroup notSavedMenuGroup = MenuGroup.builder()
            .id(1000L)
            .name("강정메뉴")
            .build();

        Product savedProduct = productDao.save(createProduct(18_000));
        MenuProductRequest menuProduct = createMenuProductRequest(savedProduct);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);

        MenuRequest request = createMenuRequest(18_000, notSavedMenuGroup, menuProducts
        );

        assertThatThrownBy(() -> menuService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 상품을 포함한 메뉴 추가")
    @Test
    void create_Fail_With_NotExistProduct() {
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup("강정메뉴"));

        Product notSavedProduct = Product.builder()
            .id(1000L)
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
        MenuGroup savedMenuGroup = menuGroupDao.save(createMenuGroup("강정메뉴"));

        Product savedProduct = productDao.save(createProduct(18_000));
        MenuProductRequest menuProduct = createMenuProductRequest(savedProduct);
        List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);

        MenuRequest request = createMenuRequest(18_000, savedMenuGroup, menuProducts);

        menuService.create(request);
        menuService.create(request);

        List<MenuResponse> list = menuService.list();

        assertThat(list).hasSize(2);
    }
}