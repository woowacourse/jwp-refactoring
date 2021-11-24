package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuRequest.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuResponse.MenuProductResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MenuApiTest extends ApiTest {

    private static final String BASE_URL = "/api/menus";

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    private MenuGroup menuGroup;
    private List<Product> products;
    private List<Menu> menus;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        products = new ArrayList<>();
        menus = new ArrayList<>();

        menuGroup = menuGroupRepository.save(new MenuGroup("두마리메뉴"));

        products.add(productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000))));
        products.add(productRepository.save(new Product("양념치킨", BigDecimal.valueOf(16000))));

        menus.add(menuRepository.save(new Menu("후라이드치킨", BigDecimal.valueOf(16000),
            menuGroup.getId(),
            new MenuProducts(
                Collections.singletonList(new MenuProduct(products.get(0).getId(), 1))
            ))));
        menus.add(menuRepository.save(new Menu("양념치킨", BigDecimal.valueOf(16000),
            menuGroup.getId(),
            new MenuProducts(
                Collections.singletonList(new MenuProduct(products.get(1).getId(), 1))
            ))));
    }

    @DisplayName("메뉴 등록")
    @Test
    void postMenu() {
        MenuRequest request = new MenuRequest(
            "후라이드+후라이드",
            BigDecimal.valueOf(19000),
            menuGroup.getId(),
            Collections.singletonList(new MenuProductRequest(products.get(0).getId(), 2))
        );

        ResponseEntity<MenuResponse> responseEntity = testRestTemplate.postForEntity(BASE_URL,
            request, MenuResponse.class);
        MenuResponse response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getId()).isEqualTo(response.getMenuProducts().get(0).getMenuId());
        assertThat(response).usingComparatorForType(
                BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class
            ).usingRecursiveComparison()
            .ignoringFields("id", "menuProducts.seq", "menuProducts.menuId")
            .isEqualTo(request);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void getMenus() {
        ResponseEntity<MenuResponse[]> responseEntity = testRestTemplate.getForEntity(
            BASE_URL,
            MenuResponse[].class
        );
        MenuResponse[] response = responseEntity.getBody();

        List<MenuResponse> expected = new ArrayList<>();
        for (Menu menuToAdd : menus) {
            MenuResponse menu = new MenuResponse(menuToAdd.getId(), menuToAdd.getName().getValue(),
                menuToAdd.getPrice().getValue(), menuToAdd.getMenuGroupId(),
                MenuProductResponse.listFrom(menuToAdd)
            );
            expected.add(menu);
        }

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).hasSize(menus.size());
        assertThat(response).usingRecursiveFieldByFieldElementComparator()
            .usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
            .containsAll(expected);
    }
}
