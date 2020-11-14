package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 엔티티")
class MenuTest {
    @Nested
    @DisplayName("초기 생성 팩토리 메서드는")
    class Create {
        private Menu subject() {
            return Menu.create(name, price, menuGroupId, menuProducts, publisher);
        }

        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;
        private ApplicationEventPublisher publisher;

        @Nested
        @DisplayName("메뉴 이름, 메뉴 가격, 메뉴 그룹 id, 메뉴 상품의 리스트, 이벤트 퍼블리셔가 주어지면")
        class WhenGivenThese {
            @BeforeEach
            void setUp() {
                name = "후라이드";
                price = BigDecimal.valueOf(10000);
                menuGroupId = 1L;
                menuProducts = Arrays.asList(createMenuProduct(1L, 1L, 1, null));
                publisher = new CustomEventPublisher.AlwaysPass();
            }

            @Test
            @DisplayName("id가 null인 메뉴를 생성한다")
            void create() {
                Menu menu = subject();
                assertThat(menu.getId()).isNull();
                assertThat(menu.getName()).isEqualTo("후라이드");
                assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(10000));
                assertThat(menu.getMenuGroupId()).isEqualTo(1L);
                assertThat(menu.getMenuProducts()).hasSize(1);
            }
        }

        @Nested
        @DisplayName("가격이 음수라면")
        class WithNegativePrice {
            @BeforeEach
            void setUp() {
                name = "후라이드";
                price = BigDecimal.valueOf(-10000);
                menuGroupId = 1L;
                menuProducts = Arrays.asList(createMenuProduct(1L, 1L, 1, null));
                publisher = new CustomEventPublisher.AlwaysPass();
            }

            @Test
            @DisplayName("예외가 발생한다")
            void create() {
                assertThatThrownBy(Create.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("가격이 null이라면")
        class WithNullPrice {
            @BeforeEach
            void setUp() {
                name = "후라이드";
                price = null;
                menuGroupId = 1L;
                menuProducts = Arrays.asList(createMenuProduct(1L, 1L, 1, null));
                publisher = new CustomEventPublisher.AlwaysPass();
            }

            @Test
            @DisplayName("예외가 발생한다")
            void create() {
                assertThatThrownBy(Create.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴 가격의 유효성 검증이 실패한다면")
        class WithInvalidPrice {
            @BeforeEach
            void setUp() {
                name = "후라이드";
                price = BigDecimal.valueOf(10000);
                menuGroupId = 1L;
                menuProducts = Arrays.asList(createMenuProduct(1L, 1L, 1, null));
                publisher = new CustomEventPublisher.AlwaysFail();
            }

            @Test
            @DisplayName("예외가 발생한다")
            void create() {
                assertThatThrownBy(Create.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("엔티티를 불러오는 생성 메서드는")
    class From {
        private Menu subject() {
            return Menu.from(id, name, price, menuGroupId);
        }

        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;

        @Nested
        @DisplayName("메뉴 id, 메뉴 이름, 메뉴 가격, 메뉴 그룹 id가 주어지면")
        class WithTheseGiven {
            @BeforeEach
            void setUp() {
                id = 1L;
                name = "후라이드";
                price = BigDecimal.valueOf(10000);
                menuGroupId = 2L;
            }

            @Test
            @DisplayName("메뉴 엔티티를 생성한다")
            void createMenus() {
                Menu menu = subject();

                assertThat(menu.getId()).isEqualTo(1L);
                assertThat(menu.getName()).isEqualTo("후라이드");
                assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(10000));
                assertThat(menu.getMenuGroupId()).isEqualTo(2L);
                assertThat(menu.getMenuProducts()).isEmpty();
            }
        }
    }
}