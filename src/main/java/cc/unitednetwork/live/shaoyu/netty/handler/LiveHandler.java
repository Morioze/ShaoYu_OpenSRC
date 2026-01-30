package cc.unitednetwork.live.shaoyu.netty.handler;

import rip.jnic.nativeobfuscator.Native;
import cc.unitednetwork.live.shaoyu.LiveClient;
import cc.unitednetwork.live.shaoyu.LiveUser;
import cc.unitednetwork.live.shaoyu.events.*;
import cc.unitednetwork.live.shaoyu.netty.LiveByteBuf;
import cc.unitednetwork.live.shaoyu.netty.LiveProto;
import cc.unitednetwork.live.shaoyu.netty.codec.crypto.AESDecoder;
import cc.unitednetwork.live.shaoyu.netty.codec.crypto.AESEncoder;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jnic.JNICInclude;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;
@Native
@JNICInclude
public class LiveHandler {

}
