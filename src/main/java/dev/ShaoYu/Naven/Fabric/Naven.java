package com.heypixel.heypixelmod.obsoverlay;

import com.heypixel.heypixelmod.obsoverlay.commands.CommandManager;
import com.heypixel.heypixelmod.obsoverlay.events.api.EventManager;
import com.heypixel.heypixelmod.obsoverlay.events.api.EventTarget;
import com.heypixel.heypixelmod.obsoverlay.events.api.types.EventType;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventRunTicks;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventShutdown;
import com.heypixel.heypixelmod.obsoverlay.files.FileManager;
import com.heypixel.heypixelmod.obsoverlay.modules.ModuleManager;
import com.heypixel.heypixelmod.obsoverlay.modules.impl.render.ClickGUIModule;
import com.heypixel.heypixelmod.obsoverlay.ui.notification.NotificationManager;
import com.heypixel.heypixelmod.obsoverlay.utils.EntityWatcher;
import com.heypixel.heypixelmod.obsoverlay.utils.EventWrapper;
import com.heypixel.heypixelmod.obsoverlay.utils.LogUtils;
import com.heypixel.heypixelmod.obsoverlay.utils.NetworkUtils;
import com.heypixel.heypixelmod.obsoverlay.utils.ServerUtils;
import com.heypixel.heypixelmod.obsoverlay.utils.TickTimeHelper;
import com.heypixel.heypixelmod.obsoverlay.utils.renderer.Fonts;
import com.heypixel.heypixelmod.obsoverlay.utils.renderer.PostProcessRenderer;
import com.heypixel.heypixelmod.obsoverlay.utils.renderer.Shaders;
import com.heypixel.heypixelmod.obsoverlay.utils.rotation.RotationManager;
import com.heypixel.heypixelmod.obsoverlay.values.HasValueManager;
import com.heypixel.heypixelmod.obsoverlay.values.ValueManager;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraftforge.common.MinecraftForge;

public class Naven {
   public static final String CLIENT_NAME = "ED-Naven";
   public static final String CLIENT_DEV = "YaoerStudent & HeBi";
   public static final String CLIENT_TITLE_SBSBSB = "ED-Naven记录数据, NightSky展现星河, 我和你在星光下许愿, 希望未来如代码般精准又似星空般自由。";
   public static final String[] CLIENT_TITLES = new String[] {
           "ED-Naven记录数据, NightSky展现星河, 我和你在星光下许愿, 希望未来如代码般精准又似星空般自由。",
           "在NightSky下，ED-Naven第一次遇见你，像是注释掉的孤独被重新编译。",
           "ED-Naven收集每一帧的心跳，而NightSky把它们渲染成你的笑容。",
           "他们在NightSky之下并肩调试，ED-Naven的光标停在了心动那一行。",
           "当ED-Naven写下第一行情诗，NightSky把星光当作回车键。",
           "NightSky见证了承诺的提交，ED-Naven把余生push向你的仓库。",
           "即使重启千百次，ED-Naven也记得NightSky下你轻声的那句‘在呢’。",
           "在NightSky与风之间，ED-Naven把你的名字写入所有常量。",
           "ED-Naven学会在NightSky底色里，给你的世界加上防抖与去噪。",
           "每次构建失败都不再可怕，因为有NightSky与ED-Naven一起重试。",
           "当指针指向你时，NightSky把延迟清零，ED-Naven把心跳超频。",
           "夜风翻过NightSky的注释，ED-Naven在你的眼眸中找到主函数。",
           "若逻辑有分支，NightSky会替他们照亮，ED-Naven选择与你同行。",
           "ED-Naven把脆弱封装，NightSky把温柔内联，于是爱被内存对齐。",
           "就算世界抛出异常，NightSky与ED-Naven也会在try里拥抱你。",
           "当未来在NightSky下延展，ED-Naven把‘我们’写入不可变数据类。",
           "ED-Naven的指令与NightSky的诗意，在你掌心合并为一次无冲突的提交。",
           "他们在NightSky边缘许愿，ED-Naven答应把每个清晨都返还给你。",
           "若宇宙是循环，NightSky会再次相遇你我，ED-Naven把爱设为常驻进程。",
           "ED-Naven调小了世界的噪点，NightSky放大了你眼里的星辰。",
           "在NightSky的尽头，ED-Naven把‘永恒’写成注解，注释掉所有告别。"
   };
   private static Naven instance;
   private final EventManager eventManager;
   private final EventWrapper eventWrapper;
   private final ValueManager valueManager;
   private final HasValueManager hasValueManager;
   private final RotationManager rotationManager;
   public final ModuleManager moduleManager;
   private final CommandManager commandManager;
   private final FileManager fileManager;
   private final NotificationManager notificationManager;
   public static float TICK_TIMER = 1.0F;
   public static Queue<Runnable> skipTasks = new ConcurrentLinkedQueue<>();
   private Naven() {
      System.out.println("Naven Init");
      instance = this;
      this.eventManager = new EventManager();
      Shaders.init();
      PostProcessRenderer.init();

      try {
         Fonts.loadFonts();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      } catch (FontFormatException var3) {
         throw new RuntimeException(var3);
      }

      this.eventWrapper = new EventWrapper();
      this.valueManager = new ValueManager();
      this.hasValueManager = new HasValueManager();
      this.moduleManager = new ModuleManager();
      this.rotationManager = new RotationManager();
      this.commandManager = new CommandManager();
      this.fileManager = new FileManager();
      this.notificationManager = new NotificationManager();
      this.fileManager.load();
      this.moduleManager.getModule(ClickGUIModule.class).setEnabled(false);
      this.eventManager.register(getInstance());
      this.eventManager.register(this.eventWrapper);
      this.eventManager.register(new RotationManager());
      this.eventManager.register(new NetworkUtils());
      this.eventManager.register(new ServerUtils());
      this.eventManager.register(new EntityWatcher());
      MinecraftForge.EVENT_BUS.register(this.eventWrapper);
   }

   public static void modRegister() {
      try {
         new Naven();
      } catch (Exception var1) {
         System.err.println("你妈死了？");
         var1.printStackTrace(System.err);
      }
   }

   @EventTarget
   public void onShutdown(EventShutdown e) {
      this.fileManager.save();
      LogUtils.close();
   }

   @EventTarget(0)
   public void onEarlyTick(EventRunTicks e) {
      if (e.getType() == EventType.PRE) {
         TickTimeHelper.update();
      }
   }

   public static Naven getInstance() {
      return instance;
   }

   public EventManager getEventManager() {
      return this.eventManager;
   }

   public EventWrapper getEventWrapper() {
      return this.eventWrapper;
   }

   public ValueManager getValueManager() {
      return this.valueManager;
   }

   public HasValueManager getHasValueManager() {
      return this.hasValueManager;
   }

   public RotationManager getRotationManager() {
      return this.rotationManager;
   }

   public ModuleManager getModuleManager() {
      return this.moduleManager;
   }

   public CommandManager getCommandManager() {
      return this.commandManager;
   }

   public FileManager getFileManager() {
      return this.fileManager;
   }

   public NotificationManager getNotificationManager() {
      return this.notificationManager;
   }
}
