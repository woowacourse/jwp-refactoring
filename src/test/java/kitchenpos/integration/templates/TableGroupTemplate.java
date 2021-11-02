package kitchenpos.integration.templates;

import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupResponse;
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

    public ResponseEntity<TableGroupResponse> create(OrderTable... tables) {
        TableGroup tableGroup = new TableGroup(tables);

        return integrationTemplate.post(
            TABLE_GROUP_URL,
            tableGroup,
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
