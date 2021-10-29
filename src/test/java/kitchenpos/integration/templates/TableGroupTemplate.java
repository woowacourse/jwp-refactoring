package kitchenpos.integration.templates;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TableGroupTemplate extends IntegrationTemplate {

    public static final String TABLE_GROUP_URL = "/api/table-groups";
    public static final String UNGROUP_URL = TABLE_GROUP_URL + "/{tableGroupId}";

    public ResponseEntity<TableGroup> create(OrderTable... tables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(
                Arrays.asList(tables)
        );

        return post(
                TABLE_GROUP_URL,
                tableGroup,
                TableGroup.class
        );
    }

    public ResponseEntity<Void> ungroup(TableGroup createdTableGroup) {
        return delete(
                UNGROUP_URL,
                createdTableGroup.getId()
        );
    }
}
