package bucketofjava.tags;


import bucketofjava.tags.helpers.ItemSmith;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class TagGUI implements Listener {
    public ArrayList<Inventory> allInventories=new ArrayList<>();
    public Player pl;
    public TagGUI(Player p){
        this.pl=p;
        this.generateGUIs(this.pl);
        Bukkit.getPluginManager().registerEvents(this, TagsPlugin.instance);

    }

    public void openGUI(Player p){
        if(TagsPlugin.getAllTags(p).size()==0){
            p.sendMessage(ChatColor.RED+"You don't have any tags!");
            return;
        }
    p.openInventory(this.allInventories.get(0));
    }
    public void generateGUIs(Player p){

        for(int i=0; i<((int)TagsPlugin.getAllTags(p).size()/12)+1; i++){
            HashMap<Integer, ItemStack> items=new HashMap<>();
            if(i>=TagsPlugin.getAllTags(p).size()-1){

                for(int v=0; v<TagsPlugin.getAllTags(p).size()%12; v++){
                    items.put((1+(2*v))+(18*((int)((float) v)/4)), new ItemSmith(Material.NAME_TAG, TagsPlugin.getAllTags(p).get((i*12)+v), new String[]{}));
                }
            }else{

                for(int v=0; v<12; v++){
                items.put((1+(2*v))+(18*((int)((float) v)/4)), new ItemSmith(Material.NAME_TAG, TagsPlugin.getAllTags(p).get((i*12)+v), new String[]{}));
                }


            }
            items.put(45, new ItemSmith(Material.ARROW, ChatColor.GREEN+"Previous Page", new String[]{}));
            items.put(53, new ItemSmith(Material.ARROW, ChatColor.GREEN+"Next Page", new String[]{}));
            allInventories.add(TagsPlugin.generateGUI(items, Material.AIR, 54, "ExtraTags Page "+(i+1)));

        }
    }
    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e){
        if(allInventories.contains(e.getClickedInventory())){
            if(e.getCurrentItem()!=null && e.getCurrentItem().getType()!=Material.AIR){
                if(e.getCurrentItem().getType().equals(Material.ARROW)&&e.getCurrentItem().getItemMeta().hasDisplayName()){
                    if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Next")){
                        e.getView().close();
                        e.getWhoClicked().openInventory(allInventories.get(allInventories.indexOf(e.getClickedInventory())%allInventories.size()));
                    }else if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Previous")){
                        e.getWhoClicked().openInventory(allInventories.get(((allInventories.indexOf(e.getClickedInventory())+allInventories.size()-1)%allInventories.size())));
                    }

                }else if(e.getCurrentItem().getType().equals(Material.NAME_TAG)){
                    TagsPlugin.setPlayerTag(((Player)e.getWhoClicked()), ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    Bukkit.broadcastMessage(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    e.getView().close();

                }
            }
        }
    }


}
