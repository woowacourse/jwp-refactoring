package kitchenpos.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sun.tools.javac.util.List;
import java.math.BigDecimal;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.application.common.factory.MenuFactory;
import kitchenpos.application.common.factory.MenuGroupFactory;
import kitchenpos.application.common.factory.MenuProductFactory;
import kitchenpos.application.common.factory.ProductFactory;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupService.create(MenuGroupFactory.create("두마리메뉴"));
    }

    @DisplayName("생성 - 메뉴를 생성(등록)할 수 있다.")
    @Test
    void create_success() {
        // given
        Product savedProduct = productService.create(
            ProductFactory.create("후라이드", BigDecimal.valueOf(16_000))
        );
        MenuProduct menuProduct = MenuProductFactory.create(savedProduct, 4);

        // when
        Menu menu = MenuFactory.create("후라이드치킨",
            BigDecimal.valueOf(16_000),
            menuGroup,
            List.of(menuProduct));
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
        assertThat(savedMenu.getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId());
        assertThat(savedMenu.getMenuProducts())
            .hasSize(1)
            .extracting("seq")
            .isNotNull();
    }

    // TODO: @ParameteredTest로 수정
    @DisplayName("생성 - 메뉴 가격은 0원 이상이어야 한다.")
    @Test
    void create_menuprice_fail() {
        // given, when
        Menu menu1 = MenuFactory.create("후라이드치킨", BigDecimal.valueOf(-1), menuGroup, null);
        Menu menu2 = MenuFactory.create("양념치킨", null, menuGroup, null);

        // then
        assertThatThrownBy(() -> menuService.create(menu1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 메뉴 가격은 0원 이상이어야 합니다.");
        assertThatThrownBy(() -> menuService.create(menu2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 메뉴 가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("생성 - 존재하지 않는 상품을 메뉴로 등록할 수 없다.")
    @Test
    void create_product_fail() {
        // given
        Product nonSavedProduct = ProductFactory.create("후라이드", BigDecimal.valueOf(16_000));
        MenuProduct menuProduct = MenuProductFactory.create(nonSavedProduct, 4);

        // when
        Menu menu = MenuFactory.create("후라이드치킨",
            BigDecimal.valueOf(16_000),
            menuGroup,
            List.of(menuProduct));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 존재하지 않는 상품을 메뉴로 등록할 수 없습니다.");
    }

    @DisplayName("생성 - 메뉴에 필요한 MenuProduct가 null이면 등록할 수 없다.")
    @Test
    void create_menuproductnull_fail() {
        // given, when
        Menu menu = MenuFactory.create("후라이드치킨",
            BigDecimal.valueOf(16_000),
            menuGroup,
            null);

        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 상품없는 메뉴는 등록할 수 없습니다.");
    }

    @DisplayName("생성 - 메뉴 그룹에 속하지 않는 메뉴는 생성할 수 없다.")
    @Test
    void create_menugroup_fail() {
        // given
        Product savedProduct = productService.create(
            ProductFactory.create("후라이드", BigDecimal.valueOf(16_000))
        );
        MenuProduct menuProduct = MenuProductFactory.create(savedProduct, 4);
        MenuGroup nonSavedMenuGroup = MenuGroupFactory.create("존재하지 않는 메뉴 그룹");

        // when
        Menu menu = MenuFactory.create("후라이드치킨",
            BigDecimal.valueOf(16_000),
            nonSavedMenuGroup,
            List.of(menuProduct));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 메뉴 그룹에 속하지 않은 메뉴는 생성할 수 없습니다.");
    }

    @DisplayName("생성 - 메뉴에 속하는 모든 상품 가격의 합은 0원보다 커야한다.")
    @Test
    void create_menuproductsprice_fail() {
        // given
        Product product1 = productService.create(
            ProductFactory.create("후라이드", BigDecimal.ZERO)
        );
        MenuProduct menuProduct1 = MenuProductFactory.create(product1, 1);
        Menu menu = MenuFactory.create("후라이드치킨",
            BigDecimal.valueOf(16_000),
            menuGroup,
            List.of(menuProduct1));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 메뉴에 속하는 모든 상품 가격의 합은 0원 이상이어야 합니다.");
    }
}
