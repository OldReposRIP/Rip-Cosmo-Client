package cope.cosmos.client.features.modules.client;

import cope.cosmos.client.Cosmos;
import cope.cosmos.client.clickgui.cosmos.window.Window;
import cope.cosmos.client.clickgui.cosmos.window.WindowManager;
import cope.cosmos.client.clickgui.cosmos.window.windows.CategoryWindow;
import cope.cosmos.client.features.modules.Category;
import cope.cosmos.client.features.modules.Module;
import cope.cosmos.client.features.setting.Setting;
import java.awt.Color;
import java.util.function.Consumer;

public class ClickGUI extends Module {

    public static ClickGUI INSTANCE;
    public static Setting primaryColor = new Setting("PrimaryColor", "The primary color for the GUI", new Color(154, 81, 200, 255));
    public static Setting backgroundColor = new Setting("BackgroundColor", "The background color for the GUI", new Color(23, 23, 29, 255));
    public static Setting accentColor = new Setting("AccentColor", "The accent color for the GUI", new Color(35, 35, 45, 255));
    public static Setting secondaryColor = new Setting("SecondaryColor", "The secondary color for the GUI", new Color(12, 12, 17, 255));
    public static Setting complexionColor = new Setting("ComplexionColor", "The complexion color for the GUI", new Color(18, 18, 24, 255));
    public static Setting pauseGame = new Setting("PauseGame", "Pause the game when in GUI", Boolean.valueOf(false));

    public ClickGUI() {
        super("ClickGUI", Category.CLIENT, "This screen.");
        this.setKey(54);
        this.setExempt(true);
        ClickGUI.INSTANCE = this;
    }

    public void onEnable() {
        super.onEnable();
        ClickGUI.mc.displayGuiScreen(Cosmos.INSTANCE.getCosmosGUI());
        WindowManager.getWindows().forEach((window) -> {
            ((CategoryWindow) window).getAnimation().setState(true);
            ((CategoryWindow) window).setOpen(true);
        });
    }

    public Color getPrimaryColor() {
        return (Color) ClickGUI.primaryColor.getValue();
    }

    public Color getBackgroundColor() {
        return (Color) ClickGUI.backgroundColor.getValue();
    }

    public Color getAccentColor() {
        return (Color) ClickGUI.accentColor.getValue();
    }

    public Color getSecondaryColor() {
        return (Color) ClickGUI.secondaryColor.getValue();
    }

    public Color getComplexionColor() {
        return (Color) ClickGUI.complexionColor.getValue();
    }
}
