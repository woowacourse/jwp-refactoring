package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
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
    private MenuGroup menuGroup;

    @Autowired
    private ProductDao productDao;
    private Product product;

    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.builder()
            .name("강정메뉴")
            .build();
        menuGroup = menuGroupDao.save(menuGroup);

        product = Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .build();
        product = productDao.save(product);

        menuProduct = MenuProduct.builder()
            .productId(product.getId())
            .quantity(1)
            .build();
    }

    @DisplayName("메뉴 추가")
    @Test
    void create() {
        Menu menu = Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .menuGroupId(menuGroup.getId())
            .menuProducts(Arrays.asList(menuProduct))
            .build();

        Menu create = menuService.create(menu);

        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("[예외] 가격이 0보다 작은 메뉴 추가")
    @Test
    void create_Fail_With_InvalidPrice() {
        Menu menu = Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(-1))
            .menuGroupId(menuGroup.getId())
            .menuProducts(Arrays.asList(menuProduct))
            .build();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 메뉴 그룹에 속한 메뉴 추가")
    @Test
    void create_Fail_With_NotExistMenuGroup() {
        Menu menu = Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .menuGroupId(100L)
            .menuProducts(Arrays.asList(menuProduct))
            .build();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 상품을 포함한 메뉴 추가")
    @Test
    void create_Fail_With_NotExistProduct() {
        MenuProduct menuProduct = MenuProduct.builder()
            .productId(100L)
            .quantity(1)
            .build();

        Menu menu = Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .menuGroupId(menuGroup.getId())
            .menuProducts(Arrays.asList(menuProduct))
            .build();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 포함한 상품의 가격 총합보다 높은 가격의 메뉴 추가")
    @Test
    void create_Fail_With_OverPrice() {
        Menu menu = Menu.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(100_000))
            .menuGroupId(menuGroup.getId())
            .menuProducts(Arrays.asList(menuProduct))
            .build();

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }
}