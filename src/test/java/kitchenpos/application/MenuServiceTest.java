package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuService 테스트")
class MenuServiceTest {

    private Menu menu;
    private Product product;
    private MenuProduct menuProduct;
    private MenuGroup menuGroup;

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("후라이드 치킨");
        product.setPrice(BigDecimal.valueOf(16000L));

        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("치킨");

        menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드+후라이드");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setPrice(BigDecimal.valueOf(30000L));

        menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2L);

        menu.setMenuProducts(Collections.singletonList(menuProduct));
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("Menu의 price가 null이면")
        class Context_with_menu_price_null {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                // given
                menu.setPrice(null);

                // when, then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
                then(menuGroupDao)
                        .should(never())
                        .existsById(anyLong());
                then(productDao)
                        .should(never())
                        .findById(anyLong());
                then(menuDao)
                        .should(never())
                        .save(any());
                then(menuProductDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("Menu의 price가 음수라면")
        class Context_with_menu_price_negative {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                // given
                menu.setPrice(BigDecimal.valueOf(-1000L));

                // when, then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
                then(menuGroupDao)
                        .should(never())
                        .existsById(anyLong());
                then(productDao)
                        .should(never())
                        .findById(anyLong());
                then(menuDao)
                        .should(never())
                        .save(any());
                then(menuProductDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("Menu의 price가 Product의 price * quantity보다 큰 경우")
        class Context_with_menu_price_bigger_than_product_price_multiply_quantity {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                // given
                product.setPrice(BigDecimal.valueOf(16000L));
                menu.setPrice(BigDecimal.valueOf(34000L));
                given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
                given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));

                // when, then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
                then(menuGroupDao)
                        .should(times(1))
                        .existsById(anyLong());
                then(productDao)
                        .should(times(1))
                        .findById(anyLong());
                then(menuDao)
                        .should(never())
                        .save(any());
                then(menuProductDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("Menu의 menuGroupId에 해당하는 MenuGroup가 DB에 존재하지 않는다면")
        class Context_with_menuGroup_not_exist {

            @Test
            @DisplayName("예외가 발생한다")
            void it_throws_exception() {
                // given
                menu.setMenuGroupId(2L);
                given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(false);

                // when, then
                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
                then(menuGroupDao)
                        .should(times(1))
                        .existsById(anyLong());
                then(productDao)
                        .should(never())
                        .findById(anyLong());
                then(menuDao)
                        .should(never())
                        .save(any());
                then(menuProductDao)
                        .should(never())
                        .save(any());
            }
        }

        @Nested
        @DisplayName("정상적인 경우라면")
        class Context_with_correct_case {

            @Test
            @DisplayName("Menu를 반환한다")
            void it_return_menu() {
                // given
                given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
                given(productDao.findById(menuProduct.getProductId())).willReturn(Optional.of(product));
                given(menuDao.save(menu)).willReturn(menu);
                given(menuProductDao.save(menuProduct)).willReturn(menuProduct);

                // when
                Menu createdMenu = menuService.create(menu);

                // then
                assertThat(createdMenu).isEqualTo(menu);
                then(menuGroupDao)
                        .should(times(1))
                        .existsById(menu.getMenuGroupId());
                then(productDao)
                        .should(times(1))
                        .findById(menuProduct.getProductId());
                then(menuDao)
                        .should(times(1))
                        .save(menu);
                then(menuProductDao)
                        .should(times(1))
                        .save(menuProduct);
            }
        }
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Test
        @DisplayName("Menu 리스트를 반환한다.")
        void it_return_menu_list() {
            // given
            Product anotherProduct = new Product();
            anotherProduct.setId(2L);
            anotherProduct.setName("양념 치킨");
            anotherProduct.setPrice(BigDecimal.valueOf(17000L));

            Menu anotherMenu = new Menu();
            anotherMenu.setId(2L);
            anotherMenu.setName("양념+양념");
            anotherMenu.setMenuGroupId(menuGroup.getId());
            anotherMenu.setPrice(BigDecimal.valueOf(32000L));

            MenuProduct anotherMenuProduct = new MenuProduct();
            anotherMenuProduct.setSeq(2L);
            anotherMenuProduct.setMenuId(anotherMenu.getId());
            anotherMenuProduct.setProductId(anotherProduct.getId());
            anotherMenuProduct.setQuantity(2L);

            given(menuDao.findAll()).willReturn(Arrays.asList(menu, anotherMenu));
            given(menuProductDao.findAllByMenuId(anyLong())).willReturn(Collections.singletonList(menuProduct),
                    Collections.singletonList(anotherMenuProduct));

            // when
            List<Menu> menus = menuService.list();

            // then
            assertThat(menus).containsAll(Arrays.asList(menu, anotherMenu));
            assertThat(menus).hasSize(2);
            then(menuDao)
                    .should(times(1))
                    .findAll();
            then(menuProductDao)
                    .should(times(2))
                    .findAllByMenuId(anyLong());
        }
    }
}