package kitchenpos.application;

import kitchenpos.application.dto.TableChangeEmptyStatusRequest;
import kitchenpos.application.dto.TableChangeNumberOfGuestRequest;
import kitchenpos.application.dto.TableCreateRequest;
import kitchenpos.application.dto.TableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.ORDER_FOR;
import static kitchenpos.fixture.OrderTableFixtures.EMPTY_TABLE;
import static kitchenpos.fixture.OrderTableFixtures.NOT_EMPTY_TABLE;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private OrderTable emptyTable;

    private OrderTable notEmptyTable;

    @BeforeEach
    void setup() {
        notEmptyTable = orderTableDao.save(NOT_EMPTY_TABLE());
        emptyTable = orderTableDao.save(EMPTY_TABLE());
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void createOrderTable() {
        // given
        TableCreateRequest request = new TableCreateRequest(0, true);

        // when
        final TableResponse response = tableService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    @DisplayName("생성된 테이블 정보들을 가져온다.")
    void getTableList() {
        // given

        // when
        final List<TableResponse> response = tableService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotEmpty();
            final TableResponse lastResponse = response.get(response.size() - 1);
            softly.assertThat(lastResponse.getId()).isEqualTo(emptyTable.getId());
        });
    }

    @Nested
    @DisplayName("테이블 상태 변경 테스트")
    class ChangeEmptyStatusTest {

        @Test
        @DisplayName("상태변경에 성공한다. - empty true -> false")
        void successFromTrueToFalse() {
            // given
            final TableChangeEmptyStatusRequest request = new TableChangeEmptyStatusRequest(false);

            // when
            final TableResponse response = tableService.changeEmpty(emptyTable.getId(), request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(emptyTable.getId());
                softly.assertThat(response.isEmpty()).isFalse();
            });
        }

        @Test
        @DisplayName("상태변경에 성공한다. - empty true -> false")
        void successFromFalseToTrue() {
            // given
            final TableChangeEmptyStatusRequest request = new TableChangeEmptyStatusRequest(true);

            // when
            final TableResponse response = tableService.changeEmpty(notEmptyTable.getId(), request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(notEmptyTable.getId());
                softly.assertThat(response.isEmpty()).isTrue();
            });
        }

        @Test
        @DisplayName("테이블 그룹에 속해있으면 상태를 변경할 수 없다.")
        void throwExceptionWithGroupedTable() {
            // given
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setCreatedDate(LocalDateTime.now());
            final TableGroup createdTableGroup = tableGroupDao.save(tableGroup);

            notEmptyTable.setId(null);
            notEmptyTable.setTableGroupId(createdTableGroup.getId());
            notEmptyTable = orderTableDao.save(notEmptyTable);

            final TableChangeEmptyStatusRequest request = new TableChangeEmptyStatusRequest(true);

            // when
            // then
            final Long tableId = notEmptyTable.getId();
            Assertions.assertThatThrownBy(() -> tableService.changeEmpty(tableId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "테이블의 주문 상테 : {0}")
        @ValueSource(strings = {"MEAL", "COOKING"})
        @DisplayName("완료되지 않은 주문이 있으면 empty상태로 변경할 수 없다.")
        void throwExceptionWithUnCompletedOrder(final String status) {
            // given
            final Order order = ORDER_FOR(notEmptyTable.getId(), status);
            orderDao.save(order);

            final TableChangeEmptyStatusRequest request = new TableChangeEmptyStatusRequest(true);

            // when
            // then
            final Long tableId = notEmptyTable.getId();
            Assertions.assertThatThrownBy(() -> tableService.changeEmpty(tableId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("테이블의 손님의 수를 바꿀 수 있다.")
    class ChangeGuestNumberTest {

        @Test
        @DisplayName("손님의 수를 변경하는데 성공한다.")
        void success() {
            // given
            final TableChangeNumberOfGuestRequest request = new TableChangeNumberOfGuestRequest(1);

            // when
            final TableResponse response = tableService.changeNumberOfGuests(notEmptyTable.getId(), request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isEqualTo(notEmptyTable.getId());
                softly.assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
            });
        }

        @Test
        @DisplayName("변경하려는 손님의 수가 0보다 작은경우 예외가 발생한다.")
        void throwExceptionWithNegativeGuestNumber() {
            // given
            final TableChangeNumberOfGuestRequest request = new TableChangeNumberOfGuestRequest(-1);

            // when
            // then
            final Long tableId = notEmptyTable.getId();
            Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("empty상태의 테이블에서는 손님의 수를 변경할 경우 예외가 발생한다.")
        void throwExceptionWithEmptyTable() {
            // given
            final TableChangeNumberOfGuestRequest request = new TableChangeNumberOfGuestRequest(1);

            // when
            // then
            final Long tableId = emptyTable.getId();
            Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
