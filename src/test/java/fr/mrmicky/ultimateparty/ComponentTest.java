package fr.mrmicky.ultimateparty;

import fr.mrmicky.ultimateparty.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    private static final ChatColor RED = ChatColor.of("#ff0a0a");
    private static final ChatColor BLUE = ChatColor.of("#1500ff");

    @Test
    void testHexConversion() {
        assertEquals("Hello World", ChatUtils.colorHex("Hello World"));
        assertEquals("#ff0a0aHello World", ChatUtils.colorHex("#ff0a0aHello World"));
        assertEquals(RED + "Hello World", ChatUtils.colorHex("&#ff0a0aHello World"));
        assertEquals(RED + "Hello" + BLUE + " World", ChatUtils.colorHex("&#ff0a0aHello&#1500ff World"));
        assertEquals(RED + "Hello World" + BLUE, ChatUtils.colorHex("&#ff0a0aHello World&#1500ff"));
        assertEquals(RED.toString(), ChatUtils.colorHex("&#ff0a0a"));
        assertEquals(RED.toString() + BLUE, ChatUtils.colorHex("&#ff0a0a&#1500ff"));
        assertEquals("&#ff000", ChatUtils.colorHex("&#ff000"));
        assertEquals(RED + "Hello World&#1500f", ChatUtils.colorHex("&#ff0a0aHello World&#1500f"));
        assertEquals("#ff0000 Hello", ChatUtils.colorHex("#ff0000 Hello"));
    }
}
