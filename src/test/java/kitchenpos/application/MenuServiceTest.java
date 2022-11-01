package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.exception.IllegalPriceException;
import kitchenpos.exception.MenuTotalPriceException;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.support.fixtures.MenuFixtures;
import kitchenpos.support.fixtures.MenuGroupFixtures;
import kitchenpos.support.fixtures.MenuProductFixtures;
import kitchenpos.support.fixtures.ProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:truncate.sql")
@SpringBootTest
class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Product product = ProductFixtures.CHICKEN.create();
        final Product savedProduct = productRepository.save(product);

        final MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 3);
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu.getName(), menu.getPrice(),
                menu.getMenuGroupId(), Collections.singletonList(menuProductRequest));

        // when
        final Menu saved = menuService.create(menuCreateRequest);

        // then
        final MenuProduct actual = MenuProductFixtures.create(saved, savedProduct.getId(), 3);

        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo("두마리 치킨 콤보"),
                () -> assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal("30000")),
                () -> assertThat(saved.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(saved.getMenuProducts()).usingElementComparatorOnFields(
                                "menu", "productId", "quantity")
                        .hasSize(1)
                        .containsExactly(actual)
        );
    }

    @Test
    @DisplayName("가격을 설정하지 않고 메뉴를 생성하면 예외가 발생한다")
    void createExceptionWithoutPrice() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu.getName(), null,
                menu.getMenuGroupId(), new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isExactlyInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("0원 이하로 가격을 설정하고 메뉴를 생성하면 예외가 발생한다")
    void createExceptionWrongPrice() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu.getName(), new BigDecimal(-1),
                menu.getMenuGroupId(), new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isExactlyInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹 id로 메뉴를 생성하면 예외가 발생한다")
    void createExceptionWrongMenuGroup() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu.getName(), menu.getPrice(),
                -1L, new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isExactlyInstanceOf(NotFoundMenuGroupException.class);
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 메뉴를 생성하면 예외가 발생한다")
    void createExceptionWrongMenuProducts() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final MenuProductRequest menuProductRequest = new MenuProductRequest(-1L, 2);
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu.getName(), menu.getPrice(),
                menu.getMenuGroupId(), Collections.singletonList(menuProductRequest));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isExactlyInstanceOf(NotFoundProductException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품들의 가격합보다 크거나 같게 생성하면 예외가 발생한다")
    void createExceptionWrongPriceWithMenuProductsPriceSum() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Product product = ProductFixtures.CHICKEN.create();
        final Product savedProduct = productRepository.save(product);

        final MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 2);
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menu.getName(), new BigDecimal(50000),
                menu.getMenuGroupId(), Collections.singletonList(menuProductRequest));

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isExactlyInstanceOf(MenuTotalPriceException.class);
    }

    @Test
    @DisplayName("모든 메뉴를 조회한다")
    void list() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.createWithMenuGroup(savedMenuGroup);
        final Menu saved = menuRepository.save(menu);

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertAll(
                () -> assertThat(menus).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(menus).extracting("id")
                        .contains(saved.getId()),
                () -> assertThat(menus).extracting("menuProducts")
                        .isNotEmpty()
        );
    }
}
