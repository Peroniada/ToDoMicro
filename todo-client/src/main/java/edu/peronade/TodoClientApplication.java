package edu.peronade;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.hateoas.Resources;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@EnableBinding(ToDoTaskChannels.class)
@IntegrationComponentScan
@EnableCircuitBreaker
@EnableFeignClients
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class TodoClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoClientApplication.class, args);
	}
}


@RestController
@RequestMapping("/toDoTasks")
class ToDoApiGateway {

	private final ToDoTasksReader toDoTasksReader;
	private final ToDoTasksWriter toDoTasksWriter;

	@Autowired
	public ToDoApiGateway(ToDoTasksReader toDoTasksReader, ToDoTasksWriter toDoTasksWriter)
	{
		this.toDoTasksReader = toDoTasksReader;
		this.toDoTasksWriter = toDoTasksWriter;
	}

	@RequestMapping(method = RequestMethod.POST)
	public void write(@RequestBody ToDoTask toDoTask)
	{
		this.toDoTasksWriter.write(toDoTask.getTaskName());
	}

	public Collection<String> fallback() {
		return new ArrayList<>();
	}

	@HystrixCommand(fallbackMethod = "fallback")
	@RequestMapping(method = RequestMethod.GET, value = "/tasks")
	public Collection<String> tasks()
	{
		return this.toDoTasksReader.read()
				.getContent()
				.stream()
				.map(t -> t.getTaskName())
				.collect(Collectors.toList());
	}
}

interface ToDoTaskChannels {
	@Output
	MessageChannel output();
}

@FeignClient("todo-service")
interface ToDoTasksReader {

	@RequestMapping(method = RequestMethod.GET, value = "/toDoTasks")
	Resources<ToDoTask> read();
}

@MessagingGateway
interface ToDoTasksWriter {
	@Gateway(requestChannel = "output")
	void write (String task);
}

class ToDoTask {

	public String getTaskName() {
		return taskName;
	}

	private String taskName;

}