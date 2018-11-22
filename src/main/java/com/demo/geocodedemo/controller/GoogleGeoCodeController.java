package com.demo.geocodedemo.controller;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/geocode/{address}")
public class GoogleGeoCodeController {

    @Autowired
    @EndpointInject(uri="direct:start")
    private ProducerTemplate template;

    @Value("${google.api.key}")
    private String apiKey;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String getDetails(@PathVariable(name = "address") String address) {
        if (StringUtils.isEmpty(address.trim())) {
            return new String("{\"status\": \"ERR\"}");
        }
        final Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("address", address);
        headers.put("apiKey", apiKey);
        Exchange exchange = template.send("direct:start", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeaders(headers);

            }
        });

        String out = exchange.getOut().getBody(String.class);
        return out;
    }
}
