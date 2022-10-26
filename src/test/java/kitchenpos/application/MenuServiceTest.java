package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;

@SpringBootTest
@Transactional
class MenuServiceTest {

    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupService.create(new MenuGroup("test"));

        Product product = productService.create(new Product("test", BigDecimal.valueOf(100)));
        menuProducts = Arrays.asList(new MenuProduct(product, 10));
    }

    @Test
    @DisplayName("Menu를 생성한다.")
    void create() {
        //given
        Menu menu = new Menu("test", BigDecimal.valueOf(100), menuGroup, menuProducts);

        //when
        Menu savedMenu = menuService.create(menu);

        //then
        assertThat(savedMenu.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 MenuGroupId면 예외를 발생시킨다.")
    void createWithNotExistMenuGroupIdError() {
        //given, when
        menuGroupRepository.delete(menuGroup);
        Menu menu = new Menu("test", BigDecimal.valueOf(100), menuGroup, menuProducts);

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {500000, 16001})
    @DisplayName("개별 상품의 합이 menu 가격의 합보다 클 경우 예외를 발생시킨다.")
    void createWithCheaperPriceError(int price) {
        //when
        Menu menu = new Menu("test", BigDecimal.valueOf(price), menuGroup, menuProducts);

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 메뉴목록을 조회한다.")
    void findByList() {
        //given
        List<Menu> menus = menuService.list();

        //when
        Menu menu = new Menu("test", BigDecimal.valueOf(100), menuGroup, menuProducts);

        menuService.create(menu);

        //then
        assertThat(menuService.list()).hasSize(menus.size() + 1);
    }
}
