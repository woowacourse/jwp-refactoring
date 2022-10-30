package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.TestFixture;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuRepository;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class MenuServiceTest {

    private final MenuService menuService;
    private final MenuRepository menuRepository;
    private final TestFixture testFixture;

    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    public MenuServiceTest(MenuService menuService, MenuRepository menuRepository, TestFixture testFixture) {
        this.menuService = menuService;
        this.menuRepository = menuRepository;
        this.testFixture = testFixture;
    }

    @BeforeEach
    void setUp() {
        Product savedProduct = testFixture.삼겹살();
        MenuGroup savedMenuGroup = testFixture.삼겹살_종류();
        MenuProduct menuProduct = new MenuProduct(savedProduct, 1L);

        this.menuGroup = savedMenuGroup;
        this.menuProducts = List.of(menuProduct);
    }

    @DisplayName("메뉴의 가격이 존재하지 않는다면 예외가 발생한다.")
    @Test
    public void menuWithNullPrice() {
        MenuProduct menuProduct = menuProducts.get(0);
        List<MenuProductRequest> menuProductRequests = List.of(
                new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity())
        );

        MenuRequest menu = new MenuRequest("맛있는 메뉴", null, menuGroup.getId(), menuProductRequests);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 음수라면 예외가 발생한다.")
    @Test
    public void menuWithNegativePrice() {
        MenuProduct menuProduct = menuProducts.get(0);
        List<MenuProductRequest> menuProductRequests = List.of(
                new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity())
        );

        MenuRequest menu = new MenuRequest("맛있는 메뉴", BigDecimal.valueOf(-1), menuGroup.getId(), menuProductRequests);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 존재하지 않다면 예외가 발생한다.")
    @Test
    public void menuGroupNotSaved() {
        MenuProduct menuProduct = menuProducts.get(0);
        List<MenuProductRequest> menuProductRequests = List.of(
                new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity())
        );

        MenuRequest menu = new MenuRequest("맛있는 메뉴", BigDecimal.valueOf(1000), null, menuProductRequests);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(DataAccessException.class);
    }

    @DisplayName("상품의 가격 합보다 메뉴의 가격이 비싸다면 예외가 발생한다.")
    @Test
    public void menuProductPriceDoesNotExceedTotalSum() {
        MenuProduct menuProduct = menuProducts.get(0);
        List<MenuProductRequest> menuProductRequests = List.of(
                new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity())
        );

        MenuRequest menu = new MenuRequest("맛있는 메뉴", BigDecimal.valueOf(1500), menuGroup.getId(), menuProductRequests);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴들을 출력할 수 있다.")
    @Test
    public void menulist() {
        Menu menu1 = new Menu("맛있는 메뉴", BigDecimal.valueOf(1000), menuGroup, menuProducts);
        Menu menu2 = new Menu("적당히 맛있는 메뉴", BigDecimal.valueOf(1000), menuGroup, menuProducts);
        menuRepository.save(menu1);
        menuRepository.save(menu2);

        assertThat(menuService.list()).hasSize(2);
    }
}
