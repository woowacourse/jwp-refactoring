package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(scripts = "classpath:truncate.sql")
@Transactional
@SpringBootTest
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
    @DisplayName("메뉴는 ")
    class Create {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void create() {
            // given
            final Product product = new Product("후라이드치킨", new BigDecimal("15000.00"));
            productRepository.save(product);
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("치킨"));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1);
            final MenuCreateRequest request = new MenuCreateRequest("후라이드치킨", new BigDecimal("15000.00"),
                    savedMenuGroup.getId(), List.of(menuProductRequest));

            // when
            final Menu savedMenu = menuService.create(request);

            // then
            assertAll(
                    () -> assertThat(savedMenu.getName()).isEqualTo("후라이드치킨"),
                    () -> assertThat(savedMenu.getPrice()).isEqualTo("15000.00"),
                    () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(1L)
            );
        }

        @Test
        @DisplayName("메뉴 그룹을 찾을 수 없는 경우 예외가 발생한다.")
        void throwsExceptionWhenPriceIsNull() {
            // given
            final Product product = new Product("후라이드치킨", new BigDecimal("15000.00"));
            productRepository.save(product);
            menuGroupRepository.save(new MenuGroup("치킨"));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1);
            final MenuCreateRequest request = new MenuCreateRequest("후라이드치킨", new BigDecimal("15000.00"),
                    2L, List.of(menuProductRequest));

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당하는 메뉴 그룹이 존재하지 않습니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, Integer.MIN_VALUE, -300000})
        @DisplayName("가격이 0보다 작다면 예외가 발생한다.")
        void throwsExceptionWhenPriceIsUnderZero(int price) {
            // given
            final Product product = new Product("후라이드치킨", new BigDecimal("15000.00"));
            productRepository.save(product);
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("치킨"));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1);
            final MenuCreateRequest request = new MenuCreateRequest("후라이드치킨", new BigDecimal(price),
                    savedMenuGroup.getId(), List.of(menuProductRequest));

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 0보다 작을 수 없습니다.");
        }

        @Test
        @DisplayName("메뉴에 해당하는 상품이 존재하지 않는 경우에 예외가 발생한다.")
        void throwsExceptionWhenProductIdNonExist() {
            // given
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("치킨"));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
            final MenuCreateRequest request = new MenuCreateRequest("후라이드치킨", new BigDecimal("31000.00"),
                    savedMenuGroup.getId(), List.of(menuProductRequest));

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당하는 상품이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("메뉴 가격이 상품의 금액(가격 * 수량)의 합보다 큰 경우 예외가 발생한다.")
        void throwsExceptionWhenPriceIsUnderZero() {
            // given
            final Product product = new Product("후라이드치킨", new BigDecimal("15000.00"));
            productRepository.save(product);
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("치킨"));
            final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1);
            final MenuCreateRequest request = new MenuCreateRequest("후라이드치킨", new BigDecimal("31000.00"),
                    savedMenuGroup.getId(), List.of(menuProductRequest));

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격은 금액(가격 * 수량)의 합보다 클 수 없습니다.");
        }
    }

    @Test
    @DisplayName("메뉴 목록을 정상적으로 조회한다.")
    void list() {
        // given
        final MenuValidator menuValidator = new MenuValidator(productRepository);
        final Product productA = new Product("후라이드치킨", new BigDecimal("15000.00"));
        productRepository.save(productA);
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("치킨"));
        final MenuProduct menuProductA = new MenuProduct(null, productA.getId(), 1);
        final Menu menuA = Menu.of(savedMenuGroup.getId(), List.of(menuProductA), "후라이드치킨",
                new BigDecimal("15000.00"), menuValidator);
        menuRepository.save(menuA);

        final Product productB = new Product("양념치킨", new BigDecimal("17000.00"));
        productRepository.save(productB);
        final MenuProduct menuProductB = new MenuProduct(null, productB.getId(), 1);
        final Menu menuB = Menu.of(savedMenuGroup.getId(), List.of(menuProductB), "양념치킨", new BigDecimal("17000.00"),
                menuValidator);
        menuRepository.save(menuB);

        // when
        final List<Menu> menus = menuService.list();

        // then
        final Menu savedMenuA = menus.get(0);
        final Menu savedMenuB = menus.get(1);
        assertAll(
                () -> assertThat(menus).hasSize(2),
                () -> assertThat(savedMenuA.getName()).isEqualTo("후라이드치킨"),
                () -> assertThat(savedMenuA.getPrice()).isEqualTo("15000.00"),
                () -> assertThat(savedMenuA.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(savedMenuB.getName()).isEqualTo("양념치킨"),
                () -> assertThat(savedMenuB.getPrice()).isEqualTo("17000.00"),
                () -> assertThat(savedMenuB.getMenuGroupId()).isEqualTo(1L)
        );
    }
}
