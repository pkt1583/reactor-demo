import org.reactivestreams.Subscription;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.Scanner;
import java.util.stream.IntStream;


public class ReactorTest {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ReactorTest.class);

    public static void main(String[] args) throws InterruptedException {
        ReactorTest test = new ReactorTest();
        Flux<Integer> intergerTill100 = test.createFlux();
        //intergerTill100.log().subscribeOn(Schedulers.parallel()).subscribe(integer -> logger.info("Consumed value {} ", integer));
       /* intergerTill100.log().doOnComplete(() -> logger.info("Completed000")).doOnSubscribe(subscription -> subscription.request(10)).doOnComplete(() -> System.out.println("Completed")).subscribe();*/

        intergerTill100.map(integer -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return integer * 2;
        }).subscribe(new BaseSubscriber<Integer>() {


            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(10);
            }

            @Override
            protected void hookOnNext(Integer value) {
                // int numberOfRequest = 10;
                //request(numberOfRequest);
                /*for (int i = 0; i < numberOfRequest; i++) {
                 //   logger.info("THe value is {}", i);
                }*/
               /* try {
                    Thread.sleep(1000); //some IO processing logic
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/


            }

            @Override
            protected void hookOnComplete() {
                super.hookOnComplete();
                logger.info("Completed requesting elements ");
            }
        });
        System.out.println(" I have completed my task");
        Scanner scanner = new Scanner(System.in);
        scanner.nextInt();
    }

    public Flux<Integer> createFlux() {
        Flux<Integer> flux = Flux.fromStream(IntStream.range(1, 1000).boxed()).log();//.publishOn(Schedulers.parallel());
        Flux<Integer> another = Flux.fromStream(IntStream.range(1001, 9999).boxed()).log();//.publishOn(Schedulers.parallel());
        Flux<Integer> returnFlux = flux.log().mergeWith(another);
        //  returnFlux.publishOn(Schedulers.newParallel("Publisher-"));
        return returnFlux;
    }
}
