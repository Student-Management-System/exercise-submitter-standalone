package net.ssehub.teaching.exercise_submitter.standalone.themes;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * This class handles the different Themes.
 * 
 * @author lukas
 *
 */
public class ThemeManager {

    /**
     * Enum for the different Themes.
     * 
     * @author lukas
     *
     */
    public enum Theme {
        LIGHT, INTELLIJ, DARK, DRACULA,
    }
    /**
     * Returns the current selected Theme or if no selected the Default.
     * @return {@link LookAndFeel}
     */
    public static LookAndFeel getSavedTheme() {
        LookAndFeel look = null;
        Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
        String name = prefs.get("themename", null);

        if (name != null) {
            for (Theme theme : Theme.values()) {
                if (theme.toString().equals(name)) {
                    look = getThemeMap().get(theme);
                }
            }

        } else {
            look = new FlatLightLaf();
        }

        return look;

    }
    /**
     * Changes the Theme.
     * 
     * @param theme
     */
    public static void changeTheme(Theme theme) {
        Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
        prefs.put("themename", theme.toString());
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, 
                            "Program needs to restart to apply changes", 
                            "Warning!" , JOptionPane.WARNING_MESSAGE));
    }
    /**
     * Gets the map of themes.
     * 
     * @return {@link Map}.
     */
    private static Map<Theme, LookAndFeel> getThemeMap() {
        Map<Theme, LookAndFeel> map = new HashMap<Theme, LookAndFeel>();
        map.put(Theme.LIGHT, new FlatLightLaf());
        map.put(Theme.DARK, new FlatDarkLaf());
        map.put(Theme.DRACULA, new FlatDarculaLaf());
        map.put(Theme.INTELLIJ, new FlatIntelliJLaf());
        return map;
    }

}
