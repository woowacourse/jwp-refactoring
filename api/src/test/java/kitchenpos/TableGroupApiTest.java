package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.OrderTable;
import kitchenpos.menu.domain.OrderTableRepository;
import kitchenpos.menu.domain.TableGroup;
import kitchenpos.menu.domain.TableGroupRepository;
import kitchenpos.menu.dto.OrderTableResponse;
import kitchenpos.menu.dto.TableGroupRequest;
import kitchenpos.menu.dto.TableGroupRequest.OrderTableOfGroupRequest;
import kitchenpos.menu.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TableGroupApiTest extends ApiTest {

    private static final String BASE_URL = "/api/table-groups";

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private List<TableGroup> tableGroups;
    private List<OrderTable> orderTables;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        orderTables = new ArrayList<>();
        tableGroups = new ArrayList<>();

        orderTables.add(orderTableRepository.save(new OrderTable(0, true)));
        orderTables.add(orderTableRepository.save(new OrderTable(0, true)));
        tableGroups.add(tableGroupRepository.save(new TableGroup(orderTables)));
    }

    @DisplayName("단체 지정 등록")
    @Test
    void postTableGroups() {
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(
            new OrderTableOfGroupRequest(orderTables.get(0).getId()),
            new OrderTableOfGroupRequest(orderTables.get(1).getId())
        ));
        ResponseEntity<TableGroupResponse> responseEntity = testRestTemplate.postForEntity(
            BASE_URL,
            request,
            TableGroupResponse.class
        );
        TableGroupResponse response = responseEntity.getBody();
        List<OrderTableResponse> expected = orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderTables())
            .hasSameSizeAs(orderTables)
            .usingRecursiveFieldByFieldElementComparator()
            .usingElementComparatorIgnoringFields("id", "tableGroupId", "empty")
            .hasSameElementsAs(expected);
        for (OrderTableResponse orderTable : response.getOrderTables()) {
            assertThat(orderTable.getTableGroupId()).isEqualTo(response.getId());
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @DisplayName("단체 지정 제거")
    @Test
    void deleteTableGroup() {
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
            BASE_URL + "/" + tableGroups.get(0).getId(),
            HttpMethod.DELETE,
            new HttpEntity<>(new HashMap<>(), new HttpHeaders()),
            String.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
