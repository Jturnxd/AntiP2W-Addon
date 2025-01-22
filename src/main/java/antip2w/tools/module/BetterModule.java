package antip2w.tools.module;

import antip2w.tools.AntiP2WTools;
import antip2w.tools.util.MCUtilWrapper;
import meteordevelopment.meteorclient.systems.modules.Module;

public class BetterModule extends Module implements MCUtilWrapper {

    public BetterModule(String name, String description) {
        super(AntiP2WTools.CATEGORY, name, description);
    }

}
