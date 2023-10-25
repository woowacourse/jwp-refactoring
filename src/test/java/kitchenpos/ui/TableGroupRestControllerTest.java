package kitchenpos.ui;

import io.restassured.RestAssured;
import kitchenpos.application.TableGroupService;
import kitchenpos.common.controller.ControllerTest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

class TableGroupRestControllerTest extends ControllerTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void TableGroup을_생성하면_201을_반환한다() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 0, true));
        final var 요청_준비 = RestAssured.given()
                .body(new TableGroupCreateRequest(List.of(orderTable.getId(), orderTable1.getId())))
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .post("/api/table-groups");

        // then
        응답.then().assertThat().statusCode(CREATED.value());
    }

    @Test
    void TableGroup을_풀면_204를_반환한다() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 0, true));
        final Long tableGroupId = tableGroupService.create(List.of(orderTable.getId(), orderTable1.getId()));
        final var 요청_준비 = RestAssured.given()
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .delete("/api/table-groups/" + tableGroupId);

        // then
        응답.then().assertThat().statusCode(NO_CONTENT.value());
    }
}
