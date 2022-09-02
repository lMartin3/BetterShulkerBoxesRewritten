package dev.martinl.bsbrewritten.configuration.types;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.util.GradientUtil;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class ConfigMessage {
    private final String content;

    public String get() {
        return GradientUtil.colorify(content);
    }

    public String getRaw() {
        return content;
    }

    public void send(CommandSender sender, String... placeholders) {
        send(sender, this, true, placeholders);
    }
    public void send(CommandSender sender, boolean prefix, String... placeholders) {
        send(sender, this, prefix, placeholders);
    }

    public static void send(CommandSender sender, ConfigMessage message, boolean prefix, String... placeholders) {
        if(message==null||message.getRaw()==null||message.getRaw().isEmpty()) return;
        String content = (prefix ? BSBRewritten.getInstance().getBSBConfig().getPrefix().getRaw() : "") + message.getRaw();
        for(int i=0;i+1<placeholders.length;i=2) {
            content = content.replace(placeholders[i], placeholders[i+1]);
        }
        sender.sendMessage(GradientUtil.colorify(content));
    }
}
