package com.panyukovnn.linkshortener.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.panyukovnn.linkshortener.dto.common.CommonResponse;
import com.panyukovnn.linkshortener.dto.common.ValidationError;
import com.panyukovnn.linkshortener.exceptions.NotFoundShortLinkException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class LinkShortenerExceptionHandler {

    private final String notFoundPage;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ValidationError> validationErrors = bindingResult.getFieldErrors().stream()
            .map(fe -> ValidationError.builder()
                .field(fe.getField())
                .message(fe.getDefaultMessage())
                .build())
            .toList();

        log.warn("Ошибка валидации: {}", validationErrors, e);

        return CommonResponse.builder()
            .errorMessage("Ошибка валидации")
            .validationErrors(validationErrors)
            .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public CommonResponse<?> handlerException(Exception e) {
        log.error("Непредвиденное исключение: {}", e.getMessage(), e);

        return CommonResponse.builder()
            .id(UUID.randomUUID())
            .errorMessage("Непредвиденное исключение:" + e.getMessage())
            .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundShortLinkException.class)
    public ResponseEntity<String> handleNotFoundPageException(NotFoundShortLinkException e) {
        log.warn(e.getMessage(), e);

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.TEXT_HTML)
            .body(notFoundPage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<?> handleInvalidFormatException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException ife) {

            List<ValidationError> errors = ife.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .filter(fieldName -> !"body".equals(fieldName))
                .map(fieldName -> ValidationError.builder()
                    .field(fieldName)
                    .message("Некорректный формат данных")
                    .build())
                .toList();

            log.error("Ошибки формата данных: {}", errors, e);

            return CommonResponse.builder()
                .errorMessage("Ошибки валидации формата данных")
                .validationErrors(errors)
                .build();
        }

        return handlerException(e);
    }


}
