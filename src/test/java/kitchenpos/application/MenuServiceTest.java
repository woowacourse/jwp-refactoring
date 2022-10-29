package kitchenpos.application;

import static kitchenpos.support.MenuFixture.menuRequest;
import static kitchenpos.support.MenuGroupFixture.menuGroup;
import static kitchenpos.support.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.support.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.support.IntegrationServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends IntegrationServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 가격에_null을_입력할_경우 extends IntegrationServiceTest {

            private final BigDecimal NULL_PRICE = null;

            private final MenuRequest menuRequest = menuRequest(NULL_PRICE);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menuRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수이어야 합니다.");
            }
        }

        @Nested
        class 가격에_음수를_입력될_경우 extends IntegrationServiceTest {

            private final int MINUS_PRICE = -1;

            private final MenuRequest menuRequest = menuRequest(MINUS_PRICE);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> menuService.create(menuRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 양의 정수이어야 합니다.");
            }
        }

        @Nested
        class 없는_메뉴_그룹의_ID가_입력될_경우 extends IntegrationServiceTest {

            private final long NOT_EXIST_MENU_GROUP_ID = -1L;

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {
                final Product 양념_치칸 = productRepository.save(product("양념 치칸", 16_000));

                menuRequest = MenuRequest.builder()
                        .name("양념 치킨 메뉴")
                        .intPrice(16_000)
                        .menuGroup(menuGroup(NOT_EXIST_MENU_GROUP_ID))
                        .menuProducts(List.of(new MenuProduct(양념_치칸, 1L)))
                        .build();
            }

            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> menuService.create(menuRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 메뉴 그룹의 ID입니다.");
            }
        }

        @Nested
        class 메뉴상품의_상품ID가_존재하지_않는_경우 extends IntegrationServiceTest {

            private static final long NOT_EXIST_MENU_PRODUCT_NUMBER = -1L;

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {
                MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("두마리메뉴"));
                productRepository.save(new Product("후라이드", BigDecimal.valueOf(16_000)));
                final Product 존재하지않는_product = product(NOT_EXIST_MENU_PRODUCT_NUMBER, "존재 하지 않는 Product", 16_000);
                final MenuProduct 존재하지않는_menuProduct = new MenuProduct(존재하지않는_product, 1);

                menuRequest = MenuRequest.builder()
                        .menuGroup(menuGroup)
                        .menuProducts(존재하지않는_menuProduct)
                        .intPrice(16_000)
                        .build();
            }

            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> menuService.create(menuRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴상품의 상품ID가 존재하지 않습니다.");
            }
        }

        @Nested
        class 메뉴_가격이_상품가격과_메뉴상품양의_곱보다_큰_경우 extends IntegrationServiceTest {

            private final int LARGER_MENU_PRICE = 40_000;

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {

                final MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹);

                Product 후라이드_치킨 = productRepository.save(product("후라이드 치킨", 18_000));
                Product 간장_치킨 = productRepository.save(product("간장 치킨", 19_000));

                menuRequest = MenuRequest.builder()
                        .name("간장 후라이드 세트 메뉴")
                        .intPrice(LARGER_MENU_PRICE)
                        .menuGroup(menuGroup)
                        .menuProducts(new MenuProduct(후라이드_치킨, 1L), new MenuProduct(간장_치킨, 1L))
                        .build();
            }

            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> menuService.create(menuRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("메뉴의 가격은 상품들의 총 가격보다 클 수 없습니다.");
            }
        }

        @Nested
        class 정상적으로_메뉴를_생성가능한_경우 extends IntegrationServiceTest {

            private MenuRequest menuRequest;

            @BeforeEach
            void setUp() {

                final MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹);
                final Product 후라이드_치킨 = productRepository.save(product("후라이드 치킨", 18_000));
                final Product 양념_치킨 = productRepository.save(product("양념 치킨", 19_000));
                final MenuProduct 후라이드_두마리 = new MenuProduct(후라이드_치킨, 2L);
                final MenuProduct 양념_한마리 = new MenuProduct(양념_치킨, 1L);

                menuRequest = MenuRequest.builder()
                        .name("[할인] 후라이드 양념 치킨 세 마리 세트")
                        .intPrice(50_000) // 정가 18,000 * 2 + 19,000 = 55,000원
                        .menuProducts(후라이드_두마리, 양념_한마리)
                        .menuGroup(menuGroup)
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
    }

    @Nested
    class list_메서드는 extends IntegrationServiceTest {

        @BeforeEach
        void setUp() {

            final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("두마리메뉴"));

            final Product product1 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(16_000)));
            final Product product2 = productRepository.save(new Product("양념치킨", BigDecimal.valueOf(16_000)));
            final Product product3 = productRepository.save(new Product("반반치킨", BigDecimal.valueOf(16_000)));

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
