package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    private MenuGroup menuGroup;
    private Product productA;
    private Product productB;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private MenuProduct menuProductA;
    private MenuProduct menuProductB;

    @BeforeEach
    void setUpForMenu() {
        menuGroup = menuGroupDao.save(new MenuGroup("순살 두 마리"));
        productA = productDao.save(new Product("순살 까르보치킨", new BigDecimal(20000)));
        productB = productDao.save(new Product("순살 짜장치킨", new BigDecimal(19000)));
        name = "순살 까르보 한 마리 + 순살 짜장 한 마리";
        price = new BigDecimal(35000);
        menuGroupId = menuGroup.getId();
        menuProductA = new MenuProduct(null, productA.getId(), 1L);
        menuProductB = new MenuProduct(null, productB.getId(), 1L);
    }

    @DisplayName("메뉴를 생성할 수 있다")
    @Test
    void create() {
        // given
        final var menuRequest = new Menu(name, price, menuGroupId, List.of(menuProductA, menuProductB));

        // when
        final var menu = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName()).isEqualTo(name),
                () -> assertThat(menu.getPrice().doubleValue()).isEqualTo(price.doubleValue()),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroupId),
                () -> assertThat(menuProductA.getMenuId()).isEqualTo(menu.getId()),
                () -> assertThat(menuProductB.getMenuId()).isEqualTo(menu.getId())
        );
    }

    @DisplayName("create 메서드는")
    @Nested
    class Create {
        @DisplayName("price가 null일 경우 예외가 발생한다")
        @Test
        void should_fail_when_price_is_null() {
            // given
            final var menuRequest = new Menu(name, null, menuGroupId, List.of(menuProductA, menuProductB));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> menuService.create(menuRequest)
            );
        }

        @DisplayName("price가 음수일 경우 예외가 발생한다")
        @Test
        void should_fail_when_price_is_less_than_zero() {
            // given
            final var menuRequest = new Menu(name, new BigDecimal(-30000), menuGroupId,
                    List.of(menuProductA, menuProductB));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> menuService.create(menuRequest)
            );
        }

        @DisplayName("메뉴 그룹 아이디가 유효하지 않으면 예외가 발생한다")
        @Test
        void should_fail_when_menuGroupId_is_invalid() {
            // given
            final var menuRequest = new Menu(name, price, -1L, List.of(menuProductA, menuProductB));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> menuService.create(menuRequest)
            );
        }

        @DisplayName("menuProducts가 null이면 예외가 발생한다")
        @Test
        void should_fail_when_menuProducts_is_null() {
            // given
            final var menuRequest = new Menu(name, price, menuGroupId, null);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> menuService.create(menuRequest)
            );
        }

        @DisplayName("menuProducts가 비어있으면 예외가 발생한다")
        @Test
        void should_fail_when_menuProducts_is_empty() {
            // given
            final var menuRequest = new Menu(name, price, -1L, new ArrayList<>());

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> menuService.create(menuRequest)
            );
        }

        @DisplayName("price가 menuProducts 총합계액 보다 크면 예외가 발생한다")
        @Test
        void should_fail_when_price_is_greater_than_sum_of_menuProducts_price() {
            // given
            final var menuRequest = new Menu(name, new BigDecimal(40000), menuGroupId,
                    List.of(menuProductA, menuProductB));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> menuService.create(menuRequest)
            );
        }
    }

    @DisplayName("전체 메뉴를 조회할 수 있다")
    @Test
    void list() {
        // given
        final var menuRequest = new Menu(name, price, menuGroupId, List.of(menuProductA, menuProductB));
        final var secondName = name + "2";
        final var secondPrice = new BigDecimal(25000);
        final var menuRequest2 = new Menu(secondName, secondPrice, menuGroupId, List.of(menuProductA, menuProductB));
        menuService.create(menuRequest);
        menuService.create(menuRequest2);

        // when
        final var actual = menuService.list();
        final var prices = actual.stream()
                .map(Menu::getPrice)
                .map(BigDecimal::doubleValue)
                .collect(Collectors.toList());
        final var menuProducts = actual.stream()
                .flatMap(menu -> menu.getMenuProducts().stream())
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(actual).extracting("id").containsExactly(1L, 2L),
                () -> assertThat(actual).extracting("name").containsExactly(name, secondName),
                () -> assertThat(prices).containsExactly(price.doubleValue(), secondPrice.doubleValue()),
                () -> assertThat(actual).extracting("menuGroupId").containsExactly(menuGroupId, menuGroupId),
                () -> assertThat(menuProducts).extracting("productId").containsExactly(1L, 2L, 1L, 2L),
                () -> assertThat(menuProducts).extracting("quantity").containsExactly(1L, 1L, 1L, 1L)
        );
    }
}
