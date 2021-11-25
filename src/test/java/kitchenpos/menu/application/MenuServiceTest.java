package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuProductResponse;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Menu Service 테스트")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Menu를 생성할 때")
    @Nested
    class CreateMenu {

        @DisplayName("Menu의 Price가 Null이면 예외가 발생한다.")
        @Test
        void priceNullException() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));
            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuReqeust를_생성한다("엄청난 메뉴", null, menuGroup.getId(), menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        }

        @DisplayName("Menu의 Price가 0보다 작으면 예외가 발생한다.")
        @Test
        void priceNegativeException() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));
            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuReqeust를_생성한다("엄청난 메뉴", -1, menuGroup.getId(), menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        }

        @DisplayName("Menu의 MenuGroupId가 존재하지 않는 경우 발생한다.")
        @Test
        void noExistMenuGroupIdException() {
            // given
            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuReqeust를_생성한다("엄청난 메뉴", 5_600, -1L, menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(MenuGroupNotFoundException.class);
        }

        @DisplayName("Menu의 Product가 실제로 존재하지 않는 경우 예외가 발생한다.")
        @Test
        void noExistProductException() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));
            Product 없는_상품 = new Product(-1L, "없는 상품", BigDecimal.TEN);
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(없는_상품, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(없는_상품, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuReqeust를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(ProductNotFoundException.class);
        }

        @DisplayName("Menu의 총 Price가 Product들의 Price 합보다 클 경우 예외가 발생한다.")
        @Test
        void menuPriceNotMatchException() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));
            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuReqeust를_생성한다("엄청난 메뉴", 5_601, menuGroup.getId(), menuProducts);

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        }

        @DisplayName("정상적인 경우 menuProduct가 함께 저장되어 반환된다.")
        @Test
        void success() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));
            Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
            Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));
            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuRequest request = MenuReqeust를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            // when
            MenuResponse response = menuService.create(request);

            // then
            assertThat(response.getId()).isNotNull();
            assertThat(response.getName()).isEqualTo(request.getName());
            assertThat(response.getPrice().compareTo(request.getPrice())).isEqualTo(0);
            assertThat(response.getMenuGroupId()).isEqualTo(request.getMenuGroup());
            for (MenuProductResponse menuProductResponse : response.getMenuProducts()) {
                assertThat(menuProductResponse.getSeq()).isNotNull();
                assertThat(menuProductResponse.getMenuId()).isEqualTo(response.getId());
            }
        }
    }

    @DisplayName("전체 Menu 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<MenuResponse> beforeSavedMenus = menuService.list();

        MenuGroup menuGroup = menuGroupRepository.save(MenuGroup을_생성한다("엄청난 그룹"));

        Product 치즈버거 = productRepository.save(Product를_생성한다("치즈버거", 4_000));
        Product 콜라 = productRepository.save(Product를_생성한다("치즈버거", 1_600));

        MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
        MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
        List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

        beforeSavedMenus.add(menuService.create(MenuReqeust를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts)));
        beforeSavedMenus.add(menuService.create(MenuReqeust를_생성한다("할인 메뉴", 4_600, menuGroup.getId(), menuProducts)));

        // when
        List<MenuResponse> afterSavedMenus = menuService.list();

        // then
        assertThat(afterSavedMenus).hasSize(beforeSavedMenus.size());
        assertThat(afterSavedMenus).usingRecursiveComparison()
            .ignoringFields("price")
            .isEqualTo(beforeSavedMenus);
    }

    private MenuRequest MenuReqeust를_생성한다(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return MenuReqeust를_생성한다(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    private MenuRequest MenuReqeust를_생성한다(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        return new MenuGroup(name);
    }

    private MenuProductRequest MenuProductRequest를_생성한다(Product product, long quantity) {
        return new MenuProductRequest(product.getId(), quantity);
    }

    private Product Product를_생성한다(String name, int price) {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
