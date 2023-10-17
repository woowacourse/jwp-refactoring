package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static kitchenpos.fixture.FixtureFactory.메뉴_그룹_생성;
import static kitchenpos.fixture.FixtureFactory.메뉴_상품_생성;
import static kitchenpos.fixture.FixtureFactory.메뉴_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_저장할_수_있다() {
        // given
        final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("피자"));
        final Product product = productDao.save(new Product("치즈피자", new BigDecimal(3_000)));
        final MenuProduct menuProduct = 메뉴_상품_생성(menuGroup.getId(), product.getId(), 3);

        final Menu expected = 메뉴_생성("치즈피자", new BigDecimal(5_000), menuGroup.getId(), List.of(menuProduct));

        // when
        final Menu actual = menuService.create(expected);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("치즈피자"),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(new BigDecimal(5_000)),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(actual.getMenuProducts()).hasSize(1)
        );
    }

    @Nested
    class 메뉴_생성_실패 {

        @Test
        void 금액이_지정되지_않았다면_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("피자"));
            final Product product = productDao.save(new Product("치즈피자", new BigDecimal(3_000)));
            final MenuProduct menuProduct = 메뉴_상품_생성(menuGroup.getId(), product.getId(), 3);

            final Menu expected = 메뉴_생성("치즈피자", null, menuGroup.getId(), List.of(menuProduct));

            // expected
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 금액이_0보다_작다면_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("피자"));
            final Product product = productDao.save(new Product("치즈피자", new BigDecimal(3_000)));
            final MenuProduct menuProduct = 메뉴_상품_생성(menuGroup.getId(), product.getId(), 3);

            final Menu expected = 메뉴_생성("치즈피자", new BigDecimal(-1), menuGroup.getId(), List.of(menuProduct));

            // expected
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_지정되지_않았다면_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("피자"));
            final Product product = productDao.save(new Product("치즈피자", new BigDecimal(3_000)));
            final MenuProduct menuProduct = 메뉴_상품_생성(menuGroup.getId(), product.getId(), 3);

            final Menu expected = 메뉴_생성("치즈피자", new BigDecimal(-1), null, List.of(menuProduct));

            // expect
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_상품에_상품이_지정되지_않았다면_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("피자"));
            final MenuProduct menuProduct = 메뉴_상품_생성(menuGroup.getId(), null, 3);

            final Menu expected = 메뉴_생성("치즈피자", new BigDecimal(-1), null, List.of(menuProduct));

            // expect
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격의_종합이_상품_가격보다_크다면_예외가_발생한다() {
            // given
            final MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("피자"));
            final Product product = productDao.save(new Product("치즈피자", new BigDecimal(3_000)));
            final MenuProduct menuProduct = 메뉴_상품_생성(menuGroup.getId(), product.getId(), 1);

            final Menu expected = 메뉴_생성("치즈피자", new BigDecimal(5_000), menuGroup.getId(), List.of(menuProduct));

            // expect
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 메뉴_목록을_조회할_수_있다() {
        // when
        final List<Menu> actual = menuService.list();

        // then
        assertThat(actual).hasSize(6);
    }

}
