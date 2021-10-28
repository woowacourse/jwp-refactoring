package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("Menu 인수 테스트")
class MenuAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Menu를 생성한다.")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Product product = productRepository.save(new Product("product", BigDecimal.valueOf(1000)));

        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            product.getId(), 10L);
        MenuCreateRequest request = new MenuCreateRequest("menu", BigDecimal.valueOf(5000),
            menuGroup.getId(), Collections.singletonList(menuProductCreateRequest));

        MenuResponse response = makeResponse("/api/menus", TestMethod.POST, request)
            .as(MenuResponse.class);

        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getName()).isEqualTo(request.getName()),
            () -> assertThat(response.getPrice()).isEqualTo(request.getPrice())
        );
    }

    @DisplayName("Menu 생성 실패 - 금액이 0보다 작다.")
    @Test
    void create_fail_price() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Product product = productRepository.save(new Product("product", BigDecimal.valueOf(1000)));

        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            product.getId(), 10L);
        MenuCreateRequest request = new MenuCreateRequest("menu", BigDecimal.valueOf(-500),
            menuGroup.getId(), Collections.singletonList(menuProductCreateRequest));

        int actual = makeResponse("/api/menus", TestMethod.POST, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Menu 생성 실패 - 메뉴 그룹이 존재하지 않는다.")
    @Test
    void create_fail_menu_group_non_exist() {
        Product product = productRepository.save(new Product("product", BigDecimal.valueOf(1000)));

        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            product.getId(), 10L);
        MenuCreateRequest request = new MenuCreateRequest("menu", BigDecimal.valueOf(-500),
            999L, Collections.singletonList(menuProductCreateRequest));

        int actual = makeResponse("/api/menus", TestMethod.POST, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Menu 생성 실패 - 메뉴 상품이 존재하지 않는다.")
    @Test
    void create_fail_menu_product_non_exist() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));

        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(999L, 10L);
        MenuCreateRequest request = new MenuCreateRequest("menu", BigDecimal.valueOf(-500),
            menuGroup.getId(), Collections.singletonList(menuProductCreateRequest));

        int actual = makeResponse("/api/menus", TestMethod.POST, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Menu 생성 실패 - 메뉴 가격이 메뉴 상품들 총액보다 높다.")
    @Test
    void create_fail_menu_price_more_than_total_menu_product_price() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Product product = productRepository.save(new Product("product", BigDecimal.valueOf(1000)));

        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            product.getId(), 10L);
        MenuCreateRequest request = new MenuCreateRequest("menu", BigDecimal.valueOf(200000),
            menuGroup.getId(), Collections.singletonList(menuProductCreateRequest));

        int actual = makeResponse("/api/menus", TestMethod.POST, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Menu 리스트를 불러온다.")
    @Test
    void list() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Product product = productRepository.save(new Product("product", BigDecimal.valueOf(1000)));

        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            product.getId(), 10L);
        MenuCreateRequest request = new MenuCreateRequest("menu", BigDecimal.valueOf(5000),
            menuGroup.getId(), Collections.singletonList(menuProductCreateRequest));

        makeResponse("/api/menus", TestMethod.POST, request)
            .as(MenuResponse.class);
        makeResponse("/api/menus", TestMethod.POST, request)
            .as(MenuResponse.class);

        List<MenuResponse> responses = makeResponse("/api/menus", TestMethod.GET).jsonPath()
            .getList(".", MenuResponse.class);
        assertThat(responses.size()).isEqualTo(2);
    }
}
