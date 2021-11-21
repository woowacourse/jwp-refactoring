package kitchenpos.testtool.response;

import org.springframework.http.HttpStatus;

import java.util.List;

public interface HttpResponse {

    <T> T convertBody(Class<T> tClass);

    <T> List<T> convertBodyToList(Class<T> tClass);

    HttpStatus statusCode();
}
