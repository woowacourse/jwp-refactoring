package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    private MenuGroup menuGroup;
    private Product product;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        this.menuGroup = menuGroupDao.save(
                new MenuGroup("세트 메뉴")
        );

        this.product = productDao.save(
                new Product("짜장면", new BigDecimal(30_000))
        );

        this.menuProduct = new MenuProduct(product.getId(), 10L);
    }

    @DisplayName("create 메소드는 ")
    @Nested
    class CreateMethod {

        @DisplayName("메뉴를 생성한다.")
        @Test
        void Should_CreateMenu() {
            // given
            Menu menu = new Menu("세트1", new BigDecimal(20000), menuGroup.getId(), List.of(menuProduct));

            // when
            Menu actual = menuService.create(menu);

            // then
            assertAll(() -> {
                assertThat(actual.getName()).isEqualTo(menu.getName());
                assertThat(actual.getPrice().doubleValue()).isEqualTo(menu.getPrice().doubleValue());
            });
        }

        @DisplayName("메뉴 가격이 null이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_PriceOfMenuIsNull() {
            // given
            Menu menu = new Menu("세트1", null, menuGroup.getId(), List.of(menuProduct));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 메뉴 그룹이 존재하지 않는다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuGroupDoesNotExist() {
            // given
            Menu menu = new Menu("세트1", new BigDecimal(10000), menuGroup.getId() + 1, List.of(menuProduct));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 메뉴 상품 리스트 중 존재하지 않은 상품이 있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_ProductDoesNotExistInMenuProductList() {
            // given
            MenuProduct menuProduct = new MenuProduct(product.getId() + 1, 1L);
            Menu menu = new Menu("세트1", new BigDecimal(10000), menuGroup.getId(), List.of(menuProduct));

            // when
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 모든 메뉴 상품 목록에 대해 '상품 가격 * 메뉴 상품 수량' 의 합이 메뉴의 가격보다 크다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuPriceIsBiggerThanSumOfProductOfProductPriceAndQuantity() {
            // given
            Menu menu = new Menu("세트1", new BigDecimal(1_000_000), menuGroup.getId(), List.of(menuProduct));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {

        @DisplayName("저장된 전체 메뉴 리스트를 조회한다.")
        @Test
        void Should_ReturnAllMenuList() {
            // given
            Menu menu1 = new Menu("세트1", new BigDecimal(1_000), menuGroup.getId(), List.of(menuProduct));
            Menu menu2 = new Menu("세트2", new BigDecimal(2_000), menuGroup.getId(), List.of(menuProduct));
            Menu menu3 = new Menu("세트3", new BigDecimal(3_000), menuGroup.getId(), List.of(menuProduct));
            Menu menu4 = new Menu("세트4", new BigDecimal(4_000), menuGroup.getId(), List.of(menuProduct));

            menuService.create(menu1);
            menuService.create(menu2);
            menuService.create(menu3);
            menuService.create(menu4);

            // when
            List<Menu> actual = menuService.list();

            // then
            assertAll(() -> {
                assertThat(actual).hasSize(4);
            });
        }
    }
}
