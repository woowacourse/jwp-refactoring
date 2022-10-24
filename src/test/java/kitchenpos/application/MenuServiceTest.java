package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("메뉴 등록 시 메뉴 정상 저장")
    @Test
    void create() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var coke = productDao.save(new Product("콜라", new BigDecimal(1_000)));

        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);
        final var cokeInMenu = new MenuProduct(coke.getId(), 2);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var menu = new Menu("피자와 콜라", new BigDecimal(10_500), italian.getId(), List.of(pizzaInMenu, cokeInMenu));

        final var result = menuService.create(menu);
        assertThat(menu).isEqualTo(result);
    }

    @DisplayName("메뉴 등록 시 가격이 null이면 예외 발생")
    @Test
    void create_priceIsNull_throwsException() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));

        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var menu = new Menu("피자", null, italian.getId(), List.of(pizzaInMenu));
        assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 가격이 0 미만이면 예외 발생")
    @Test
    void create_priceIsUnderZero_throwsException() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));

        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var menu = new Menu("피자", new BigDecimal(-1), italian.getId(), List.of(pizzaInMenu));
        assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 가격이 각 상품의 수량과 가격을 곱한 값의 총합보다 크다면 예외 발생")
    @Test
    void create_priceOverTotalOfMenuProducts_throwException() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var coke = productDao.save(new Product("콜라", new BigDecimal(1_000)));

        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);
        final var cokeInMenu = new MenuProduct(coke.getId(), 2);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var priceOverTotal = pizza.getPrice().add(coke.getPrice()).add(new BigDecimal(1));
        final var menu = new Menu("피자와 콜라", priceOverTotal, italian.getId(), List.of(pizzaInMenu, cokeInMenu));

        final var result = menuService.create(menu);
        assertThat(menu).isEqualTo(result);
    }

    @DisplayName("메뉴 등록 시 존재하지 않는 메뉴 그룹이라면 예외 발생")
    @Test
    void create_menuGroupNotExists_throwsException() {
        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var coke = productDao.save(new Product("콜라", new BigDecimal(1_000)));

        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);
        final var cokeInMenu = new MenuProduct(coke.getId(), 2);

        final var notExistMenuGroupId = getMenuGroupIdNotExist();
        final var menu = new Menu("피자와 콜라", new BigDecimal(10_500), notExistMenuGroupId,
                List.of(pizzaInMenu, cokeInMenu));

        assertThatThrownBy(
                () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴 목록 조회")
    @Test
    void list() {
        final var existingMenus = menuService.list();

        final var pizza = productDao.save(new Product("피자", new BigDecimal(10_000)));
        final var pasta = productDao.save(new Product("파스타", new BigDecimal(9_000)));
        final var coke = productDao.save(new Product("콜라", new BigDecimal(1_000)));

        final var pizzaInMenu = new MenuProduct(pizza.getId(), 1);
        final var pastaInMenu = new MenuProduct(pasta.getId(), 1);
        final var cokeInPizzaMenu = new MenuProduct(coke.getId(), 2);
        final var cokeInPastaMenu = new MenuProduct(coke.getId(), 2);

        final var italian = menuGroupDao.save(new MenuGroup("양식"));
        final var pizzaAndCoke = new Menu("피자와 콜라", new BigDecimal(10_500), italian.getId(),
                List.of(pizzaInMenu, cokeInPizzaMenu));
        final var pastaAndCoke = new Menu("파스타와 콜라", new BigDecimal(9_500), italian.getId(),
                List.of(pastaInMenu, cokeInPastaMenu));

        menuService.create(pizzaAndCoke);
        menuService.create(pastaAndCoke);

        final var result = menuService.list();
        final var filteredResult = filterNewlyCreated(existingMenus, result);
        assertThat(filteredResult).containsExactly(pizzaAndCoke, pastaAndCoke);
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

    private List<Menu> filterNewlyCreated(final List<Menu> olds, final List<Menu> news) {
        return news.stream()
                .filter(menu -> !olds.contains(menu))
                .collect(Collectors.toList());
    }
}
