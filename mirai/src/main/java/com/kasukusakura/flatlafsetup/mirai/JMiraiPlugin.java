package com.kasukusakura.flatlafsetup.mirai;

import com.kasukusakura.flatlafsetup.FlatLafOptionsUI;
import net.mamoe.mirai.console.command.*;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginClasspath;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.util.AnsiMessageBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;

public class JMiraiPlugin extends JavaPlugin {

    public JMiraiPlugin() {
        super(JvmPluginDescription.loadFromResource("com/kasukusakura/flatsetup/mirai/plugin.yml", JMiraiPlugin.class.getClassLoader()));
    }

    @Override
    public void onLoad(PluginComponentStorage $) {
        JvmPluginClasspath jvmPluginClasspath = getJvmPluginClasspath();
        String flatlafVer = "2.6";

        try {
            jvmPluginClasspath.downloadAndAddToPath(
                    jvmPluginClasspath.getPluginSharedLibrariesClassLoader(),
                    Arrays.asList(
                            "com.formdev:flatlaf:" + flatlafVer,
                            "com.formdev:flatlaf-extras:" + flatlafVer,
                            "com.formdev:flatlaf-intellij-themes:" + flatlafVer
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            Class.forName("com.kasukusakura.flatlafsetup.FlatLafSetup")
                    .getMethod("setupLafClasspath")
                    .invoke(null);

            Class.forName("com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme")
                    .getMethod("setup")
                    .invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {


        CommandManager cm = CommandManager.INSTANCE;
        cm.registerCommand(new CMDX(this, "flatlaf"), true);

        getLogger().info(
                AnsiMessageBuilder.create(1024, !ConsoleCommandSender.INSTANCE.isAnsiSupported())
                        .reset()
                        .append("Type `")
                        .lightYellow()
                        .append("/flatlaf")
                        .reset()
                        .append("` in CONSOLE to open settings gui")
                        .toString()
        );
    }

    public static class CMDX extends JSimpleCommand {
        public CMDX(@NotNull CommandOwner owner, @NotNull String primaryName, @NotNull String... secondaryNames) {
            super(owner, primaryName, secondaryNames);
        }

        @SimpleCommand.Handler
        public void listenConsole(SystemCommandSender scs) {
            JFrame jFrame = new JFrame();
            FlatLafOptionsUI.setup(jFrame);
            jFrame.pack();
            jFrame.setLocationRelativeTo(null);
            jFrame.setVisible(true);
            jFrame.pack();
            jFrame.setLocationRelativeTo(null);
            jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }
}
