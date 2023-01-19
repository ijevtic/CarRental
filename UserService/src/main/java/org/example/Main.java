package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.util.LongServiceResponse;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        String json = "{\"statusCode\":200,\"message\":\"Company found\",\"data\":3}";
        ObjectMapper objectMapper = new ObjectMapper();
        LongServiceResponse employee = objectMapper.readValue(json, LongServiceResponse.class);

        System.out.println(employee);
    }
}