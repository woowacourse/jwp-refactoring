package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;

class MenuServiceTest extends ServiceTest {

    @Autowired
    protected MenuRepository menuRepository;
    @Autowired
    protected MenuGroupRepository menuGroupRepository;
    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected MenuService menuService;

    @Test
    @DisplayName("메뉴를 저장한다")
    void create() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Product createdProduct = createProduct();

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("test", BigDecimal.ONE, createdMenuGroup.getId(),
            List.of(new MenuProductCreateRequest(createdProduct.getId(), 10)));
        // when
        Menu createdMenu = menuService.create(menuCreateRequest);

        // then
        assertAll(
            () -> assertThat(createdMenu).isNotNull(),
            () -> assertThat(createdMenu.getName()).isEqualTo("test")
        );
    }

    @Test
    @DisplayName("메뉴 가격은 함께 등록되어야 한다")
    void nullPrice() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Product createdProduct = createProduct();

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("test", null, createdMenuGroup.getId(),
            List.of(new MenuProductCreateRequest(createdProduct.getId(), 10)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격은 음수일 수 없다")
    void minusPrice() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Product createdProduct = createProduct();

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("test", BigDecimal.valueOf(-100),
            createdMenuGroup.getId(),
            List.of(new MenuProductCreateRequest(createdProduct.getId(), 10)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹에 속해있지 않을 수 없다")
    void withoutMenuGroup() {
        // given
        Product createdProduct = createProduct();

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("test", BigDecimal.valueOf(-100), null,
            List.of(new MenuProductCreateRequest(createdProduct.getId(), 10)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구성 상품 가격의 합을 초과하는 가격을 설정할 수 없다")
    void priceBiggerThanSum() {
        // given
        MenuGroup createdMenuGroup = createMenuGroup();
        Product createdProduct = createProduct();

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("test", BigDecimal.valueOf(101),
            createdMenuGroup.getId(),
            List.of(new MenuProductCreateRequest(createdProduct.getId(), 10)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다")
    void list() {
        // given

        // when
        List<Menu> menus = menuService.list();

        // then
        assertAll(
            () -> assertThat(menus).hasSameSizeAs(menuRepository.findAll())
        );
    }

    private MenuGroup createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup("testGroup");
        return menuGroupRepository.save(menuGroup);
    }

    private Product createProduct() {
        Product product = new Product("testProduct", BigDecimal.TEN);
        return productRepository.save(product);
    }
}
