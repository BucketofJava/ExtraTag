package bucketofjava.tags.events.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import bucketofjava.tags.TagsPlugin;
import bucketofjava.tags.helpers.ActionSound;
import bucketofjava.tags.helpers.Utilities;

public class BlockClick implements Listener {

    private TagsPlugin main = null;
    public BlockClick(TagsPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
      //  Utilities.playSound(ActionSound.ERROR, event.getPlayer());

    }

}