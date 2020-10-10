package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @MockBean
    private MenuDao menuDao;

    @MockBean
    private ProductDao productDao;

    @MockBean
    private MenuProductDao menuProductDao;

    @MockBean
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        Product product = createProduct(1L, "콜라", BigDecimal.valueOf(2000L));
        MenuProduct menuProduct = createMenuProduct(null, 2L, 1L);

        Menu menu = createMenu(1L, 1L, Arrays.asList(menuProduct), "콜라세트", BigDecimal.valueOf(1900L));

        given(productDao.findById(any(Long.class))).willReturn(Optional.of(product));
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(menuProduct);
        given(menuGroupDao.existsById(any(Long.class))).willReturn(true);
        given(menuDao.save(any(Menu.class))).willReturn(menu);

        Menu savedMenu = menuService.create(menu);

        assertAll(
            () -> assertThat(savedMenu.getId()).isNotNull(),
            () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(1L),
            () -> assertThat(savedMenu.getPrice()).isEqualTo(BigDecimal.valueOf(1900)),
            () -> assertThat(savedMenu.getName()).isEqualTo("콜라세트"),
            () -> assertThat(savedMenu.getMenuProducts().get(0)).isEqualTo(menuProduct)
        );

    }

    @DisplayName("메뉴 가격이 음수거나 null일 경우 예외가 발생하는지 확인한다.")
    @Test
    void createInvalidPriceMenu() {
        Menu menuWithNullPrice = new Menu();
        menuWithNullPrice.setPrice(null);

        Menu menuWithMinusPrice = new Menu();
        menuWithMinusPrice.setPrice(BigDecimal.valueOf(-1L));

        assertAll(
            () -> assertThatThrownBy(
                () -> menuService.create(menuWithNullPrice)
            ).isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(
                () -> menuService.create(menuWithMinusPrice)
            ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("메뉴가 아무 메뉴 그룹에도 속하지 않을 시 예외가 발생하는지 확인한다.")
    @Test
    void createInvalidMenuGroup() {
        given(menuGroupDao.existsById(any(Long.class))).willReturn(false);

        Menu menu = createMenu(1L, 1L, Arrays.asList(), "둘둘치킨", BigDecimal.valueOf(2000L));

        assertThatThrownBy(
            () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 개별 상품 가격의 총합보다 비쌀 시 예외가 발생하는지 확인한다.")
    @Test
    void createInvalidDiscountMenu() {
        Product product = createProduct(1L, "콜라", BigDecimal.valueOf(2000L));
        MenuProduct menuProduct = createMenuProduct(null, 2L, 1L);
        Menu menu = createMenu(1L, 1L, Arrays.asList(menuProduct), "콜라세트", BigDecimal.valueOf(2100L));

        given(menuGroupDao.existsById(any(Long.class))).willReturn(true);
        given(productDao.findById(any(Long.class))).willReturn(Optional.of(product));
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(menuProduct);
        given(menuDao.save(any(Menu.class))).willReturn(menu);

        assertThatThrownBy(
            () -> menuService.create(menu)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹을 조회한다.")
    @Test
    void list() {
        Product product = createProduct(1L, "콜라", BigDecimal.valueOf(2000L));
        MenuProduct menuProduct = createMenuProduct(null, 2L, 1L);

        Menu menu = createMenu(1L, 1L, Arrays.asList(menuProduct), "콜라세트", BigDecimal.valueOf(2000L));

        given(menuGroupDao.existsById(any(Long.class))).willReturn(true);
        given(menuProductDao.save(any(MenuProduct.class))).willReturn(menuProduct);
        given(productDao.findById(any(Long.class))).willReturn(Optional.of(product));
        given(menuDao.save(any(Menu.class))).willReturn(menu);
        given(menuDao.findAll()).willReturn(Arrays.asList(menu));

        menuService.create(menu);
        List<Menu> menus = menuService.list();

        Menu savedMenu = menus.get(0);
        assertAll(
            () -> assertThat(menus.size()).isEqualTo(1),
            () -> assertThat(savedMenu.getId()).isNotNull(),
            () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(1L),
            () -> assertThat(savedMenu.getPrice()).isEqualTo(BigDecimal.valueOf(2000L)),
            () -> assertThat(savedMenu.getName()).isEqualTo("콜라세트")
        );
    }
}
