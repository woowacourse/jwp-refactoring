package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
class MenuServiceTest {

    private final MenuService menuService;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    private Product 저장된_양념_치킨;
    private Product 저장된_후라이드_치킨;
    private MenuProduct 메뉴_상품_1;
    private MenuProduct 메뉴_상품_2;
    private MenuGroup 저장된_메뉴_그룹;

    public MenuServiceTest(final MenuService menuService,
                           final MenuGroupDao menuGroupDao,
                           final ProductDao productDao) {
        this.menuService = menuService;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @BeforeEach
    void setUp() {
        final MenuGroup 메뉴_그룹 = 메뉴_그룹(null, "양념 반 후라이드 반");
        final Product 양념_치킨 = 상품(null, "양념 치킨", BigDecimal.valueOf(12000, 2));
        final Product 후라이드_치킨 = 상품(null, "후라이드 치킨", BigDecimal.valueOf(10000, 2));
        저장된_양념_치킨 = productDao.save(양념_치킨);
        저장된_후라이드_치킨 = productDao.save(후라이드_치킨);
        메뉴_상품_1 = 메뉴_상품(null, null, 저장된_양념_치킨.getId(), 1);
        메뉴_상품_2 = 메뉴_상품(null, null, 저장된_후라이드_치킨.getId(), 1);
        저장된_메뉴_그룹 = menuGroupDao.save(메뉴_그룹);
    }

    @Nested
    class 메뉴_등록_시 {

        @Test
        void 메뉴를_정상적으로_등록한다() {
            // given
            final Menu 메뉴 = 메뉴(null, "메뉴", BigDecimal.valueOf(22000, 2), 저장된_메뉴_그룹.getId(), List.of(메뉴_상품_1, 메뉴_상품_2));

            // when
            final Menu 저장된_메뉴 = menuService.create(메뉴);

            // then
            assertAll(
                    () -> assertThat(저장된_메뉴.getId()).isNotNull(),
                    () -> assertThat(저장된_메뉴).usingRecursiveComparison()
                            .ignoringFields("id", "menuProducts.seq")
                            .isEqualTo(메뉴)
            );
        }

        @Test
        void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            final Menu 메뉴 = 메뉴(null, "메뉴", BigDecimal.valueOf(-22000, 2), 저장된_메뉴_그룹.getId(), List.of(메뉴_상품_1, 메뉴_상품_2));

            // expected
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_없으면_예외가_발생한다() {
            // given
            final Menu 메뉴 = 메뉴(null, "메뉴", null, 저장된_메뉴_그룹.getId(), List.of(메뉴_상품_1, 메뉴_상품_2));

            // expected
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
            // given
            final Menu 메뉴 = 메뉴(null, "메뉴", BigDecimal.valueOf(22000, 2), 저장된_메뉴_그룹.getId() + 1,
                    List.of(메뉴_상품_1, 메뉴_상품_2));

            // expected
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품이_존재하지_않으면_예외가_발생한다() {
            // given
            final Menu 메뉴 = 메뉴(null, "메뉴", BigDecimal.valueOf(22000, 2), 저장된_메뉴_그룹.getId(),
                    List.of(메뉴_상품_1, 메뉴_상품(null, null, 메뉴_상품_2.getProductId() + 1, 1)));

            // expected
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 입력한_가격이_상품들의_가격합_보다_크면_예외가_발생한다() {
            // given
            final Menu 메뉴 = 메뉴(null, "메뉴", BigDecimal.valueOf(23000, 2), 저장된_메뉴_그룹.getId(), List.of(메뉴_상품_1, 메뉴_상품_2));

            // expected
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴_목록을_정상적으로_조회한다() {
        // given
        final Menu 메뉴1 = 메뉴(null, "메뉴1", BigDecimal.valueOf(22000, 2), 저장된_메뉴_그룹.getId(), List.of(메뉴_상품_1, 메뉴_상품_2));

        final MenuGroup 저장된_메뉴_그룹2 = menuGroupDao.save(메뉴_그룹(null, "양념 2개"));
        final MenuProduct 메뉴2_상품 = 메뉴_상품(null, null, 저장된_양념_치킨.getId(), 2);
        final Menu 메뉴2 = 메뉴(null, "메뉴2", BigDecimal.valueOf(24000, 2), 저장된_메뉴_그룹2.getId(), List.of(메뉴2_상품));

        final Menu 저장된_메뉴1 = menuService.create(메뉴1);
        final Menu 저장된_메뉴2 = menuService.create(메뉴2);

        // when
        final List<Menu> 메뉴들 = menuService.list();

        // then
        assertThat(메뉴들).usingRecursiveComparison()
                .isEqualTo(List.of(저장된_메뉴1, 저장된_메뉴2));
    }
}
