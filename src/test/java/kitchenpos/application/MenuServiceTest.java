package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A"));

        //when
        Menu savedMenu = menuService.create(new Menu("신메뉴", BigDecimal.ZERO, menuGroup.getId(), List.of()));

        //then
        assertAll(
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getPrice().compareTo(BigDecimal.ZERO)).isZero(),
                () -> assertThat(savedMenu.getMenuProducts()).isEmpty()
        );
    }

    @DisplayName("메뉴의 가격이 음수이거나 없으면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource
    @NullSource
    void createFailureWhenPriceIsNegative(BigDecimal price) {
        //given
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A"));

        //when
        //then
        assertThatThrownBy(
                () -> menuService.create(new Menu("신메뉴", price, menuGroup.getId(), List.of())))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<BigDecimal> createFailureWhenPriceIsNegative() {
        return Stream.of(BigDecimal.valueOf(-1L), BigDecimal.valueOf(-99L));
    }

    @DisplayName("메뉴의 메뉴 그룹 아이디가 없으면 예외가 발생한다.")
    @Test
    void createFailureWhenNotExistsMenuGroupId() {
        final Long noMenuGroupId = null;

        //when
        //then
        assertThatThrownBy(
                () -> menuService.create(new Menu("신메뉴", BigDecimal.ZERO, noMenuGroupId, List.of())))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴에 포함된 (제품 가격*수량)보다 크면 예외가 발생한다.")
    @Test
    void createFailureWhenMenuPriceLessThanMenuProductPrice() {
        //given
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A"));
        Product product = productDao.save(new Product("제육볶음", BigDecimal.ONE));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(
                new Menu("신메뉴", BigDecimal.TEN, menuGroup.getId(), List.of(new MenuProduct(product.getId(), 3)))))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        //given
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A"));
        menuService.create(new Menu("신메뉴", BigDecimal.ZERO, menuGroup.getId(), List.of()));

        //when
        //then
        assertThat(menuService.list()).hasSize(1);
    }
}
