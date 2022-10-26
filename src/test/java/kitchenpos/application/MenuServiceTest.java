package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("create 메서드는")
    @Nested
    class create {

        private final Product pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        private final Product coke = productDao.save(new Product("콜라", new BigDecimal(1_000)));

        private final MenuProduct pizzaInMenu = new MenuProduct(pizza.getId(), 1);
        private final MenuProduct cokeInMenu = new MenuProduct(coke.getId(), 1);

        private final MenuGroup italian = menuGroupDao.save(new MenuGroup("양식"));

        @DisplayName("메뉴를 등록하고, 등록된 메뉴를 반환한다")
        @Test
        void saveMenu() {
            final var menu = new Menu("피자와 콜라", new BigDecimal(10_500), italian.getId(),
                    List.of(pizzaInMenu, cokeInMenu));
            final var result = menuService.create(menu);

            assertThat(result).isEqualTo(menu);
        }

        @DisplayName("가격이 null이면")
        @Nested
        class priceIsNull {

            private final BigDecimal invalidPrice = null;

            @DisplayName("예외를 던진다")
            @Test
            void throwsException() {
                final var invalidMenu = new Menu("피자와 콜라", invalidPrice, italian.getId(),
                        List.of(pizzaInMenu, cokeInMenu));

                assertThatThrownBy(
                        () -> menuService.create(invalidMenu)
                ).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("가격이 0 미만이면")
        @Nested
        class priceIsUnderZero {

            private final BigDecimal invalidPrice = new BigDecimal(-1);

            @DisplayName("예외를 던진다")
            @Test
            void throwsException() {
                final var invalidMenu = new Menu("피자와 콜라", invalidPrice, italian.getId(),
                        List.of(pizzaInMenu, cokeInMenu));

                assertThatThrownBy(
                        () -> menuService.create(invalidMenu)
                ).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("가격이 각 상품의 수량과 가격을 곱한 값의 총합보다 크다면")
        @Nested
        class priceOverTotalOfMenuProducts {

            private final BigDecimal pizzaTotalPrice = pizza.getPrice()
                    .multiply(new BigDecimal(pizzaInMenu.getQuantity()));
            private final BigDecimal cokeTotalPrice = coke.getPrice()
                    .multiply(new BigDecimal(cokeInMenu.getQuantity()));

            private final BigDecimal invalidPrice = pizzaTotalPrice.add(cokeTotalPrice)
                    .add(new BigDecimal(1));

            @DisplayName("에외를 던진다")
            @Test
            void throwsException() {
                final var invalidMenu = new Menu("피자와 콜라", invalidPrice, italian.getId(),
                        List.of(pizzaInMenu, cokeInMenu));

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
                final var menu = new Menu("피자와 콜라", new BigDecimal(10_500), notExistMenuGroupId,
                        List.of(pizzaInMenu, cokeInMenu));

                assertThatThrownBy(
                        () -> menuService.create(menu)
                ).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class list {

        private final Product pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        private final Product pasta = productDao.save(new Product("파스타", new BigDecimal(9_000)));
        private final Product coke = productDao.save(new Product("콜라", new BigDecimal(1_000)));

        private final MenuProduct pizzaInMenu = new MenuProduct(pizza.getId(), 1);
        private final MenuProduct pastaInMenu = new MenuProduct(pasta.getId(), 1);
        private final MenuProduct cokeInPizzaMenu = new MenuProduct(coke.getId(), 1);
        private final MenuProduct cokeInPastaMenu = new MenuProduct(coke.getId(), 1);

        private final MenuGroup italian = menuGroupDao.save(new MenuGroup("양식"));

        @DisplayName("등록된 모든 메뉴를 조회한다")
        @Test
        void findAllMenus() {
            final var pizzaAndCoke = new Menu("피자와 콜라", new BigDecimal(10_500), italian.getId(),
                    List.of(pizzaInMenu, cokeInPizzaMenu));
            final var pastaAndCoke = new Menu("파스타와 콜라", new BigDecimal(9_500), italian.getId(),
                    List.of(pastaInMenu, cokeInPastaMenu));

            menuService.create(pizzaAndCoke);
            menuService.create(pastaAndCoke);

            final var result = menuService.list();
            assertThat(result).contains(pizzaAndCoke, pastaAndCoke);
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
