package kitchenpos.integration.templates;

import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.factory.OrderTableFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TableGroupTemplate {

    public static final String TABLE_GROUP_URL = "/api/table-groups";
    public static final String UNGROUP_URL = TABLE_GROUP_URL + "/{tableGroupId}";

    private final IntegrationTemplate integrationTemplate;

    public TableGroupTemplate(IntegrationTemplate integrationTemplate) {
        this.integrationTemplate = integrationTemplate;
    }

    public ResponseEntity<TableGroupResponse> create(OrderTables tables) {
        TableGroupRequest tableGroupRequet =
            new TableGroupRequest(null, null, OrderTableFactory.dtoList(tables));

        return integrationTemplate.post(
            TABLE_GROUP_URL,
            tableGroupRequet,
            TableGroupResponse.class
        );
    }

    public ResponseEntity<Void> ungroup(TableGroup createdTableGroup) {
        return integrationTemplate.delete(
            UNGROUP_URL,
            createdTableGroup.getId()
        );
    }
}
