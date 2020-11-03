package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 테이블 서비스")
class TableServiceTest {
    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateTable {
        private OrderTable request;

        private OrderTable subject() {
            return tableService.create(request);
        }

        @Nested
        @DisplayName("손님 수와 빈 테이블 여부가 주어지면")
        class IfGiven {
            @BeforeEach
            void setUp() {
                request = createOrderTableRequest(3, false);
            }

            @Test
            @DisplayName("주문 테이블이 생성된다")
            void createOrderTable() {
                given(orderTableDao.save(any(OrderTable.class))).willAnswer(i -> {
                    OrderTable saved = i.getArgument(0, OrderTable.class);
                    saved.setId(1L);
                    return saved;
                });

                OrderTable result = subject();

                assertAll(
                        () -> assertThat(result.getId()).isNotNull(),
                        () -> assertThat(result.getNumberOfGuests()).isEqualTo(3),
                        () -> assertThat(result.isEmpty()).isFalse()
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
        class IfGiven {
            private List<OrderTable> orderTables;

            @BeforeEach
            void setUp() {
                orderTables = Arrays.asList(
                        createOrderTable(1L, false, null, 2),
                        createOrderTable(2L, false, 2L, 2),
                        createOrderTable(3L, true, 2L, 2),
                        createOrderTable(4L, false, null, 2)
                );
            }

            @Test
            @DisplayName("전체 주문 테이블을 조회한다")
            void createOrderTables() {
                given(orderTableDao.findAll()).willReturn(orderTables);

                List<OrderTable> result = subject();

                assertThat(result).usingRecursiveComparison().isEqualTo(orderTables);
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
        class IfGiven {
            private OrderTable orderTable;

            @BeforeEach
            void setUp() {
                orderTableId = 1L;
                orderTable = createOrderTable(orderTableId, false, null, 4);
                request = modifyOrderTableStatusRequest(true);
            }

            @Test
            @DisplayName("해당 주문 테이블의 빈 테이블 여부를 수정한다")
            void createOrderTables() {
                given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));
                given(orderDao.existsByOrderTableIdAndOrderStatusIn(eq(orderTableId), anyList())).willReturn(false);
                given(orderTableDao.save(any(OrderTable.class)))
                        .willAnswer(i -> i.getArgument(0, OrderTable.class));

                OrderTable result = subject();

                assertAll(
                        () -> assertThat(result).isEqualToIgnoringGivenFields(orderTable, "empty"),
                        () -> assertThat(result.isEmpty()).isTrue()
                );
            }
        }

        @Nested
        @DisplayName("주문 테이블 id에 해당하는 주문 테이블이 존재하지 않을 경우")
        class NotExistOrderTable {
            @BeforeEach
            void setUp() {
                orderTableId = 1L;
                request = modifyOrderTableStatusRequest(true);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

                assertThatThrownBy(ChangeEmptyOrderTable.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("조회한 주문 테이블의 단체 id가 이미 존재할 경우")
        class AlreadyExistTableGroupId {
            @BeforeEach
            void setUp() {
                orderTableId = 1L;
                request = modifyOrderTableStatusRequest(true);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                given(orderTableDao.findById(orderTableId))
                        .willReturn(Optional.of(createOrderTable(orderTableId, false, 2L, 4)));

                assertThatThrownBy(ChangeEmptyOrderTable.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문 테이블의 주문 상태가 '조리' 또는 '식사 중'일 경우")
        class CookingOrMealStatus {
            @BeforeEach
            void setUp() {
                orderTableId = 1L;
                request = modifyOrderTableStatusRequest(true);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                given(orderTableDao.findById(orderTableId))
                        .willReturn(Optional.of(createOrderTable(orderTableId, false, null, 4)));
                given(orderDao.existsByOrderTableIdAndOrderStatusIn(eq(orderTableId), anyList())).willReturn(true);

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
        class IfGiven {
            private OrderTable orderTable;

            @BeforeEach
            void setUp() {
                orderTableId = 1L;
                orderTable = createOrderTable(orderTableId, false, null, 4);
                request = modifyOrderTableNumOfGuestRequest(2);
            }

            @Test
            @DisplayName("해당 주문 테이블의 손님 수를 수정한다")
            void createOrderTables() {
                given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));
                given(orderTableDao.save(any(OrderTable.class)))
                        .willAnswer(i -> i.getArgument(0, OrderTable.class));

                OrderTable result = subject();

                assertAll(
                        () -> assertThat(result).isEqualToIgnoringGivenFields(orderTable, "numberOfGuests"),
                        () -> assertThat(result.getNumberOfGuests()).isEqualTo(2)
                );
            }
        }

        @Nested
        @DisplayName("주문 테이블 id에 해당하는 주문 테이블이 존재하지 않을 경우")
        class NotExistOrderTable {
            @BeforeEach
            void setUp() {
                orderTableId = 1L;
                request = modifyOrderTableNumOfGuestRequest(4);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

                assertThatThrownBy(ChangeNumberOfGuestOrderTable.this::subject)
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("요청의 손님 수가 음수인 경우")
        class NegativeNumberOfGuests {
            @BeforeEach
            void setUp() {
                orderTableId = 1L;
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
                orderTableId = 1L;
                request = modifyOrderTableNumOfGuestRequest(2);
            }

            @Test
            @DisplayName("예외가 발생한다")
            void throwsException() {
                given(orderTableDao.findById(orderTableId))
                        .willReturn(Optional.of(createOrderTable(orderTableId, true, null, 4)));

                assertThatThrownBy(ChangeNumberOfGuestOrderTable.this::subject)
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}