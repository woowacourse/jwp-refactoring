package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.menu_product.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import support.fixture.MenuBuilder;
import support.fixture.MenuGroupBuilder;
import support.fixture.ProductBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

            final MenuProduct menuProduct1 = new MenuProduct();
            menuProduct1.setProduct(product1);
            menuProduct1.setQuantity(quantity);
            final MenuProduct menuProduct2 = new MenuProduct();
            menuProduct2.setProduct(product1);
            menuProduct2.setQuantity(quantity);

            final Menu menu = new MenuBuilder()
                    .setMenuGroup(menuGroup)
                    .setMenuProducts(List.of(menuProduct1, menuProduct2))
                    .build();

            final List<Menu> expect = menuRepository.findAll();
            expect.add(menu);

            menuRepository.save(menu);

            // when
            final List<Menu> actual = menuService.list();

            // then
            assertEquals(expect.size(), actual.size());

            for (int i = 0; i < actual.size(); i++) {
                final Menu actualMenu = actual.get(i);
                final Menu expectMenu = expect.get(i);

                assertAll(
                        () -> assertEquals(expectMenu.getName(), actualMenu.getName()),
                        () -> assertThat(expectMenu.getPrice()).isEqualByComparingTo(actualMenu.getPrice()),
                        () -> assertEquals(expectMenu.getMenuGroup().getId(), actualMenu.getMenuGroup().getId())
                );
            }
        }
    }

    @Nested
    @DisplayName("메뉴를 생성할 수 있다.")
    class MenuCreateTest {


        private MenuBuilder menuBuilder;

        @BeforeEach
        void setUp() {
            final Product product = productRepository.save(new ProductBuilder().setPrice(BigDecimal.valueOf(100_000_000))
                    .build());

            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(1);

            menuBuilder = new MenuBuilder()
                    .setMenuGroup(menuGroup)
                    .setMenuProducts(List.of(menuProduct));
        }

        @ParameterizedTest
        @CsvSource(value = {"0", "1", "10000"})
        @DisplayName("상품 가격이 0 이상이고 MenuGroup이 존재하며 MenuProduct에 속하는 모든 상품의 가격 * 수량의 합보다 작으면 정상적으로 저장된다.")
        void saveTest(final BigDecimal price) {
            // given
            final Menu menu = menuBuilder
                    .setPrice(price)
                    .build();

            // when
            final Menu actual = menuService.create(menu);

            // then
            assertAll(
                    () -> assertEquals(menu.getName(), actual.getName()),
                    () -> assertThat(menu.getPrice()).isEqualByComparingTo(actual.getPrice()),
                    () -> assertEquals(menu.getMenuGroup().getId(), actual.getMenuGroup().getId())
            );
        }

        @ParameterizedTest
        @NullSource
        @CsvSource(value = {"-1", "-2", "-100000"})
        @DisplayName("상품 가격이 null이거나 0 미만이면 IllegalArgumentException이 발생한다.")
        void smallerThenZeroPriceTest(final BigDecimal price) {
            // given
            final Menu menu = menuBuilder
                    .setPrice(price)
                    .build();

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        @DisplayName("MenuGroup이 존재하지 않으면 IllegalArgumentException이 발생한다.")
        void invalidGroupIdTest() {
            // given
            final MenuGroup notSavedMenuGroup = new MenuGroupBuilder()
                    .build();

            final Menu menu = menuBuilder
                    .setMenuGroup(notSavedMenuGroup)
                    .build();

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        @DisplayName("Menu의 가격이 MenuProduct에 속하는 모든 상품의 가격 * 수량의 합보다 크면 IllegalArgumentException이 발생한다.")
        void largerThenTotalProductPriceTest() {
            // given
            final int price = 1;

            final Menu menu = menuBuilder
                    .setPrice(BigDecimal.valueOf(price))
                    .setMenuProducts(Collections.emptyList())
                    .build();

            // when & then
            assertThrowsExactly(IllegalArgumentException.class, () -> menuService.create(menu));
        }
    }
}
