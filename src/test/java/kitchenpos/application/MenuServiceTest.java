package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Menu 메뉴는 이름, 가격, 메뉴 그룹, 메뉴 상품 리스트를 가지고 있습니다.
 * <p>
 * create (메뉴를 생성할 수 있습니다.)
 * <p>
 * price 가 필수값입니다. price > 0 조건을 만족해야합니다. 존재하는 menu group 이어야 합니다. menu 가 가지고 있는 product 가 존재하는 상품이어야합니다. findAll (메뉴
 * 전체를 조회합니다.)
 */

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;
    /*
    Data 유무와 별개로 실제 비지니스 로직이 정상적으로 진행되는지 확인하기 위해 격리
     */
    @MockBean
    private MenuDao menuDao;
    @MockBean
    private MenuGroupDao menuGroupDao;
    @MockBean
    private MenuProductDao menuProductDao;
    @MockBean
    private ProductDao productDao;

    @Test
    void 메뉴의_가격이_0이하면_예외를_반환한다() {
        // given
        final Menu 세트A = new Menu("세트A", BigDecimal.ZERO, 1L, null);

        // when then
        assertThatThrownBy(() -> menuService.create(세트A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_Null_이면_예외를_반환한다() {
        // given
        final Menu 세트A = new Menu("세트A", null, 1L, null);

        // when then
        assertThatThrownBy(() -> menuService.create(세트A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴그룹이_존재하지_않으면_예외를_반환한다() {
        //given
        final Menu 세트A = new Menu("세트A", BigDecimal.valueOf(1000), 1L, null);
        메뉴그룹에서_없는_메뉴로_세팅한다();

        // when then
        assertThatThrownBy(() -> menuService.create(세트A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴가_존재하지_않으면_예외를_반환한다() {
        //given
        final Menu 세트A = get세트A();
        메뉴그룹에서_있는_메뉴로_세팅한다();
        없는_상품으로_세팅한다();

        assertThatThrownBy(() -> menuService.create(세트A))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * test fixture
     */

    private void 메뉴그룹에서_없는_메뉴로_세팅한다() {
        Mockito.when(menuGroupDao.existsById(any()))
                .thenReturn(false);
    }

    private void 메뉴그룹에서_있는_메뉴로_세팅한다() {
        Mockito.when(menuGroupDao.existsById(any()))
                .thenReturn(true);
    }

    private void 없는_상품으로_세팅한다() {
        Mockito.when(productDao.findById(any()))
                .thenReturn(Optional.empty());
    }

    private Menu get세트A() {
        final Product 짜장면 = new Product(1L, "짜장면", BigDecimal.valueOf(9000));
        final Product 짬뽕 = new Product(2L, "짬뽕", BigDecimal.valueOf(9000));
        final Product 탕수육 = new Product(3L, "탕수육", BigDecimal.valueOf(20000));

        final Menu 세트A = new Menu(1L, "세트A", BigDecimal.valueOf(38000), 1L, null);

        final MenuProduct 짜장면_주문량 = new MenuProduct(세트A.getId(), 짜장면.getId(), 1);
        final MenuProduct 짬뽕_주문량 = new MenuProduct(세트A.getId(), 짬뽕.getId(), 1);
        final MenuProduct 탕수육_주문량 = new MenuProduct(세트A.getId(), 탕수육.getId(), 1);
        세트A.setMenuProducts(Arrays.asList(짜장면_주문량, 짬뽕_주문량, 탕수육_주문량));

        return 세트A;
    }
}
