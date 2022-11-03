package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JpaMenuGroupRepository;
import kitchenpos.menu.domain.JpaMenuRepository;
import kitchenpos.order.domain.JpaOrderRepository;
import kitchenpos.table.domain.JpaOrderTableRepository;
import kitchenpos.product.domain.JpaProductRepository;
import kitchenpos.table.domain.JpaTableGroupRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.table.ui.request.OrderTableIdRequest;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/***
 * 테스트 내에 사용하는 Mock 객체들을 관리하는 객체입니다.
 * Application Context 재사용을 위해서 상속 구조를 가지고 있습니다.
 */
@SpringBootTest
public class ServiceTest {

    @MockBean
    protected JpaMenuRepository menuRepository;
    @MockBean
    protected JpaMenuGroupRepository menuGroupRepository;
    @MockBean
    protected JpaProductRepository productRepository;
    @MockBean
    protected JpaOrderRepository orderRepository;
    @MockBean
    protected JpaOrderTableRepository orderTableRepository;
    @MockBean
    protected JpaTableGroupRepository tableGroupRepository;

    /**
     * order test fixture
     */
    protected void 완료된_주문_조회() {
        Mockito.when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(주문_생성(OrderStatus.COMPLETION)));
    }

    protected void 진행중_주문_조회() {
        Mockito.when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(주문_생성(OrderStatus.COOKING)));
    }

    protected void 메뉴_리스트_세팅(Long count) {
        List<Menu> menus = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            menus.add(new Menu());
        }
        Mockito.when(menuRepository.findAllById(any())).thenReturn(menus);
    }

    protected void 존재하지않는_테이블_세팅() {
        Mockito.when(orderTableRepository.existsById(anyLong())).thenReturn(false);
    }

    protected void 주문에_테이블이_없을때_세팅() {
        Mockito.when(orderRepository.findByOrderTableId(anyLong())).thenReturn(Optional.empty());
    }

    protected OrderTable 테이블_생성(Long id) {
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(),
                Arrays.asList(new OrderTable(), new OrderTable()));
        return new OrderTable(id, tableGroup, 1, false);
    }

    protected TableGroup 테이블_그룹_생성() {
        return new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블_생성(1L), 테이블_생성(2L)));
    }

    protected OrderTable 테이블_그룹_없는_테이블_생성(Long id) {
        return new OrderTable(id, null, 1, false);
    }

    protected OrderTableIdRequest 테이블_요청_생성(Long id) {
        return new OrderTableIdRequest(1L);
    }

    protected void 테이블_그룹이_없는_테이블_세팅(Long id) {
        Mockito.when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(new OrderTable(id, null, 1, false)));
    }

    protected void 존재하는_테이블_세팅() {
        Mockito.when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블_생성(1L)));
    }

    protected void 존재하는_요리중_테이블_세팅() {
        final Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(1L, 1L, 5)));
        Mockito.when(orderRepository.findByOrderTableId(anyLong())).thenReturn(Optional.of(order));
    }

    protected Order 주문_생성(OrderStatus status) {
        final Order 주문 = new Order(1L, 1L, status.name(), LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(1L, 1L, 5)));
        final OrderLineItem 주문_수량 = new OrderLineItem(1L, 1L, 1);

        주문.setOrderLineItems(Arrays.asList(주문_수량));

        return 주문;
    }

    protected OrderRequest 주문_요청_생성(OrderStatus status) {
        final OrderLineItemRequest 주문_수량 = new OrderLineItemRequest(1L, 1L, 1);
        final OrderRequest 주문 = new OrderRequest(1L, status.name(), LocalDateTime.now(), Arrays.asList(주문_수량));
        return 주문;
    }

    /**
     * menu test fixture
     */

    protected void 메뉴그룹에서_없는_메뉴로_세팅한다() {
        Mockito.when(menuGroupRepository.existsById(any()))
                .thenReturn(false);
    }

    protected void 메뉴그룹에서_있는_메뉴로_세팅한다() {
        Mockito.when(menuGroupRepository.existsById(any()))
                .thenReturn(true);
    }

    protected void 없는_상품으로_세팅한다() {
        Mockito.when(productRepository.existsById(any()))
                .thenReturn(false);
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

    /**
     * table Group test fixture
     */

//    protected void 그룹_내_주문_상태를_진행중으로_설정() {
//        Mockito.when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);
//    }
//
//    protected void 테이블_내_주문_상태를_진행중으로_설정() {
//        Mockito.when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);
//    }
    protected void 그룹_id로_조회시_객체_반환하도록_세팅() {
        Mockito.when(tableGroupRepository.findById(anyLong()))
                .thenReturn(Optional.of(테이블_그룹_생성()));
    }

    /**
     * table  test fixture
     */

}
