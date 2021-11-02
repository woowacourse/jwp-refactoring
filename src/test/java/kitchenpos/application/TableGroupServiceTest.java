package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableGroupService 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    private final OrderLineItemFixture orderLineItemFixture = new OrderLineItemFixture();
    private final OrderFixture orderFixture = new OrderFixture();
    private final OrderTableFixture orderTableFixture = new OrderTableFixture();
    private final TableGroupFixture tableGroupFixture = new TableGroupFixture();

    private OrderLineItem 주문1_메뉴1;
    private OrderLineItem 주문1_메뉴2;
    private List<OrderLineItem> 주문1_메뉴_리스트;
    private Order 주문1;
    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;
    private List<OrderTable> 주문1_테이블들;

    @BeforeEach
    public void setUp() {
        주문1_메뉴1 = orderLineItemFixture.주문_메뉴_생성(1L, 1L, 1L, 1);
        주문1_메뉴2 = orderLineItemFixture.주문_메뉴_생성(1L, 1L, 2L, 1);
        주문1_메뉴_리스트 = orderLineItemFixture.주문_메뉴_리스트_생성(주문1_메뉴1, 주문1_메뉴2);
        주문1 = orderFixture.주문_생성(1L, OrderStatus.COOKING.name(), 주문1_메뉴_리스트);
        주문_테이블1 = orderTableFixture.주문_테이블_생성(1L, null, 2, true);
        주문_테이블2 = orderTableFixture.주문_테이블_생성(2L, null, 4, true);
        주문1_테이블들 = orderTableFixture.주문_테이블_리스트_생성(주문_테이블1, 주문_테이블2);
    }

    @Test
    @DisplayName("테이블 그룹 생성 테스트 - 성공")
    public void createTest() throws Exception {
        // given
        TableGroup 테이블그룹1 = tableGroupFixture.테이블_그룹_생성(now(), 주문1_테이블들);
        List<Long> orderTableIds = 주문1_테이블들.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(주문1_테이블들);

        TableGroup expected = tableGroupFixture.테이블_그룹_생성(now(), 주문1_테이블들);
        given(tableGroupDao.save(테이블그룹1)).willReturn(expected);
        given(orderTableDao.save(주문_테이블1)).willReturn(주문_테이블1);
        given(orderTableDao.save(주문_테이블2)).willReturn(주문_테이블2);

        // when
        TableGroup actual = tableGroupService.create(테이블그룹1);

        // then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("")
    public void ungroupTest() throws Exception {
        // given
        Long tableGroupId = 1L;
        주문_테이블1 = orderTableFixture.주문_테이블_생성(1L, tableGroupId, 2, true);
        주문_테이블2 = orderTableFixture.주문_테이블_생성(2L, tableGroupId, 4, true);
        주문1_테이블들 = orderTableFixture.주문_테이블_리스트_생성(주문_테이블1, 주문_테이블2);
        List<Long> orderTableIds = 주문1_테이블들.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(주문1_테이블들);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays
                .asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        OrderTable 주문_테이블1_expected = orderTableFixture.주문_테이블_생성(1L, null, 2, false);
        OrderTable 주문_테이블2_expected = orderTableFixture.주문_테이블_생성(2L, null, 4, false);

        given(orderTableDao.save(주문_테이블1)).willReturn(주문_테이블1_expected);
        given(orderTableDao.save(주문_테이블2)).willReturn(주문_테이블2_expected);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        assertNull(주문_테이블1.getTableGroupId());
        assertNull(주문_테이블2.getTableGroupId());
        assertFalse(주문_테이블1.isEmpty());
        assertFalse(주문_테이블2.isEmpty());
    }
}
