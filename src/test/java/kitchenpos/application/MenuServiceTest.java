//package kitchenpos.application;
//
//import static kitchenpos.fixtures.MenuFixture.MENU;
//import static kitchenpos.fixtures.MenuGroupFixture.MENU_GROUP;
//import static kitchenpos.fixtures.MenuProductFixture.MENU_PRODUCT;
//import static kitchenpos.fixtures.ProductFixture.PRODUCT;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import java.math.BigDecimal;
//import java.util.List;
//import kitchenpos.domain.Menu;
//import kitchenpos.domain.MenuGroup;
//import kitchenpos.domain.MenuProduct;
//import kitchenpos.domain.Product;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//
//@SuppressWarnings("NonAsciiCharacters")
//@DisplayNameGeneration(ReplaceUnderscores.class)
//@Sql("/truncate.sql")
//@SpringBootTest
//class MenuServiceTest {
//
//    @Autowired
//    private MenuService menuService;
//
//    @Autowired
//    private MenuGroupService menuGroupService;
//
//    @Autowired
//    private ProductService productService;
//
//    @Test
//    void 메뉴를_생성한다() {
//        // given
//        final Product product = PRODUCT();
//        final MenuGroup menuGroup = MENU_GROUP();
//
//        final Product savedProduct = productService.create(product);
//        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
//        final MenuProduct menuProduct = MENU_PRODUCT(savedProduct, 10L);
//
//        final Menu menu = MENU(savedMenuGroup, List.of(menuProduct));
//
//        // when
//        final Menu actual = menuService.create(menu);
//
//        // then
//        assertThat(actual).usingRecursiveComparison()
//                          .ignoringFields("id", "price")
//                          .isEqualTo(menu);
//    }
//
//    @Test
//    void 메뉴의_가격이_null_이라면_예외가_발생한다() {
//        // given
//        final Product product = PRODUCT();
//        final MenuGroup menuGroup = MENU_GROUP();
//
//        final Product savedProduct = productService.create(product);
//        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
//        final MenuProduct menuProduct = MENU_PRODUCT(savedProduct, 10L);
//
//        final Menu menu = MENU(savedMenuGroup, List.of(menuProduct), null);
//
//        // when & then
//        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 메뉴의_가격이_음수라면_예외가_발생한다() {
//        // given
//        final Product product = PRODUCT();
//        final MenuGroup menuGroup = MENU_GROUP();
//
//        final Product savedProduct = productService.create(product);
//        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
//        final MenuProduct menuProduct = MENU_PRODUCT(savedProduct, 10L);
//
//        final Menu menu = MENU(savedMenuGroup, List.of(menuProduct), BigDecimal.valueOf(-100));
//
//        // when & then
//        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 메뉴의_메뉴_그룹이_실재하지_않는다면_예외가_발생한다() {
//        // given
//        final Product product = PRODUCT();
//        final MenuGroup menuGroup = MENU_GROUP();
//
//        final Product savedProduct = productService.create(product);
//        final MenuProduct menuProduct = MENU_PRODUCT(savedProduct, 10L);
//
//        final Menu menu = MENU(menuGroup, List.of(menuProduct));
//
//        // when & then
//        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 메뉴의_메뉴_상품이_실재하지_않는_상품이라면_예외가_발생한다() {
//        // given
//        final Product product = PRODUCT();
//        final MenuGroup menuGroup = MENU_GROUP();
//
//        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
//        final MenuProduct menuProduct = MENU_PRODUCT(product, 10L);
//
//        final Menu menu = MENU(savedMenuGroup, List.of(menuProduct));
//
//        // when & then
//        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 메뉴의_메뉴_가격이_모든_상품들의_가격_합보다_크다면_예외가_발생한다() {
//        // given
//        final Product product = PRODUCT(BigDecimal.valueOf(3_000));
//        final MenuGroup menuGroup = MENU_GROUP();
//
//        final Product savedProduct = productService.create(product);
//        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
//        final MenuProduct menuProduct = MENU_PRODUCT(savedProduct, 10L);
//
//        final Menu menu = MENU(savedMenuGroup, List.of(menuProduct), BigDecimal.valueOf(40_000));
//
//        // when & then
//        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 메뉴에_대해_전체_조회를_할_수_있다() {
//        // given
//        final Product product = PRODUCT();
//        final MenuGroup menuGroup = MENU_GROUP();
//
//        final Product savedProduct = productService.create(product);
//        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
//        final MenuProduct menuProduct = MENU_PRODUCT(savedProduct, 10L);
//
//        final Menu menu = MENU(savedMenuGroup, List.of(menuProduct));
//        menuService.create(menu);
//
//        // when
//        final List<Menu> menus = menuService.list();
//
//        // then
//        assertThat(menus).hasSize(1);
//    }
//}