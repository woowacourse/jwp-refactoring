package kitchenpos.table.application;

import kitchenpos.table.application.dto.request.CreateTableGroupRequest;
import kitchenpos.table.application.dto.request.CreateTableGroupRequest.TableInfo;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TablesValidatorTest {

    private TablesValidator tablesValidator = new TablesValidator();

    @Test
    void 그룹화_하려는_테이블이_빈_리스트면_예외발생() {
        assertThatThrownBy(() -> tablesValidator.validate(Collections.emptyList(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹화_하려는_테이블이_하나면_예외발생() {
        OrderTable orderTable = new OrderTable(3, false);
        assertThatThrownBy(() -> tablesValidator.validate(List.of(orderTable), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹화_하려는_테이블_중_실제_존재하는_테이블_개수와_요청_개수가_다르면_예외발생() {
        CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(List.of(new TableInfo(1L), new TableInfo(2L), new TableInfo(3L)));
        OrderTable notEmptyOrderTable1 = new OrderTable(3, false);
        OrderTable notEmptyOrderTable2 = new OrderTable(3, false);

        assertThatThrownBy(() -> tablesValidator.validate(List.of(notEmptyOrderTable1, notEmptyOrderTable2), createTableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹화_하려는_테이블_중_빈_테이블이_있으면_예외발생() {
        CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(List.of(new TableInfo(1L), new TableInfo(2L)));

        OrderTable notEmptyOrderTable = new OrderTable(3, false);
        OrderTable emptyOrderTable = new OrderTable(3, true);
        assertThatThrownBy(() -> tablesValidator.validate(List.of(notEmptyOrderTable, emptyOrderTable), createTableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹화_하려는_테이블_중_이미_테이블_그룹이_있으면_예외발생() {
        CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(List.of(new TableInfo(1L), new TableInfo(2L)));

        OrderTable notEmptyOrderTable1 = new OrderTable(3, false);
        OrderTable notEmptyOrderTable2 = new OrderTable(3, false);

        notEmptyOrderTable1.changeTableGroup(new TableGroup());

        assertThatThrownBy(() -> tablesValidator.validate(List.of(notEmptyOrderTable1, notEmptyOrderTable2), createTableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
