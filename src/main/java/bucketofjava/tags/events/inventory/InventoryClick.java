package bucketofjava.tags.events.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import bucketofjava.tags.TagsPlugin;
import bucketofjava.tags.helpers.Utilities;

import static bucketofjava.tags.helpers.ActionSound.CLICK;

public class InventoryClick implements Listener {

    private TagsPlugin main = null;
    public InventoryClick(TagsPlugin main) {
        this.main = main;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Utilities.playSound(CLICK, (Player)event.getWhoClicked());
    }

}