package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuProductDao menuProductDao;

    private List<Menu> preSavedMenus;
    private Menu menu1;
    private Menu menu2;
    private List<MenuProduct> preSavedMenuProducts;

    @BeforeEach
    void setUp() {
        preSavedMenus = menuDao.findAll();
        preSavedMenuProducts = menuProductDao.findAll();

        final MenuGroup menuGroup = menuGroupDao.findById(1L).get();

        menu1 = new Menu();
        menu1.setName("메뉴1");
        menu1.setPrice(BigDecimal.valueOf(1000));
        menu1.setMenuGroupId(menuGroup.getId());

        menu2 = new Menu();
        menu2.setName("메뉴2");
        menu2.setPrice(BigDecimal.valueOf(2000));
        menu2.setMenuGroupId(menuGroup.getId());
    }

    @Test
    @DisplayName("모든 메뉴 그룹 목록을 조회할 수 있다.")
    void should_return_menu_list_when_request_list() {
        // given
        final List<Menu> menus = List.of(menu1, menu2);
        menus.forEach(menu -> menuDao.save(menu));

        final List<Menu> expect = preSavedMenus;
        expect.addAll(menus);

        // when
        final List<Menu> actual = menuService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "price", "menuProducts")
                .isEqualTo(expect);
    }

    @Nested
    @DisplayName("메뉴를 생성할 수 있다.")
    class MenuCreateTest {

        private Menu menu;

        @BeforeEach
        void setUp() {
            final long menuGroupId = 1L;

            menu = new Menu();
            menu.setName("메뉴");
            menu.setPrice(BigDecimal.ZERO);
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(preSavedMenuProducts);
        }

        @ParameterizedTest
        @CsvSource(value = {"0", "1", "10000"})
        @DisplayName("상품 가격이 0 이상이고 group id가 존재하며 메뉴의 가격이 상품 가격의 총 합보다 작거나 같으면 저장할 수 있다.")
        void saveTest(final BigDecimal price) {
            // given
            menu.setPrice(price);

            // when
            final Menu actual = menuService.create(menu);

            // then
            assertEquals(menu.getName(), actual.getName());
            assertEquals(0, menu.getPrice().compareTo(actual.getPrice()));
            assertEquals(menu.getMenuGroupId(), actual.getMenuGroupId());
        }

        @ParameterizedTest
        @NullSource
        @CsvSource(value = {"-1", "-2", "-100000"})
        @DisplayName("상품 가격이 null이거나 0 미만이면 IllegalArgumentException이 발생한다.")
        void smallerThenZeroPriceTest(final BigDecimal price) {
            // given
            menu.setPrice(price);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> menuService.create(menu));
        }

        @Test
        @DisplayName("groupid가 존재하지 않으면 IllegalArgumentException이 발생한다.")
        void invalidGroupIdTest() {
            // given
            final long invalidGroupId = 100000L;

            menu.setMenuGroupId(invalidGroupId);

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> menuService.create(menu));
        }

        @Test
        @DisplayName("메뉴 가격이 상품 가격의 총 합보다 크면 IllegalArgumentException이 발생한다.")
        void largerThenTotalProductPriceTest() {
            // given
            menu.setPrice(BigDecimal.valueOf(100_000_000));

            // when & then
            assertThrowsExactly(IllegalArgumentException.class,
                    () -> menuService.create(menu));
        }
    }
}
