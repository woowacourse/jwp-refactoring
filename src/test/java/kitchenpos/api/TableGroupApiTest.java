package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.generator.TableGenerator;
import kitchenpos.generator.TableGroupGenerator;
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
    private JdbcTemplateTableGroupDao tableGroupDao;

    @Autowired
    private JdbcTemplateOrderTableDao orderTableDao;

    private List<TableGroup> tableGroups;
    private List<OrderTable> orderTables;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        orderTables = new ArrayList<>();
        tableGroups = new ArrayList<>();

        orderTables.add(orderTableDao.save(TableGenerator.newInstance(0, true)));
        orderTables.add(orderTableDao.save(TableGenerator.newInstance(0, true)));
        tableGroups.add(tableGroupDao.save(TableGroupGenerator.newInstance(LocalDateTime.now())));
    }

    @DisplayName("단체 지정 등록")
    @Test
    void postTableGroups() {
        TableGroup request = TableGroupGenerator.newInstance(Arrays.asList(
            orderTables.get(0).getId(),
            orderTables.get(1).getId()
        ));
        ResponseEntity<TableGroup> responseEntity = testRestTemplate.postForEntity(BASE_URL,
            request, TableGroup.class);
        TableGroup response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderTables())
            .hasSameSizeAs(orderTables)
            .usingRecursiveFieldByFieldElementComparator()
            .usingElementComparatorIgnoringFields("id", "tableGroupId", "empty")
            .hasSameElementsAs(orderTables);
        for (OrderTable orderTable : response.getOrderTables()) {
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
