package com.lgy.util.event;

import android.util.Log;

import androidx.annotation.NonNull;

import com.jakewharton.rxrelay3.BehaviorRelay;
import com.jakewharton.rxrelay3.PublishRelay;
import com.jakewharton.rxrelay3.Relay;
import com.jakewharton.rxrelay3.ReplayRelay;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 1.注册事件
 *         LGYEventBus.getInstance().register(String.class);
 * 2.监听事件
 *         disposable = LGYEventBus.getInstance().toObservable(String.class, new Consumer<String>() {
 *             @Override
 *             public void accept(String s) throws Throwable {
 *                 Log.d(tag, "accept: "+s);
 *             }
 *         });
 * 3.发送事件
 *         LGYEventBus.getInstance().post(String.class,"test ReplayRelay--message from MainActivity",LGYEventBus.TYPE_REPLAY_RELAY);
 * 4.反注册事件
 * LGYEventBus.getInstance().unRegister(disposable);
 * 或
 * 注销ReplayRelay类型的监听，ReplayRelay监听如果不注销，会收到两次信息
 * LGYEventBus.getInstance().unRegisterReplayRelay(disposable,String.class);
 */
public class LGYEventBus {

    private HashMap<Class<?>, Relay<Object>> mSubjects;
    //只接受订阅后的事件
    public static final byte TYPE_PUBLISH_RELAY = 0;
    //接受订阅前和订阅后的所有事件
    public static final byte TYPE_BEHAVIOR_RELAY = 1;
    //接收订阅之前的最后一个事件和订阅之后的事件
    public static final byte TYPE_REPLAY_RELAY = 2;

    private static byte defaultType = TYPE_PUBLISH_RELAY;
    private static volatile LGYEventBus instance;


    private LGYEventBus() {
        mSubjects = new HashMap<>();
    }

    public static LGYEventBus getInstance() {
        if (instance == null) {
            synchronized (LGYEventBus.class) {
                if (instance == null) {
                    instance = new LGYEventBus();
                }
            }
        }
        return instance;
    }

    private Relay<Object> createBus(byte type) {
        Relay<Object> bus = null;
        if (type == TYPE_PUBLISH_RELAY) {
            bus = PublishRelay.create().toSerialized();
        } else if (type == TYPE_BEHAVIOR_RELAY) {
            bus = BehaviorRelay.create().toSerialized();
        } else if (type == TYPE_REPLAY_RELAY) {
            bus = ReplayRelay.create().toSerialized();
        }
        return bus;
    }


    /**
     * 发送消息
     * @param eventType
     * @param object
     * @param busType
     * @param <T>
     */
    public <T> void post(@NonNull Class<T> eventType, @NonNull T object,byte busType) {
        if (mSubjects.isEmpty()) {
            Relay<Object> bus = createBus(busType);
            mSubjects.put(eventType, bus);
        }
        for (Class<?> key : mSubjects.keySet()) {
            if (key.getName().equals(eventType.getName())) {
                Relay<Object> bus = mSubjects.get(key);
                bus.accept(object);
                break;
            }
        }
    }

    /**
     * 发送消息
     * @param eventType
     * @param object
     * @param <T>
     */
    public <T> void post(@NonNull Class<T> eventType, @NonNull T object) {
        post(eventType,object,defaultType);
    }

    /**
     * 注册TYPE_PUBLISH_RELAY事件
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> register(Class<T> eventType) {
        Relay<Object> bus = null;
        if (mSubjects.containsKey(eventType)) {
            bus = mSubjects.get(eventType);
        } else {
            bus = createBus(defaultType);
            mSubjects.put(eventType, bus);
        }
        return bus.ofType(eventType);
    }

    /**
     * 注册事件
     * @param eventType
     * @param relayType
     * @param <T>
     * @return
     */
    public <T> Observable<T> register(Class<T> eventType, byte relayType) {
        Relay<Object> bus = null;
        if (mSubjects.containsKey(eventType)) {
            bus = mSubjects.get(eventType);
        } else {
            bus = createBus(relayType);
            mSubjects.put(eventType, bus);
        }
        return bus.ofType(eventType);
    }

    public <T> Disposable toObservable(Class<T> eventType, Consumer<T> onNext) {
        return register(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext);
    }

    public <T> Disposable toObservable(
            Class<T> eventType, Scheduler subScheduler, Scheduler obsScheduler, Consumer<T> onNext) {
        return register(eventType).subscribeOn(subScheduler).observeOn(obsScheduler).subscribe(onNext);
    }

    public <T> Disposable toObservable(Class<T> eventType, Consumer<T> onNext, Consumer onError) {
        return register(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    public <T> Disposable toObservable(
            Class<T> eventType,
            Scheduler subScheduler,
            Scheduler obsScheduler,
            Consumer<T> onNext,
            Consumer onError) {
        return register(eventType)
                .subscribeOn(subScheduler)
                .observeOn(obsScheduler)
                .subscribe(onNext, onError);
    }

    public <T> Disposable toObservable(
            Class<T> eventType, Consumer<T> onNext, Consumer onError, Action onComplete) {
        return register(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete);
    }

    public <T> Disposable toObservable(
            Class<T> eventType,
            Scheduler subScheduler,
            Scheduler obsScheduler,
            Consumer<T> onNext,
            Consumer onError,
            Action onComplete) {
        return register(eventType)
                .subscribeOn(subScheduler)
                .observeOn(obsScheduler)
                .subscribe(onNext, onError, onComplete);
    }


    /**
     * 是否有被观察
     * @param type
     * @param <T>
     * @return
     */
    public <T> boolean isObserver(Class<T> type) {
        boolean result = false;
        if (!mSubjects.isEmpty()) {
            for (Class<?> key : mSubjects.keySet()) {
                if (key.getName().equals(type.getName())) {
                    Relay<Object> bus = mSubjects.get(key);
                    if (bus != null) {
                        result =  bus.hasObservers();
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 注销监听
     * @param disposable
     */
    public void unRegister(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 注销ReplayRelay类型的监听，ReplayRelay监听如果不注销，会收到两次信息
     * @param disposable
     */
    public void unRegisterReplayRelay(Disposable disposable,Class<?> eventType) {
        Log.e("LGY","isObserver:"+isObserver(eventType));
        if (mSubjects != null) {
            mSubjects.remove(eventType);
        }
        unRegister(disposable);
    }
}
