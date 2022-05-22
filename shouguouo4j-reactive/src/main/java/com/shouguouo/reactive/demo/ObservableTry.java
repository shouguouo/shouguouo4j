package com.shouguouo.reactive.demo;

import com.shouguouo.common.util.OutputUtils;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

import java.util.concurrent.TimeUnit;

/**
 * @author shouguouo
 * @date 2022-05-18 21:45:19
 */
public class ObservableTry {

    public static void main(String[] args) throws InterruptedException {
        Disposable disposable =
                // 被观察者
                Flowable.just("On", "Off", "On", "On")
                        // delay 1s
                        .delay(1, TimeUnit.SECONDS)
                        .subscribeWith(
                                new DisposableSubscriber<String>() {
                                    // 观察者
                                    @Override
                                    public void onNext(String s) {
                                        System.out.println("onNext: " + s);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        System.err.println(throwable.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        System.out.println("结束观察");
                                    }
                                }
                        );
        TimeUnit.SECONDS.sleep(1);
        disposable.dispose();
        // 1. Observable.just()
        // 2. Observable.fromXXX()
        // 3. Observable.range(int start, int count)
        // 4. Observable.empty()
        // 5. Observable.never() test时比较有用
        // 6. Observable.error()
        OutputUtils.cuttingLine("tryMultipleSubscribe");
        tryMultipleSubscribe();
        OutputUtils.cuttingLine("tryMultipleSubscribeCached");
        tryMultipleSubscribeCached();
    }

    private static void tryMultipleSubscribe() {
        Observable<Integer> ints =
                Observable.create(subscriber -> {
                    OutputUtils.println("Create");
                    subscriber.onNext(42);
                    subscriber.onComplete();
                });
        OutputUtils.println("Starting");
        ints.subscribe(i -> OutputUtils.println("Element A: " + i));
        ints.subscribe(i -> OutputUtils.println("Element B: " + i));
        OutputUtils.println("Exit");
    }

    private static void tryMultipleSubscribeCached() {
        Observable<Integer> ints =
                Observable.<Integer>create(subscriber -> {
                    OutputUtils.println("Create");
                    subscriber.onNext(42);
                    subscriber.onComplete();
                })
                        .cache();
        OutputUtils.println("Starting");
        ints.subscribe(i -> OutputUtils.println("Element A: " + i));
        ints.subscribe(i -> OutputUtils.println("Element B: " + i));
        OutputUtils.println("Exit");
    }

    public static <T> Observable<T> just(T t) {
        return Observable.create(subscriber -> {
            subscriber.onNext(t);
            subscriber.onComplete();
        });
    }

    public static <T> Observable<T> empty() {
        return Observable.create(Emitter::onComplete);
    }

    public static <T> Observable<T> never() {
        return Observable.create(subscriber -> {
        });
    }

    public static <T> Observable<T> error(Throwable t) {
        return Observable.create(subscriber -> {
            subscriber.onError(t);
        });
    }

}
