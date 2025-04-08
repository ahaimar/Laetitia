package com.pack.Laetitia.packManager.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pack.Laetitia.packManager.domin.ApiAuthentication;
import com.pack.Laetitia.packManager.domin.Response;
import com.pack.Laetitia.packManager.exceptio.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.BiConsumer;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

import static java.time.LocalTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RequestUtils {

    private static BiConsumer<HttpServletResponse, Response> writeRequest = (httpServletResponse , response) -> {
      try {
          var outPutSteam = httpServletResponse.getOutputStream();
          new ObjectMapper().writeValue(outPutSteam, response);
          outPutSteam.flush();
      } catch (Exception e) {
          throw new ApiException(e.getMessage());
      }
    };

    private static BiFunction<Exception, HttpStatus, String> errorReason = (exception, httpStatus) ->{

        if (httpStatus.isSameCodeAs(FORBIDDEN)) { return "You do not have enough permission";}
        if (httpStatus.isSameCodeAs(UNAUTHORIZED)) { return "You are not logged in" ;}
        if (exception instanceof DisabledException
                || exception instanceof LockedException
                ||exception instanceof BadCredentialsException
                || exception instanceof CredentialsExpiredException
                || exception instanceof ApiException
        ) { return exception.getMessage();}
        if (httpStatus.is5xxServerError()) {
            return "An Internal Server Error occurred";
        }else return "An Error occurred. Please try again";
    };

    public static Response getResponse(HttpServletRequest request, Map<?, ?> data
                                       ,String message
                                       ,HttpStatus status) {

        return new Response(LocalDateTime.now().toString()
                ,status.value()
                ,request.getRequestURI()
                , HttpStatus.valueOf(status.value())
                ,message
                ,EMPTY
                ,data
                );
    }

    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response,Exception exception) {

        if (exception instanceof AccessDeniedException) {

            Response apiResponse = getErrorResponse(request, response, exception, FORBIDDEN);
            writeRequest.accept(response, apiResponse);
        }
    }

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception, HttpStatus status) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        return new Response(now().toString()
                , status.value()
                , request.getRequestURI()
                , HttpStatus.valueOf(status.value())
                , errorReason.apply(exception, status)
                ,getRootCauseMessage(exception)
                , emptyMap()
        );
    }

}