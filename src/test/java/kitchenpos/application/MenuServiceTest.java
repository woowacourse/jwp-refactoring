package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴를_생성할_수_있다() {
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(20000)));

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));

        MenuProductRequest menuProductRequest1 = new MenuProductRequest(product1.getId(), 2);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(product2.getId(), 1);

        MenuRequest request = new MenuRequest("메뉴", new BigDecimal(35000), menuGroup.getId(),
                List.of(menuProductRequest1, menuProductRequest2));

        MenuResponse actual = menuService.create(request);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo("메뉴");
            assertThat(actual.getPrice().compareTo(new BigDecimal(35000))).isEqualTo(0);
            assertThat(actual.getMenuGroupId()).isEqualTo(menuGroup.getId());
            assertThat(actual.getMenuProducts()).hasSize(2);
        });
    }

    @Test
    void 메뉴의_가격이_음수인_경우_메뉴를_생성할_수_없다() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));

        MenuRequest request = new MenuRequest("메뉴", new BigDecimal(-1), menuGroup.getId(), new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격의_합보다_작거나_같은_경우_메뉴를_생성할_수_없다() {
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(20000)));

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));

        MenuProductRequest menuProductRequest1 = new MenuProductRequest(product1.getId(), 2);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(product2.getId(), 1);

        MenuRequest request = new MenuRequest("메뉴", new BigDecimal(50000), menuGroup.getId(),
                List.of(menuProductRequest1, menuProductRequest2));

        assertThatThrownBy(() -> menuService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_메뉴를_조회할_수_있다() {
        Product product1 = productRepository.save(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productRepository.save(new Product("상품2", new BigDecimal(20000)));

        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));

        MenuProduct menuProduct1 = new MenuProduct(product1, 2);
        MenuProduct menuProduct2 = new MenuProduct(product2, 1);
        MenuProduct menuProduct3 = new MenuProduct(product2, 1);
        MenuProduct menuProduct4 = new MenuProduct(product2, 1);

        Menu menu1 = new Menu("메뉴1", new BigDecimal(35000), menuGroup, List.of(menuProduct1, menuProduct2));
        Menu menu2 = new Menu("메뉴2", new BigDecimal(38000), menuGroup, List.of(menuProduct3, menuProduct4));

        menuRepository.save(menu1);
        menuRepository.save(menu2);

        List<MenuResponse> actual = menuService.list();

        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual).extracting("menuProducts")
                    .hasSize(2);
        });
    }
}
