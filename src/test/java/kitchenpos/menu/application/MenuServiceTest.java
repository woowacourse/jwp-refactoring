package kitchenpos.menu.application;

import static kitchenpos.support.fixture.MenuFixture.menuRequest;
import static kitchenpos.support.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.support.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.presentation.dto.request.MenuProductRequest;
import kitchenpos.menu.presentation.dto.request.MenuRequest;
import kitchenpos.support.IntegrationServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends IntegrationServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 가격에_null을_입력할_경우 extends IntegrationServiceTest {

            private final BigDecimal NULL_인_가격 = null;

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {
                MenuGroup 메뉴그룹 = menuGroupRepository.save(메뉴_그룹);
                Product 후라이드_치킨 = productRepository.save(product("후라이드 치킨", 18_000));
                MenuProductRequest 후라이드_두마리 = new MenuProductRequest(후라이드_치킨.getId(), 2L);

                menuRequest = MenuRequest.builder()
                        .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                        .price(NULL_인_가격)
                        .menuGroup(메뉴그룹)
                        .menuProductRequests(후라이드_두마리)
                        .build();
            }

            @Test
            void 예외가_발생한다() {

                assert_throws_create("가격은 양의 정수이어야 합니다.", menuRequest);
            }
        }

        @Nested
        class 가격에_음수를_입력될_경우 extends IntegrationServiceTest {

            private final int 음수인_가격 = -1;

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {
                MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹);
                Product 후라이드_치킨 = productRepository.save(product("후라이드 치킨", 18_000));
                MenuProductRequest 후라이드_두마리 = new MenuProductRequest(후라이드_치킨.getId(), 2L);

                menuRequest = MenuRequest.builder()
                        .name("치킨은 살 안쪄요, 살은 내가 쪄요")
                        .intPrice(음수인_가격)
                        .menuGroup(menuGroup)
                        .menuProductRequests(후라이드_두마리)
                        .build();
            }

            @Test
            void 예외가_발생한다() {

                assert_throws_create("가격은 양의 정수이어야 합니다.", menuRequest);
            }
        }

        @Nested
        class 없는_메뉴_그룹의_ID가_입력될_경우 extends IntegrationServiceTest {

            private final long NOT_EXIST_MENU_GROUP_ID = -1L;

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {
                Product 후라이드_치킨 = productRepository.save(product("후라이드 치킨", 18_000));
                MenuProductRequest 후라이드_두마리 = new MenuProductRequest(후라이드_치킨.getId(), 2L);

                menuRequest = MenuRequest.builder()
                        .name("양념 치킨 메뉴")
                        .intPrice(16_000)
                        .menuGroupId(NOT_EXIST_MENU_GROUP_ID)
                        .menuProductRequests(List.of(후라이드_두마리))
                        .build();
            }

            @Test
            void 예외가_발생한다() {

                assert_throws_create("존재하지 않는 메뉴 그룹의 ID입니다.", menuRequest);
            }
        }

        @Nested
        class 메뉴상품의_상품ID가_존재하지_않는_경우 extends IntegrationServiceTest {

            private static final long 존재하지않는_상품ID = -1L;

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {
                MenuGroup 메뉴그룹 = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
                productRepository.save(new Product("후라이드", BigDecimal.valueOf(16_000)));
                MenuProductRequest 존재하지않는_메뉴상품 = new MenuProductRequest(존재하지않는_상품ID, 1);

                menuRequest = MenuRequest.builder()
                        .menuGroup(메뉴그룹)
                        .menuProductRequests(존재하지않는_메뉴상품)
                        .intPrice(16_000)
                        .build();
            }

            @Test
            void 예외가_발생한다() {

                assert_throws_create("메뉴상품의 상품ID가 존재하지 않습니다.", menuRequest);
            }
        }

        @Nested
        class 메뉴_가격이_상품가격과_메뉴상품양의_곱보다_큰_경우 extends IntegrationServiceTest {

            private final int LARGER_MENU_PRICE = 40_000;

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {

                MenuGroup 메뉴그룹 = menuGroupRepository.save(메뉴_그룹);

                Product 후라이드_치킨 = productRepository.save(product("후라이드 치킨", 18_000));
                Product 간장_치킨 = productRepository.save(product("간장 치킨", 19_000));

                menuRequest = MenuRequest.builder()
                        .name("간장 후라이드 세트 메뉴")
                        .intPrice(LARGER_MENU_PRICE)
                        .menuGroup(메뉴그룹)
                        .menuProductRequests(new MenuProductRequest(후라이드_치킨.getId(), 1L),
                                new MenuProductRequest(간장_치킨.getId(), 1L))
                        .build();
            }

            @Test
            void 예외가_발생한다() {

                assert_throws_create("메뉴의 가격은 상품들의 총 가격보다 클 수 없습니다.", menuRequest);
            }

        }

        @Nested
        class 정상적으로_메뉴를_생성가능한_경우 extends IntegrationServiceTest {

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {

                MenuGroup 메뉴그룹 = menuGroupRepository.save(메뉴_그룹);

                Product 후라이드_치킨 = productRepository.save(product("후라이드 치킨", 18_000));
                Product 양념_치킨 = productRepository.save(product("양념 치킨", 19_000));

                MenuProductRequest 후라이드_두마리 = new MenuProductRequest(후라이드_치킨.getId(), 2L);
                MenuProductRequest 양념_한마리 = new MenuProductRequest(양념_치킨.getId(), 1L);

                menuRequest = MenuRequest.builder()
                        .name("[할인] 후라이드 양념 치킨 세 마리 세트")
                        .intPrice(50_000) // 정가 18,000 * 2 + 19,000 = 55,000원
                        .menuProductRequests(후라이드_두마리, 양념_한마리)
                        .menuGroup(메뉴그룹)
                        .build();
            }

            @Test
            void 저장된_메뉴가_반환된다() {

                Menu savedMenu = menuService.create(menuRequest);

                assertAll(
                        () -> assertThat(savedMenu).isNotNull(),
                        () -> assertThat(menuRequest.getId()).isNull(),
                        () -> assertThat(savedMenu.getId()).isNotNull()
                );
            }
        }

        private void assert_throws_create(final String message, final MenuRequest menuRequest) {

            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(message);
        }
    }

    @Nested
    class list_메서드는 extends IntegrationServiceTest {

        @BeforeEach
        void setUp() {

            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("두마리메뉴"));

            Product product1 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16_000)));
            Product product2 = productRepository.save(new Product("양념치킨", BigDecimal.valueOf(16_000)));
            Product product3 = productRepository.save(new Product("반반치킨", BigDecimal.valueOf(16_000)));

            menuService.create(menuRequest(menuGroup, product1, 16_000));
            menuService.create(menuRequest(menuGroup, product2, 16_000));
            menuService.create(menuRequest(menuGroup, product3, 16_000));
        }

        @Test
        void 메뉴목록을_반환한다() {

            List<Menu> actual = menuService.list();

            assertThat(actual).hasSize(3);
        }
    }
}
