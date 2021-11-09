package kitchenpos.application;

import kitchenpos.builder.MenuGroupBuilder;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class MenuServiceTest extends BaseServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuProductDao menuProductDao;

    Menu menu;
    MenuGroup savedMenuGroup;
    Product savedProduct;
    MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = TestFixtureFactory.메뉴그룹_인기_메뉴();
        savedMenuGroup = menuGroupDao.save(menuGroup);

        Product product = TestFixtureFactory.상품_후라이드_치킨();
        savedProduct = productRepository.save(product);

        menuProduct = TestFixtureFactory.메뉴상품_매핑_생성(savedProduct, 1L);

        menu = TestFixtureFactory.메뉴_후라이드_치킨_한마리(savedMenuGroup, savedProduct, menuProduct);
    }

    @DisplayName("[메뉴 생성] 메뉴를 정상적으로 생성한다.")
    @Test
    void create() {
        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo(this.menu.getName());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(this.menu.getMenuGroupId());
        assertThat(savedMenu.getMenuProducts()).hasSize(1);
    }

    @DisplayName("[메뉴 생성] 메뉴의 가격이 상품가격의 총합보다 비싸면 예외가 발생한다.")
    @Test
    void createWithOverPrice() {
        // given
        BigDecimal overPrice = savedProduct.getPrice().add(new BigDecimal(16001));
        Menu menu = TestFixtureFactory.메뉴_생성("후라이드 한마리", overPrice, savedMenuGroup, menuProduct);

        // when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[메뉴 생성] 메뉴의 가격이 음수면 예외가 발생한다.")
    @Test
    void createZeroPriceMenu() {
        // given
        BigDecimal negativePrice = new BigDecimal(-1);
        Menu menu = TestFixtureFactory.메뉴_생성("후라이드 한마리", negativePrice, savedMenuGroup, menuProduct);

        // when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[메뉴 생성] 메뉴가 메뉴 그룹에 속하지 않거나 포함시킬 메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createWithoutMenuGroup() {
        // given
        MenuGroup nullIdMenuGroup = new MenuGroupBuilder()
                .id(null)
                .name("존재하지 않는 메뉴그룹")
                .build();
        Menu menu1 = TestFixtureFactory.메뉴_생성("후라이드 한마리", savedProduct.getPrice(), nullIdMenuGroup, menuProduct);
        MenuGroup nonExistMenuGroup = new MenuGroupBuilder()
                .id(99999L)
                .name("존재하지 않는 메뉴그룹")
                .build();
        Menu menu2 = TestFixtureFactory.메뉴_생성("후라이드 한마리", savedProduct.getPrice(), nonExistMenuGroup, menuProduct);

        // when then
        assertThatThrownBy(() -> menuService.create(menu1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> menuService.create(menu2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[메뉴 전체 조회] 메뉴 전체를 조회한다.")
    @Test
    void list() {
        // given
        Menu savedMenu1 = menuService.create(this.menu);
        Menu menu1 = TestFixtureFactory.메뉴_생성("JMT 후라이드 치킨", new BigDecimal(16000), savedMenuGroup, menuProduct);
        Menu savedMenu2 = menuService.create(this.menu);
        Menu menu2 = TestFixtureFactory.메뉴_생성("통큰 후라이드 치킨", new BigDecimal(8000), savedMenuGroup, menuProduct);
        Menu savedMenu3 = menuService.create(this.menu);

        // when
        List<Menu> menu = menuService.list();

        // then
        assertThat(menu).hasSize(3);
        isSameMenu(menu.get(0), savedMenu1);
        isSameMenu(menu.get(1), savedMenu2);
        isSameMenu(menu.get(2), savedMenu3);
    }

    private void isSameMenu(Menu menu, Menu otherMenu) {
        assertThat(menu.getId()).isEqualTo(otherMenu.getId());
        assertThat(menu.getName()).isEqualTo(otherMenu.getName());
        assertThat(menu.getMenuGroupId()).isEqualTo(otherMenu.getMenuGroupId());
        assertThat(menu.getMenuProducts()).hasSize(otherMenu.getMenuProducts().size());
    }
}