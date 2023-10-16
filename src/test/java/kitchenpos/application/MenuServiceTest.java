package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @Nested
    class create_성공_테스트 {

        @Test
        void 메뉴를_생성하다() {
            // given
            final var product1 = new Product("상품_이름1", BigDecimal.valueOf(1000));
            final var product2 = new Product("상품_이름2", BigDecimal.valueOf(200));
            final var menuProduct1 = new MenuProduct(1L, 1L, 10);
            final var menuProduct2 = new MenuProduct(1L, 2L, 5);
            final var menu = new Menu("메뉴_이름", BigDecimal.valueOf(11000L), 1L, List.of(menuProduct1, menuProduct2));

            given(productDao.findById(1L)).willReturn(Optional.of(product1));
            given(productDao.findById(2L)).willReturn(Optional.of(product2));
            given(menuGroupDao.existsById(1L)).willReturn(true);
            given(menuDao.save(menu)).willReturn(menu);
            given(menuProductDao.save(menuProduct1)).willReturn(menuProduct1);
            given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);

            // when
            final var actual = menuService.create(menu);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(menu);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 가격이_존재하지_않으면_에러를_반환한다() {
            // given
            final var menu = new Menu("메뉴_이름", null, 1L, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 메뉴의 금액이 없거나, 음수입니다.");
        }

        @Test
        void 가격이_0보다_작으면_에러를_반환한다() {
            // given
            final var menu = new Menu("메뉴_이름", BigDecimal.valueOf(-1000), 1L, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 메뉴의 금액이 없거나, 음수입니다.");
        }

        @Test
        void 존재하지_않은_메뉴_그룹을_사용하면_에러를_반환한다() {
            // given
            final var menu = new Menu("메뉴_이름", BigDecimal.valueOf(1000), 1L, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 메뉴 그룹입니다.");
        }

        @Test
        void 존재하지_않는_상품을_사용하면_에러를_반환한다() {
            // given
            final var menuProduct1 = new MenuProduct(1L, 1L, 10);
            final var menu = new Menu("메뉴_이름", BigDecimal.valueOf(1000), 1L, List.of(menuProduct1));

            given(menuGroupDao.existsById(1L)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 상품입니다.");
        }

        @Test
        void 메뉴의_총_가격이_각_상품의_합보다_크면_에러를_반환한다() {
            // given
            final var menu = new Menu("메뉴_이름", BigDecimal.valueOf(1000), 1L, Collections.emptyList());

            given(menuGroupDao.existsById(1L)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 총 금액이 각 상품의 합보다 큽니다.");
        }
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 메뉴_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given
            given(menuService.list()).willReturn(Collections.emptyList());

            // when
            final var actual = menuService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 메뉴가_하나_이상_존재하면_메뉴_목록을_반환한다() {
            // given
            final var menu = new Menu("메뉴_이름", BigDecimal.valueOf(1000), 1L, Collections.emptyList());
            given(menuService.list()).willReturn(List.of(menu));

            final var expected = List.of(menu);

            // when
            final var actual = menuService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class list_실패_테스트 {
    }
}
