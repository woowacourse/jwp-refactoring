package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

@DisplayName("메뉴 서비스 테스트")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Product product = productRepository.save(new Product("product", 1000L));

        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            product.getId(), 10L);
        MenuCreateRequest request = new MenuCreateRequest("menu", 5000L,
            menuGroup.getId(), Collections.singletonList(menuProductCreateRequest));

        MenuResponse created = menuService.create(request);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getName()).isEqualTo("menu"),
            () -> assertThat(created.getPrice().intValue()).isEqualTo(5000)
        );
    }

    @DisplayName("메뉴 리스트를 불러온다.")
    @Test
    void list() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Product product = productRepository.save(new Product("product", 1000L));

        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            product.getId(), 10L);
        MenuCreateRequest request = new MenuCreateRequest("menu", 5000L,
            menuGroup.getId(), Collections.singletonList(menuProductCreateRequest));

        menuService.create(request);
        menuService.create(request);

        List<MenuResponse> menus = menuService.list();
        assertThat(menus.size()).isEqualTo(2);
    }
}