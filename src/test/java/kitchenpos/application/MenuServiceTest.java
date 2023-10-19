package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

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

//    @Nested
//    class 메뉴_등록 {
//
//        @Test
//        void 메뉴를_등록한다() {
//            // given
//            Menu menu = new Menu();
//            menu.setName("한마리 메뉴");
//            menu.setPrice(BigDecimal.valueOf(19_000));
//            menu.setMenuGroup(1L);
//
//            MenuProduct menuProduct = new MenuProduct();
//            menuProduct.setProductId(1L);
//            menuProduct.setQuantity(2);
//
//            menu.setMenuProducts(List.of(menuProduct));
//
//            Product product = new Product();
//            product.setId(1L);
//            product.setPrice(BigDecimal.valueOf(10_000));
//
//            given(menuGroupDao.existsById(anyLong())).willReturn(true);
//            given(productDao.findById(anyLong())).willReturn(Optional.of(product));
//            given(menuDao.save(any(Menu.class))).willReturn(menu);
//            given(menuProductDao.save(any(MenuProduct.class))).willReturn(menuProduct);
//
//            // when
//            Menu result = menuService.create(menu);
//
//            // then
//            assertThat(result)
//                    .usingRecursiveComparison()
//                    .ignoringFields("id")
//                    .isEqualTo(menu);
//        }
//
//        @Test
//        void 메뉴그룹이_존재하지_않으면_예외가_발생한다() {
//            // given
//            Menu menu = new Menu();
//            menu.setName("한마리 메뉴");
//            menu.setPrice(BigDecimal.valueOf(19_000));
//
//            MenuProduct menuProduct = new MenuProduct();
//            menuProduct.setProductId(1L);
//            menuProduct.setQuantity(2);
//
//            menu.setMenuProducts(List.of(menuProduct));
//
//            Product product = new Product();
//            product.setId(1L);
//            product.setPrice(BigDecimal.valueOf(10_000));
//
//            given(menuGroupDao.existsById(null)).willReturn(false);
//
//            // when, then
//            assertThatThrownBy(() -> menuService.create(menu))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Nested
//        class 메뉴_가격 {
//
//            @ParameterizedTest
//            @ValueSource(longs = {-1, -12312, -999})
//            void 메뉴_가격이_음수인_경우_에러가_발생한다(Long value) {
//                // given
//                Menu menu = new Menu();
//                menu.setName("한마리 메뉴");
//                menu.setPrice(BigDecimal.valueOf(value));
//                menu.setMenuGroupId(1L);
//
//                MenuProduct menuProduct = new MenuProduct();
//                menuProduct.setProductId(1L);
//                menuProduct.setQuantity(2);
//                menu.setMenuProducts(List.of(menuProduct));
//
//                Product product = new Product();
//                product.setId(1L);
//                product.setPrice(BigDecimal.valueOf(10_000));
//
//                // when, then
//                assertThatThrownBy(() -> menuService.create(menu))
//                        .isInstanceOf(IllegalArgumentException.class);
//            }
//
//            @Test
//            void 메뉴_가격이_null인_경우_에러가_발생한다() {
//                // given
//                Menu menu = new Menu();
//                menu.setName("한마리 메뉴");
//                menu.setMenuGroupId(1L);
//
//                MenuProduct menuProduct = new MenuProduct();
//                menuProduct.setProductId(1L);
//                menuProduct.setQuantity(2);
//                menu.setMenuProducts(List.of(menuProduct));
//
//                Product product = new Product();
//                product.setId(1L);
//                product.setPrice(BigDecimal.valueOf(10_000));
//
//                // when, then
//                assertThatThrownBy(() -> menuService.create(menu))
//                        .isInstanceOf(IllegalArgumentException.class);
//            }
//
//            @Test
//            void 메뉴_가격이_메뉴_상품의_총_가격보다_큰_경우_예외가_발생한다() {
//                // given
//                Menu menu = new Menu();
//                menu.setName("한마리 메뉴");
//                menu.setPrice(BigDecimal.valueOf(23_000));
//                menu.setMenuGroupId(1L);
//
//                MenuProduct menuProductA = new MenuProduct();
//                menuProductA.setProductId(1L);
//                menuProductA.setQuantity(1);
//                MenuProduct menuProductB = new MenuProduct();
//                menuProductB.setProductId(2L);
//                menuProductB.setQuantity(1);
//
//                menu.setMenuProducts(List.of(menuProductA, menuProductB));
//
//                Product productA = new Product();
//                productA.setId(1L);
//                productA.setPrice(BigDecimal.valueOf(10_000));
//                Product productB = new Product();
//                productB.setId(2L);
//                productB.setPrice(BigDecimal.valueOf(12_000));
//
//                given(menuGroupDao.existsById(anyLong())).willReturn(true);
//                given(productDao.findById(1L)).willReturn(Optional.of(productA));
//                given(productDao.findById(2L)).willReturn(Optional.of(productB));
//
//                // when, then
//                assertThatThrownBy(() -> menuService.create(menu))
//                        .isInstanceOf(IllegalArgumentException.class);
//            }
//        }
//    }
//
//    @Test
//    void 모든_메뉴_목록을_불러온다() {
//        // given
//        Menu menu = new Menu();
//        menu.setName("한마리 메뉴");
//        menu.setPrice(BigDecimal.valueOf(19_000));
//        menu.setMenuGroupId(1L);
//
//        MenuProduct menuProduct = new MenuProduct();
//        menuProduct.setProductId(1L);
//        menuProduct.setQuantity(2);
//
//        menu.setMenuProducts(List.of(menuProduct));
//
//        Product product = new Product();
//        product.setId(1L);
//        product.setPrice(BigDecimal.valueOf(10_000));
//
//        given(menuDao.findAll()).willReturn(List.of(menu));
//        given(menuProductDao.findAllByMenuId(menu.getId())).willReturn(menu.getMenuProducts());
//
//        // when
//        List<Menu> menus = menuService.list();
//
//        // then
//        assertThat(menus.get(0)).isEqualTo(menu);
//    }

}
