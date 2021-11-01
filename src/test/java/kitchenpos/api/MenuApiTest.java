package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.request.MenuRequest.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.MenuResponse.MenuProductResponse;
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

    @Autowired
    private MenuProductRepository menuProductRepository;

    private MenuGroup menuGroup;
    private List<Product> products;
    private List<Menu> menus;
    private List<MenuProduct> menuProducts;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        products = new ArrayList<>();
        menus = new ArrayList<>();
        menuProducts = new ArrayList<>();

        menuGroup = menuGroupRepository.save(new MenuGroup("두마리메뉴"));

        products.add(productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000))));
        products.add(productRepository.save(new Product("양념치킨", BigDecimal.valueOf(16000))));

        menus.add(menuRepository.save(new Menu(
            "후라이드치킨",
            BigDecimal.valueOf(16000),
            menuGroup
        )));
        menus.add(menuRepository.save(new Menu(
            "양념치킨",
            BigDecimal.valueOf(16000),
            menuGroup
        )));

        menuProducts.add(menuProductRepository.save(new MenuProduct(
            menus.get(0),
            products.get(0),
            1)
        ));
        menuProducts.add(menuProductRepository.save(new MenuProduct(
            menus.get(1),
            products.get(1),
            1)
        ));
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

        ResponseEntity<MenuResponse> responseEntity = testRestTemplate.postForEntity(
            BASE_URL,
            request,
            MenuResponse.class
        );
        MenuResponse response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getId()).isEqualTo(response.getMenuProducts().get(0).getMenuId());
        assertThat(response).usingComparatorForType(
                BigDecimalComparator.BIG_DECIMAL_COMPARATOR,
                BigDecimal.class
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
        for (int i = 0; i < menus.size(); i++) {
            Menu menuToAdd = menus.get(i);
            MenuResponse menu = new MenuResponse(
                menuToAdd.getId(),
                menuToAdd.getName(),
                menuToAdd.getPrice().getValue(),
                menuToAdd.getMenuGroup().getId(),
                Collections.singletonList(MenuProductResponse.from(menuProducts.get(i)))
            );
            expected.add(menu);
        }

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).hasSize(2);
        assertThat(response).usingRecursiveFieldByFieldElementComparator()
            .usingComparatorForType(
                BigDecimalComparator.BIG_DECIMAL_COMPARATOR,
                BigDecimal.class
            ).containsAll(expected);
    }
}
