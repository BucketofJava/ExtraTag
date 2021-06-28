package bucketofjava.tags.events;


import bucketofjava.tags.TagsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TagsListener implements Listener {
    public TagsListener(){}
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e){
        if(TagsPlugin.currentTag.containsKey(e.getPlayer().getUniqueId()))
        {
           TagsPlugin.setPlayerTag(e.getPlayer(), TagsPlugin.currentTag.get(e.getPlayer().getUniqueId()));
        }

    }
    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent e){
        if(TagsPlugin.PlayerTags.containsKey(e.getPlayer().getUniqueId())){
            TagsPlugin.PlayerTags.get(e.getPlayer().getUniqueId()).remove();
            TagsPlugin.PlayerTags.remove(e.getPlayer().getUniqueId());}
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(TagsPlugin.PlayerTags.containsKey(e.getPlayer().getUniqueId())){
            //TagsPlugin.PlayerTags.get(e.getPlayer().getUniqueId()).teleport(e.getTo().add(0, 0.5, 0));
        }


    }
}
