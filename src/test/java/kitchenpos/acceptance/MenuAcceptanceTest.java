package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuAcceptanceTest extends AcceptanceTest {

    private List<Product> products;
    private MenuGroup 세트_메뉴;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();

        products.add(createProduct("후라이드 치킨", 9_000));
        products.add(createProduct("감자튀김", 5_500));
        products.add(createProduct("사워 크림 소스", 500));
        products.add(createProduct("맥주 500cc", 4_000));

        세트_메뉴 = createMenuGroup("세트 메뉴");
    }

    /**
     * Feature: 메뉴를 관리한다.
     * <p>
     * Given 상품들이 등록되어 있다, 메뉴 그룹이 등록되어 있다.
     * <p>
     * When 메뉴를 등록한다. Then 메뉴가 등록된다.
     * <p>
     * When 모든 메뉴 목록을 조회한다. Then 메뉴 목록이 조회된다.
     */
    @Test
    @DisplayName("메뉴를 관리한다.")
    void manageMenu() {
        // 메뉴 등록
        Menu response = createMenu("후라이드 세트", products, 16_000L, 세트_메뉴.getId());

        assertThat(response.getId()).isNotNull();
        assertThat(response.getMenuGroupId()).isEqualTo(세트_메뉴.getId());
        assertThat(response.getName()).isEqualTo("후라이드 세트");

        assertThatMenuContainsProducts(response, products);
    }

    private void assertThatMenuContainsProducts(Menu menu, List<Product> products) {
        List<MenuProduct> responseMenuProducts = menu.getMenuProducts();

        for (Product product : products) {
            assertThat(doesMenuContainProduct(responseMenuProducts, product)).isTrue();
        }
    }

    private boolean doesMenuContainProduct(List<MenuProduct> menuProducts, Product product) {
        return menuProducts.stream()
            .anyMatch(menuProduct -> menuProduct.getProductId()
                .equals(product.getId()));
    }
}
