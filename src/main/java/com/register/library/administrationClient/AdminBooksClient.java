package com.register.library.administrationClient;

import books.wsdl.GetBooksRequest;
import books.wsdl.GetBooksResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class AdminBooksClient extends WebServiceGatewaySupport {

    public GetBooksResponse getBooks() {
        GetBooksRequest request = new GetBooksRequest();

        logger.info("Request to administration component");

        GetBooksResponse response = (GetBooksResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:9088/ws/books.wsdl", request,
                        new SoapActionCallback("http://www.adrianBooks.com/springsoap/gen"));

        return response;
    }
}
