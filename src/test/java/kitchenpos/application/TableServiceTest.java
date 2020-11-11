package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.application.fixture.OrderFixture.createOrder;
import static kitchenpos.application.fixture.OrderTableFixture.*;
import static kitchenpos.application.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ServiceIntegrationTest
@DisplayName("주문 테이블 서비스")
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateTable {
        private OrderTable request;

        private OrderTable subject() {
            return tableService.create(request);
        }

        @Nested
        @DisplayName("손님 수와 빈 테이블 여부가 주어지면")
        class WithNumOfGuestAndIsEmpty {
            @BeforeEach
            void setUp() {
                request = createOrderTableRequest(3, false);
            }

            @Test
            @DisplayName("주문 테이블이 생성된다")
            void createOrderTable() {
                OrderTable result = subject();

                assertAll(
                        () -> assertThat(result.getId()).isNotNull(),
                        () -> assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                        () -> assertThat(result.isEmpty()).isEqualTo(request.isEmpty())
                );
            }
        }
    }

    @Nested
    @DisplayName("조회 메서드는")
    class FindTable {
        private List<OrderTable> subject() {
            return tableService.list();
        }

        @Nested
        @DisplayName("주문 테이블이 저장되어 있으면")
        class WhenOrderTableSaved {
            private List<OrderTable> orderTables;

            @BeforeEach
            void setUp() {
                TableGroup tablegroup = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList()));
                orderTables = Arrays.asList(
                        createOrderTable(null, false, null, 2),
                        createOrderTable(null, false, tablegroup.getId(), 2),
                        createOrderTable(null, true, tablegroup.getId(), 2),
                        createOrderTable(null, false, null, 2)
                );
                for (OrderTable orderTable : orderTables) {
                    OrderTable persisted = orderTableDao.save(orderTable);
                    orderTable.setId(persisted.getId());
                }
            }

            @Test
            @DisplayName("전체 주문 테이블을 조회한다")
            void createOrderTables() {
                List<OrderTable> result = subject();

                assertThat(result).usingFieldByFieldElementComparator().containsAll(orderTables);
            }
        }
    }

    @Nested
    @DisplayName("주문 테이블의 비어있음 여부 수정 메서드는")
    class ChangeEmptyOrderTable {
        private Long orderTableId;
        private OrderTable request;

        private OrderTable subject() {
            return tableService.changeEmpty(orderTableId, request);
        }

        @Nested
        @DisplayName("주문 테이블이 저장되어 있고, 테이블 id와 빈 테이블 여부가 주어지면")
        class WhenOrderTableSavedAndWithTableIdAndIsEmpty {
            private OrderTable orderTable;

            @BeforeEach
            void setUp() {
                orderTable = orderTableDao.save(createOrderTable(orderTableId, false, null, 4));
                orderDao.save(createOrder(null, OrderStatus.COMPLETION, orderTable.getId(), LocalDateTime.now()));

                orderTableId = orderTable.getId();
                request = modifyOrderTableEmptyRequest(true);
            }

            @Test
            @DisplayName("해당 주문 테이블의 빈 테이블 여부를 수정한다")
            void createOrderTables() {
                OrderTable result = subject();

                assertAll(
                        () -> assertThat(result).isEqualToIgnoringGivenFields(orderTable, "empty"),
                        () -> assertThat(result.isEmpty()).isEqualTo(request.isEmpty())
                );
            }
        }

        @Nested
        @DisplayName("주문 테이블 id에 해당하는 주문 테이블이 존재하지 않을 경우")
        class NotExistOrderTable {
            @BeforeEach
            void setUp() {
                orderTableId = 0L;
                request = modifyOrderTableEmptyRequest(true);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                assertThatThrownBy(ChangeEmptyOrderTable.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("조회한 주문 테이블의 단체 id가 이미 존재할 경우")
        class AlreadyExistTableGroupId {
            @BeforeEach
            void setUp() {
                TableGroup tablegroup = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList()));
                OrderTable orderTable = orderTableDao.save(createOrderTable(orderTableId, false, tablegroup.getId(), 4));

                orderTableId = orderTable.getId();
                request = modifyOrderTableEmptyRequest(true);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                assertThatThrownBy(ChangeEmptyOrderTable.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문 테이블의 주문 상태가 '조리' 또는 '식사 중'일 경우")
        class CookingOrMealStatus {
            @BeforeEach
            void setUp() {
                OrderTable orderTable = orderTableDao.save(createOrderTable(orderTableId, false, null, 4));
                orderDao.save(createOrder(null, OrderStatus.COOKING, orderTable.getId(), LocalDateTime.now()));

                orderTableId = orderTable.getId();
                request = modifyOrderTableEmptyRequest(true);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                assertThatThrownBy(ChangeEmptyOrderTable.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("주문 테이블 손님 수 수정 메서드는")
    class ChangeNumberOfGuestOrderTable {
        private Long orderTableId;
        private OrderTable request;

        private OrderTable subject() {
            return tableService.changeNumberOfGuests(orderTableId, request);
        }

        @Nested
        @DisplayName("주문 테이블이 저장되어 있고, 테이블 id와 손님 수가 주어지면")
        class WhenOrderTableSavedAndWithTableIdAndNumOfGuests {
            private OrderTable orderTable;

            @BeforeEach
            void setUp() {
                orderTable = orderTableDao.save(createOrderTable(orderTableId, false, null, 4));

                orderTableId = orderTable.getId();
                request = modifyOrderTableNumOfGuestRequest(2);
            }

            @Test
            @DisplayName("해당 주문 테이블의 손님 수를 수정한다")
            void createOrderTables() {
                OrderTable result = subject();

                assertAll(
                        () -> assertThat(result).isEqualToIgnoringGivenFields(orderTable, "numberOfGuests"),
                        () -> assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests())
                );
            }
        }

        @Nested
        @DisplayName("주문 테이블 id에 해당하는 주문 테이블이 존재하지 않을 경우")
        class NotExistOrderTable {
            @BeforeEach
            void setUp() {
                orderTableId = 0L;
                request = modifyOrderTableNumOfGuestRequest(4);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                assertThatThrownBy(ChangeNumberOfGuestOrderTable.this::subject)
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("요청의 손님 수가 음수인 경우")
        class NegativeNumberOfGuests {
            @BeforeEach
            void setUp() {
                OrderTable orderTable = orderTableDao.save(createOrderTable(orderTableId, false, null, 4));
                orderTableId = orderTable.getId();
                request = modifyOrderTableNumOfGuestRequest(-4);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                assertThatThrownBy(ChangeNumberOfGuestOrderTable.this::subject)
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문 테이블이 비어있을 경우")
        class EmptyTable {
            @BeforeEach
            void setUp() {
                OrderTable orderTable = orderTableDao.save(createOrderTable(orderTableId, true, null, 4));
                orderTableId = orderTable.getId();
                request = modifyOrderTableNumOfGuestRequest(2);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                assertThatThrownBy(ChangeNumberOfGuestOrderTable.this::subject)
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}