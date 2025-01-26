package antip2w.tools;

import antip2w.tools.command.*;
import antip2w.tools.module.*;
import antip2w.tools.util.Util;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntiP2WTools extends MeteorAddon {

    public static final Category CATEGORY = new Category("AntiP2W", Items.BARRIER.getDefaultStack());
    public static final Logger LOGGER = LoggerFactory.getLogger("AntiP2W");
    public static final boolean IS_DEV_ENV = Boolean.parseBoolean(System.getProperty("fabric.development", "false"));
    public static final ModMetadata METADATA = FabricLoader.getInstance().getModContainer("antip2w").orElseThrow().getMetadata();

    public static final String COMMIT = Util.make(() -> {
        String commit = METADATA.getCustomValue("commit-hash").getAsString();
        return commit.equals("unknown") ? null : commit;
    });

    public static final String BUILD_TIME = METADATA.getCustomValue("build-time").getAsString();

    @Override
    public void onInitialize() {
        addCommands();
        addModules();
    }

    private void addCommands() {
        Commands.add(new Hologram());
        Commands.add(new LoverfellaDupe());
        Commands.add(new OffhandCrash());
        Commands.add(new PurpurCrash());
        Commands.add(new Reconnect());
    }

    private void addModules() {
        Modules modules = Modules.get();

        modules.add(new AntiExploit());
        modules.add(new AutoAuth());
        modules.add(new BetterMacros());
        modules.add(new BetterToasts());
        modules.add(new BookColors());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "antip2w.tools";
    }

    private static final GithubRepo REPO = COMMIT == null ? null : new GithubRepo("AntiP2WDevelopment", "AntiP2W-Addon", "v2", null);

    @Override
    public GithubRepo getRepo() {
        return REPO;
    }

    @Override
    public String getWebsite() {
        return METADATA.getContact().get("homepage").orElseThrow();
    }

    @Override
    public String getCommit() {
        return COMMIT;
    }
}
