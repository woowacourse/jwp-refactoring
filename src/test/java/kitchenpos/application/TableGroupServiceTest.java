//package kitchenpos.application;
//
//import kitchenpos.config.Dataset;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.domain.*;
//import org.assertj.core.util.Lists;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.BDDMockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TableGroupServiceTest {
//
//    @Mock
//    private TableGroupDao tableGroupDao;
//
//    @Mock
//    private OrderDao orderDao;
//
//    @Mock
//    private OrderTableDao orderTableDao;
//
//    @InjectMocks
//    private TableGroupService service;
//
//    @BeforeEach
//    public void setUp() {
//
//    }
//
//    @DisplayName("단체 지정 실패 - 주문 테이블이 없을 경우")
//    @Test
//    public void createFailZeroTable() {
//
//
//        assertThatThrownBy(() -> service.create(tableGroup))
//            .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("단체 지정 실패 - 주문 테이블 갯수가 1개일 경우")
//    @Test
//    public void createFailSingleTable() {
//        tableGroup.setOrderTables(Lists.newArrayList(orderTable1));
//
//        assertThatThrownBy(() -> service.create(tableGroup))
//            .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("단체 지정 실패 - 지정하고자 하는 테이블 갯수와 저장가능한 테이블 갯수가 다를 경우")
//    @Test
//    public void createFailNotMatchTableCount() {
//        given(orderTableDao.findAllByIdIn(any())).willReturn(Lists.newArrayList());
//
//        assertThatThrownBy(() -> service.create(tableGroup))
//            .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("단체 지정 실패 - 이미 지정된 테이블 단체가 있을 경우")
//    @Test
//    public void createFailExistedTableGroup() {
//        orderTable1.setTableGroupId(1L);
//        given(orderTableDao.findAllByIdIn(any())).willReturn(Lists.newArrayList(orderTable1, orderTable2));
//
//        assertThatThrownBy(() -> service.create(tableGroup))
//            .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("단체 지정")
//    @Test
//    public void createTableGroup() {
//        given(orderTableDao.findAllByIdIn(any())).willReturn(Lists.newArrayList(orderTable1, orderTable2));
//        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);
//
//        final TableGroup savedTableGroup = service.create(tableGroup);
//
//        assertThat(savedTableGroup.getId()).isEqualTo(20L);
//        assertThat(savedTableGroup.getOrderTables()).hasSize(2);
//        assertThat(savedTableGroup.getOrderTables().get(0).isEmpty()).isFalse();
//        assertThat(savedTableGroup.getOrderTables().get(1).isEmpty()).isFalse();
//    }
//
//    @DisplayName("단체 해제 실패 - 주문 상태가 조리 중, 식사 중일 경우")
//    @Test
//    public void unGroupFail() {
//        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Lists.newArrayList(orderTable1, orderTable2));
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);
//
//        assertThatThrownBy(() -> service.ungroup(tableGroup.getId()))
//            .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("단체 해제")
//    @Test
//    public void unGroup() {
//        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Lists.newArrayList(orderTable1, orderTable2));
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
//
//        service.ungroup(tableGroup.getId());
//
//        assertThat(orderTable1.isEmpty()).isFalse();
//        assertThat(orderTable2.isEmpty()).isFalse();
//    }
//}
