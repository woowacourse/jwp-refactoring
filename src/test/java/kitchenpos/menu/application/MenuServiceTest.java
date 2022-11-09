package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugruop.domain.MenuGroup;
import kitchenpos.menugruop.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    private Product product;

    private MenuGroup menuGroup;

    @Override
    public void setObject() {
        product = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000)));
        menuGroup = menuGroupRepository.save(new MenuGroup("1번 메뉴 그룹"));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        final String name = "1번 메뉴";
        final BigDecimal price = BigDecimal.valueOf(10000);
        final MenuCreateRequest request = new MenuCreateRequest(name, price, menuGroup.getId(),
                createMenuProductRequest(product.getId()));

        final MenuResponse response = menuService.create(request);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getMenuGroupId()).isNotNull(),
                () -> assertThat(response.getMenuProducts()).isNotEmpty(),
                () -> assertThat(response.getName()).isEqualTo(name),
                () -> assertThat(response.getPrice().longValue()).isEqualTo(price.longValue())
        );
    }

    @DisplayName("메뉴 등록 시 메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createWithNoMenuGroup() {
        final MenuCreateRequest request = new MenuCreateRequest("1번 메뉴", BigDecimal.valueOf(10000), 9999L,
                createMenuProductRequest(product.getId()));

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴들을 조회할 수 있다.")
    @Test
    void list() {
        final Menu newMenu = new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId()));
        final Menu menu = menuRepository.save(newMenu);
        final MenuProduct menuProduct = new MenuProduct(menu.getId(), product.getId(), 10);
        menuProductRepository.save(menuProduct);

        final List<MenuResponse> response = menuService.list();

        assertAll(
                () -> assertThat(response).hasSize(1),
                () -> assertThat(response.get(0).getMenuProducts()).isNotEmpty()
        );
    }

    private MenuProducts createMenuProducts(final Long... productIds) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final Long productId : productIds) {
            menuProducts.add(new MenuProduct(productId, 1L, BigDecimal.valueOf(10000)));
        }
        return new MenuProducts(menuProducts, BigDecimal.valueOf(9000));
    }

    private List<MenuProductRequest> createMenuProductRequest(final Long... productIds) {
        final List<MenuProductRequest> menuProducts = new ArrayList<>();
        for (final Long productId : productIds) {
            menuProducts.add(new MenuProductRequest(productId, 1L));
        }
        return menuProducts;
    }
}
