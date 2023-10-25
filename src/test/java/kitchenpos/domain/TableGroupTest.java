package kitchenpos.domain;

import static kitchenpos.exception.ExceptionType.INVALID_TABLES_COUNT_OF_TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.TableGroup.Builder;
import kitchenpos.exception.ExceptionType;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class TableGroupTest {

    @Test
    @DisplayName("주문 테이블 그룹을 생성할 수 있다.")
    void create() {
        // given & when
        TableGroup tableGroup = TableGroupFixture.TABLE_GROUP_AVAILABLE.toEntity();

        // then
        assertAll(
            () -> assertThat(tableGroup.getOrderTables()).hasSize(2),
            () -> assertThat(tableGroup.getOrderTables().get(0).getTableGroup()).isEqualTo(
                tableGroup),
            () -> assertThat(tableGroup.getOrderTables().get(1).getTableGroup()).isEqualTo(
                tableGroup)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("주문 테이블이 없으면 그룹을 생성할 수 없다.")
    void create_fail1(List<OrderTable> orderTables) {
        // given & when
        Builder builder = new Builder()
            .orderTables(orderTables);

        // then
        assertThatThrownBy(() -> builder.build())
            .hasMessageContaining(INVALID_TABLES_COUNT_OF_TABLE_GROUP.getMessage());
    }

    @Test
    @DisplayName("주문 테이블이 2개 이하이면 그룹을 생성할 수 없다.")
    void create_fail2() {
        // given & when
        Builder builder = new Builder()
            .orderTables(List.of(OrderTableFixture.EMPTY_TABLE1.toEntity()));

        // then
        assertThatThrownBy(() -> builder.build())
            .hasMessageContaining(INVALID_TABLES_COUNT_OF_TABLE_GROUP.getMessage());
    }

    @Test
    @DisplayName("주문 테이블이 중복되면 그룹을 생성할 수 없다.")
    void create_fail3() {
        // given & when
        Builder builder = new Builder()
            .orderTables(List.of(
                OrderTableFixture.EMPTY_TABLE1.toEntity(),
                OrderTableFixture.EMPTY_TABLE1.toEntity()
            ));

        // then
        assertThatThrownBy(() -> builder.build())
            .hasMessageContaining(ExceptionType.DUPLICATED_TABLES_OF_TABLE_GROUP.getMessage());
    }

    @Test
    @DisplayName("주문 테이블이 이미 그룹에 속해있으면 그룹을 생성할 수 없다.")
    void create_fail4() {
        // given
        TableGroup existingTableGroup = TableGroupFixture.TABLE_GROUP_AVAILABLE.toEntity();

        // when
        Builder builder = new Builder()
            .orderTables(List.of(
                existingTableGroup.getOrderTables().get(0),
                OrderTableFixture.EMPTY_TABLE2.toEntity()
            ));

        // then
        assertThatThrownBy(() -> builder.build())
            .hasMessageContaining(ExceptionType.ALREADY_ASSIGNED_TABLE_GROUP.getMessage());
    }

    @Test
    @DisplayName("주문 테이블이 비어있지 않으면 그룹을 생성할 수 없다.")
    void create_fail5() {
        // given & when
        Builder builder = new Builder()
            .orderTables(List.of(
                OrderTableFixture.EMPTY_TABLE1.toEntity(),
                OrderTableFixture.OCCUPIED_TABLE.toEntity()
            ));

        // then
        assertThatThrownBy(() -> builder.build())
            .hasMessageContaining(ExceptionType.NOT_EMPTY_ORDER_TABLE_IN_TABLE_GROUP.getMessage());
    }
}
