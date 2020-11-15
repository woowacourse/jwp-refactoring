package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {
    private OrderTable orderTable;

    @Nested
    @DisplayName("empty 여부 변경 메서드는")
    class DESCRIBE_ChangeOrderStatus {
        private OrderTable subject() {
            return orderTable.changeOrderStatus(empty, publisher);
        }

        private boolean empty;
        private ApplicationEventPublisher publisher;

        @Nested
        @DisplayName("그룹이 없는 테이블과 변경할 empty 여부, 퍼블리셔가 주어지면")
        class CONTEXT_Given {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, true, null, 4);

                empty = !orderTable.isEmpty();
                publisher = new CustomEventPublisher.AlwaysPass();
            }

            @Test
            @DisplayName("empty 여부가 변경된다")
            void IT_changeEmpty() {
                OrderTable updated = subject();

                assertThat(updated).isEqualToIgnoringGivenFields(orderTable, "empty");
                assertThat(updated.isEmpty()).isEqualTo(empty);
            }
        }

        @Nested
        @DisplayName("테이블 그룹 id가 이미 존재한다면")
        class CONTEXT_With_TableGroupId {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, true, 1L, 4);

                empty = !orderTable.isEmpty();
                publisher = new CustomEventPublisher.AlwaysPass();
            }

            @Test
            @DisplayName("예외가 발생한다")
            void IT_changeEmpty() {
                assertThatThrownBy(DESCRIBE_ChangeOrderStatus.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("검증 이벤트가 실패한다면")
        class CONTEXT_WhenValidateEventFail {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, true, null, 4);

                empty = !orderTable.isEmpty();
                publisher = new CustomEventPublisher.AlwaysFail();
            }

            @Test
            @DisplayName("예외가 발생한다")
            void IT_changeEmpty() {
                assertThatThrownBy(DESCRIBE_ChangeOrderStatus.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }


    @Nested
    @DisplayName("손님수 변경 메서드는")
    class DESCRIBE_ChangeNumOfGuest {
        private OrderTable subject() {
            return orderTable.changeNumOfGuests(numberOfGuests);
        }

        private int numberOfGuests;

        @Nested
        @DisplayName("비어있지 않은 테이블과 변경할 손님 수가 주어지면")
        class CONTEXT_Given {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, false, null, 4);
                numberOfGuests = 3;
            }

            @Test
            @DisplayName("손님 수를 변경한다")
            void IT_changeNumOfGuests() {
                OrderTable updated = subject();

                assertThat(updated).isEqualToIgnoringGivenFields(orderTable, "numberOfGuests");
                assertThat(updated.getNumberOfGuests()).isEqualTo(numberOfGuests);
            }
        }

        @Nested
        @DisplayName("비어있는 테이블이 주어지면")
        class CONTEXT_WithEmptyTable {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, true, null, 4);
                numberOfGuests = 3;
            }

            @Test
            @DisplayName("예외가 발생한다")
            void IT_changeNumOfGuests() {
                assertThatThrownBy(DESCRIBE_ChangeNumOfGuest.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("손님수가 음수라면")
        class CONTEXT_WithNegativeGuests {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, true, null, 4);
                numberOfGuests = -4;
            }

            @Test
            @DisplayName("예외가 발생한다")
            void IT_changeNumOfGuests() {
                assertThatThrownBy(DESCRIBE_ChangeNumOfGuest.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }


    @Nested
    @DisplayName("groupBy 메서드는")
    class DESCRIBE_GroupBy {
        private OrderTable subject() {
            return orderTable.groupBy(tableGroupId);
        }

        private Long tableGroupId;

        @Nested
        @DisplayName("테이블이 비어있고, 테이블 그룹 id가 없고, 테이블 그룹 id가 주어지면")
        class CONTEXT_WhenTableEmptyAndNoTableGroupWithTableGroupId {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, true, null, 4);
                tableGroupId = 1L;
            }

            @Test
            @DisplayName("테이블에 그룹 id를 할당하고 비어있지 않다고 표시한다")
            void IT_changeTableGroupId() {
                OrderTable updated = subject();

                assertThat(updated).isEqualToIgnoringGivenFields(orderTable, "empty", "tableGroupId");
                assertThat(updated.getTableGroupId()).isEqualTo(tableGroupId);
                assertThat(updated.isEmpty()).isFalse();
            }
        }

        @Nested
        @DisplayName("테이블이 비어있지 않다면")
        class CONTEXT_WhenTableNotEmpty {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, false, null, 4);
                tableGroupId = 1L;
            }

            @Test
            @DisplayName("예외가 발생한다")
            void IT_changeTableGroupId() {
                assertThatThrownBy(DESCRIBE_GroupBy.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("테이블 그룹 id가 이미 존재한다면")
        class CONTEXT_WhenTableGroupIdExist {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, true, 2L, 4);
                tableGroupId = 1L;
            }

            @Test
            @DisplayName("예외가 발생한다")
            void IT_changeTableGroupId() {
                assertThatThrownBy(DESCRIBE_GroupBy.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("ungroup 메서드는")
    class DESCRIBE_Ungroup {
        private OrderTable subject() {
            return orderTable.ungroup(publisher);
        }

        private ApplicationEventPublisher publisher;

        @Nested
        @DisplayName("퍼블리셔가 주어지면")
        class CONTEXT_Given {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, false, 2L, 4);

                publisher = new CustomEventPublisher.AlwaysPass();
            }

            @Test
            @DisplayName("테이블 그룹 할당이 해제되고, 비어있지 않다고 표시된다")
            void IT_changeEmpty() {
                OrderTable updated = subject();

                assertThat(updated).isEqualToIgnoringGivenFields(orderTable, "empty", "tableGroupId");
                assertThat(updated.isEmpty()).isFalse();
                assertThat(updated.getTableGroupId()).isNull();
            }
        }

        @Nested
        @DisplayName("검증 이벤트가 실패한다면")
        class CONTEXT_WhenValidateEventFail {
            @BeforeEach
            void setUp() {
                orderTable = createOrderTable(1L, true, null, 4);

                publisher = new CustomEventPublisher.AlwaysFail();
            }

            @Test
            @DisplayName("예외가 발생한다")
            void IT_changeEmpty() {
                assertThatThrownBy(DESCRIBE_Ungroup.this::subject).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}