package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

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

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() {
        // given
        final Menu menu = new Menu();
        menu.setName("메뉴");
        menu.setPrice(BigDecimal.valueOf(30L));
        menu.setMenuProducts(List.of(
            new MenuProduct() {{
                setProductId(1L);
                setQuantity(1L);
            }},
            new MenuProduct() {{
                setProductId(2L);
                setQuantity(2L);
            }}
        ));
        menu.setMenuGroupId(1L);

        given(menuGroupDao.existsById(any(Long.class)))
            .willReturn(true);
        given(productDao.findById(any(Long.class)))
            .willReturn(Optional.of(new Product() {{
                setId(1L);
                setPrice(BigDecimal.TEN);
            }}))
            .willReturn(Optional.of(new Product() {{
                setId(2L);
                setPrice(BigDecimal.TEN);
            }}));
        given(menuDao.save(any(Menu.class)))
            .willReturn(new Menu() {{
                setId(1L);
                setName("메뉴");
                setPrice(BigDecimal.valueOf(30L));
                setMenuProducts(List.of(
                    new MenuProduct() {{
                        setProductId(1L);
                        setQuantity(1L);
                    }},
                    new MenuProduct() {{
                        setProductId(2L);
                        setQuantity(2L);
                    }}
                ));
            }});
        given(menuProductDao.save(any(MenuProduct.class)))
            .willReturn(new MenuProduct() {{
                setSeq(1L);
                setMenuId(1L);
                setProductId(1L);
                setQuantity(1L);
            }})
            .willReturn(new MenuProduct() {{
                setSeq(2L);
                setMenuId(1L);
                setProductId(2L);
                setQuantity(2L);
            }});

        // when
        final Menu created = menuService.create(menu);

        // then
        assertThat(created.getId()).isEqualTo(menu.getId());
        assertThat(created.getName()).isEqualTo(menu.getName());
        assertThat(created.getPrice()).isEqualTo(menu.getPrice());
    }

    @DisplayName("메뉴의 가격이 null 이면 예외가 발생한다.")
    @Test
    void create_failNullPrice() {
        // given
        final Menu menu = new Menu();
        menu.setPrice(null);

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 음수면 예외가 발생한다.")
    @Test
    void create_failNegativePrice() {
        // given
        final Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1L));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 MemberGroupId 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_failNotExistMemberGroupId() {
        // given
        final Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(30L));
        menu.setMenuGroupId(1L);

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 Product 가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void create_failNotExistProduct() {
        // given
        final Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(30L));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(
            new MenuProduct() {{
                setProductId(0L);
                setQuantity(1L);
            }}
        ));

        given(menuGroupDao.existsById(any(Long.class)))
            .willReturn(true);
        given(productDao.findById(any(Long.class)))
            .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴의 상품 가격의 합보다 크면 예외가 발생한다.")
    @Test
    void create_failGreaterPrice() {
        // given
        final Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(10L));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(
            new MenuProduct() {{
                setProductId(0L);
                setQuantity(1L);
            }}
        ));

        given(menuGroupDao.existsById(any(Long.class)))
            .willReturn(true);
        given(productDao.findById(any(Long.class)))
            .willReturn(Optional.of(new Product() {{
                setId(1L);
                setPrice(BigDecimal.valueOf(5L));
            }}));

        // when
        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
