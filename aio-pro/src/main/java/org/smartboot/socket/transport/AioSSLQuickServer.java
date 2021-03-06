/*
 * Copyright (c) 2018, org.smartboot. All rights reserved.
 * project name: smart-socket
 * file name: AioSSLQuickServer.java
 * Date: 2018-02-04
 * Author: sandao
 */

package org.smartboot.socket.transport;

import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.extension.ssl.ClientAuth;
import org.smartboot.socket.extension.ssl.SSLConfig;
import org.smartboot.socket.extension.ssl.SSLService;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * AIO服务端
 * Created by 三刀 on 2017/6/28.
 */
public class AioSSLQuickServer<T> extends AioQuickServer<T> {
    private SSLConfig sslConfig = new SSLConfig();

    private SSLService sslService;

    public AioSSLQuickServer() {
    }

    /**
     * @param port             绑定服务端口号
     * @param protocol         协议编解码
     * @param messageProcessor 消息处理器
     */
    public AioSSLQuickServer(int port, Protocol<T> protocol, MessageProcessor<T> messageProcessor) {
        super(port, protocol, messageProcessor);
    }

    /**
     * 打印banner
     *
     * @param out
     */
    private static void printBanner(PrintStream out) {
        out.println(IoServerConfig.BANNER);
        out.println(" :: smart-socket (tls/ssl) ::\t(" + IoServerConfig.VERSION + ")");
    }

    public void start() throws IOException {
        if (config.isBannerEnabled()) {
            printBanner(System.out);
        }
        //启动SSL服务
        sslService = new SSLService(sslConfig);
        start0();
    }

    @Override
    protected void createSession(AsynchronousSocketChannel channel) {
        //连接成功则构造AIOSession对象
        AioSession session = new SSLAioSession<T>(channel, config, aioReadCompletionHandler, aioWriteCompletionHandler, sslService);
        session.initSession();
    }

    public AioSSLQuickServer<T> setKeyStore(String keyStoreFile, String keystorePassword) {
        sslConfig.setKeyFile(keyStoreFile);
        sslConfig.setKeystorePassword(keystorePassword);
        return this;
    }

    public AioSSLQuickServer<T> setKeyPassword(String keyPassword) {
        sslConfig.setKeyPassword(keyPassword);
        return this;
    }

    public AioSSLQuickServer<T> setTrust(String trustFile, String trustPassword) {
        sslConfig.setTrustFile(trustFile);
        sslConfig.setTrustPassword(trustPassword);
        return this;
    }

    public AioSSLQuickServer<T> setClientAuth(ClientAuth clientAuth) {
        sslConfig.setClientAuth(clientAuth);
        return this;
    }

}
