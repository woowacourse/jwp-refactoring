package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    @DisplayName("menu의 menuGroup이 존재하지 않은 경우 예외가 발생한다.")
    @Test
    void create_ifMenuGroupNotExist_throwsException() {
        // given
        final Product product = productRepository.save(new Product("name", BigDecimal.valueOf(3000)));
        final MenuProductRequest menuProduct1 = new MenuProductRequest(product.getId(), 3);
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1);
        final MenuRequest menu = new MenuRequest("메뉴1", BigDecimal.valueOf(1000), 99L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @DisplayName("menu의 menuProduct가 존재하지 않은 경우 예외가 발생한다.")
    @Test
    void create_ifMenuProductNotExist_throwsExcpetion() {
        // given
        final MenuProductRequest menuProduct1 = new MenuProductRequest(99L, 3);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("name"));
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1);
        final MenuRequest menu = new MenuRequest("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @DisplayName("menu 가격이 0보다 작은 경우 예외가 발생한다.")
    @Test
    void create_ifMenuPriceIsNegative_throwsException() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("name", BigDecimal.valueOf(3000)));
        final MenuProductRequest menuProduct1 = new MenuProductRequest(product.getId(), 3);
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1);
        final MenuRequest menuRequest = new MenuRequest("메뉴1", BigDecimal.valueOf(-1000), menuGroup.getId(),
                menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양수여야합니다.");
    }

    @DisplayName("menu 가격이 null인 경우 예외가 발생한다.")
    @Test
    void create_ifMenuPriceIsNull_throwsException() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("name", BigDecimal.valueOf(3000)));
        final MenuProductRequest menuProduct1 = new MenuProductRequest(product.getId(), 3);
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1);
        final MenuRequest menu = new MenuRequest("메뉴1", null, menuGroup.getId(), menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 양수여야합니다.");
    }

    @DisplayName("menu의 가격이 menu의 product들의 가격보다 비싼 경우 예외가 발생한다.")
    @Test
    void create_ifMenuPriceMoreExpensiveThanProducts_throwsException() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("name", BigDecimal.valueOf(3000)));
        final MenuProductRequest menuProduct1 = new MenuProductRequest(product.getId(), 1);
        final List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct1);
        final MenuRequest menu = new MenuRequest("메뉴1", BigDecimal.valueOf(40000), 1L, menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격과 메뉴 상품의 총 가격은 동일해야합니다.");
    }

    @DisplayName("menu가 정상적으로 저장이 된다.")
    @Test
    void create() {
        // given
        final Product product = productRepository.save(new Product("name", BigDecimal.valueOf(32000)));
        final MenuProductRequest menuProduct1 = new MenuProductRequest(product.getId(), 1);
        final List<MenuProductRequest> menuProductRequests = Arrays.asList(menuProduct1);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹1"));

        final MenuRequest menuRequest = new MenuRequest("메뉴1", BigDecimal.valueOf(32000), menuGroup.getId(),
                menuProductRequests);

        // when
        final Menu menu = menuService.create(menuRequest);

        // then
        assertThat(menu.getId()).isNotNull();
    }

    @DisplayName("menu들을 조회한다.")
    @Test
    void list() {
        // given
        final Product product = productRepository.save(new Product("name", BigDecimal.valueOf(3000)));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("name"));
        final MenuProduct menuProduct = new MenuProduct(product, 3);
        final Menu menu = new Menu("name", BigDecimal.valueOf(3000), menuGroup, Arrays.asList(menuProduct));
        final Menu savedMenu = menuRepository.save(menu);

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertThat(menus.size()).isEqualTo(1);
    }
}
