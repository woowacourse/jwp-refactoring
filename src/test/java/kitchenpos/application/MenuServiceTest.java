package kitchenpos.application;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU2_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.request.MenuProductCommand;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
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
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("Menu를 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("가격이 음수일 경우 실패한다.")
        void priceNegativeFailed() {
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
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
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
            MenuCommand menuCommand = new MenuCommand(MENU1_NAME, MENU1_PRICE, menuGroup.getId(),
                    List.of(new MenuProductCommand(0L, 1)));

            assertThatThrownBy(() -> menuService.create(menuCommand))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("제품이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("Product가격의 합보다 메뉴 가격이 높을 경우 실패한다.")
        void priceIsOverProductPriceSumFailed() {
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
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
            MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
            Product product = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
            MenuCommand menuCommand = new MenuCommand(MENU1_NAME, MENU1_PRICE, menuGroup.getId(),
                    List.of(new MenuProductCommand(product.getId(), 1)));

            MenuResponse menuResponse = menuService.create(menuCommand);
            assertThat(menuResponse.id()).isNotNull();
        }
    }

    @Test
    @DisplayName("모든 메뉴를 조회한다.")
    void list() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
        Product product = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        MenuCommand menuCommand = new MenuCommand(MENU1_NAME, MENU1_PRICE, menuGroup.getId(),
                List.of(new MenuProductCommand(product.getId(), 1)));

        menuService.create(menuCommand);
        List<Menu> menus = menuService.list();
        assertThat(menus).hasSize(1);
    }
}
