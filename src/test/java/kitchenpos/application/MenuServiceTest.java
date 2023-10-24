package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.CHICKEN_MENU_PRODUCT;
import static kitchenpos.fixture.MenuFixture.CHICKEN_MENU_PRODUCT_REQUEST;
import static kitchenpos.fixture.MenuFixture.CHICKEN_SET_MENU_REQUEST;
import static kitchenpos.fixture.MenuFixture.createChickenSetMenuById;
import static kitchenpos.fixture.MenuGroupFixture.createChickenSetMenuGroupById;
import static kitchenpos.fixture.ProductFixture.createChickenProductById;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        productDao.save(createChickenProductById(null));
        menuGroupDao.save(createChickenSetMenuGroupById(null));
    }

    @Nested
    class create_메서드는 {

        @Test
        void 메뉴를_생성한다() {
            // when
            final Menu createdMenu = menuService.create(CHICKEN_SET_MENU_REQUEST);

            // then
            assertThat(createdMenu)
                    .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .usingRecursiveComparison()
                    .isEqualTo(createChickenSetMenuById(createdMenu.getId()));
        }

        @Test
        void
        가격이_null이면_예외가_발생한다() {
            // given
            final MenuRequest invalidMenuRequest = new MenuRequest(
                    "메뉴",
                    null,
                    1L,
                    List.of(CHICKEN_MENU_PRODUCT_REQUEST)
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(invalidMenuRequest));
        }

        @Test
        void 가격이_음수이면_예외가_발생한다() {
            // given
            final MenuRequest invalidMenuRequest = new MenuRequest(
                    "메뉴",
                    BigDecimal.valueOf(-100),
                    1L,
                    List.of(CHICKEN_MENU_PRODUCT_REQUEST)
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(invalidMenuRequest));
        }

        @Test
        void 메뉴의_그룹이_존재하지_않으면_예외가_발생한다() {
            // given
            final long nonExistMenuGroupId = 99L;
            final MenuRequest invalidMenuRequest = new MenuRequest(
                    "메뉴",
                    BigDecimal.valueOf(10000),
                    nonExistMenuGroupId,
                    List.of(CHICKEN_MENU_PRODUCT_REQUEST)
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(invalidMenuRequest));
        }

        @Test
        void 메뉴의_상품이_존재하지_않으면_예외가_발생한다() {
            // given
            final long nonExistProductId = 99L;
            final MenuProductRequest invalidMenuProductRequest = new MenuProductRequest(nonExistProductId, 1L);
            final MenuRequest invalidMenuRequest = new MenuRequest(
                    "메뉴",
                    BigDecimal.valueOf(10000),
                    1L,
                    List.of(invalidMenuProductRequest)
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(invalidMenuRequest));
        }

        @Test
        void 메뉴의_가격이_상품의_총합보다_크면_예외가_발생한다() {
            // given
            final MenuRequest invalidMenuRequest = new MenuRequest(
                    "메뉴",
                    BigDecimal.valueOf(99999),
                    1L,
                    List.of(CHICKEN_MENU_PRODUCT_REQUEST)
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(invalidMenuRequest));
        }
    }

    @Test
    void list_메서드는_모든_메뉴를_조회한다() {
        // given
        final Menu createdMenu1 = menuService.create(CHICKEN_SET_MENU_REQUEST);
        final Menu createdMenu2 = menuService.create(CHICKEN_SET_MENU_REQUEST);

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertThat(menus)
                .usingRecursiveComparison()
                .isEqualTo(List.of(createdMenu1, createdMenu2));
    }
}
