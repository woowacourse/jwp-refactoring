package kitchenpos.application;

import kitchenpos.domain.dto.MenuRequest;
import kitchenpos.domain.dto.MenuResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import support.fixture.MenuBuilder;
import support.fixture.MenuGroupBuilder;
import support.fixture.MenuProductBuilder;
import support.fixture.ProductBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroupBuilder().build());
    }

    @Nested
    @DisplayName("메뉴 목록 조회 테스트")
    class MenuListTest {

        private Product product1;
        private Product product2;

        @BeforeEach
        void setUp() {
            product1 = productRepository.save(new ProductBuilder().build());
            product2 = productRepository.save(new ProductBuilder().build());
        }

        @Test
        @DisplayName("모든 Menu의 목록을 조회한다.")
        void should_return_menu_list_when_request_list() {
            // given
            final int quantity = 2;

            final MenuProduct menuProduct1 = new MenuProductBuilder()
                    .setProduct(product1)
                    .setQuantity(quantity)
                    .build();
            final MenuProduct menuProduct2 = new MenuProductBuilder()
                    .setProduct(product2)
                    .setQuantity(quantity)
                    .build();

            final Menu menu = new MenuBuilder()
                    .setMenuGroup(menuGroup)
                    .setMenuProducts(List.of(menuProduct1, menuProduct2))
                    .build();

            menuRepository.save(menu);

            final List<Menu> menus = menuRepository.findAll();
            final List<Long> expect = menus.stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList());

            // when
            final List<MenuResponse> menuResponses = menuService.list();
            final List<Long> actual = menuResponses.stream()
                    .map(MenuResponse::getId)
                    .collect(Collectors.toList());

            // then
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expect);
        }
    }

    @Nested
    @DisplayName("메뉴를 생성할 수 있다.")
    class MenuCreateTest {


        private MenuBuilder menuBuilder;

        @BeforeEach
        void setUp() {
            final Product product = productRepository.save(new ProductBuilder()
                    .setPrice(BigDecimal.valueOf(100_000_000))
                    .build());

            final MenuProduct menuProduct = new MenuProductBuilder()
                    .setProduct(product)
                    .setQuantity(1)
                    .build();

            menuBuilder = new MenuBuilder()
                    .setMenuGroup(menuGroup)
                    .setMenuProducts(List.of(menuProduct));
        }

        @Test
        @DisplayName("상품 가격이 0 이상이고 MenuGroup이 존재하며 MenuProduct에 속하는 모든 상품의 가격 * 수량의 합보다 작으면 정상적으로 저장된다.")
        void saveTest() {
            // given
            final MenuRequest request = new MenuRequest("메뉴", BigDecimal.ZERO, menuGroup.getId(), Collections.emptyList());

            final MenuResponse expect = new MenuResponse(null, request.getName(), request.getPrice(),
                    request.getMenuGroupId(), Collections.emptyList());

            // when
            final MenuResponse actual = menuService.create(request);

            // then
            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expect);
        }

        @Test
        @DisplayName("MenuGroup이 존재하지 않으면 IllegalArgumentException이 발생한다.")
        void invalidGroupIdTest() {
            // given
            final long invalidMenuGroupId = -1L;
            final MenuRequest request = new MenuRequest("메뉴", BigDecimal.ZERO, invalidMenuGroupId,
                    Collections.emptyList());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> menuService.create(request));
        }

        @Test
        @DisplayName("Menu의 가격이 MenuProduct에 속하는 모든 상품의 가격 * 수량의 합보다 크면 IllegalArgumentException이 발생한다.")
        void largerThenTotalProductPriceTest() {
            // given
            final MenuRequest request = new MenuRequest("메뉴", BigDecimal.ONE, menuGroup.getId(),
                    Collections.emptyList());

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> menuService.create(request));
        }
    }
}
