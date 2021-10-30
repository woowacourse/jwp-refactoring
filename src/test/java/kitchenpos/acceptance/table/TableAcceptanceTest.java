package kitchenpos.acceptance.table;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);

        ResponseEntity<OrderTable> responseEntity = testRestTemplate.postForEntity(
                "/api/tables",
                table,
                OrderTable.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        OrderTable response = responseEntity.getBody();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getNumberOfGuests()).isEqualTo(0);
        assertThat(response.isEmpty()).isTrue();
    }

    @DisplayName("테이블 목록 조회")
    @Test
    void list() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);

        orderTableDao.save(table);
        orderTableDao.save(table2);

        ResponseEntity<List<OrderTable>> responseEntity = testRestTemplate.exchange(
                "/api/tables",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderTable>>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<OrderTable> response = responseEntity.getBody();
        assertThat(response).hasSize(2);
        assertThat(response)
                .extracting(OrderTable::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @DisplayName("주문 가능 상태를 변경 성공")
    @Test
    void changeEmpty() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setEmpty(false);

        testRestTemplate.put("/api/tables/" + savedTable.getId() + "/empty", table2);

        savedTable = orderTableDao.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.isEmpty()).isFalse();

        testRestTemplate.put("/api/tables/" + savedTable.getId() + "/empty", table);

        savedTable = orderTableDao.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.isEmpty()).isTrue();
    }

    @DisplayName("주문 가능 상태를 변경 실패 - 잘못된 테이블 아이디")
    @Test
    void changeEmptyByIncorrectTableId() {
        OrderTable table = new OrderTable();
        table.setEmpty(true);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + 100 + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(table),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 가능 상태를 변경 실패 - 테이블 그룹 아이디가 존재")
    @Test
    void changeEmptyWhenTableHasTableGroupId() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        table.setTableGroupId(savedTableGroup.getId());
        OrderTable savedTable = orderTableDao.save(table);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(table),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 가능 상태를 변경 성공 - 계산 완료 상태의 주문")
    @Test
    void changeEmptyWhenOrderIsCompletion() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);
        table.setEmpty(false);

        Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(table),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        savedTable = orderTableDao.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 가능 상태를 변경 실패 - 조리 상태의 주문")
    @Test
    void changeEmptyWhenOrderIsCooking() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);

        Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(table),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("주문 가능 상태를 변경 실패 - 식사 상태의 주문")
    @Test
    void changeEmptyWhenOrderIsMeal() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);

        Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/empty",
                HttpMethod.PUT,
                new HttpEntity<>(table),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("인원 변경 성공")
    @Test
    void changeNumberOfGuests() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(false);
        OrderTable savedTable = orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(10);

        testRestTemplate.put("/api/tables/" + savedTable.getId() + "/number-of-guests", table2);

        savedTable = orderTableDao.findById(savedTable.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(savedTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("인원 변경 실패 - 0 미만")
    @Test
    void changeNumberOfGuestsByNegativeNumber() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(false);
        OrderTable savedTable = orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(-1);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/number-of-guests",
                HttpMethod.PUT,
                new HttpEntity<>(table2),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("인원 변경 실패 - 잘못된 테이블 아이디")
    @Test
    void changeNumberOfGuestsByIncorrectTableId() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(false);
        orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(10);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + 100 + "/number-of-guests",
                HttpMethod.PUT,
                new HttpEntity<>(table2),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("인원 변경 실패 - 테이블 주문 불가능 상태")
    @Test
    void changeNumberOfGuestsByIncorrectTableState() {
        OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);
        OrderTable savedTable = orderTableDao.save(table);

        OrderTable table2 = new OrderTable();
        table2.setNumberOfGuests(10);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                "/api/tables/" + savedTable.getId() + "/number-of-guests",
                HttpMethod.PUT,
                new HttpEntity<>(table2),
                Void.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
