package de.holisticon.camunda.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.camunda.bpm.engine.rest.ProcessEngineRestService;
import org.camunda.bpm.engine.rest.dto.ProcessEngineDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.rest.sub.runtime.ProcessInstanceResource;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class RestClientTest {

    public static final String BASE_ADDRESS = "http://localhost:8080/engine-rest";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void gets_engine_names_via_client_api() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_ADDRESS);
        target = target.path("engine");
        logger.info(target.getUri().toString());

        Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = builder.get();
        List list = response.readEntity(List.class);

        assertThat(list).hasSize(1);
        assertThat(((ProcessEngineDto)list.get(0)).getName()).isEqualTo("default");

    }

    @Test
        public void _() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_ADDRESS);
        target = target.path("default").path("process-instance").path("e058bbce-6ccb-11e4-99e9-b8e85641c4bc");

        final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).get();

        ByteArrayInputStream arrayInputStream = (ByteArrayInputStream) response.getEntity();
        Scanner scanner = new Scanner(arrayInputStream);
        scanner.useDelimiter("\\Z");//To read all scanner content in one String
        String data = "";
        if (scanner.hasNext())
            data = scanner.next();
        System.out.println(data);

        logger.info(response.readEntity(ProcessInstanceDto.class).toString());
    }

    @Test
    public void start_process() {
        final String expected = "e058bbce-6ccb-11e4-99e9-b8e85641c4bc";

        new JAXRSClientFactoryBean();

        final ProcessEngineRestService service = JAXRSClientFactory.create(BASE_ADDRESS, ProcessEngineRestService.class, new ArrayList<Class<?>>() {{
            add(JacksonJsonProvider.class);
        }});

        final ProcessInstanceDto instance = service.getProcessInstanceService("default").getProcessInstance(expected).getProcessInstance();

        assertThat(instance.getId()).isEqualTo(expected);

//        logger.info(service.getProcessEngineNames().toString());
    }
}
