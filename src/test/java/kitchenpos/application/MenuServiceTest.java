package kitchenpos.application;

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
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuRequest.MenuInnerMenuProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends FakeSpringContext {

    private final MenuService menuService = new MenuService(menus, products, menuGroups);

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
            final var request = new MenuRequest(
                    menu.getName(),
                    menu.getPrice(),
                    menu.getMenuGroupId(),
                    mapToInnerRequests(menu)
            );

            final var result = menuService.create(request);

            assertAll(
                    () -> assertThat(result.getName()).isEqualTo(menu.getName()),
                    () -> assertThat(result.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                    () -> assertThat(result.getPrice().compareTo(menu.getPrice())).isEqualTo(0)
            );
        }

        private List<MenuInnerMenuProductRequest> mapToInnerRequests(final Menu menu) {
            return menu.getMenuProducts()
                    .stream()
                    .map(menuProduct -> new MenuInnerMenuProductRequest(
                            menuProduct.getProductId(),
                            menuProduct.getQuantity()))
                    .collect(Collectors.toList());
        }

        @DisplayName("메뉴 그룹이 존재하지 않는 값이라면")
        @Nested
        class menuGroupNotExists {

            private final Long notExistMenuGroupId = getMenuGroupIdNotExist();

            @DisplayName("예외를 던진다")
            @Test
            void throwsException() {
                final var menu = new Menu(null, "피자와 콜라", BigDecimal.valueOf(0), notExistMenuGroupId);
                final var request = new MenuRequest(
                        menu.getName(),
                        menu.getPrice(),
                        menu.getMenuGroupId(),
                        mapToInnerRequests(menu)
                );

                assertThatThrownBy(
                        () -> menuService.create(request)
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
                            && menu.getPrice().compareTo(target.getPrice()) == 0)
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
