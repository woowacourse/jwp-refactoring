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

    private Menu menu;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = saveMenuGroup();

        menu = Menu.of("TestMenu", BigDecimal.valueOf(1000), menuGroup);
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면, 생성할 수 없다.")
    @Test
    void createFailTest_ByMenuGroupIsNotExists() {
        //given
        Long invalidId = 99L;

        assertThat(menuGroupRepository.existsById(invalidId)).isFalse();

        //when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 있는 상품이 존재하지 않으면, 생성할 수 없다")
    @Test
    void createFailTest_ByMenuProductIsNotExists() {
        //given
        Long invalidId = 99L;
        Product product = new Product();
        product.setId(invalidId);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProduct(product);

        menu.addMenuProduct(menuProduct);

        assertThat(menuGroupRepository.existsById(menuGroup.getId())).isTrue();
        assertThat(productRepository.findById(invalidId)).isEmpty();

        //when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 존재하는 (상품 x 개수)의 가격 합계보다 메뉴 금액이 큰 경우 생성할 수 없다.")
    @Test
    void createFailTest_ByMenuProductsTotalPriceIsLessThanMenuPrice() {
        //given
        BigDecimal price = menu.getPrice();

        Product savedProduct = saveProductAmountOf(price.intValue() - 1);
        MenuProduct menuProduct = createMenuProduct(savedProduct);

        menu.addMenuProduct(menuProduct);

        assertThat(menuGroupRepository.existsById(menuGroup.getId())).isTrue();
        assertThat(productRepository.findById(savedProduct.getId())).isPresent();

        //when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성하면, 메뉴에 존재하는 상품들도 저장된다.")
    @Test
    void createSuccessTest() {
        //given
        int price = 10000;

        Product savedProduct = saveProductAmountOf(price);
        MenuProduct menuProduct = createMenuProduct(savedProduct);

        menu.addMenuProduct(menuProduct);

        //when
        Menu savedMenu = menuService.create(menu);

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
                        .ignoringFields("menuId", "seq")
                        .isEqualTo(List.of(menuProduct))
        );
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void listSuccessTest() {
        //given
        int price = 10000;

        Product savedProduct = saveProductAmountOf(price);

        Menu savedMenu = menuRepository.save(menu);

        MenuProduct menuProduct = createMenuProduct(savedProduct);
        menuProduct.setMenu(savedMenu);
        MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);

        //when
        List<Menu> findMenus = menuService.list();

        //then
        assertAll(
                () -> assertThat(findMenus).usingRecursiveComparison()
                        .ignoringFields("price", "menuProducts")
                        .isEqualTo(List.of(savedMenu)),
                () -> assertThat(findMenus).hasSize(1),
                () -> assertThat(findMenus.get(0).getPrice()).isEqualByComparingTo(savedMenu.getPrice()),
                () -> assertThat(findMenus.get(0).getMenuProducts()).usingRecursiveComparison()
                        .isEqualTo(List.of(savedMenuProduct))
        );
    }

    private MenuGroup saveMenuGroup() {
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        return menuGroupRepository.save(menuGroup);
    }

    private Product saveProductAmountOf(int price) {
        Product product = new Product();
        product.setName("TestName");
        product.setPrice(BigDecimal.valueOf(price));

        return productRepository.save(product);
    }

    private MenuProduct createMenuProduct(Product savedProduct) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProduct(savedProduct);
        menuProduct.setQuantity(1);

        return menuProduct;
    }

}
