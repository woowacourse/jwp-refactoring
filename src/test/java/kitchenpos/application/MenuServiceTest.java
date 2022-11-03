package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.여러마리_메뉴_그룹;
import static kitchenpos.application.fixture.MenuGroupFixture.치킨;
import static kitchenpos.application.fixture.ProductFixture.양념_치킨;
import static kitchenpos.application.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;


class MenuServiceTest extends ServiceTestBase {

    @Autowired
    private MenuService menuService;


    @DisplayName("전체 메뉴를 조회한다")
    @Test
    void findAll() {
        // given
        Product productChicken1 = productRepository.save(후라이드_치킨());
        Product productChicken2 = productRepository.save(양념_치킨());
        MenuGroup chickenMenuGroup = menuGroupRepository.save(치킨());

        MenuProduct menuProductChicken1 = createMenuProduct(productChicken1.getId(), 1, productChicken1.getPrice());
        MenuProduct menuProductChicken2 = createMenuProduct(productChicken2.getId(), 1, productChicken2.getPrice());

        Menu twoChickens = createMenu("두마리메뉴",
                BigDecimal.valueOf(35000),
                chickenMenuGroup.getId(),
                Arrays.asList(menuProductChicken1, menuProductChicken2));

        Menu oneChicken = createMenu("한마리메뉴",
                BigDecimal.valueOf(18000),
                chickenMenuGroup.getId(),
                Collections.singletonList(menuProductChicken1));
        menuRepository.save(twoChickens);
        menuRepository.save(oneChicken);

        // when
        List<MenuResponse> menus = menuService.list();

        //then
        assertAll(
                () -> assertThat(menus).hasSize(2),
                () -> assertThat(menus).extracting("menuProducts").isNotNull()
        );
    }


    @DisplayName("메뉴의 가격이 null이면 예외가 발생한다.")
    @Test
    void createMenePriceNull() {
        // given
        MenuGroup chickenMenuGroup = menuGroupRepository.save(여러마리_메뉴_그룹());
        Product chicken = productRepository.save(후라이드_치킨());
        Product seasonedChicken = productRepository.save(양념_치킨());
        List<MenuProductRequest> menuProductRequests = Arrays.asList(
                new MenuProductRequest(chicken.getId(), 1),
                new MenuProductRequest(seasonedChicken.getId(), 2));
        MenuRequest menuRequest = createMenuRequest("세마리 메뉴", null, chickenMenuGroup.getId(), menuProductRequests);

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 가격은 비어있거나 0보다 작을 수 없습니다.");
    }

    @DisplayName("메뉴의 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createMenuPrice0() {
        // given
        MenuGroup chickenMenuGroup = menuGroupRepository.save(치킨());
        Product chicken = productRepository.save(후라이드_치킨());
        Product seasonedChicken = productRepository.save(양념_치킨());
        List<MenuProductRequest> menuProducts = Arrays.asList(
                new MenuProductRequest(chicken.getId(), 1),
                new MenuProductRequest(seasonedChicken.getId(), 2));
        MenuRequest menuRequest = createMenuRequest("세마리 메뉴", BigDecimal.valueOf(-1000), chickenMenuGroup.getId(),
                menuProducts);

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 가격은 비어있거나 0보다 작을 수 없습니다.");
    }

    @DisplayName("메뉴 그룹의 아이디가 존재하지 않으면 예외를 발생한다.")
    @Test
    void createNoProduct() {
        // given
        Product chicken = productRepository.save(후라이드_치킨());
        Product seasonedChicken = productRepository.save(양념_치킨());
        List<MenuProductRequest> menuProducts = Arrays.asList(
                new MenuProductRequest(chicken.getId(), 1),
                new MenuProductRequest(seasonedChicken.getId(), 2));
        MenuRequest menuRequest = createMenuRequest("세마리 메뉴", BigDecimal.valueOf(40000), 0L, menuProducts);

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴 그룹입니다.");
    }

    @DisplayName("MenuProduct의 product가 존재하지 않으면 예외를 발생한다.")
    @Test
    void createMenuGroupId() {
        // given
        MenuGroup chickenMenuGroup = menuGroupRepository.save(치킨());
        List<MenuProductRequest> menuProducts = Arrays.asList(
                new MenuProductRequest(0L, 1));
        MenuRequest menuRequest = createMenuRequest("세마리 메뉴", BigDecimal.ZERO, chickenMenuGroup.getId(),
                menuProducts);

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 상품입니다.");
    }

