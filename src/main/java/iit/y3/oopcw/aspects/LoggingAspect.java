package iit.y3.oopcw.aspects;

import com.google.gson.Gson;
import iit.y3.oopcw.dto.response.Api_RSPNS;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger("perfLog");

    public static String changeDtoIntoJsonString(Object dto) {
        Gson gson = new Gson();
        return gson.toJson(dto);
    }

    private static String populateKeyValString(
            Map<String, String> map
    ) {
        String value = "";
        if (map != null && map.size() > 0) {

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                value = key + "=" + val;
            }

        }
        return value;
    }

    private static String populateJsonString(
            Map<String, Object> map
    ) {
        String value = "";
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object val = entry.getValue();
                value = changeDtoIntoJsonString(val);
            }
        }
        return value;
    }

    @Around(value = "@annotation(EnableResultLogger)")
    public Object handleAccessToken(ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        Object executionResult;
        Map<String, String> reqPathVariableMap = new HashMap<>();
        Map<String, Object> reqBodyMap = new HashMap<>();
        Object[] args = proceedingJoinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (annotation instanceof PathVariable) {
                    PathVariable pathVariable = (PathVariable) annotation;
                    reqPathVariableMap.put(pathVariable.value(), (String) args[argIndex]);
                } else if (annotation instanceof RequestParam) {
                    RequestParam reqVariable = (RequestParam) annotation;
                    reqPathVariableMap.put(reqVariable.name(), (String) args[argIndex]);
                } else if (annotation instanceof RequestHeader) {
                    RequestHeader reqVariable = (RequestHeader) annotation;
                    reqPathVariableMap.put(reqVariable.value(), (String) args[argIndex]);
                } else if (annotation instanceof RequestBody && args[argIndex] != null) {
                    /* RequestBody reqBody = (RequestBody) annotation; */
                    reqBodyMap.put(args[argIndex].getClass().getSimpleName().toLowerCase(), args[argIndex]);
                }
            }
        }
        long start = System.currentTimeMillis();
        executionResult = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        long timeSpan = end - start;
        ResponseEntity<Api_RSPNS> result = (ResponseEntity<Api_RSPNS>) executionResult;
        dispatchToTheLog(method, reqPathVariableMap, result, reqBodyMap, timeSpan);
        return executionResult;
    }

    private void dispatchToTheLog(
            Method method
            , Map<String, String> reqPathVariableMap
            , ResponseEntity<Api_RSPNS> result
            , Map<String, Object> reqBodyMap
            , long timeSpan
    ) {
        if (method.isAnnotationPresent(EnableResultLogger.class)) {
            String traceId = reqPathVariableMap.getOrDefault("traceId", "");
            String input = populateKeyValString(reqPathVariableMap) + " "
                    + (populateJsonString(reqBodyMap).contains("password")
                    ? "contains sensitive data"
                    : populateJsonString(reqBodyMap));
            String output = changeDtoIntoJsonString(result.getBody());
            /*String authorization = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                    .getHeader(HttpHeaders.AUTHORIZATION);
            if (null == authorization)*/
            String authorization = reqPathVariableMap.getOrDefault("user", "");
            logger.debug(
                    "Method|{}| "
                            + "\t User|{}| "
                            + "\t TraceId|{}| "
                            + "\t ExecutionTime(ms)|{}| "
                            + "\t Input|{}| "
                            + "\t HttpStatus|{}| "
                            + "\t\n Response|{}|"
                    , method.getName()
                    , authorization
                    , traceId
                    , timeSpan
                    , input
                    , result.getStatusCode()
                    , output);
        }
    }
}