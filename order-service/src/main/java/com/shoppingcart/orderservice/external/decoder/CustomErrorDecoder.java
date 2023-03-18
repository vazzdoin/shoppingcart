package com.shoppingcart.orderservice.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.orderservice.exception.CustomException;
import com.shoppingcart.orderservice.external.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

/*A custom error decoders to convert the error and pass on the errors to controller as error
response that are encountered from the feign client

More Details :
Whenever the feign client(ProductServiceFeign) is going to encounter and exception,
that exception is decoded by the CustomErrorDecoder and converted to the CustomException of the
Order Service which will be caught by the @ControllerAdvice of this Order Service and proper error
message would be returned rather than a vague error message in the response.

e.g. Response to reduceQuantity internal feign client call in case quantity is not sufficient
{
    "errorMessage": "Product does not have sufficient quantity",
    "errorCode": "INSUFFICIENT_QUANTITY"
}

*/
@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("url ::{}", response.request().url());
        log.info("headers::{}", response.request().headers());

        try {
            ErrorResponse errorResponse
                    = objectMapper.readValue(response.body().asInputStream(),
                    ErrorResponse.class);
            return new CustomException(errorResponse.getErrorMessage(),
                    errorResponse.getErrorCode(),
                    response.status());
        } catch (IOException e) {
            throw new CustomException("Internal Server Error",
                    "INTERNAL_SERVER_ERROR",
                    500);
        }
    }
}
