package com.github.zhaoyue0605.rpc.transport.server;

import com.github.zhaoyue0605.rpc.dto.KuggaRequest;
import com.github.zhaoyue0605.rpc.dto.KuggaResponse;
import com.github.zhaoyue0605.rpc.exception.KuggaException;
import com.github.zhaoyue0605.rpc.serialize.Serializer;
import com.github.zhaoyue0605.rpc.transport.transcoding.RequestDecoder;
import com.github.zhaoyue0605.rpc.transport.transcoding.RequestEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Netty 服务端
 *
 * @author Yue
 * @date 2020/9/10
 */
@Slf4j
public class NettyServer {

    private Serializer serializer;

    public static final int PORT = 18888;

    public NettyServer(Serializer serializer) {
        this.serializer = serializer;
    }

    public void start(RequestHandler requestHandler) {
        String host = getHost();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 当客户端第一次进行请求的时候才会进行初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new RequestEncoder(serializer, KuggaRequest.class));
                            ch.pipeline().addLast(new RequestDecoder(serializer, KuggaResponse.class));
                            ch.pipeline().addLast(new NettyServerHandler(requestHandler));
                        }
                    });
            // 绑定端口，同步等待绑定成功
            ChannelFuture f = b.bind(host, PORT).sync();
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动Netty服务器时发生异常:", e);
        } finally {
            log.error("关闭Netty线程池");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new KuggaException("获取IP地址失败");
        }
    }

}
