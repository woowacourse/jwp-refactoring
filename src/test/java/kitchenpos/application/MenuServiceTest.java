package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductName;
import kitchenpos.domain.ProductPrice;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductRepository productRepository;

    private Long savedMenuGroupId;
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        final Product product = new Product(new ProductName("상품"), new ProductPrice(BigDecimal.ONE));
        final Product savedProduct = productRepository.save(product);
        final MenuGroup menuGroup = new MenuGroup(null, "메뉴 그룹");
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        savedMenuGroupId = savedMenuGroup.getId();
        menuProducts.add(new MenuProduct(null, null, savedProduct.getId(), 2));
    }

    @Nested
    class 메뉴를_등록한다 {
        @Test
        void 메뉴가_정상적으로_등록된다() {
            final Menu menu = new Menu(null, "메뉴", BigDecimal.ONE, savedMenuGroupId, menuProducts);
            final Menu savedMenu = menuService.create(menu);

            assertSoftly(softly -> {
                softly.assertThat(savedMenu.getId()).isNotNull();
                softly.assertThat(savedMenu.getName()).isEqualTo(menu.getName());
                softly.assertThat(savedMenu.getPrice()).isEqualByComparingTo(menu.getPrice());
                softly.assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            });
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 255})
        void 메뉴_이름은_255자_이하이다(int length) {
            final Menu menu = new Menu(null, "메".repeat(length), BigDecimal.ONE, savedMenuGroupId, menuProducts);
            final Menu savedMenu = menuService.create(menu);

            assertSoftly(softly -> {
                softly.assertThat(savedMenu.getId()).isNotNull();
                softly.assertThat(savedMenu.getName()).isEqualTo(menu.getName());
                softly.assertThat(savedMenu.getPrice()).isEqualByComparingTo(menu.getPrice());
                softly.assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            });
        }

        @Test
        void 메뉴_이름이_없으면_예외가_발생한다() {
            final Menu menu = new Menu(null, null, BigDecimal.ONE, savedMenuGroupId, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격이_없으면_예외가_발생한다() {
            final Menu menu = new Menu(null, "메뉴", null, savedMenuGroupId, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_이름이_256자_이상이면_예외가_발생한다() {
            final Menu menu = new Menu(null, "메".repeat(256), BigDecimal.ONE, savedMenuGroupId, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격이_0원_보다_작으면_예외가_발생한다() {
            final Menu menu = new Menu(null, "메뉴", BigDecimal.valueOf(-1), savedMenuGroupId, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격이_1000조_이상이면_예외가_발생한다() {
            final Product product = productRepository.save(new Product(new ProductName("상품"), new ProductPrice(BigDecimal.valueOf(Math.pow(10, 16)))));
            menuProducts.add(new MenuProduct(null, null, product.getId(), 10));

            final Menu menu = new Menu(null, "메뉴", BigDecimal.valueOf(Math.pow(10, 17)), savedMenuGroupId, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_없으면_예외가_발생한다() {
            final Menu menu = new Menu(null, "메뉴", BigDecimal.ONE, null, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
            final Menu menu = new Menu(null, "메뉴", BigDecimal.ONE, savedMenuGroupId + 1, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_상품이_존재하지_않으면_예외가_발생한다() {
            final Product product = productRepository.save(new Product(new ProductName("상품"), new ProductPrice(BigDecimal.ONE)));
            menuProducts.add(new MenuProduct(null, null, product.getId() + 1, 1));

            final Menu menu = new Menu(null, "메뉴", BigDecimal.ZERO, savedMenuGroupId, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격이_메뉴_상품들의_금액을_합한_가격보다_크면_예외가_발생한다() {
            final Menu menu = new Menu(null, "메뉴", BigDecimal.valueOf(3), savedMenuGroupId, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴의_목록을_조회한다() {
        final List<Menu> expected = menuService.list();
        for (int i = 0; i < 3; i++) {
            final Menu menu = new Menu(null, "메뉴" + i, BigDecimal.ONE, savedMenuGroupId, menuProducts);
            expected.add(menuService.create(menu));
        }

        final List<Menu> result = menuService.list();

        assertThat(result).hasSize(expected.size());
    }
}
