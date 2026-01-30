package cc.unitednetwork.live.shaoyu;

import rip.jnic.nativeobfuscator.Native;
import cc.unitednetwork.live.shaoyu.events.EventLiveConnectionStatus;
import cc.unitednetwork.live.shaoyu.netty.LiveProto;
import cc.unitednetwork.live.shaoyu.netty.codec.FrameDecoder;
import cc.unitednetwork.live.shaoyu.netty.codec.FrameEncoder;
import cc.unitednetwork.live.shaoyu.netty.codec.crypto.RSADecoder;
import cc.unitednetwork.live.shaoyu.netty.codec.crypto.RSAEncoder;

import dev.ShaoYu.Naven.Fabric.events.api.EventManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import oshi.SystemInfo;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Native
public class LiveClient {
    public static LiveClient INSTANCE;

    private final NioEventLoopGroup workerGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("Live-Worker"));
    private final LiveReconnectionThread reconnectionThread = new LiveReconnectionThread();
    private final AtomicBoolean isConnecting = new AtomicBoolean();
    private final EventManager eventManager = new EventManager();
    private final HashMap<UUID, LiveUser> liveUserMap = new HashMap<>();
//    private final RSAPrivateKey rsaPrivateKey;
//    private final RSAPublicKey rsaPublicKey;
//    private final String hardwareId;

    public int serversideProtocolVersion = LiveProto.PROTOCOL_VERSION;
    public String autoUsername;
    public String autoPassword;
    public LiveUser liveUser;

    private Channel channel;

    public LiveClient() {

    }

    public void connect() {

    }
    public void check() {

    }
    public void sendPacket(LiveProto.LivePacket packet) {

    }

    public void shutdown() {

    }

    public void startReconnectionThread() {
//        reconnectionThread.start();
    }

    public void stopReconnectionThread() {

    }

    public String getHardwareId() {
        return "1";
    }

    public boolean isConnecting() {
        return isConnecting.get();
    }

    public boolean isOpen() {
        return channel != null && channel.isOpen();
    }

    public boolean isActive() {
        return channel != null && channel.isActive();
    }

    public HashMap<UUID, LiveUser> getLiveUserMap() {
        return liveUserMap;
    }

    public EventManager getEventManager() {
        return eventManager;
    }


}
