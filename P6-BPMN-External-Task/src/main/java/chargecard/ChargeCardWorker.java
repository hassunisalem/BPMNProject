package chargecard;

import java.util.logging.Logger;
import org.camunda.bpm.client.ExternalTaskClient;

public class ChargeCardWorker
{
    private final static Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());

    public static void main(String[] args)
    {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();
        LOGGER.info("Not yet requests");

        // subscribe to an external task topic as specified in the process
        client.subscribe("charge-card")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here
                    System.out.println("Test of External Service: Pay");
                    // Get a process variable
                    Long age = (Long) externalTask.getVariable("age");
                    Boolean bigGuns = (Boolean) externalTask.getVariable("bigGuns");

                    if (bigGuns & age > 17)
                        LOGGER.info("hardball field is booked with bigGuns!");
                    if (bigGuns & age < 18)
                        LOGGER.info("hardball field is booked with bigGuns and with the supervison of an adult!");
                    else
                        LOGGER.info("hardball field is booked without bigGuns!");


                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}
