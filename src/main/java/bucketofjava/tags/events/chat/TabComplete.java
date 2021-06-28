package bucketofjava.tags.events.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import bucketofjava.tags.TagsPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TabComplete implements TabCompleter {

    private TagsPlugin main = null;
    public TabComplete(TagsPlugin main) { this.main = main; }
    // @EventHandler
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        // verify sender is a player
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;

        ArrayList<String> arguments = new ArrayList<>();

        // tab completion for /exchange command
        if (command.getName().equals("tags")) {

            // no arguments
            if (args.length == 1){
                if (player.hasPermission("tags.user")) { arguments.addAll(Arrays.asList("help", "info", "tutorial", "menu")); }
                if (player.hasPermission("tags.admin")) { arguments.addAll(Arrays.asList("reload")); }

                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[0].toLowerCase())) iter.remove(); }
            }
        }

        return arguments;
    }

}
