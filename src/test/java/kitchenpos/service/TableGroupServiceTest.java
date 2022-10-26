package kitchenpos.service;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@SpringBootTest
@Sql({"classpath:/truncate.sql", "classpath:/set_up.sql"})
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void create() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(new OrderTableRequest(1L), new OrderTableRequest(8L))
        );

        TableGroup tableGroup = tableGroupService.create(tableGroupCreateRequest);

        assertAll(
                () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(2),
                () -> assertThat(tableGroup.getOrderTables().get(0).getTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(tableGroup.getOrderTables().get(1).getTableGroupId()).isEqualTo(tableGroup.getId())
        );
    }
    @DisplayName("테이블이 없는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableEmpty() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of());

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("테이블이 하나만 있는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableOne() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(new OrderTableRequest(1L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("존재하지 않는 테이블이 있는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableNotExist() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(new OrderTableRequest(1L),
                        new OrderTableRequest(6L),
                        new OrderTableRequest(100L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("주문을 할 수 없는 테이블이 있는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableNotEmpty() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(new OrderTableRequest(1L),
                        new OrderTableRequest(2L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("이미 테이블 그룹에 묶인 테이블이 있는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableAlreadyGrouped() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(new OrderTableRequest(1L),
                        new OrderTableRequest(4L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("테이블 그룹 지정을 해제한다")
    @Test
    void ungroup() {
        tableGroupService.ungroup(1L);

        assertThat(jdbcTemplateOrderTableDao.findAllByTableGroupId(1L).isEmpty()).isTrue();
    }
    @DisplayName("주문이 진행중인 테이블이 있는 테이블 그룹 지정을 해제할 수 없다")
    @Test
    void ungroup_orderTableOrderComplete() {
        assertThatThrownBy(() -> tableGroupService.ungroup(2L)).isInstanceOf(IllegalArgumentException.class);
    }
}
