package kitchenpos.ui;

import static kitchenpos.fixture.TableGroupFactory.tableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.table.OrderTableDao;
import kitchenpos.domain.table.TableGroupDao;
import kitchenpos.fixture.OrderTableFactory;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupRequest.TableGroupInnerOrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class TableGroupRestControllerTest {

    @Autowired
    private TableGroupRestController tableGroupRestController;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("테이블 그룹 등록")
    @Test
    void create() {
        final var singleTable = orderTableDao.save(OrderTableFactory.emptyTable(1));
        final var coupleTable = orderTableDao.save(OrderTableFactory.emptyTable(2));

        final var singTableRequest = new TableGroupInnerOrderTable(singleTable.getId());
        final var coupleTableRequest = new TableGroupInnerOrderTable(coupleTable.getId());
        final var request = new TableGroupRequest(List.of(singTableRequest, coupleTableRequest));

        final var response = tableGroupRestController.create(request);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation()).isNotNull()
        );
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        final var singleTable = orderTableDao.save(OrderTableFactory.emptyTable(1));
        final var coupleTable = orderTableDao.save(OrderTableFactory.emptyTable(2));
        final var group = tableGroupDao.save(tableGroup(singleTable, coupleTable));

        final var response = tableGroupRestController.ungroup(group.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
