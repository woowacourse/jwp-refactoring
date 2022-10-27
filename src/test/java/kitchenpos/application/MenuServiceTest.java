package kitchenpos.application;

import static kitchenpos.fixture.MenuFactory.MENU_QUANTITY;
import static kitchenpos.fixture.MenuFactory.menu;
import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static kitchenpos.fixture.ProductFactory.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends FakeSpringContext {

    private final MenuService menuService = new MenuService(
            menuDao, menuGroupDao, menuProductDao, productDao);

    @DisplayName("create 메서드는")
    @Nested
    class create {

        private final Product pizza = productDao.save(product("피자", 10_000));
        private final Product coke = productDao.save(product("콜라", 1_000));

        private final MenuGroup italian = menuGroupDao.save(menuGroup("양식"));

        @DisplayName("메뉴를 등록하고, 등록된 메뉴를 반환한다")
        @Test
        void saveMenu() {
            final var menu = menu("피자와 콜라", italian, List.of(pizza, coke));
            final var result = menuService.create(menu);

            assertAll(
                    () -> assertThat(result.getName()).isEqualTo(menu.getName()),
                    () -> assertThat(result.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                    () -> assertThat(result.getPrice().getPrice().compareTo(menu.getPrice().getPrice())).isEqualTo(0)
            );
        }

        @DisplayName("가격이 각 상품의 수량과 가격을 곱한 값의 총합보다 크다면")
        @Nested
        class priceOverTotalOfMenuProducts {

            private final BigDecimal pizzaTotalPrice = pizza.getPrice().getPrice().multiply(new BigDecimal(MENU_QUANTITY));
            private final BigDecimal cokeTotalPrice = coke.getPrice().getPrice().multiply(new BigDecimal(MENU_QUANTITY));

            private final BigDecimal invalidPrice = pizzaTotalPrice.add(cokeTotalPrice)
                    .add(new BigDecimal(1));

            @DisplayName("에외를 던진다")
            @Test
            void throwsException() {
                final var invalidMenu = menu("피자와 콜라", invalidPrice, italian, List.of(pizza, coke));

                assertThatThrownBy(
                        () -> menuService.create(invalidMenu)
                ).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("메뉴 그룹이 존재하지 않는 값이라면")
        @Nested
        class menuGroupNotExists {

            private final Long notExistMenuGroupId = getMenuGroupIdNotExist();

            @DisplayName("예외를 던진다")
            @Test
            void throwsException() {
                final var menu = menu("피자와 콜라", italian, List.of(pizza, coke));
                menu.setMenuGroupId(notExistMenuGroupId);

                assertThatThrownBy(
                        () -> menuService.create(menu)
                ).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class list {

        private final Product pizza = productDao.save(product("피자", 10_000));
        private final Product pasta = productDao.save(product("파스타", 9_000));
        private final Product coke = productDao.save(product("콜라", 1_000));

        private final MenuGroup italian = menuGroupDao.save(menuGroup("양식"));

        @DisplayName("등록된 모든 메뉴를 조회한다")
        @Test
        void findAllMenus() {
            final var pizzaAndCoke = menuDao.save(menu("피자와 콜라", italian, List.of(pizza, coke)));
            final var pastaAndCoke = menuDao.save(menu("파스타와 콜라", italian, List.of(pasta, coke)));

            final var result = menuService.list();
            final var foundPizzaAndCokeMenu = findMenuInList(result, pizzaAndCoke);
            final var foundPastaAndCokeMenu = findMenuInList(result, pastaAndCoke);

            assertAll(
                    () -> assertThat(foundPizzaAndCokeMenu).isPresent(),
                    () -> assertThat(foundPastaAndCokeMenu).isPresent()
            );
        }

        private Optional<Menu> findMenuInList(final List<Menu> result, final Menu target) {
            return result.stream()
                    .filter(menu -> menu.getName().equals(target.getName())
                            && menu.getMenuGroupId().equals(target.getMenuGroupId())
                            && menu.getPrice().getPrice().compareTo(target.getPrice().getPrice()) == 0)
                    .findAny();
        }
    }

    private Long getMenuGroupIdNotExist() {
        final var menuGroups = menuGroupDao.findAll();
        final var largestById = menuGroups.stream().max(Comparator.comparing(MenuGroup::getId));
        if (largestById.isEmpty()) {
            return 1L;
        }
        return largestById.get()
                .getId() + 1L;
    }
}
