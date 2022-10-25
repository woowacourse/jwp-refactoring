package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.support.ClassConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("MenuService의")
public class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @MockBean
    private MenuGroupDao menuGroupDao;
    @MockBean
    private ProductDao productDao;
    @MockBean
    private MenuProductDao menuProductDao;

    @Nested
    @DisplayName("create 메서드는")
    class Create {
        private static final long PRODUCT_A_ID = 1L;
        private static final long PRODUCT_B_ID = 2L;
        private static final long MENU_ID = 1L;
        private static final int PRODUCT_PRICE = 10000;
        private static final long MENU_GROUP_ID = 1L;
        private static final long MENU_PRICE = 10000;

        private Product productA;
        private Product productB;
        private MenuProduct menuProductA;
        private MenuProduct menuProductB;
        private Menu menu;

        @BeforeEach
        void setUp() {
            productA = ClassConstructor.product(PRODUCT_A_ID, "상품A", BigDecimal.valueOf(PRODUCT_PRICE));
            productB = ClassConstructor.product(PRODUCT_B_ID, "상품B", BigDecimal.valueOf(PRODUCT_PRICE));

            menuProductA = ClassConstructor.menuProduct(null, MENU_ID, PRODUCT_A_ID, 2);
            menuProductB = ClassConstructor.menuProduct(null, MENU_ID, PRODUCT_B_ID, 2);

            menu = ClassConstructor.menu(MENU_ID, "메뉴 이름", BigDecimal.valueOf(MENU_PRICE), MENU_GROUP_ID,
                    Arrays.asList(menuProductA, menuProductB));

            given(menuGroupDao.existsById(menu.getMenuGroupId()))
                    .willReturn(true);
            given(productDao.findById(PRODUCT_A_ID))
                    .willReturn(Optional.of(productA));
            given(productDao.findById(PRODUCT_B_ID))
                    .willReturn(Optional.of(productB));

        }

        @Test
        @DisplayName("등록할 수 있는 메뉴를 받으면, 메뉴를 저장하고 내용을 반환한다.")
        void success() {
            //when
            Menu actual = menuService.create(menu);

            //then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getMenuProducts()).hasSize(2)
            );
        }

        @Test
        @DisplayName("메뉴 가격이 없으면, 예외를 던진다.")
        void fail_noPrice() {
            //given
            menu.setPrice(null);

            //when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 가격이 음수면, 예외를 던진다.")
        void fail_priceIsNegative() {
            //given
            menu.setPrice(BigDecimal.valueOf(-1));

            //when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴가 포함될 메뉴그룹이 존재하지 않으면, 예외를 던진다.")
        void fail_noExistMenuGroup() {
            //given
            given(menuGroupDao.existsById(menu.getMenuGroupId()))
                    .willReturn(false);

            //when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴에 포함될 상품들이 존재하지 않으면, 예외를 던진다.")
        void fail_noExistMenuProduct() {
            //given
            given(productDao.findById(PRODUCT_A_ID))
                    .willReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴의 가격보다 포함된 상품들의 총합이 싸면, 예외를 던진다.")
        void fail_sumIsCheaperThenPrice() {
            //given
            productA.setPrice(BigDecimal.valueOf(100));
            menuProductA.setQuantity(1);
            productB.setPrice(BigDecimal.valueOf(100));
            menuProductB.setQuantity(1);

            menu.setPrice(BigDecimal.valueOf(201));

            //when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListMethod {
        private static final long PRODUCT_A_ID = 1L;
        private static final long PRODUCT_B_ID = 2L;
        private static final long MENU_ID = 1L;
        private static final long MENU_GROUP_ID = 1L;
        private static final long MENU_PRICE = 10000;

        private MenuProduct menuProductA;
        private MenuProduct menuProductB;
        private Menu menu;

        @Test
        @DisplayName("전체 메뉴를 조회할 때, 메뉴상품도 같이 조회할 수 있다.")
        void success_getMenuProducts() {
            //given
            menuProductA = ClassConstructor.menuProduct(null, MENU_ID, PRODUCT_A_ID, 2);
            menuProductB = ClassConstructor.menuProduct(null, MENU_ID, PRODUCT_B_ID, 2);

            menu = ClassConstructor.menu(MENU_ID, "메뉴 이름", BigDecimal.valueOf(MENU_PRICE), MENU_GROUP_ID,
                    Arrays.asList(menuProductA, menuProductB));

            given(menuProductDao.findAllByMenuId(1L))
                    .willReturn(Arrays.asList(menuProductA, menuProductB));

            //when
            List<Menu> actual = menuService.list();
            Menu actualMenu = actual.stream()
                    .filter(it -> it.getId().equals(1L))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);

            //then
            assertThat(actualMenu.getMenuProducts()).hasSize(2);
        }
    }
}
