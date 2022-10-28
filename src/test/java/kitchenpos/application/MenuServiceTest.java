package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.request.MenuProductCommand;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("메뉴 관련 기능에서")
@SpringBootTest
@ExtendWith(DataClearExtension.class)
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("메뉴를 생성할 때")
    class CreateMenu {

        @Test
        @DisplayName("메뉴를 정상적으로 생성한다.")
        void create() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
            Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));

            MenuCommand menuRequest = new MenuCommand("강정", BigDecimal.valueOf(18000), menuGroup.getId(),
                    List.of(new MenuProductCommand(product.getId(), 2)));
            MenuResponse response = menuService.create(menuRequest);

            assertThat(response.getId()).isNotNull();
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @Test
            @DisplayName("가격이 0원 미만이면 예외가 발생한다.")
            void createMenuPriceLessThanZero() {
                MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
                Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));

                MenuCommand menuRequest = new MenuCommand("강정", BigDecimal.valueOf(-1), menuGroup.getId(),
                        List.of(new MenuProductCommand(product.getId(), 2)));

                assertThatThrownBy(() -> menuService.create(menuRequest))
                        .hasMessage("가격이 0원 이상이어야 합니다");
            }

            @Test
            @DisplayName("메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
            void createMenuNotFoundMenuGroup() {
                Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));

                MenuCommand menuRequest = new MenuCommand("강정", BigDecimal.valueOf(18000), 1L,
                        List.of(new MenuProductCommand(product.getId(), 2)));

                assertThatThrownBy(() -> menuService.create(menuRequest))
                        .hasMessage("메뉴 그룹이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("상품이 존재하지 않으면 예외가 발생한다.")
            void createMenuNotFoundProduct() {
                MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));

                MenuCommand menuRequest = new MenuCommand("강정", BigDecimal.valueOf(18000), menuGroup.getId(),
                        List.of(new MenuProductCommand(1L, 2)));

                assertThatThrownBy(() -> menuService.create(menuRequest))
                        .hasMessage("상품이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("메뉴의 가격이 상품의 가격보다 높으면 예외가 발생한다.")
            void createMenuComparePrice() {
                MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
                Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));

                MenuCommand menuRequest = new MenuCommand("강정", BigDecimal.valueOf(37000), menuGroup.getId(),
                        List.of(new MenuProductCommand(product.getId(), 2)));

                assertThatThrownBy(() -> menuService.create(menuRequest))
                        .hasMessage("메뉴의 가격이 상품의 가격보다 높습니다.");
            }
        }
    }

    @Test
    @DisplayName("존재하는 메뉴을 모두 조회한다.")
    void list() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        Product product = productRepository.save(new Product("강정치킨", BigDecimal.valueOf(18000)));

        Menu menu = menuRepository.save(new Menu("강정치킨", BigDecimal.valueOf(37000), menuGroup.getId()));

        menu.addMenuProduct(new MenuProduct(menu.getId(), product.getId(), 2));

        assertThat(menuService.list()).hasSize(1);
    }
}
