package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ServiceTest {

    @MockBean
    protected MenuDao menuDao;
    @MockBean
    protected MenuGroupDao menuGroupDao;
    @MockBean
    protected MenuProductDao menuProductDao;
    @MockBean
    protected ProductDao productDao;
    @MockBean
    protected OrderDao orderDao;
    @MockBean
    protected OrderLineItemDao orderLineItemDao;
    @MockBean
    protected OrderTableDao orderTableDao;


    /**
     * menu test fixture
     */
    protected void 완료된_주문_조회() {
        Mockito.when(orderDao.findById(anyLong())).thenReturn(Optional.of(주문_생성(OrderStatus.COMPLETION)));
    }

    protected void 메뉴존재유뮤세팅(Long count) {
        Mockito.when(menuDao.countByIdIn(any())).thenReturn(count);
    }

    protected void 존재하지않는_테이블_세팅() {
        Mockito.when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());
    }

    protected void 존재하는_테이블_세팅() {
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        Mockito.when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
    }

    protected Order 주문_생성(OrderStatus status) {
        final Order 주문 = new Order(1L, 1L, status.name(), LocalDateTime.now(), null);
        final OrderLineItem 주문_수량 = new OrderLineItem(1L, 1L, 1L, 1);

        주문.setOrderLineItems(Arrays.asList(주문_수량));

        return 주문;
    }

    /**
     * order test fixture
     */

    protected void 메뉴그룹에서_없는_메뉴로_세팅한다() {
        Mockito.when(menuGroupDao.existsById(any()))
                .thenReturn(false);
    }

    protected void 메뉴그룹에서_있는_메뉴로_세팅한다() {
        Mockito.when(menuGroupDao.existsById(any()))
                .thenReturn(true);
    }

    protected void 없는_상품으로_세팅한다() {
        Mockito.when(productDao.findById(any()))
                .thenReturn(Optional.empty());
    }

    protected Menu get세트A() {
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
