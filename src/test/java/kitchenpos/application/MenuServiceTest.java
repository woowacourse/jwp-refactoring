package kitchenpos.application;

import static kitchenpos.fixtures.TestFixtures.메뉴_그룹_생성;
import static kitchenpos.fixtures.TestFixtures.메뉴_상품_생성_요청;
import static kitchenpos.fixtures.TestFixtures.메뉴_생성_요청;
import static kitchenpos.fixtures.TestFixtures.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("MenuService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_메뉴가_입력되면 extends ServiceTest {

            MenuGroup menuGroup;

            @BeforeEach
            void setUp() {
                menuGroup = menuGroupRepository.save(메뉴_그룹_생성("한마리메뉴"));
                productRepository.save(상품_생성("치킨", BigDecimal.valueOf(1_000L)));
            }

            final MenuProductCreateRequest menuProductCreateRequest = 메뉴_상품_생성_요청(1L, 1L, 5);
            final MenuCreateRequest request = 메뉴_생성_요청("후라이드", BigDecimal.valueOf(1_000L), 1L,
                    List.of(menuProductCreateRequest));

            @Test
            void 해당_메뉴를_반환한다() {
                final MenuResponse actual = menuService.create(request);

                assertThat(actual).isNotNull();
            }
        }

        @Nested
        class 메뉴_가격이_상품들의_합보다_크다면 extends ServiceTest {

            MenuGroup menuGroup;

            @BeforeEach
            void setUp() {
                menuGroup = menuGroupRepository.save(메뉴_그룹_생성("한마리메뉴"));
                productRepository.save(상품_생성("치킨", BigDecimal.valueOf(1_000L)));
            }

            final MenuProductCreateRequest menuProductCreateRequest = 메뉴_상품_생성_요청(1L, 1L, 1);
            final MenuCreateRequest request = 메뉴_생성_요청("후라이드", BigDecimal.valueOf(2_000L), 1L,
                    List.of(menuProductCreateRequest));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 가격이_null인_메뉴가_입력되면 extends ServiceTest {

            private final MenuCreateRequest request = new MenuCreateRequest("후라이드치킨", null,
                    1L, null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 가격이_음수인_메뉴가_입력되면 extends ServiceTest {

            final MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(-1_000L),
                    1L, null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 존재하지_않는_메뉴_그룹으로_메뉴가_입력되면 extends ServiceTest {

            final MenuCreateRequest menuCreateRequest = new MenuCreateRequest("후라이드치킨", BigDecimal.valueOf(1_000L), 1L,
                    null);

            @Test
            void 해당_메뉴를_반환한다() {
                assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출되면 extends ServiceTest {

            @Test
            void 모든_메뉴를_반환한다() {
                final List<MenuResponse> actual = menuService.list();

                assertThat(actual).isEmpty();
            }
        }
    }
}
