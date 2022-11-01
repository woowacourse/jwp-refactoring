package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 생성할 때")
    @Nested
    class Create extends IntegrationTest {

        private MenuGroup menuGroup;
        private List<MenuProduct> menuProducts;

        @BeforeEach
        void setUp() {
            menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
            productDao.save(new Product("상품1", 1000L));
            productDao.save(new Product("상품2", 2000L));
            menuProducts = productDao.findAll().stream()
                    .map(product -> new MenuProduct(product.getId(), 2))
                    .collect(Collectors.toList());
        }

        @DisplayName("성공")
        @Test
        void success() {
            // when
            Menu actual = menuService.create("후라이드", 2000L, menuGroup.getId(), menuProducts);

            // then
            assertAll(
                    () -> assertThat(menuDao.findById(actual.getId())).isPresent(),
                    () -> assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(2000L)),
                    () -> assertThat(actual.getMenuProducts()).hasSize(2)
            );
        }

        @DisplayName("가격이 0보다 작으면 예외를 발생시킨다.")
        @Test
        void priceLessThanZero_exception() {
            // then
            assertThatThrownBy(() -> menuService.create("후라이드", -1L, menuGroup.getId(), menuProducts))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 상품들이 등록되어 있지 않으면 예외를 발생시킨다.")
        @Test
        void notFoundProduct_exception() {
            // given
            MenuProduct menuProduct = new MenuProduct(0L, 2);
            ArrayList<MenuProduct> menuProducts = new ArrayList<>();
            menuProducts.add(menuProduct);

            // then
            assertThatThrownBy(() -> menuService.create("후라이드", -1L, menuGroup.getId(), menuProducts))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품들의 (가격 * 개수) 합보다 메뉴의 가격이 크면 예외를 발생시킨다.")
        @Test
        void priceMoreThanSumOfProducts_exception() {
            // then
            assertThatThrownBy(() -> menuService.create("후라이드", 6100L, menuGroup.getId(), menuProducts))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹이 존재하지 않으면 예외를 발생시킨다.")
        @Test
        void notExistMenuGroup_exception() {
            // then
            assertThatThrownBy(() -> menuService.create("후라이드", 5900L, 0L, menuProducts))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴그룹1"));
        Product product = productDao.save(new Product("상품1", 1000L));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2, BigDecimal.valueOf(1000));
        ArrayList<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);
        Menu menu = new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), menuProducts);
        menuDao.save(menu);

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
