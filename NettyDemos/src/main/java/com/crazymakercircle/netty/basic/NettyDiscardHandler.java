package com.crazymakercircle.netty.basic;

import com.crazymakercircle.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf in = (ByteBuf) msg;
        try {
            Logger.info("收到消息,丢弃如下:");
            byte[] tempByte = new byte[in.readableBytes()];
            if (in.isReadable())
                in.readBytes(tempByte);
            System.out.println(new String(tempByte, "UTF-8"));
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
