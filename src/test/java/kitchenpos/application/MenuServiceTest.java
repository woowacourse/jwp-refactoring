package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu();
        menu.setName("TestMenu");
    }

    @Test
    @DisplayName("메뉴 금액이 null인 경우, 생성할 수 없다.")
    void createFailTest_ByPriceIsNull() {
        //when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "메뉴 금액이 0원 미만인 경우, 생성할 수 없다")
    @ValueSource(ints = {-100, -1})
    void createFailTest_ByPriceIsLessThanZero(int price) {
        //given
        menu.setPrice(BigDecimal.valueOf(price));

        //when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("메뉴 그룹이 존재하지 않으면, 생성할 수 없다.")
    @Test
    void createFailTest_ByMenuGroupIsNotExists() {
        //given
        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(99L);

        //when
        assertThat(menuGroupDao.existsById(99L)).isFalse();

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 있는 상품이 존재하지 않으면, 생성할 수 없다")
    @Test
    void createFailTest_ByMenuProductIsNotExists() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("TestMenuGroup");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(99L);

        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        //when
        assertThat(menuGroupDao.existsById(savedMenuGroup.getId())).isTrue();
        assertThat(productDao.findById(99L)).isEmpty();

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 존재하는 (상품 x 개수)의 가격 합계보다 메뉴 금액이 큰 경우 생성할 수 없다.")
    @Test
    void createFailTest_ByMenuProductsTotalPriceIsLessThanMenuPrice() {
        //given
        int price = 10000;

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("TestMenuGroup");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Product product = new Product();
        product.setName("TestName");
        product.setPrice(BigDecimal.valueOf(price));
        Product savedProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);

        menu.setPrice(BigDecimal.valueOf(price + 1));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        //when
        assertThat(menuGroupDao.existsById(savedMenuGroup.getId())).isTrue();
        assertThat(productDao.findById(savedProduct.getId())).isPresent();

        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성하면, 메뉴에 존재하는 상품들도 저장된다.")
    @Test
    void createSuccessTest() {
        //given
        int price = 10000;

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("TestMenuGroup");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Product product = new Product();
        product.setName("TestName");
        product.setPrice(BigDecimal.valueOf(price));
        Product savedProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);

        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        //when
        Menu savedMenu = menuService.create(menu);
        List<Menu> findMenus = menuService.list()
                .stream()
                .filter(menu -> menu.getId().equals(savedMenu.getId()))
                .collect(Collectors.toList());

        //then

        assertAll(
                () -> assertThat(findMenus).extractingResultOf("getId")
                        .containsExactly(savedMenu.getId()),
                () -> assertThat(findMenus).extractingResultOf("getName")
                        .containsExactly(savedMenu.getName()),
                () -> assertThat(findMenus).extractingResultOf("getPrice")
                        .containsExactly(savedMenu.getPrice()),
                () -> assertThat(findMenus).extractingResultOf("getMenuGroupId")
                        .containsExactly(savedMenu.getMenuGroupId()),

                () -> assertThat(findMenus).hasSize(1),
                () -> assertThat(findMenus.get(0).getMenuProducts())
                        .usingRecursiveComparison()
                        .ignoringFields("menuId", "seq")
                        .isEqualTo(List.of(menuProduct))
        );

    }

}
