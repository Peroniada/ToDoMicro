package edu.peronade;

import edu.peronade.entity.ToDoTask;
import edu.peronade.repositories.ToDoTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@EnableBinding (ToDoTaskChannels.class)
@EnableDiscoveryClient
@SpringBootApplication
public class TodoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoServiceApplication.class, args);
    }
}

interface ToDoTaskChannels {
    @Input
    SubscribableChannel input();
}

@Component
class SampleDataCLR implements CommandLineRunner {
    private final ToDoTaskRepository toDoTaskRepository;

    @Autowired
    public SampleDataCLR(final ToDoTaskRepository toDoTaskRepository) {
        this.toDoTaskRepository = toDoTaskRepository;
    }


    @Override
    public void run(String... args) throws RuntimeException {
        Stream.of("Buy eggz", "Get some Cake", "Prepare some omelette", "Find someone to eat with", "Get some beer", "Become famous with Stunning dancing performance").forEach(task -> toDoTaskRepository.save(new ToDoTask(task)));
        toDoTaskRepository.findAll().forEach(System.out::println);
    }

}

@MessageEndpoint
class ToDoTaskProcessor {
    @ServiceActivator(inputChannel = "input")
    public void onNewTask(String taskName) {
        this.toDoTaskRepository.save(new ToDoTask(taskName));
    }

    private final ToDoTaskRepository toDoTaskRepository;

    @Autowired
    public ToDoTaskProcessor(ToDoTaskRepository toDoTaskRepository) {
        this.toDoTaskRepository = toDoTaskRepository;
    }
}
