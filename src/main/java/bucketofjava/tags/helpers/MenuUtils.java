package bucketofjava.tags.helpers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import bucketofjava.multiversion.API;
import bucketofjava.multiversion.XMaterial;
import bucketofjava.tags.TagsPlugin;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {

    private TagsPlugin main = null;
    public MenuUtils(TagsPlugin main) { this.main = main; }

    public static void tutorialMenu(Player player) {
        ItemStack book = new ItemStack(XMaterial.WRITTEN_BOOK.parseMaterial());
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setAuthor("BucketofJava");
        meta.setTitle("Welcome to tagsPlugin!");

        List<String> pages = new ArrayList<String>();

        // exmaple main menu
        pages.add(ChatColor.translateAlternateColorCodes('&',
                "      &7&lWelcome to:" + "\n" +
                        "   &c&ltags&5&lPlugin&r" + "\n" +
                        "This guide book will show you everything you need to know about tags! Happy reading!" + "\n" +
                        "" + "\n" +
                        " - BucketofJava" + "\n" +
                        "" + "\n" +
                        "&7&lGo to the next page for info on a second page!"));

        // example secondary page



        meta.setPages(pages);
        book.setItemMeta(meta);

        Utilities.playSound(ActionSound.CLICK, player);
        API.openBook(book, player);
    }

}
