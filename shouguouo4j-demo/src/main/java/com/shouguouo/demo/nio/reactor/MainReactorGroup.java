package com.shouguouo.demo.nio.reactor;

import java.nio.channels.ServerSocketChannel;

/**
 * main reactor线程组
 *
 * @author shouguouo
 * @date 2022-05-03 15:14:24
 */
public class MainReactorGroup {

    /**
     * 只支持一个线程
     */
    private final MainReactor[] mainReactors = new MainReactor[1];

    private SubReactorGroup subReactorGroup;

    public void initAndStart(ServerSocketChannel ssc) {
        mainReactors[0] = new MainReactor(ssc, subReactorGroup);
        mainReactors[0].start();
    }

    public void related(SubReactorGroup subReactorGroup) {
        this.subReactorGroup = subReactorGroup;
    }

}
