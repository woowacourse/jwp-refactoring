package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreationRequest;
import kitchenpos.dto.MenuProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Sql(value = "/initialization.sql")
class MenuServiceTest {

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

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = saveMenuGroup();
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면, 생성할 수 없다.")
    @Test
    void createFailTest_ByMenuGroupIsNotExists() {
        //given
        Long invalidId = 99L;
        Product product = saveProductAmountOf(1000);
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);

        MenuCreationRequest request = new MenuCreationRequest(
                "TestMenu",
                BigDecimal.valueOf(1000),
                invalidId,
                List.of(menuProductRequest)
        );

        assertThat(menuGroupRepository.existsById(invalidId)).isFalse();

        //when then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴 그룹입니다.");
    }

    @DisplayName("메뉴에 있는 상품이 존재하지 않으면, 생성할 수 없다")
    @Test
    void createFailTest_ByMenuProductIsNotExists() {
        //given
        Long invalidId = 99L;
        MenuProductRequest menuProductRequestWithInvalidProductId = new MenuProductRequest(invalidId, 1L);

        MenuCreationRequest request = new MenuCreationRequest(
                "TestMenu",
                BigDecimal.valueOf(1000),
                menuGroup.getId(),
                List.of(menuProductRequestWithInvalidProductId)
        );

        assertThat(menuGroupRepository.existsById(menuGroup.getId())).isTrue();
        assertThat(productRepository.findById(invalidId)).isEmpty();

        //when then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴에 존재하지 않는 상품이 포함되어 있습니다.");
    }

    @DisplayName("메뉴에 존재하는 (상품 x 개수)의 가격 합계보다 메뉴 금액이 큰 경우 생성할 수 없다.")
    @Test
    void createFailTest_ByMenuProductsTotalPriceIsLessThanMenuPrice() {
        //given
        BigDecimal price = BigDecimal.valueOf(1000);
        Product product = saveProductAmountOf(price.intValue() - 1);

        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuCreationRequest request = new MenuCreationRequest(
                "TestMenu",
                price,
                menuGroup.getId(),
                List.of(menuProductRequest)
        );

        assertThat(menuGroupRepository.existsById(menuGroup.getId())).isTrue();
        assertThat(productRepository.findById(product.getId())).isPresent();

        //when then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 금액의 합계는 각 상품들의 합계보다 클 수 없습니다.");;
    }

    @DisplayName("메뉴를 생성하면, 메뉴에 존재하는 상품들도 저장된다.")
    @Test
    void createSuccessTest() {
        //given
        int price = 10000;

        Product product = saveProductAmountOf(price);
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuCreationRequest request = new MenuCreationRequest(
                "TestMenu",
                BigDecimal.valueOf(price),
                menuGroup.getId(),
                List.of(menuProductRequest)
        );

        //when
        Menu savedMenu = menuService.create(request);

        //then
        Menu findMenu = menuRepository.findById(savedMenu.getId()).get();
        List<MenuProduct> findMenuProducts = menuProductRepository.findAllByMenuId(findMenu.getId());

        assertAll(
                () -> assertThat(findMenu).usingRecursiveComparison()
                        .ignoringFields("price", "menuProducts")
                        .isEqualTo(savedMenu),
                () -> assertThat(findMenu.getPrice())
                        .isEqualByComparingTo(savedMenu.getPrice()),
                () -> assertThat(findMenuProducts).usingRecursiveComparison()
                        .ignoringFields("seq")
                        .isEqualTo(List.of(MenuProduct.create(findMenu, 1L, product)))
        );
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void listSuccessTest() {
        //given
        int price = 10000;
        Product product = saveProductAmountOf(price);
        MenuGroup menuGroup = saveMenuGroup();

        Menu menu = Menu.createWithEmptyMenuProducts("TestMenu", BigDecimal.valueOf(price), menuGroup);
        MenuProduct menuProduct = createMenuProduct(menu, product);
        menu.addMenuProduct(menuProduct);

        menuRepository.save(menu);

        //when
        List<Menu> findMenus = menuService.list();

        //then
        assertAll(
                () -> assertThat(findMenus).usingRecursiveComparison()
                        .ignoringFields("price", "menuProducts")
                        .isEqualTo(List.of(menu)),
                () -> assertThat(findMenus).hasSize(1),
                () -> assertThat(findMenus.get(0).getPrice()).isEqualByComparingTo(menu.getPrice()),
                () -> assertThat(findMenus.get(0).getMenuProducts()).usingRecursiveComparison()
                        .isEqualTo(List.of(menuProduct))
        );
    }

    private MenuGroup saveMenuGroup() {
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        return menuGroupRepository.save(menuGroup);
    }

    private Product saveProductAmountOf(int price) {
        Product product = Product.create("TestProduct", BigDecimal.valueOf(price));

        return productRepository.save(product);
    }

    private MenuProduct createMenuProduct(Menu menu, Product savedProduct) {
        return MenuProduct.create(menu, 1L, savedProduct);
    }

}
