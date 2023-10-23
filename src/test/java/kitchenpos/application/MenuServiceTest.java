package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Nested
    class create_성공_테스트 {

        @Test
        void 메뉴를_생성하다() {
            // given
            productDao.save(new Product("상품_이름1", BigDecimal.valueOf(1000)));
            productDao.save(new Product("상품_이름2", BigDecimal.valueOf(200)));
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(11000L), 1L, Collections.emptyList()));
            final var menuProduct1 = menuProductDao.save(new MenuProduct(1L, 1L, 10));
            final var menuProduct2 = menuProductDao.save(new MenuProduct(1L, 2L, 5));
            final var request = new MenuCreateRequest("메뉴_이름", BigDecimal.valueOf(11000L), 1L,
                    List.of(menuProduct1, menuProduct2));

            // when
            final var actual = menuService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 존재하지_않은_메뉴_그룹을_사용하면_에러를_반환한다() {
            // given
            final var request = new MenuCreateRequest("메뉴_이름", BigDecimal.valueOf(1000), 1L, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 메뉴 그룹입니다.");
        }

        @Test
        void 가격이_존재하지_않으면_에러를_반환한다() {
            // given
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            final var request = new MenuCreateRequest("메뉴_이름", null, 1L, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void 가격이_0보다_작으면_에러를_반환한다() {
            // given
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            final var request = new MenuCreateRequest("메뉴_이름", BigDecimal.valueOf(-1000), 1L, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 메뉴의 금액이 없거나, 음수입니다.");
        }

        @Test
        void 존재하지_않는_상품을_사용하면_에러를_반환한다() {
            // given
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            final var menuProduct1 = new MenuProduct(1L, 1L, 10);
            final var request = new MenuCreateRequest("메뉴_이름", BigDecimal.valueOf(1000), 1L, List.of(menuProduct1));

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 존재하지 않는 상품입니다.");
        }

        @Test
        void 메뉴의_총_가격이_각_상품의_합보다_크면_에러를_반환한다() {
            // given
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            final var request = new MenuCreateRequest("메뉴_이름", BigDecimal.valueOf(1000), 1L, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 총 금액이 각 상품의 합보다 큽니다.");
        }
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 메뉴_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given & when
            final var actual = menuService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 메뉴가_하나_이상_존재하면_메뉴_목록을_반환한다() {
            // given
            menuGroupDao.save(new MenuGroup("메뉴_그룹_이름"));
            final var menu = menuDao.save(new Menu("메뉴_이름", BigDecimal.valueOf(1000), 1L, Collections.emptyList()));
            final var response = MenuResponse.toResponse(menu);
            final var expected = List.of(response);

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
