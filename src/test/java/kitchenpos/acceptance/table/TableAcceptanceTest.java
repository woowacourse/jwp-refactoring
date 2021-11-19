package kitchenpos.acceptance.table;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블 등록 성공")
    @Test
    void create() {
        OrderTableRequest table = new OrderTableRequest(0, true);

        ResponseEntity<OrderTableResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/tables",
                table,
                OrderTableResponse.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderTableResponse response = responseEntity.getBody();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getNumberOfGuests()).isEqualTo(0);
        assertThat(response.isEmpty()).isTrue();
    }

    @DisplayName("테이블 목록 조회")
    @Test
    void list() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable table2 = new OrderTable(null, 0, true);

        orderTableRepository.save(table);
        orderTableRepository.save(table2);

        ResponseEntity<List<OrderTableResponse>> responseEntity = testRestTemplate.exchange(
                "/api/tables",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderTableResponse>>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<OrderTableResponse> response = responseEntity.getBody();
        assertThat(response).hasSize(2);
        assertThat(response)
                .extracting(OrderTableResponse::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @DisplayName("주문 가능 상태를 변경 성공")
    @Test
    void changeEmpty() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(false);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(true);

        testRestTemplate.put("/api/tables/" + savedTable.getId() + "/empty", orderTableRequest1);

        savedTable = orderTableRepository.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.isEmpty()).isFalse();

        testRestTemplate.put("/api/tables/" + savedTable.getId() + "/empty", orderTableRequest2);

        savedTable = orderTableRepository.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.isEmpty()).isTrue();
    }

    @DisplayName("주문 가능 상태를 변경 실패 - 잘못된 테이블 아이디")
    @Test
    void changeEmptyByIncorrectTableId() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(true);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + 100 + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(orderTableRequest),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 가능 상태를 변경 실패 - 테이블 그룹 아이디가 존재")
    @Test
    void changeEmptyWhenTableHasTableGroupId() {
        TableGroup tableGroup = new TableGroup();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        OrderTable table = new OrderTable(savedTableGroup, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(orderTableRequest),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 가능 상태를 변경 성공 - 계산 완료 상태의 주문")
    @Test
    void changeEmptyWhenOrderIsCompletion() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);
        Order order = new Order(savedTable, OrderStatus.COMPLETION.name());
        orderRepository.save(order);
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(orderTableRequest),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        savedTable = orderTableRepository.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 가능 상태를 변경 실패 - 조리 상태의 주문")
    @Test
    void changeEmptyWhenOrderIsCooking() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);
        Order order = new Order(savedTable, OrderStatus.COOKING.name());
        orderRepository.save(order);
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(orderTableRequest),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 가능 상태를 변경 실패 - 식사 상태의 주문")
    @Test
    void changeEmptyWhenOrderIsMeal() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);
        Order order = new Order(savedTable, OrderStatus.MEAL.name());
        orderRepository.save(order);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(orderTableRequest),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("인원 변경 성공")
    @Test
    void changeNumberOfGuests() {
        OrderTable table = new OrderTable(null, 0, false);
        OrderTable savedTable = orderTableRepository.save(table);

        OrderTableRequest orderTableRequest = new OrderTableRequest(10);

        testRestTemplate.put("/api/tables/" + savedTable.getId() + "/number-of-guests", orderTableRequest);

        savedTable = orderTableRepository.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("인원 변경 실패 - 0 미만")
    @Test
    void changeNumberOfGuestsByNegativeNumber() {
        OrderTable table = new OrderTable(null, 0, false);
        OrderTable savedTable = orderTableRepository.save(table);

        OrderTableRequest orderTableRequest = new OrderTableRequest(-1);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/number-of-guests",
                HttpMethod.PUT,
                new HttpEntity<>(orderTableRequest),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("인원 변경 실패 - 잘못된 테이블 아이디")
    @Test
    void changeNumberOfGuestsByIncorrectTableId() {
        OrderTable table = new OrderTable(null, 0, false);
        orderTableRepository.save(table);

        OrderTableRequest orderTableRequest = new OrderTableRequest(10);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + 100 + "/number-of-guests",
                HttpMethod.PUT,
                new HttpEntity<>(orderTableRequest),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("인원 변경 실패 - 테이블 주문 불가능 상태")
    @Test
    void changeNumberOfGuestsByIncorrectTableState() {
        OrderTable table = new OrderTable(null, 0, true);
        OrderTable savedTable = orderTableRepository.save(table);

        OrderTableRequest orderTableRequest = new OrderTableRequest(10);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/number-of-guests",
                HttpMethod.PUT,
                new HttpEntity<>(orderTableRequest),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
