package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuService 단위 테스")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("Menu 가격이 null이면")
        @Nested
        class Context_price_null {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Menu menu = new Menu();
                menu.setPrice(null);
                menu.setName("kevin");
                menu.setMenuGroupId(1L);
                menu.setMenuProducts(Collections.emptyList());

                // when, then
                assertThatCode(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("Menu 가격이 음수면")
        @Nested
        class Context_price_negative {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Menu menu = new Menu();
                menu.setPrice(BigDecimal.valueOf(-1));
                menu.setName("kevin");
                menu.setMenuGroupId(1L);
                menu.setMenuProducts(Collections.emptyList());

                // when, then
                assertThatCode(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @DisplayName("Menu가 속한 MenuGroup이 존재하지 않는다면")
        @Nested
        class Context_menu_group_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Menu menu = new Menu();
                menu.setPrice(BigDecimal.valueOf(100));
                menu.setName("kevin");
                menu.setMenuGroupId(1L);
                menu.setMenuProducts(Collections.emptyList());
                given(menuGroupDao.existsById(1L)).willReturn(false);

                // when, then
                assertThatCode(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);

                verify(menuGroupDao, times(1)).existsById(1L);
            }
        }

        @DisplayName("Menu에 속한 MenuProduct와 연결된 Product를 조회할 수 없으면")
        @Nested
        class Context_product_of_menu_product_not_found {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Menu menu = new Menu();
                MenuProduct menuProduct1 = new MenuProduct();
                MenuProduct menuProduct2 = new MenuProduct();
                menu.setPrice(BigDecimal.valueOf(100));
                menu.setName("kevin");
                menu.setMenuGroupId(1L);
                menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
                menuProduct1.setProductId(1L);
                menuProduct2.setProductId(2L);
                given(menuGroupDao.existsById(1L)).willReturn(true);
                given(productDao.findById(1L)).willReturn(Optional.empty());

                // when, then
                assertThatCode(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);

                verify(menuGroupDao, times(1)).existsById(1L);
                verify(productDao, times(1)).findById(1L);
            }
        }

        @DisplayName("Menu 가격이 MenuProduct들의 누계를 초과하면")
        @Nested
        class Context_menu_product_sum_gt_menu_price {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Menu menu = new Menu();
                MenuProduct menuProduct1 = new MenuProduct();
                MenuProduct menuProduct2 = new MenuProduct();
                Product product1 = new Product();
                Product product2 = new Product();
                menu.setPrice(BigDecimal.valueOf(61));
                menu.setName("kevin");
                menu.setMenuGroupId(1L);
                menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
                menuProduct1.setProductId(1L);
                menuProduct1.setQuantity(3);
                menuProduct2.setProductId(2L);
                menuProduct2.setQuantity(2);
                product1.setPrice(BigDecimal.valueOf(10));
                product2.setPrice(BigDecimal.valueOf(15));
                given(menuGroupDao.existsById(1L)).willReturn(true);
                given(productDao.findById(1L)).willReturn(Optional.of(product1));
                given(productDao.findById(2L)).willReturn(Optional.of(product2));

                // when, then
                assertThatCode(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);

                verify(menuGroupDao, times(1)).existsById(1L);
                verify(productDao, times(2)).findById(anyLong());
            }
        }

        @DisplayName("Menu 가격이 0 이상 양수 및 MemberProduct 누계 이하이고 MenuGroup이 존재하면")
        @Nested
        class Context_other_valid_case {

            @DisplayName("Menu를 정상 등록한다.")
            @ParameterizedTest
            @ValueSource(ints = {59, 60})
            void it_saves_and_returns_menu(int price) {
                // given
                Menu menu = new Menu();
                Menu savedMenu = new Menu();
                MenuProduct menuProduct1 = new MenuProduct();
                MenuProduct menuProduct2 = new MenuProduct();
                Product product1 = new Product();
                Product product2 = new Product();
                menu.setPrice(BigDecimal.valueOf(price));
                menu.setName("kevin");
                menu.setMenuGroupId(1L);
                menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
                savedMenu.setId(1L);
                savedMenu.setName("kevin");
                savedMenu.setMenuGroupId(1L);
                menuProduct1.setProductId(1L);
                menuProduct1.setQuantity(3);
                menuProduct2.setProductId(2L);
                menuProduct2.setQuantity(2);
                product1.setPrice(BigDecimal.valueOf(10));
                product2.setPrice(BigDecimal.valueOf(15));
                given(menuGroupDao.existsById(1L)).willReturn(true);
                given(productDao.findById(1L)).willReturn(Optional.of(product1));
                given(productDao.findById(2L)).willReturn(Optional.of(product2));
                given(menuDao.save(menu)).willReturn(savedMenu);
                given(menuProductDao.save(menuProduct1)).willReturn(menuProduct1);
                given(menuProductDao.save(menuProduct2)).willReturn(menuProduct2);

                // when
                Menu response = menuService.create(menu);

                // then
                assertThat(response.getId()).isOne();
                assertThat(response.getMenuProducts()).hasSize(2)
                    .extracting("productId", "quantity")
                    .containsExactly(tuple(1L, 3L), tuple(2L, 2L));

                verify(menuGroupDao, times(1)).existsById(1L);
                verify(productDao, times(2)).findById(anyLong());
                verify(menuDao, times(1)).save(menu);
                verify(menuProductDao, times(2)).save(any(MenuProduct.class));
            }
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("Menu 목록을 조회한다.")
        @Test
        void it_returns_menu_list() {
            // given
            Menu menu = new Menu();
            Menu menu2 = new Menu();
            MenuProduct menuProduct1 = new MenuProduct();
            MenuProduct menuProduct2 = new MenuProduct();
            menu.setId(1L);
            menu2.setId(2L);
            menuProduct1.setQuantity(1L);
            menuProduct2.setQuantity(2L);
            given(menuDao.findAll()).willReturn(Arrays.asList(menu, menu2));
            given(menuProductDao.findAllByMenuId(1L)).willReturn(Arrays.asList(menuProduct1));
            given(menuProductDao.findAllByMenuId(2L)).willReturn(Arrays.asList(menuProduct2));

            // when
            List<Menu> response = menuService.list();

            // then
            assertThat(response).hasSize(2);

            verify(menuDao, times(1)).findAll();
            verify(menuProductDao, times(2)).findAllByMenuId(anyLong());
        }
    }
}
