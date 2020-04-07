package com.register.library.administrationClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class AdminBooksConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("books.wsdl");
        return marshaller;
    }

    @Bean
    public AdminBooksClient adminBooksClient(Jaxb2Marshaller marshaller) {
        AdminBooksClient client = new AdminBooksClient();
        client.setDefaultUri("http://localhost:9088/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
