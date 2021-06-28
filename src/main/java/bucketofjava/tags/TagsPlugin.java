package bucketofjava.tags;

import bucketofjava.tags.events.TagsListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Slime;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import bucketofjava.tags.commands.MainPluginCommand;
import bucketofjava.tags.events.block.BlockClick;
import bucketofjava.tags.events.chat.TabComplete;
import bucketofjava.tags.events.inventory.InventoryClick;
import bucketofjava.tags.helpers.Utilities;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import java.io.File;
import java.util.*;

public class TagsPlugin extends JavaPlugin {
    public static HashMap<UUID, String> currentTag=new HashMap<>();
    // console and IO
    private File langFile;
    public static TagsPlugin instance;
    private FileConfiguration langFileConfig;
    private static String permHeader="extratags.tag.";
    // chat messages
    private Map<String, String> phrases = new HashMap<String, String>();

    public static HashMap<UUID, Slime> PlayerTags= new HashMap<>();
    // core settings
    public static String prefix = "&c&l[&5&ltagsPlugin&c&l] &8&l"; // generally unchanged unless otherwise stated in config
    public static String consolePrefix = "[tagsPlugin] ";

    // customizable settings
    public static boolean customSetting = false;


    public static Inventory generateGUI(HashMap<Integer, ItemStack> values, Material defaultMaterial, int size, String title){
        Inventory newGUI=Bukkit.createInventory(null, size, title);
        for(int i=0; i<size; i++){
            if(values.containsKey(i)){
            newGUI.setItem(i, values.get(i));
            }else{
                newGUI.setItem(i, new ItemStack(defaultMaterial, 1));
            }
        }
        return newGUI;

    }


    public static ArrayList<String> getAllTags(Player p){
        ArrayList<String> tags=new ArrayList<String>();
    for(PermissionAttachmentInfo pa:p.getEffectivePermissions()){
        if(pa.getPermission().contains(permHeader)){
            tags.add(pa.getPermission().replaceAll(permHeader, ""));
        }
    }
    return tags;
    }
    public static void setPlayerTag(Player p, String tag){
        if(currentTag.containsKey(p.getUniqueId())){
            currentTag.replace(p.getUniqueId(), tag);
            if (PlayerTags.containsKey(p.getUniqueId())) {
                PlayerTags.get(p.getUniqueId()).remove();
                PlayerTags.remove(p.getUniqueId());
            }


        }else{
            currentTag.put(p.getUniqueId(), tag);
        }
        /*ScoreboardManager sm=Bukkit.getScoreboardManager();
        Scoreboard s=sm.getNewScoreboard();
        Objective o=s.registerNewObjective("tag", "", "dummy");
        o.setDisplayName(p.getName());
        o.setDisplaySlot(DisplaySlot.BELOW_NAME);
        p.setCustomName(tag);
        p.setScoreboard(s);*/
        //Slime as1=(Slime) p.getWorld().spawnEntity(p.getLocation().add(0, 0, 0), EntityType.ARMOR_STAND);
        Slime as=(Slime) p.getWorld().spawnEntity(p.getLocation().add(0, 0.5, 0), EntityType.SLIME);
        as.setSize(2);
        as.setSwimming(false);
        as.setCustomName(tag);
        as.setCustomNameVisible(true);
        as.setInvulnerable(true);
        as.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 40, false, false));
        as.setCollidable(false);
        //as.setGravity(false);
        p.addPassenger(as);


        PlayerTags.remove(p.getUniqueId());
        PlayerTags.put(p.getUniqueId(), as);
        BukkitRunnable br=new BukkitRunnable() {
            @Override
            public void run() {
            p.addPassenger(as);
            }
        };
        br.runTaskTimer(TagsPlugin.instance, 0L, 1L);

    }


    public void onEnable(){
        // load config.yml (generate one if not there)
        loadConfiguration();

        // load language.yml (generate one if not there)
        loadLangFile();

        // register commands and events
        registerCommands();
        registerEvents();
        instance=this;

        // posts confirmation in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been enabled");

        // example scheduled task
        //if (autoPurge){
        //    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
        //        public void run() {
        //            if (debug) Bukkit.getLogger().info("Automatically Purged " + Utilities.purge(shops, consolePrefix, debug, purgeAge) + " empty shops that haven't been active in the past " + purgeAge + " hour(s).");
        //            if (!debug) Utilities.purge(shops, consolePrefix, debug, purgeAge);
        //        }
        //    }, 20 * 60 * 60, 20 * 60 * 60);
        //}
    }

    public void onDisable(){
        // posts exit message in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been disabled");
    }

    private void registerCommands() {
        getCommand("tags").setExecutor(new MainPluginCommand(this));

        // set up tab completion
        getCommand("tags").setTabCompleter(new TabComplete(this));
    }
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BlockClick(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(this), this);
        getServer().getPluginManager().registerEvents(new TagsListener(), this);
    }



    // load the config file and apply settings
    public void loadConfiguration() {
        // prepare config.yml (generate one if not there)
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()){
            Utilities.loadResource(this, "config.yml");
        }
        FileConfiguration config = this.getConfig();

        // general settings
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("plugin-prefix"));

        customSetting = config.getBoolean("custom-setting");
        // put more settings here

        Bukkit.getLogger().info(consolePrefix + "Settings Reloaded from config");
    }

    // load the language file and apply settings
    public void loadLangFile() {

        // load language.yml (generate one if not there)
        langFile = new File(getDataFolder(), "language.yml");
        langFileConfig = new YamlConfiguration();
        if (!langFile.exists()){ Utilities.loadResource(this, "language.yml"); }

        try { langFileConfig.load(langFile); }
        catch (Exception e3) { e3.printStackTrace(); }

        for(String priceString : langFileConfig.getKeys(false)) {
            phrases.put(priceString, langFileConfig.getString(priceString));
        }
    }

    // getters
    public String getPhrase(String key) {
        return phrases.get(key);
    }
    public String getVersion() {
        return getDescription().getVersion();
    }

}
