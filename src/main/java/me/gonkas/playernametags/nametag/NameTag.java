package me.gonkas.playernametags.nametag;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Player;

public class NameTag {

    Player owner;
    CaveSpider[] entities = new CaveSpider[3];

    NameTag(Player owner, Component[] nametag) {
        this.owner = owner;

        for (int i=0; i < 3; i++) {
            entities[i] = owner.getWorld().spawn(owner.getLocation(), CaveSpider.class);
            entities[i].customName(nametag[i]);
            entities[i].setCustomNameVisible(true);
            entities[i].setInvisible(true);
            entities[i].setAI(false);

            entities[i].registerAttribute(Attribute.SCALE);
            entities[i].getAttribute(Attribute.SCALE).setBaseValue(switch (i) {case 0 -> 1; case 1 -> 2.4; default -> 3.8;});
        }
    }

    private CaveSpider getMain() {return entities[0];}
    private CaveSpider getTitle() {return entities[1];}
    private CaveSpider getSubtitle() {return entities[2];}

    public Player getOwner() {return owner;}

    public Component getMainText() {return getMain().customName();}
    public Component getTitleText() {return getTitle().customName();}
    public Component getSubtitleText() {return getSubtitle().customName();}

    public void setMainText(Component component) {getMain().customName(component);}
    public void setTitleText(Component component) {getTitle().customName(component);}
    public void setSubtitleText(Component component) {getSubtitle().customName(component);}

    public boolean isMainVisible() {return getMain().isCustomNameVisible();}
    public boolean isTitleVisible() {return getTitle().isCustomNameVisible();}
    public boolean isSubtitleVisible() {return getSubtitle().isCustomNameVisible();}

    public void setMainVisible(boolean value) {getMain().setCustomNameVisible(value && !((TextComponent) getMain().customName()).content().isEmpty());}
    public void setTitleVisible(boolean value) {getTitle().setCustomNameVisible(value && !((TextComponent) getTitle().customName()).content().isEmpty());}
    public void setSubtitleVisible(boolean value) {getSubtitle().setCustomNameVisible(value && !((TextComponent) getSubtitle().customName()).content().isEmpty());}
}
