package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuServiceTest {
    
    @Nested
    class create_메서드는 {

        @Nested
        class 가격이_null인_경우 extends ServiceTest {

            private final MenuCreateRequest request = new MenuCreateRequest("메뉴", null, 1L, null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 null 혹은 0 미만일 수 없습니다.");
            }
        }

        @Nested
        class 가격이_0_미만일_경우 extends ServiceTest {

            private static final int INVALID_PRICE = -1000;

            private final MenuCreateRequest request = new MenuCreateRequest("메뉴", new BigDecimal(INVALID_PRICE), 1L,
                    null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 null 혹은 0 미만일 수 없습니다.");
            }
        }

        @Nested
        class 입력받은_MenuGroup이_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_MENUGROUP_ID = -1L;

            private final MenuCreateRequest request = new MenuCreateRequest("메뉴", new BigDecimal(1000),
                    NOT_EXIST_MENUGROUP_ID, null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 MenuGroup 입니다.");
            }
        }

        @Nested
        class 입력받은_Product가_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_PRODUCT_ID = -1L;

            private final MenuCreateRequest request = new MenuCreateRequest("메뉴", new BigDecimal(1000), 1L,
                    List.of(new MenuProductRequest(NOT_EXIST_PRODUCT_ID, 1L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 Product 입니다.");
            }
        }

        @Nested
        class 입력받은_가격이_총_금액보다_큰_경우 extends ServiceTest {

            private final MenuCreateRequest request = new MenuCreateRequest("메뉴", new BigDecimal(40000), 1L,
                    List.of(new MenuProductRequest(1L, 1L), new MenuProductRequest(2L, 1L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격이 총 합계 금액보다 클 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_입력을_받을_경우 extends ServiceTest {

            private final MenuCreateRequest request = new MenuCreateRequest("메뉴", new BigDecimal(32000), 1L,
                    List.of(new MenuProductRequest(1L, 1L), new MenuProductRequest(2L, 1L)));

            @Test
            void Menu를_생성하고_반환한다() {
                final Menu actual = menuService.create(request);

                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual.getName()).isEqualTo("메뉴"),
                        () -> assertThat(actual.getPrice()).isEqualByComparingTo(new BigDecimal(32000)),
                        () -> assertThat(actual.getMenuGroupId()).isEqualTo(1L),
                        () -> assertThat(actual.getMenuProducts()).extracting("productId", "quantity")
                                .containsExactly(tuple(1L, 1L), tuple(2L, 1L))
                );
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출하는_경우 extends ServiceTest {

            private static final int EXPECT_SIZE = 6;

            @Test
            void Menu의_목록을_반환한다() {
                final List<Menu> actual = menuService.list();

                assertThat(actual).hasSize(EXPECT_SIZE);
            }
        }
    }
}
