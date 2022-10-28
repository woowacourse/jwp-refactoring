package kitchenpos.ui;

import static kitchenpos.fixture.TableGroupFactory.tableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.fixture.OrderTableFactory;
import org.junit.jupiter.api.Disabled;
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

    @Disabled
    @DisplayName("테이블 그룹 등록")
    @Test
    void create() {
        final var singleTable = orderTableDao.save(OrderTableFactory.emptyTable(1));
        final var coupleTable = orderTableDao.save(OrderTableFactory.emptyTable(2));

        final var response = tableGroupRestController.create(tableGroup(singleTable, coupleTable));

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation()).isNotNull()
        );
    }

    @Disabled
    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        final var singleTable = orderTableDao.save(OrderTableFactory.emptyTable(1));
        final var coupleTable = orderTableDao.save(OrderTableFactory.emptyTable(2));
        final var group = tableGroupDao.save(tableGroup(singleTable, coupleTable));

        final var response = tableGroupRestController.ungroup(group.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
