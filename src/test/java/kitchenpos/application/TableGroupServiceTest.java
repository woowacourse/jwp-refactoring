package kitchenpos.application;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

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
//    @DisplayName("단체 지정을 생성은")
//    @Nested
//    class Create {
//
//        private TableGroup tableGroup;
//        private OrderTable orderTable1;
//        private OrderTable orderTable2;
//
//        @BeforeEach
//        void setUp() {
//            tableGroup = createTableGroup();
//            orderTable1 = createOrderTable();
//            orderTable1.setEmpty(true);
//            orderTable2 = createOrderTable();
//            orderTable2.setEmpty(true);
//        }
//
//        private TableGroup subject() {
//            return tableGroupService.create(tableGroup);
//        }
//
//        @DisplayName("주문 테이블의 개수가 2 미만일 경우 생성할 수 없다.")
//        @Test
//        void createIfLessThanTwo() {
//            tableGroup.setOrderTables(Collections.singletonList(createOrderTable()));
//
//            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("존재하지 않는 주문 테이블이 포함되어 있는 경우 생성할 수 없다.")
//        @Test
//        void createIfHasNotExistOrderTable() {
//            tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
//            when(orderTableDao.findAllByIdIn(any())).thenReturn(Collections.singletonList(orderTable1));
//
//            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("주문 테이블들이 빈 테이블이 아닌 경우 생성할 수 없다.")
//        @Test
//        void createIfNotEmptyOrderTable() {
//            orderTable1.setEmpty(false);
//            List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
//            tableGroup.setOrderTables(orderTables);
//            when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);
//
//            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("단체 지정에 이미 속해있는 주문 테이블을 포함할 경우 생성할 수 없다.")
//        @Test
//        void createIfAlreadyInTableGroup() {
//            orderTable1.setTableGroupId(1L);
//            List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
//            tableGroup.setOrderTables(orderTables);
//            when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);
//
//            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("조건을 만족하는 경우 생성할 수 있다.")
//        @Test
//        void create() {
//            List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
//            tableGroup.setOrderTables(orderTables);
//            when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);
//            when(tableGroupDao.save(any())).thenReturn(tableGroup);
//            when(orderTableDao.save(any())).thenReturn(createOrderTable());
//
//            assertDoesNotThrow(this::subject);
//        }
//    }
//
//    @DisplayName("단체 지정 해제는")
//    @Nested
//    class Ungroup {
//
//        private final List<Long> orderTableIds = Arrays.asList(1L, 2L);
//        private final Long tableGroupId = 1L;
//
//        @BeforeEach
//        void setUp() {
//            OrderTable orderTable1 = createOrderTable(1L);
//            OrderTable orderTable2 = createOrderTable(2L);
//            when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
//        }
//
//        @DisplayName("조리, 식사 상태의 주문 테이블을 포함한 경우 해제할 수 없다.")
//        @Test
//        void ungroupException() {
//            when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
//                    orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
//
//            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("조건을 만족하는 경우 해제할 수 있다.")
//        @Test
//        void ungroup() {
//            when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
//                    orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
//
//            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
//        }
//    }
}
