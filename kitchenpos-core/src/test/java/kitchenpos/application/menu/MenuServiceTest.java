package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.application.ServiceTest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Nested
    class 메뉴_등록 {

        @Test
        void 메뉴를_등록할_수_있다() {
            // given
            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            final var request = MenuFixture.메뉴요청_생성(menu);

            // when
            final var actual = menuService.create(request);

            // then
            final var expected = MenuResponse.toResponse(menu);
            assertThat(actual).usingRecursiveComparison()
                    .comparingOnlyFields("name", "price")
                    .isEqualTo(expected);
            assertThat(actual.getMenuProducts()).usingRecursiveComparison()
                    .comparingOnlyFields("quantity")
                    .isEqualTo(expected.getMenuProducts());
        }

        @Test
        void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var request = MenuFixture.메뉴요청_망고치킨_N원_생성(-1, savedMenuGroup, menuProduct1, menuProduct2);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
            // given
            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);

            final var invalidMenuGroup = MenuGroupFixture.메뉴그룹_존재X();
            final var menu = MenuFixture.메뉴_망고치킨_17000원(invalidMenuGroup, menuProduct1, menuProduct2);
            final var request = MenuFixture.메뉴요청_생성(menu);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_상품이_존재하지_않으면_예외가_발생한다() {
            // given
            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var invalidProduct = ProductFixture.상품_존재X();
            final var menuProduct = MenuProductFixture.메뉴상품_생성(invalidProduct, 2L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct);
            final var request = MenuFixture.메뉴요청_생성(menu);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격과_메뉴_상품의_총_가격이_다르면_예외가_발생한다() {
            // given
            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var invalidProduct = ProductFixture.상품_망고_N원(0);
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(invalidProduct, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(invalidProduct, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var request = MenuFixture.메뉴요청_망고치킨_N원_생성(17_000, savedMenuGroup, menuProduct1, menuProduct2);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_목록_조회 {

        @Test
        void 메뉴_목록을_조회할_수_있다() {
            // given
            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            단일_메뉴_저장(menu);

            // when
            final var actual = menuService.list();

            // then
            final var expected = Collections.singletonList(MenuResponse.toResponse(menu));
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }
}
