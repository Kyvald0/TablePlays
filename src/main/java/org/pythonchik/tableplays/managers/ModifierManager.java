package org.pythonchik.tableplays.managers;

import org.pythonchik.tableplays.managers.modifiers.*;

import java.util.*;
import java.util.regex.Pattern;

public class ModifierManager {

    // map of Regex to class, implementing BaseModifier.
    private static final Map<Pattern, BaseModifier> MODIFIER_MAP = new LinkedHashMap<>();

    static {
        MODIFIER_MAP.put(Pattern.compile("RESETCMD"), new ResetCMDModifier());
        MODIFIER_MAP.put(Pattern.compile("RCMDP[123456789]\\d*"), new RandomCMDModifier());
        MODIFIER_MAP.put(Pattern.compile("ALIGN"), new AlignModifier());
        MODIFIER_MAP.put(Pattern.compile("FLIP"), new FlipModifier());
    }

    public static void applyModifiers(ModifierContext context, List<String> modifiers) {
        // Iterate through all modifiers
        Iterator<String> iterator = modifiers.iterator();
        while (iterator.hasNext()) {
            String mod = iterator.next();

            // Check against all regex patterns in MODIFIER_MAP
            for (Pattern pattern : MODIFIER_MAP.keySet()) {
                if (pattern.matcher(mod).matches()) {
                    BaseModifier modifier = MODIFIER_MAP.get(pattern);
                    boolean result = modifier.apply(context, mod, modifiers);
                    if (result) iterator.remove();
                    break;
                }
            }
        }
    }
}
