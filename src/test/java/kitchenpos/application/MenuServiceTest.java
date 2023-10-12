package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;
    
    private Menu menu;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menu = new Menu();
        menuGroup = new MenuGroup();
    }

    @Test
    void 메뉴의_가격이_null이면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
        // given
        menu.setPrice(BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
        // given
        menu.setPrice(BigDecimal.ONE);
        menu.setMenuGroupId(1L);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_상품들이_null이면_예외가_발생한다() {
        // given
        menuGroup.setName("추천메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        menu.setPrice(BigDecimal.ZERO);
        menu.setMenuGroupId(savedMenuGroup.getId());

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 메뉴_상품이_없어도_예외가_발생하지_않는다() {
        // given
        menuGroup.setName("추천메뉴");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        menu.setPrice(BigDecimal.ZERO);
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of());
        menu.setName("메뉴");

        // when
        assertThatCode(() -> menuService.create(menu))
                .doesNotThrowAnyException();
    }

    @Nested
    class 메뉴_상품들이_있는경우 {

        private MenuProduct menuProduct1;
        private MenuProduct menuProduct2;

        @BeforeEach
        void setUp() {
            Product product1 = new Product();
            product1.setName("상품1");
            product1.setPrice(BigDecimal.valueOf(1));
            Product savedProduct1 = productDao.save(product1);

            Product product2 = new Product();
            product2.setName("상품2");
            product2.setPrice(BigDecimal.valueOf(3));
            Product savedProduct2 = productDao.save(product2);

            menuProduct1 = new MenuProduct();
            menuProduct1.setProductId(savedProduct1.getId());
            menuProduct1.setQuantity(2);

            menuProduct2 = new MenuProduct();
            menuProduct2.setProductId(savedProduct2.getId());
            menuProduct2.setQuantity(3);
        }

        @Test
        void 메뉴의_가격이_메뉴_상품_가격들의_합보다_크면_예외가_발생한다() {
            // given
            menuGroup.setName("추천메뉴");
            MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            menu.setPrice(BigDecimal.valueOf(12));
            menu.setMenuGroupId(savedMenuGroup.getId());

            menu.setMenuProducts(List.of(menuProduct1, menuProduct2));
            menu.setName("메뉴");

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴를_저장한다() {
            // given
            menuGroup.setName("추천메뉴");
            MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
            menu.setPrice(BigDecimal.valueOf(11));
            menu.setMenuGroupId(savedMenuGroup.getId());

            menu.setMenuProducts(List.of(menuProduct1, menuProduct2));
            menu.setName("메뉴");

            // when
            Menu result = menuService.create(menu);

            // then
            assertAll(
                    () -> assertThat(result.getId()).isPositive(),
                    () -> assertThat(result.getName()).isEqualTo("메뉴"),
                    () -> assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(11)),
                    () -> assertThat(result.getMenuGroupId()).isEqualByComparingTo(savedMenuGroup.getId()),
                    () -> assertThat(result.getMenuProducts()).hasSize(2),
                    () -> assertThat(result.getMenuProducts().get(0).getMenuId()).isEqualTo(result.getId()),
                    () -> assertThat(result.getMenuProducts().get(1).getMenuId()).isEqualTo(result.getId())
            );
        }
    }
}
