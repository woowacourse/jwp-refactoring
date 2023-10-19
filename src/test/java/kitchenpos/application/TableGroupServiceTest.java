//package kitchenpos.application;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.BDDMockito.given;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import kitchenpos.fixture.OrderTableFixture;
//import kitchenpos.fixture.TableGroupFixture;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@SuppressWarnings("NonAsciiCharacters")
//@ExtendWith(MockitoExtension.class)
//public class TableGroupServiceTest {
//
//    @Mock
//    private OrderDao orderDao;
//
//    @Mock
//    private OrderTableDao orderTableDao;
//
//    @Mock
//    private TableGroupDao tableGroupDao;
//
//    @InjectMocks
//    private TableGroupService tableGroupService;
//
//    private OrderTable savedOrderTable1;
//    private OrderTable savedOrderTable2;
//    private TableGroup savedTableGroup;
//
//    @BeforeEach
//    void setUp() {
//        savedOrderTable1 = new OrderTable(1L, null, 0, true);
//        savedOrderTable2 = new OrderTable(2L, null, 0, true);
//        savedTableGroup = new TableGroup(1L, LocalDateTime.now());
//    }
//
//    @Test
//    void 주문_테이블들의_정보를_받아서_테이블_그룹_정보를_등록할_수_있다() {
//        //given
//        TableGroup tableGroupRequest = new TableGroup();
//        tableGroupRequest.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));
//
//        given(orderTableDao.findAllByIdIn(anyList())).willReturn(List.of(savedOrderTable1, savedOrderTable2));
//        given(tableGroupDao.save(any(TableGroup.class))).willReturn(savedTableGroup);
//
//        //when
//        TableGroup response = tableGroupService.create(tableGroupRequest);
//
//        //then
//        assertThat(response.getCreatedDate()).isNotNull();
//        assertThat(response.getOrderTables().get(0).getTableGroupId()).isEqualTo(savedTableGroup.getId());
//        assertThat(response.getOrderTables().get(0).isEmpty()).isFalse();
//        assertThat(response.getOrderTables().get(1).getTableGroupId()).isEqualTo(savedTableGroup.getId());
//        assertThat(response.getOrderTables().get(1).isEmpty()).isFalse();
//    }
//
//    @Test
//    void 입력한_주문_테이블_정보가_없는_경우_예외처리한다() {
//        //given
//        TableGroup tableGroupRequest = new TableGroup();
//
//        //when, then
//        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
//                .isInstanceOf(IllegalArgumentException.class);
//
//    }
//
//    @Test
//    void 입력한_주문_테이블_정보가_하나인_경우_예외처리한다() {
//        //given
//        TableGroup tableGroupRequest = new TableGroup();
//        tableGroupRequest.setOrderTables(List.of(savedOrderTable1));
//
//        //when, then
//        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
////    @Test
////    void 등록되지_않은_주문_테이블_정보를_입력한_경우_예외처리한다() {
////        //given
////        OrderTable unsavedOrderTableRequest = new OrderTable();
////        unsavedOrderTableRequest.setEmpty(true);
////
////        TableGroup tableGroupRequest = new TableGroup();
////        tableGroupRequest.setOrderTables(List.of(savedOrderTable1, savedOrderTable2, unsavedOrderTableRequest));
////
////        given(orderTableDao.findAllByIdIn(anyList())).willReturn(List.of(savedOrderTable1, savedOrderTable2));
////
////        //when, then
////        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
////                .isInstanceOf(IllegalArgumentException.class);
////    }
//
//    @Test
//    void 그룹을_지으려는_등록된_테이블_상태가_empty가_아닌_경우_예외처리한다() {
//        //given
//        TableGroup tableGroupRequest = new TableGroup();
//        tableGroupRequest.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));
//
//        savedOrderTable2.setEmpty(false);
//        given(orderTableDao.findAllByIdIn(List.of(savedOrderTable1.getId(), savedOrderTable2.getId())))
//                .willReturn(List.of(savedOrderTable1, savedOrderTable2));
//
//        //when, then
//        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 그룹을_지으려는_등록된_테이블_상태가_이미_다른_그룹에_지정된_경우_예외처리한다() {
//        //given
//        savedOrderTable1.setTableGroupId(2L);
//        savedOrderTable2.setTableGroupId(2L);
//
//        TableGroup tableGroupRequest = new TableGroup();
//        tableGroupRequest.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));
//
//        given(orderTableDao.findAllByIdIn(anyList())).willReturn(List.of(savedOrderTable1, savedOrderTable2));
//
//        //when, then
//        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 등록되어_있는_테이블_그룹_정보를_삭제할_수_있다() {
//        //given
//        Long requestedTableGroupId = 1L;
//
//        savedOrderTable1.setTableGroupId(requestedTableGroupId);
//        savedOrderTable2.setTableGroupId(requestedTableGroupId);
//
//        Order savedOrder1 = new Order();
//        savedOrder1.setId(1L);
//        savedOrder1.setOrderTableId(savedOrderTable1.getId());
//        savedOrder1.setOrderStatus(OrderStatus.COMPLETION.name());
//
//        Order savedOrder2 = new Order();
//        savedOrder2.setId(2L);
//        savedOrder2.setOrderTableId(savedOrderTable2.getId());
//        savedOrder2.setOrderStatus(OrderStatus.COMPLETION.name());
//
//        given(orderTableDao.findAllByTableGroupId(requestedTableGroupId))
//                .willReturn(List.of(savedOrderTable1, savedOrderTable2));
//
//        //when
//        tableGroupService.ungroup(requestedTableGroupId);
//
//        //then
//        assertThat(savedOrderTable1.getTableGroupId()).isNull();
//        assertThat(savedOrderTable1.isEmpty()).isFalse();
//        assertThat(savedOrderTable2.getTableGroupId()).isNull();
//        assertThat(savedOrderTable2.isEmpty()).isFalse();
//    }
//
//    @Test
//    void 삭제하려는_테이블_그룹에_그룹핑되어있던_주문_테이블중에_주문_상태가_완료되지_않은_테이블들이_있다면_예외처리한다() {
//        //given
//        Long requestedTableGroupId = 1L;
//
//        savedOrderTable1.setTableGroupId(requestedTableGroupId);
//        savedOrderTable2.setTableGroupId(requestedTableGroupId);
//
//        Order savedOrder1 = new Order();
//        savedOrder1.setId(1L);
//        savedOrder1.setOrderTableId(savedOrderTable1.getId());
//        savedOrder1.setOrderStatus(OrderStatus.COOKING.name());
//
//        Order savedOrder2 = new Order();
//        savedOrder2.setId(2L);
//        savedOrder2.setOrderTableId(savedOrderTable2.getId());
//        savedOrder2.setOrderStatus(OrderStatus.COOKING.name());
//
//        given(orderTableDao.findAllByTableGroupId(requestedTableGroupId))
//                .willReturn(List.of(savedOrderTable1, savedOrderTable2));
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
//                List.of(savedOrderTable1.getId(), savedOrderTable2.getId()),
//                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
//                )).willReturn(true);
//
//        //when, then
//        assertThatThrownBy(() -> tableGroupService.ungroup(requestedTableGroupId))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//}