    @DisplayName("가격이 구매할 수 있는 product의 금액보다 크면 예외를 발생한다.")
    @Test
    void createInvalidMenuPrice() {
        // given
        MenuGroup chickenMenuGroup = menuGroupRepository.save(여러마리_메뉴_그룹());
        Product chicken = productRepository.save(후라이드_치킨());
        Product seasonedChicken = productRepository.save(양념_치킨());
        List<MenuProductRequest> menuProducts = Arrays.asList(
                new MenuProductRequest(chicken.getId(), 1),
                new MenuProductRequest(seasonedChicken.getId(), 2));
        MenuRequest menuRequest = createMenuRequest("세마리 메뉴", BigDecimal.valueOf(1000000), chickenMenuGroup.getId(),
                menuProducts);

        // when & then
        assertThatThrownBy(
                () -> menuService.create(menuRequest)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 유효하지 않습니다.");
    }

    @DisplayName("가격이 구매할 수 있는 product의 금액보다 같거나 작으면 예외를 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(ints = {56000, 55000})
    void createInvalidMenuPrice(int price) {
        // given
        MenuGroup chickenMenuGroup = menuGroupRepository.save(여러마리_메뉴_그룹());
        Product chicken = productRepository.save(후라이드_치킨());
        Product seasonedChicken = productRepository.save(양념_치킨());
        List<MenuProductRequest> menuProducts = Arrays.asList(
                new MenuProductRequest(chicken.getId(), 1),
                new MenuProductRequest(seasonedChicken.getId(), 2));
        MenuRequest menuRequest = createMenuRequest("세마리 메뉴", BigDecimal.valueOf(price), chickenMenuGroup.getId(),
                menuProducts);

        // when & then
        assertDoesNotThrow(
                () -> menuService.create(menuRequest)
        );
    }

    @DisplayName("Menu를 올바르게 저장한다.")
    @Test
    void createMenu() {
        // given
        MenuGroup chickenMenuGroup = menuGroupRepository.save(여러마리_메뉴_그룹());
        Product chicken = productRepository.save(후라이드_치킨());
        Product seasonedChicken = productRepository.save(양념_치킨());
        List<MenuProductRequest> menuProducts = Arrays.asList(
                new MenuProductRequest(chicken.getId(), 1),
                new MenuProductRequest(seasonedChicken.getId(), 2));
        MenuRequest menuRequest = createMenuRequest("세마리 메뉴", BigDecimal.valueOf(40000), chickenMenuGroup.getId(),
                menuProducts);

        // when
        MenuResponse menu = menuService.create(menuRequest);

        List<Menu> menus = menuRepository.findAll();
        Optional<Menu> foundMenu = menuRepository.findById(menu.getId());
        //then
        assertAll(
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(foundMenu).isPresent()
        );
    }

    @DisplayName("Menu를 올바르게 저장할 때 menuProduct도 올바르게 저장됨을 확인한다.")
    @Test
    void createMenuAndCheckMenuProduct() {
        // given
        MenuGroup chickenMenuGroup = menuGroupRepository.save(여러마리_메뉴_그룹());
        Product chicken = productRepository.save(후라이드_치킨());
        Product seasonedChicken = productRepository.save(양념_치킨());
        List<MenuProductRequest> menuProducts = Arrays.asList(
                new MenuProductRequest(chicken.getId(), 1),
                new MenuProductRequest(seasonedChicken.getId(), 2));
        MenuRequest menuRequest = createMenuRequest("세마리 메뉴", BigDecimal.valueOf(40_000L), chickenMenuGroup.getId(),
                menuProducts);

        // when
        MenuResponse menu = menuService.create(menuRequest);

        Optional<Menu> foundMenu1 = menuRepository.findById(menu.getId());
        //then
        assertAll(
                () -> assertThat(foundMenu1).isPresent(),
                () -> assertThat(foundMenu1.get().getMenuProducts()).hasSize(2),
                () -> assertThat(foundMenu1.get().getMenuProducts()).extracting("productId")
                        .contains(chicken.getId(), seasonedChicken.getId())
        );
    }
}
