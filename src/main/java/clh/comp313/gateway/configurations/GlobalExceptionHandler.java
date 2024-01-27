package clh.comp313.gateway.configurations;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.StatusRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<?> handleStatusRuntimeException(StatusRuntimeException e) {
        return new ResponseEntity<>(Collections.singletonMap("error", e.getStatus().getDescription()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidProtocolBufferException.class)
    public ResponseEntity<?> handleInvalidProtocolBufferException(InvalidProtocolBufferException e) {
        return new ResponseEntity<>(Collections.singletonMap("error", e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }
}
