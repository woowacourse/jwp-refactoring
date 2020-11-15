package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static kitchenpos.fixture.TableGroupFixture.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ServiceIntegrationTest
@DisplayName("단체 지정 서비스")
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateTableGroup {
        private TableGroupCreateRequest request;

        private TableGroup subject() {
            return tableGroupService.create(request);
        }

        @Nested
        @DisplayName("주문 테이블의 id 리스트가 주어지면")
        class WithOrderTableIds {
            @BeforeEach
            void setUp() {
                List<OrderTable> orderTables = new ArrayList<>();
                orderTables.add(orderTableDao.save(createOrderTable(null, true, null, 1)));
                orderTables.add(orderTableDao.save(createOrderTable(null, true, null, 3)));

                List<Long> orderTableIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
                request = createTableGroupRequest(orderTableIds);
            }

            @Test
            @DisplayName("단체 지정을 생성하고, 해당하는 주문 테이블들의 단체 id와 빈 테이블 여부를 업데이트한다")
            void createTableGroups() {
                TableGroup result = subject();

                assertAll(
                        () -> assertThat(result.getId()).isNotNull(),
                        () -> assertThat(result.getCreatedDate()).isNotNull(),
                        () -> assertThat(result.getOrderTables()).extracting(OrderTable::getTableGroupId).containsOnly(result.getId()),
                        () -> assertThat(result.getOrderTables()).extracting(OrderTable::isEmpty).containsOnly(false)
                );
            }
        }

        @Nested
        @DisplayName("주문 테이블의 id에 해당하는 주문 테이블이 하나라도 없는 경우")
        class WhenOrderTableNotExist {
            @BeforeEach
            void setUp() {
                OrderTable orderTable = orderTableDao.save(createOrderTable(null, true, null, 1));

                request = createTableGroupRequest(Arrays.asList(orderTable.getId(), 0L));
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateTableGroup.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문 테이블 중 하나라도 비어있지 않은 경우")
        class WhenNotAllOrderTableEmpty {
            @BeforeEach
            void setUp() {
                OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, true, null, 1));
                OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, false, null, 1));

                request = createTableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateTableGroup.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문 테이블의 단체 그룹 id가 하나라도 이미 존재하는 경우")
        class WhenTableGroupIdAlreadyExist {
            @BeforeEach
            void setUp() {
                TableGroup tablegroup = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList()));
                OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, true, null, 1));
                OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, true, tablegroup.getId(), 1));

                request = createTableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(CreateTableGroup.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("Ungroup 메서드는")
    class UngroupTableGroup {
        private Long tableGroupId;
        private List<Long> orderTableIds;

        private void subject() {
            tableGroupService.ungroup(tableGroupId);
        }

        @Nested
        @DisplayName("Ungroup할 단체의 id가 주어지고, 해당하는 주문 테이블들의 상태가 모두 '완료'일 경우")
        class WithGroupId {
            @BeforeEach
            void setUp() {
                TableGroup tablegroup = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList()));
                tableGroupId = tablegroup.getId();
                OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, true, tableGroupId, 1));
                OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, true, tableGroupId, 3));

                orderDao.save(createOrder(null, OrderStatus.COMPLETION, orderTable1.getId(), LocalDateTime.now()));
                orderDao.save(createOrder(null, OrderStatus.COMPLETION, orderTable2.getId(), LocalDateTime.now()));

                orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
            }

            @Test
            @DisplayName("해당하는 주문 테이블들에서 단체 id를 제거하고 비어있는 상태로 만든다")
            void ungroupTableGroup() {
                subject();

                List<OrderTable> expected = orderTableDao.findAllByIdIn(orderTableIds);
                assertAll(
                        () -> assertThat(expected).extracting(OrderTable::getTableGroupId).containsOnlyNulls(),
                        () -> assertThat(expected).extracting(OrderTable::isEmpty).containsOnly(false)
                );
            }
        }

        @Nested
        @DisplayName("해당하는 주문 테이블들의 상태가 하나라도 '조리' 또는 '식사 중'일 경우")
        class WhenOrderTableInCookingOrMeal {
            @BeforeEach
            void setUp() {
                TableGroup tablegroup = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList()));
                tableGroupId = tablegroup.getId();
                OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, true, tableGroupId, 1));
                OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, true, tableGroupId, 3));

                orderDao.save(createOrder(null, OrderStatus.COOKING, orderTable1.getId(), LocalDateTime.now()));
                orderDao.save(createOrder(null, OrderStatus.COMPLETION, orderTable2.getId(), LocalDateTime.now()));
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwException() {
                assertThatThrownBy(UngroupTableGroup.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}