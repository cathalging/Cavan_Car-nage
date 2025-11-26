package game;

public class Squirrel extends NPC implements canTrade{
    private String itemWantedName;
    private Item itemOffered;

    public Squirrel(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.invincible = true;
    }

    @Override
    public String getDescription() {
        return "A squirrel stands at the base of the tree in front of you. It clutches a drink voucher";
    }

    @Override
    public void onDeath() {
        outputController.addText("The squirrel ran away with the ");
    }

    @Override
    protected int getDialogueOption() {
        return 0;
    }

    @Override
    public String getDialogue() {
        return "Woof Woof";
    }

    @Override
    public String getDeathMessage() {
        return "";
    }

    @Override
    public void onHit(Character character) {
        outputController.addText("Did you just try to hit an animal?");
    }

    @Override
    public void setItemWantedName(String item) {
        this.itemWantedName = item;
    }

    @Override
    public void setItemOffered(Item item) {
        this.itemOffered = item;
    }

    @Override
    public String getItemWantedName() {
        return this.itemWantedName;
    }

    @Override
    public Item getItemOffered() {
        return this.itemOffered;
    }
}
