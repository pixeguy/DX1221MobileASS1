package com.example.sampleapp.Enums;

public enum PowerUpList {
    SHIELD("Shield", "Protects you from one hit.", 500, android.R.drawable.ic_menu_compass),
    SPEED_BOOST("Speed Boost", "Move 20% faster for 10s.", 300, android.R.drawable.ic_menu_directions),
    MAGNET("Magnet", "Attracts nearby coins.", 450, android.R.drawable.ic_menu_mylocation),
    DOUBLE_POINTS("Double XP", "Earn 2x points for 30s.", 600, android.R.drawable.ic_menu_today),
    DOUBLE_POINTS1("Double XP", "Earn 2x points for 30s.", 600, android.R.drawable.ic_menu_today),
    DOUBLE_POINTS2("Double XP", "Earn 2x points for 30s.", 600, android.R.drawable.ic_menu_today),
    DOUBLE_POINTS3("Double XP", "Earn 2x points for 30s.", 600, android.R.drawable.ic_menu_today),
    DOUBLE_POINTS4("Double XP", "Earn 2x points for 30s.", 600, android.R.drawable.ic_menu_today)
    ;

    private final String name;
    private final String description;
    private final int price;
    private final int iconRes;

    PowerUpList(String name, String description, int price, int iconRes) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.iconRes = iconRes;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public int getIconRes() { return iconRes; }
}
