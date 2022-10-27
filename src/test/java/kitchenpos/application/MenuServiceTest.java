package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.exception.IllegalPriceException;
import kitchenpos.exception.MenuTotalPriceException;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.MenuProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() {
        // given
        final MenuProduct menuProduct = MenuProductFixtures.create(null, 1L, 2);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuProducts(menuProduct);

        // when
        final Menu saved = menuService.create(menu);

        // then
        final MenuProduct actual = MenuProductFixtures.create(saved.getId(), 1L, 2);

        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo("두마리 치킨 콤보"),
                () -> assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal("30000")),
                () -> assertThat(saved.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(saved.getMenuProducts()).usingElementComparatorOnFields(
                                "menuId", "productId", "quantity")
                        .hasSize(1)
                        .containsExactly(actual)
        );
    }

    @Test
    @DisplayName("가격을 설정하지 않고 메뉴를 생성하면 예외가 발생한다")
    void createExceptionWithoutPrice() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO
                .createWithPrice(null);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("0원 이하로 가격을 설정하고 메뉴를 생성하면 예외가 발생한다")
    void createExceptionWrongPrice() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO
                .createWithPrice(new BigDecimal(-1));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹 id로 메뉴를 생성하면 예외가 발생한다")
    void createExceptionWrongMenuGroup() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO
                .createWithMenuGroupId(-1L);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(NotFoundMenuGroupException.class);
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 메뉴를 생성하면 예외가 발생한다")
    void createExceptionWrongMenuProducts() {
        // given
        final MenuProduct menuProduct = MenuProductFixtures.create(1L, -1L, 2);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO
                .createWithMenuProducts(menuProduct);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(NotFoundProductException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품들의 가격합보다 크거나 같게 생성하면 예외가 발생한다")
    void createExceptionWrongPriceWithMenuProductsPriceSum() {
        // given
        final MenuProduct menuProduct = MenuProductFixtures.create(1L, 1L, 2);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO
                .createWithPriceAndMenuProducts(new BigDecimal(50000), menuProduct);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(MenuTotalPriceException.class);
    }

    @Test
    @DisplayName("모든 메뉴를 조회한다")
    void list() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO
                .create();
        final Menu saved = menuDao.save(menu);

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertAll(
                () -> assertThat(menus).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(menus).extracting("id")
                        .contains(saved.getId()),
                () -> assertThat(menus).extracting("menuProducts")
                        .isNotEmpty()
        );
    }
}
