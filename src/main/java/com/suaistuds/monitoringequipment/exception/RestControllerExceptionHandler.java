package com.suaistuds.monitoringequipment.exception;

import com.suaistuds.monitoringequipment.payload.ApiResponse;
import com.suaistuds.monitoringequipment.payload.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Глобальный обработчик исключений для REST контроллеров.
 * Обрабатывает исключения и возвращает стандартизированные HTTP-ответы.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>Обработка кастомных исключений приложения</li>
 *   <li>Обработка стандартных исключений Spring</li>
 *   <li>Формирование единого формата ответов об ошибках</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@ControllerAdvice
public class RestControllerExceptionHandler {

    /**
     * Обрабатывает базовые исключения приложения.
     *
     * @param exception исключение типа SuaistudsException
     * @return ResponseEntity с ApiResponse
     */
    public ResponseEntity<ApiResponse> resolveException(SuaistudsException exception) {
        String message = exception.getMessage();
        HttpStatus status = exception.getStatus();

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setSuccess(Boolean.FALSE);
        apiResponse.setMessage(message);

        return new ResponseEntity<>(apiResponse, status);
    }

    /**
     * Обрабатывает исключения авторизации (401 UNAUTHORIZED).
     *
     * @param exception UnauthorizedException
     * @return ResponseEntity с ApiResponse
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse> resolveException(UnauthorizedException exception) {

        ApiResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Обрабатывает некорректные запросы (400 BAD_REQUEST).
     *
     * @param exception BadRequestException
     * @return ResponseEntity с ApiResponse
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(BadRequestException exception) {
        ApiResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает случаи отсутствия ресурсов (404 NOT_FOUND).
     *
     * @param exception ResourceNotFoundException
     * @return ResponseEntity с ApiResponse
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(ResourceNotFoundException exception) {
        ApiResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает ошибки доступа (403 FORBIDDEN).
     *
     * @param exception AccessDeniedException
     * @return ResponseEntity с ApiResponse
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(AccessDeniedException exception) {
        ApiResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity< >(apiResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Обрабатывает ошибки валидации параметров (400 BAD_REQUEST).
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity с детализированными сообщениями об ошибках
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> resolveException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> messages = new ArrayList<>(fieldErrors.size());
        for (FieldError error : fieldErrors) {
            messages.add(error.getField() + " - " + error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ExceptionResponse(messages, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает несоответствие типов параметров (400 BAD_REQUEST).
     *
     * @param ex MethodArgumentTypeMismatchException
     * @return ResponseEntity с сообщением об ошибке
     */
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> resolveException(MethodArgumentTypeMismatchException ex) {
        String message = "Parameter '" + ex.getParameter().getParameterName() + "' must be '"
                + Objects.requireNonNull(ex.getRequiredType()).getSimpleName() + "'";
        List<String> messages = new ArrayList<>(1);
        messages.add(message);
        return new ResponseEntity<>(new ExceptionResponse(messages, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает неподдерживаемые HTTP методы (405 METHOD_NOT_ALLOWED).
     *
     * @param ex HttpRequestMethodNotSupportedException
     * @return ResponseEntity с перечнем поддерживаемых методов
     */
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(HttpRequestMethodNotSupportedException ex) {
        String message = "Request method '" + ex.getMethod() + "' not supported. List of all supported methods - "
                + ex.getSupportedHttpMethods();
        List<String> messages = new ArrayList<>(1);
        messages.add(message);

        return new ResponseEntity<>(new ExceptionResponse(messages, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                HttpStatus.METHOD_NOT_ALLOWED.value()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Обрабатывает некорректные JSON-запросы (400 BAD_REQUEST).
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity с сообщением об ошибке
     */
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> resolveException(HttpMessageNotReadableException ex) {
        String message = "Please provide Request Body in valid JSON format";
        List<String> messages = new ArrayList<>(1);
        messages.add(message);
        return new ResponseEntity<>(new ExceptionResponse(messages, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }
}