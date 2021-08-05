package cope.cosmos.client.manager.managers;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import cope.cosmos.client.features.modules.combat.AutoCrystal;
import cope.cosmos.client.manager.Manager;
import cope.cosmos.util.Wrapper;
import java.util.Random;

public class PresenceManager extends Manager implements Wrapper {

    private static final DiscordRPC discordPresence = DiscordRPC.INSTANCE;
    private static final DiscordRichPresence richPresence = new DiscordRichPresence();
    private static final DiscordEventHandlers presenceHandlers = new DiscordEventHandlers();
    private static Thread presenceThread;
    private static final String[] presenceDetails = new String[] { "Asking linus for config help", "Begging bon to make a new GUI", "Dogging on skids", "Taking the dogs on a walk", "Owning spawn", "Grooming ops", "Biggest player by weight", "Putting on femboy socks", "Sending CPacketDoTroll", "Removing konas from mods folder", "Installing trojan", "RIP CumbiaNarcos", "Playing on 2b2t.org, Packetflying at 630 kmh", "Regearing ...", "Nomming on some corn", "Selling cosmos vouches for discord nitro!", "Watching GrandOlive through his webcam", "Forcing PapaQuill to make packs", "Leaking Spartan\'s alts", "Deleting Lence\'s configs", "Backdooring impurity.me", "Cracking latest konas :yawn:", "Autoduping on eliteanarchy.org", "Tater", "I am a " + (((Double) AutoCrystal.placeDamage.getValue()).doubleValue() < 4.0D ? "faggot." : "good config person :)"), "Injecting estrogen", "Releasing Pyro 1.5", "Stealing Quill\'s pyro account", "Releasing velocity", "Small amounts of tomfoolery", "Stealing future beta", "My game is going sicko mode", "Overdosing on crack cocaine", "Enjoying gondal.club", "Pasting phobos", "Crying and coping", "Sending credentials to webhook", "Dumping cosmos (215/331) classes", "Consuming soy products", "/killing ...", "FontRenderer extends ClassLoader", "Live-Action Role Playing", "Running Bruce client beta", "java.lang.NullPointerException", "Becoming overweight", "Ordering SevJ6 pizzas", "Putting 29 on the bin", "Caught exception from Cosmos", "SSing my address", "Thank you Ionar for SalHack!", "me.ionar.salhack", "candice", "tulips", "981 in queue", "Loading class -> FontRenderer.class", "Trashing 5 auto32k fags", "Packetflying on crystalpvp.cc", "Sending double the packets to do double the damage", "Sending shift packets", "Dueling in vanilla cpvp", "Larping about staircases", "Average cosmos user"};

    public PresenceManager() {
        super("PresenceManager", "Manages the client Discord RPC", 7);
    }

    public static void startPresence() {
        PresenceManager.discordPresence.Discord_Initialize("832656395609440277", PresenceManager.presenceHandlers, true, "");
        PresenceManager.richPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        PresenceManager.discordPresence.Discord_UpdatePresence(PresenceManager.richPresence);
        PresenceManager.presenceThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    PresenceManager.richPresence.largeImageKey = "galaxy";
                    PresenceManager.richPresence.largeImageText = "Cosmos";
                    PresenceManager.richPresence.smallImageKey = "cosmos";
                    PresenceManager.richPresence.smallImageText = "1.0.0";
                    PresenceManager.richPresence.details = PresenceManager.mc.isIntegratedServerRunning() ? "SinglePlayer" : (PresenceManager.mc.getCurrentServerData() != null ? PresenceManager.mc.getCurrentServerData().serverIP.toLowerCase() : "my game is fucked");
                    PresenceManager.richPresence.state = PresenceManager.presenceDetails[(new Random()).nextInt(PresenceManager.presenceDetails.length)];
                    PresenceManager.discordPresence.Discord_UpdatePresence(PresenceManager.richPresence);
                    Thread.sleep(3000L);
                } catch (Exception exception) {
                    ;
                }
            }

        });
        PresenceManager.presenceThread.start();
    }

    public static void interruptPresence() {
        if (PresenceManager.presenceThread != null && !PresenceManager.presenceThread.isInterrupted()) {
            PresenceManager.presenceThread.interrupt();
        }

        PresenceManager.discordPresence.Discord_Shutdown();
        PresenceManager.discordPresence.Discord_ClearPresence();
    }
}
