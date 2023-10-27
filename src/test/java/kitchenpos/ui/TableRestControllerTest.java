package kitchenpos.ui;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate_insert_data.sql")
class TableRestControllerTest {

    @Autowired
    TableRestController tableRestController;

    @DisplayName("POST {{host}}/api/table-groups")
    @Test
    void orderTable_create() {
        //given
        final OrderTableCreateRequest request = RequestFixture.orderTableCreateRequest();

        //when
        final ResponseEntity<OrderTableResponse> response = tableRestController.create(request);

        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().getId()).isNotNull();
                    soft.assertThat(response.getBody().getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
                }
        );
    }

    @DisplayName("GET {{host}}/api/tables")
    @Test
    void orderTable_list() {
        //given

        //when
        final ResponseEntity<List<OrderTableResponse>> response = tableRestController.list();

        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody()).hasSize(8);
                }
        );
    }

    @DisplayName("PUT {{host}}/api/tables/1/empty")
    @Test
    void orderTable_changeEmpty_false() {
        //given
        final OrderTableChangeEmptyRequest request = RequestFixture.orderTableChangeEmptyRequest_false();

        //when
        final ResponseEntity<OrderTableResponse> response = tableRestController.changeEmpty(1L, request);
        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().isEmpty()).isFalse();
                    soft.assertThat(response.getBody().getId()).isEqualTo(1);
                }
        );
    }

    @DisplayName("PUT {{host}}/api/tables/1/empty")
    @Test
    void orderTable_changeEmpty_true() {
        //given
        final OrderTableChangeEmptyRequest request = RequestFixture.orderTableChangeEmptyRequest_true();

        //when
        final ResponseEntity<OrderTableResponse> response = tableRestController.changeEmpty(1L, request);
        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().isEmpty()).isTrue();
                    soft.assertThat(response.getBody().getId()).isEqualTo(1);
                }
        );
    }

    @DisplayName("PUT {{host}}/api/tables/1/number-of-guests")
    @Test
    void orderTable_changeNumberOfGuests() {
        //given
        final OrderTableChangeEmptyRequest changeEmptyRequestFalse = RequestFixture.orderTableChangeEmptyRequest_false();
        tableRestController.changeEmpty(1L, changeEmptyRequestFalse);
        final OrderTableChangeNumberOfGuestsRequest request = RequestFixture.orderTableChangeNumberOfGuestsRequest();

        //when
        final ResponseEntity<OrderTableResponse> response = tableRestController.changeNumberOfGuests(1L, request);
        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(response.getBody().getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
                    soft.assertThat(response.getBody().getId()).isEqualTo(1);
                }
        );
    }
}
