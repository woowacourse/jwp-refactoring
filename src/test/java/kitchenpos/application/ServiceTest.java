package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.menu.domain.JpaMenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menuGroup.domain.JpaMenuGroupRepository;
import kitchenpos.order.domain.JpaOrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.product.domain.JpaProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.JpaOrderTableRepository;
import kitchenpos.table.domain.JpaTableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
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

    protected void 진행중_주문_조회() {
        Mockito.when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(주문_생성(OrderStatus.COOKING)));
    }

    protected void 메뉴_리스트_세팅(Long count) {
        Mockito.when(menuRepository.findAllById(any())).thenReturn(new ArrayList<>());
    }

    protected void 존재하지않는_테이블_세팅() {
        Mockito.when(orderTableRepository.existsById(anyLong())).thenReturn(false);
    }


    protected OrderTable 테이블_생성(Long id) {
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(),
                Arrays.asList(new OrderTable(1, true), new OrderTable(1, true)));
        return new OrderTable(id, tableGroup, 1, false);
    }

    protected TableGroup 테이블_그룹_생성() {
        return new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블_생성(1L), 테이블_생성(2L)));
    }


    protected OrderTableIdRequest 테이블_요청_생성(Long id) {
        return new OrderTableIdRequest(1L);
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

    /**
     * table Group test fixture
     */

    protected void 그룹_id로_조회시_객체_반환하도록_세팅() {
        Mockito.when(tableGroupRepository.findById(anyLong()))
                .thenReturn(Optional.of(테이블_그룹_생성()));
    }
}
