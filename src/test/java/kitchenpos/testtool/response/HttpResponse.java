package kitchenpos.testtool.response;

import java.util.List;
import org.springframework.http.HttpStatus;

public interface HttpResponse {

    <T> T convertBody(Class<T> tClass);

    <T> List<T> convertBodyToList(Class<T> tClass);

    HttpStatus statusCode();
}
