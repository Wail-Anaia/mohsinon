package com.mohsinon.shared.query.resolver;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mohsinon.shared.query.parser.SearchParser;
import com.mohsinon.shared.query.request.FilterRequest;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class QueryRequestResolver {

    public FilterRequest resolve(HttpServletRequest request) {

        Map<String,String> parameters = new HashMap<>();

        request.getParameterMap().forEach((key,values)->{

            if(values != null && values.length > 0){

                parameters.put(key, values[0]);

            }

        });

        return SearchParser.parse(parameters);

    }

}