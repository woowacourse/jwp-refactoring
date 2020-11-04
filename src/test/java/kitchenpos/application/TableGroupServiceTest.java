package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.application.fixture.TableGroupFixture.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 지정 서비스")
class TableGroupServiceTest {
    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateTableGroup {
        private TableGroup request;

        private TableGroup subject() {
            return tableGroupService.create(request);
        }

        @Nested
        @DisplayName("주문 테이블의 id 리스트가 주어지면")
        class WithOrderTableIds {
            private List<OrderTable> orderTables;

            @BeforeEach
            void setUp() {
                request = createTableGroupRequest(Arrays.asList(1L, 2L));
                orderTables = Arrays.asList(
                        createOrderTable(1L, true, null, 1),
                        createOrderTable(2L, true, null, 3)
                );
            }

            @Test
            @DisplayName("단체 주문을 생성하고, 해당하는 주문 테이블들의 단체 id와 빈 테이블 여부를 업데이트한다")
            void createTableGroups() {
                given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
                given(tableGroupDao.save(any(TableGroup.class))).willAnswer(i -> {
                    TableGroup tableGroup = i.getArgument(0, TableGroup.class);
                    tableGroup.setId(5L);
                    return tableGroup;
                });

                TableGroup result = subject();

                verify(orderTableDao, times(2)).save(any(OrderTable.class));
                assertAll(
                        () -> assertThat(result.getId()).isNotNull(),
                        () -> assertThat(result.getCreatedDate()).isNotNull(),
                        () -> assertThat(result.getOrderTables()).extracting(OrderTable::getTableGroupId).containsOnly(5L),
                        () -> assertThat(result.getOrderTables()).extracting(OrderTable::isEmpty).containsOnly(false)
                );
            }
        }

        @Nested
        @DisplayName("주문 테이블의 id에 해당하는 주문 테이블이 하나라도 없는 경우")
        class WhenOrderTableNotExist {
            private List<OrderTable> orderTables;

            @BeforeEach
            void setUp() {
                request = createTableGroupRequest(Arrays.asList(1L, 2L));
                orderTables = Arrays.asList(createOrderTable(1L, true, null, 1));
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

                assertThatThrownBy(CreateTableGroup.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문 테이블 중 하나라도 비어있지 않은 경우")
        class WhenNotAllOrderTableEmpty {
            private List<OrderTable> orderTables;

            @BeforeEach
            void setUp() {
                request = createTableGroupRequest(Arrays.asList(1L, 2L));
                orderTables = Arrays.asList(
                        createOrderTable(1L, true, null, 1),
                        createOrderTable(2L, false, null, 3)
                );
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

                assertThatThrownBy(CreateTableGroup.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문 테이블의 단체 그룹 id가 하나라도 이미 존재하는 경우")
        class WhenTableGroupIdAlreadyExist {
            private List<OrderTable> orderTables;

            @BeforeEach
            void setUp() {
                request = createTableGroupRequest(Arrays.asList(1L, 2L));
                orderTables = Arrays.asList(
                        createOrderTable(1L, true, null, 1),
                        createOrderTable(2L, true, 4L, 3)
                );
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

                assertThatThrownBy(CreateTableGroup.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("Ungroup 메서드는")
    class UngroupTableGroup {
        private Long tableGroupId;

        private void subject() {
            tableGroupService.ungroup(tableGroupId);
        }

        @Nested
        @DisplayName("Ungroup할 단체의 id가 주어지면")
        class WhenGroupIdGiven {
            private List<OrderTable> orderTables;

            @BeforeEach
            void setUp() {
                tableGroupId = 5L;
                orderTables = Arrays.asList(
                        createOrderTable(1L, true, null, 1),
                        createOrderTable(2L, true, null, 3)
                );
            }

            @Test
            @DisplayName("해당하는 주문 테이블들에서 단체 id를 제거하고 비어있는 상태로 만든다")
            void ungroupTableGroup() {
                given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);
                given(orderDao.existsByOrderTableIdInAndOrderStatusIn(eq(Arrays.asList(1L, 2L)), anyList()))
                        .willReturn(false);

                subject();

                ArgumentCaptor<OrderTable> orderTable = ArgumentCaptor.forClass(OrderTable.class);
                verify(orderTableDao, times(2)).save(orderTable.capture());
                assertThat(orderTable.getAllValues()).extracting(OrderTable::getTableGroupId).containsOnlyNulls();
                assertThat(orderTable.getAllValues()).extracting(OrderTable::isEmpty).containsOnly(false);
            }
        }

        @Nested
        @DisplayName("해당하는 주문 테이블들의 상태가 하나라도 '조리' 또는 '식사 중'일 경우")
        class WhenOrderTableInCookingOrMeal {
            private List<OrderTable> orderTables;

            @BeforeEach
            void setUp() {
                tableGroupId = 5L;
                orderTables = Arrays.asList(
                        createOrderTable(1L, true, null, 1),
                        createOrderTable(2L, true, null, 3)
                );
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);
                given(orderDao.existsByOrderTableIdInAndOrderStatusIn(eq(Arrays.asList(1L, 2L)), anyList()))
                        .willReturn(true);

                assertThatThrownBy(UngroupTableGroup.this::subject).isInstanceOf(IllegalArgumentException.class);

            }
        }
    }
}