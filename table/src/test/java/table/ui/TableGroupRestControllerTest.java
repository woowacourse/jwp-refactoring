package table.ui;

import table.application.dto.request.TableGroupCreateRequest;
import table.application.dto.response.TableGroupResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate_insert_data.sql")
class TableGroupRestControllerTest {

    @Autowired
    TableGroupRestController tableGroupRestController;

    @DisplayName("POST {{host}}/api/table-groups")
    @Test
    void tableGroup_create() {
        //given
        final TableGroupCreateRequest request = TableRequestFixture.tableGroupCreateRequest();

        //when
        final ResponseEntity<TableGroupResponse> response = tableGroupRestController.create(request);

        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().getId()).isNotNull();
                }
        );
    }

    @DisplayName("DELETE {{host}}/api/table-groups/1")
    @Test
    void tableGroup_ungroup() {
        //given
        final TableGroupCreateRequest request = TableRequestFixture.tableGroupCreateRequest();
        final Long tableGroupId = tableGroupRestController.create(request).getBody().getId();

        //when
        final ResponseEntity<Void> response = tableGroupRestController.ungroup(tableGroupId);
        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                }
        );
    }
}
