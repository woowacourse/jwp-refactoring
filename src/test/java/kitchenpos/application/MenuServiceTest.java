package kitchenpos.application;

import static kitchenpos.fixture.Fixture.menuFixture;
import static kitchenpos.fixture.Fixture.menuGroupFixture;
import static kitchenpos.fixture.Fixture.menuProductFixture;
import static kitchenpos.fixture.Fixture.productFixture;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    private Menu menu;

    @BeforeEach
    void setUp() {
        final MenuGroup boonsik = menuGroupDao.save(menuGroupFixture("분식"));
        final Product productD = productDao.save(productFixture(null, "떡볶이", BigDecimal.TEN));
        final Product productS = productDao.save(productFixture(null, "순대", BigDecimal.ONE));
        final Product productT = productDao.save(productFixture(null, "튀김", BigDecimal.TEN));

        final List<MenuProduct> menuProducts = List.of(
                menuProductFixture(null, productD.getId(), 2),
                menuProductFixture(null, productS.getId(), 1),
                menuProductFixture(null, productT.getId(), 1)
        );

        menu = menuFixture("떡순튀", new BigDecimal(31), boonsik.getId(), menuProducts);
    }

    @Nested
    @DisplayName("메뉴 생성 테스트")
    class MenuCreateTest {

        @Test
        @DisplayName("메뉴가 정상적으로 생성된다.")
        void createMenuWithValidData() {
            // Given & When
            Menu createdMenu = menuService.create(menu);

            // then
            assertSoftly(softly -> {
                softly.assertThat(createdMenu.getPrice()).isEqualByComparingTo(menu.getPrice());
                softly.assertThat(createdMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
                softly.assertThat(createdMenu.getMenuProducts().size()).isEqualTo(menu.getMenuProducts().size());
            });
        }

        @Test
        @DisplayName("가격이 null일 때 예외 발생한다.")
        void createMenuWithNullPrice() {
            // given
            menu.setPrice(null);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수일 때 예외 발생한다.")
        void createProductWithSubZeroPrice() {
            // Given
            BigDecimal negativePrice = BigDecimal.valueOf(-10);
            menu.setPrice(negativePrice);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 그룹 ID가 존재하지 않을 때 예외 발생")
        void createProductWithUnExistedMenuGroupId() {
            // given
            menu.setMenuGroupId(999999L);

            // when * then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴의 가격이 메뉴 상품들의 합계보다 클 때 예외 발생")
        void createdMenuWithDifferentPrice() {
            menu.setPrice(new BigDecimal(99));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("모든 메뉴를 반환한다.")
    void listTest() {
        assertThatCode(() -> menuService.list())
                .doesNotThrowAnyException();
    }
}
