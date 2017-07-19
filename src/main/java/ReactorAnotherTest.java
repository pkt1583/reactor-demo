import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class ReactorAnotherTest {
    public static void main(String[] args) {
        Flux<String> source = someStringSource();

        source.log()
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {

                        request(3);
                    }

                    @Override
                    protected void hookOnNext(String value) {
                        request(10);
                    }


                });
    }

    private static Flux<String> someStringSource() {
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 30) sink.complete();
                    return state + 1;
                });
        return flux;
    }
}
