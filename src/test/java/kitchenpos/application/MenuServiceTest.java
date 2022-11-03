package kitchenpos.application;

import static kitchenpos.fixture.Fixture.양념치킨상품_생성요청;
import static kitchenpos.fixture.Fixture.한마리메뉴_생성요청;
import static kitchenpos.fixture.Fixture.후라이드상품_생성요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.application.request.MenuCreateRequest;
import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MenuServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 가격이_null인_경우 extends ServiceTest {

            private MenuCreateRequest request;

            @BeforeEach
            void setUp() {
                final MenuGroup menuGroup = menuGroupService.create(한마리메뉴_생성요청);
                request = new MenuCreateRequest("메뉴", null, menuGroup.getId(), new ArrayList<>());
            }

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

            private MenuCreateRequest request;

            @BeforeEach
            void setUp() {
                final MenuGroup menuGroup = menuGroupService.create(한마리메뉴_생성요청);
                request = new MenuCreateRequest("메뉴", new BigDecimal(INVALID_PRICE), menuGroup.getId(),
                        new ArrayList<>());
            }

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
                    NOT_EXIST_MENUGROUP_ID, new ArrayList<>());

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

            private MenuCreateRequest request;

            @BeforeEach
            void setUp() {
                final MenuGroup menuGroup = menuGroupService.create(한마리메뉴_생성요청);
                request = new MenuCreateRequest("메뉴", new BigDecimal(1000), menuGroup.getId(),
                        List.of(new MenuProductRequest(NOT_EXIST_PRODUCT_ID, 1L)));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 Product가 존재합니다.");
            }
        }

        @Nested
        class 입력받은_가격이_총_금액보다_큰_경우 extends ServiceTest {

            private MenuCreateRequest request;

            @BeforeEach
            void setUp() {
                final MenuGroup menuGroup = menuGroupService.create(한마리메뉴_생성요청);
                final Product product1 = productService.create(후라이드상품_생성요청);
                final Product product2 = productService.create(양념치킨상품_생성요청);
                request = new MenuCreateRequest("메뉴", new BigDecimal(40000), menuGroup.getId(),
                        List.of(new MenuProductRequest(product1.getId(), 1L),
                                new MenuProductRequest(product2.getId(), 1L)));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Menu 가격은 Product 가격의 합을 초과할 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_입력을_받을_경우 extends ServiceTest {

            private MenuCreateRequest request;

            @BeforeEach
            void setUp() {
                final MenuGroup menuGroup = menuGroupService.create(한마리메뉴_생성요청);
                final Product product1 = productService.create(후라이드상품_생성요청);
                final Product product2 = productService.create(양념치킨상품_생성요청);
                request = new MenuCreateRequest("메뉴", new BigDecimal(32000), menuGroup.getId(),
                        List.of(new MenuProductRequest(product1.getId(), 1L),
                                new MenuProductRequest(product2.getId(), 1L)));
            }

            @Test
            void Menu를_생성하고_반환한다() {
                final Menu actual = menuService.create(request);
                System.out.println(actual.getId());
                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual.getName()).isEqualTo("메뉴"),
                        () -> assertThat(actual.getMenuPrice().getValue()).isEqualByComparingTo(new BigDecimal(32000)),
                        () -> assertThat(actual.getMenuGroupId()).isEqualTo(1L),
                        () -> assertThat(actual.getMenuProducts().getValue())
                                .extracting(MenuProduct::getProductId, MenuProduct::getQuantity)
                                .containsExactly(tuple(1L, 1L), tuple(2L, 1L))
                );
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출하는_경우 extends ServiceTest {

            private static final int EXPECT_SIZE = 1;

            private MenuCreateRequest request;

            @BeforeEach
            void setUp() {
                final MenuGroup menuGroup = menuGroupService.create(한마리메뉴_생성요청);
                final Product product1 = productService.create(후라이드상품_생성요청);
                final Product product2 = productService.create(양념치킨상품_생성요청);
                request = new MenuCreateRequest("메뉴", new BigDecimal(32000), menuGroup.getId(),
                        List.of(new MenuProductRequest(product1.getId(), 1L),
                                new MenuProductRequest(product2.getId(), 1L)));
            }

            @Test
            void Menu의_목록을_반환한다() {
                menuService.create(request);

                final List<Menu> actual = menuService.list();

                assertThat(actual).hasSize(EXPECT_SIZE);
            }
        }
    }
}
