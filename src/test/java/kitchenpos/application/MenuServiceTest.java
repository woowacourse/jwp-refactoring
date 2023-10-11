package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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

    @Test
    @DisplayName("상품을 저장한다.")
    void create() {
        //given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2L);
        final Menu menu = new Menu(1L, "치킨+치킨", new BigDecimal(10000), 1L, List.of(menuProduct));
        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(new Product(1L, "치킨", new BigDecimal(5000))));
        given(menuDao.save(menu)).willReturn(menu);
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);

        //when
        final Menu result = menuService.create(menu);

        //then
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(menu);
    }

    @Test
    @DisplayName("모든 메뉴를 조회한다.")
    void list() {
        //given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2L);
        final Menu menu = new Menu(1L, "치킨+치킨", new BigDecimal(10000), 1L, List.of(menuProduct));
        given(menuDao.findAll()).willReturn(List.of(menu));
        given(menuProductDao.findAllByMenuId(1L)).willReturn(List.of(menuProduct));

        //when
        final List<Menu> result = menuService.list();

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMenuProducts()).hasSize(1);
    }
}
