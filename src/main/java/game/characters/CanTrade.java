package game.characters;

import game.items.Item;

public interface CanTrade {
    public void setItemWantedName(String itemWantedName);
    public void setItemOffered(Item item);

    public String getItemWantedName();
    public Item getItemOffered();
}
