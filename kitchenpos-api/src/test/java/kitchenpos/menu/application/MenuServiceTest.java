package kitchenpos.menu.application;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.application.dto.request.MenuCommand;
import kitchenpos.menu.application.dto.request.MenuProductCommand;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.cleaner.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("Menu를 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("가격이 음수일 경우 실패한다.")
        void priceNegativeFailed() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
            MenuCommand menuCommand = new MenuCommand(MENU1_NAME, BigDecimal.valueOf(-1000), menuGroup.getId(),
                    Collections.emptyList());
            assertThatThrownBy(
                    () -> menuService.create(menuCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 0보다 커야합니다.");
        }

        @Test
        @DisplayName("MenuGroup이 존재하지 않을 경우 실패한다.")
        void menuGroupNotExistFailed() {
            MenuCommand menuCommand = new MenuCommand(MENU1_NAME, MENU1_PRICE, 0L, Collections.emptyList());
            assertThatThrownBy(() -> menuService.create(menuCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 그룹이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("Product가 존재하지 않을 경우 실패한다.")
        void productNotFoundFailed() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
            MenuCommand menuCommand = new MenuCommand(MENU1_NAME, MENU1_PRICE, menuGroup.getId(),
                    List.of(new MenuProductCommand(0L, 1)));

            assertThatThrownBy(() -> menuService.create(menuCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("제품이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("Product가격의 합보다 메뉴 가격이 높을 경우 실패한다.")
        void priceIsOverProductPriceSumFailed() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
            Product product = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
            MenuCommand menuCommand = new MenuCommand(MENU2_NAME, MENU2_PRICE, menuGroup.getId(),
                    List.of(new MenuProductCommand(product.getId(), 1)));

            assertThatThrownBy(() -> menuService.create(menuCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 가격입니다.");
        }

        @Test
        @DisplayName("메뉴를 생성한다.")
        void createMenu() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
            Product product = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
            MenuCommand menuCommand = new MenuCommand(MENU1_NAME, MENU1_PRICE, menuGroup.getId(),
                    List.of(new MenuProductCommand(product.getId(), 1)));

            MenuResponse menuResponse = menuService.create(menuCommand);
            Menu menu = menuRepository.findById(menuResponse.id()).orElseThrow();

            assertAll(
                    () -> assertThat(menu.getId()).isNotNull(),
                    () -> assertThat(menu.getMenuProducts().get(0).getSeq()).isNotNull(),
                    () -> assertThat(menu.getMenuProducts().get(0).getMenuId()).isNotNull()
            );
        }
    }

    @Test
    @DisplayName("모든 메뉴를 조회한다.")
    void list() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        Product product = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        MenuCommand menuCommand = new MenuCommand(MENU1_NAME, MENU1_PRICE, menuGroup.getId(),
                List.of(new MenuProductCommand(product.getId(), 1)));

        menuService.create(menuCommand);
        List<MenuResponse> menuResponses = menuService.list();

        assertAll(
                () -> assertThat(menuResponses).hasSize(1),
                () -> assertThat(menuResponses.get(0).menuProductResponses()).hasSize(1)
        );
    }
}
