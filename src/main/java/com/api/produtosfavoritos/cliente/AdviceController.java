package com.api.produtosfavoritos.cliente;

import com.api.produtosfavoritos.exception.ChaveDuplicadaException;
import com.api.produtosfavoritos.exception.EntidadeNaoEncontradaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AdviceController {

    Logger log = LoggerFactory.getLogger("AdviceController");

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ChaveDuplicadaException.class)
    @ResponseBody
    ResponseEntity<ErrorInfo> handleChaveDuplicadaException(HttpServletRequest req, ChaveDuplicadaException ex){
        log.error("Erro de integridade de dados: " + ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new ErrorInfo(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name() ,ex.getMessage(), req.getRequestURL()), headers, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    ResponseEntity<ErrorInfo> handleDataIntegrityException(HttpServletRequest req, DataIntegrityViolationException ex){
        log.error("Erro de integridade de dados: " + ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new ErrorInfo(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name() ,ex.getMessage(), req.getRequestURL()), headers, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    @ResponseBody
    ResponseEntity<ErrorInfo> handleEntidadeNaoEncontradaException(HttpServletRequest req, EntidadeNaoEncontradaException ex){
        log.error("Erro de entidade nao encontrada: " + ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new ErrorInfo(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name() ,ex.getMessage(), req.getRequestURL()), headers, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseBody
    ResponseEntity<ErrorInfo> handleEmptyResultException(HttpServletRequest req, EmptyResultDataAccessException ex){
        log.error("Erro de dado nao encontrada: " + ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new ErrorInfo(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name() ,ex.getMessage(), req.getRequestURL()), headers, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ResponseEntity<ErrorInfo> handleValidationException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        log.error("Erro de campo invalido: " + ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            });
        return new ResponseEntity<>(new ErrorInfo(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name() ,req.getRequestURL(), errors), headers, HttpStatus.BAD_REQUEST);
    }
}
