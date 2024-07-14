package org.pythonchik.tableplays;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.joml.AxisAngle4f;
import org.joml.Random;
import org.joml.Vector3f;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class TablePlays extends JavaPlugin implements Listener { //, CommandExecutor
    /**
     * tag -   tableplays:titem   - bool, every item should have this, a proof that this is a plugin item
     */
    NamespacedKey item = new NamespacedKey(this,"Titem");
    /**
     * tag -   tableplays:trotatable   - bool, if it should react to shift click rotation to 180
     */
    NamespacedKey rotatable = new NamespacedKey(this,"Trotatable");
    /**
     * tag -   tableplays:tcard   - bool, if item is a card of any type (poker/uno)
     */
    NamespacedKey card = new NamespacedKey(this,"Tcard");
    /**
     * tag -   tableplays:tboard   - bool, if item is a board
     */
    NamespacedKey board = new NamespacedKey(this,"TBoard");
    /**
     * tag -   tableplays:tbase   - bool, if this items should be a base to a card(to whitch they magnite)
     */
    NamespacedKey base = new NamespacedKey(this,"Tbase");
    /**
     * tag -   tableplays:thcard   - int, Custom model data of card it holds
     */
    NamespacedKey hcard = new NamespacedKey(this,"Thcard");
    /**
     * tag -   tableplays:tbundle   - bool, if item is a bundle of anything
     */
    NamespacedKey bundle = new NamespacedKey(this,"Tbundle");
    /**
     * tag -   tableplays:tbitems   - string, base64 of ArrayList<ItemStack> inside bundle
     */
    NamespacedKey Bitems = new NamespacedKey(this,"TBitems");
    /**
     * tag -   tableplays:tcbundle   - bool, if this is a bundle only for cards
     */
    NamespacedKey Cbundle = new NamespacedKey(this,"TCBundle");
    /**
     * tag -   tableplays:tcheckerb   - bool, if this is a bundle only for checkers
     */
    NamespacedKey checkerB = new NamespacedKey(this,"TCheckerB");
    /**
     * tag -   tableplays:tchessb   - bool, if this is a bundle only for chess pieces
     */
    NamespacedKey chessB = new NamespacedKey(this,"TchessB");
    /**
    / * tag -   tableplays:Tsquare   - bool, if this is a chess board square
    */
    NamespacedKey square = new NamespacedKey(this,"Tsquare");
    /**
     * tag -   tableplays:tubundle   - bool, if this is a checker
     */
    NamespacedKey checker = new NamespacedKey(this,"Tchecker");
    /**
     * tag -   tableplays:robundle   - bool, if bundle should give items in an unknown order
     */
    NamespacedKey RObundle = new NamespacedKey(this,"robundle");
    /**
     * tag -   tableplays:tbiound   - int, amount of items inside a bundle
     */
    NamespacedKey BIcount = new NamespacedKey(this,"TBIcount");
    /**
     * tag -   tableplays:tbmicount   - int, maximum abount of items inside a bundle
     */
    NamespacedKey BIMcount = new NamespacedKey(this,"TBMIcount");
    ///**
    // * tag -   tableplays:tdice   - bool, if item is a dice
    // */
    //NamespacedKey dice = new NamespacedKey(this,"Tdice");
    /**
     * tag -   tableplays:tchessp   - bool, if this is a chess piece
     */
    NamespacedKey chessp = new NamespacedKey(this,"TchessP");
    /**
     * tag -   tableplays:tdomino   - bool, if this is a domino
     */
    NamespacedKey domino = new NamespacedKey(this,"Tdomino");
    /**
     * tag -   tableplays:tdonimob   - bool, if this is a bundle for dominoes
     */
    NamespacedKey dominoB = new NamespacedKey(this,"TdominoB");


    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        RecipeManager.init(this);
        //this.getCommand("gimme").setExecutor(this);
        //this.getCommand("bundle").setExecutor(this);
        //this.getCommand("base64").setExecutor(this);
    }

    @Override
    public void onDisable() {}

    @EventHandler
    public void onItemUse(PlayerStatisticIncrementEvent event){
        if (!(event.getStatistic().equals(Statistic.USE_ITEM) && event.getMaterial() != null && event.getMaterial().equals(Material.WARPED_FUNGUS_ON_A_STICK))){
            return;
        }
        //at this point we know that one of hands have and item, we used, maybe its not a card or bundle tho!
        Player player = event.getPlayer();
        ItemStack handstack = player.getInventory().getItemInMainHand();
        ItemMeta handmeta = handstack.getItemMeta();
        ItemStack offstack = player.getInventory().getItemInOffHand();
        ItemMeta offmeta = offstack.getItemMeta();
        if (handstack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK)
                && handmeta != null
                && handmeta.getPersistentDataContainer().has(item)) {
            //in main we have an item
            if (handmeta.getPersistentDataContainer().has(card)) { // we are holding card
                if (offstack.getType().equals(Material.AIR) || offmeta == null
                        || !offmeta.getPersistentDataContainer().has(bundle)
                        || !offmeta.getPersistentDataContainer().has(BIMcount)
                        || !offmeta.getPersistentDataContainer().has(BIcount)
                        || offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) <= offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)){
                    //in the second hand we are NOT holding a correct bundle
                    Block block = player.getTargetBlockExact((int) player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(), FluidCollisionMode.NEVER);
                    if (block != null) {
                        Vector direction = player.getEyeLocation().getDirection();
                        Location eyeloc = player.getEyeLocation();
                        for (double i = 0; i < player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(); i += 0.002) {
                            eyeloc.add(direction.clone().multiply(0.002));
                            if (eyeloc.getBlock().getType() != Material.AIR) {break;}
                            Location highes = null;
                            boolean bbase = false;
                            for (Entity entity : eyeloc.getWorld().getNearbyEntities(eyeloc, 0.02, 0.02, 0.02)) {
                                if (entity instanceof Interaction) {
                                    if (highes == null) {
                                        highes = entity.getLocation();
                                        if (((ItemDisplay) entity.getPassengers().getFirst()).getItemStack() != null && ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta() != null)
                                            bbase = ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta().getPersistentDataContainer().has(base);
                                    }
                                    if (highes.getY() < entity.getLocation().getY()) {
                                        highes = entity.getLocation();
                                        if (((ItemDisplay) entity.getPassengers().getFirst()).getItemStack() != null && ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta() != null)
                                            bbase = ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta().getPersistentDataContainer().has(base);
                                    }
                                }
                            }
                            if (highes != null) {
                                if (bbase) {
                                    eyeloc.setX(highes.getX());
                                    eyeloc.setZ(highes.getZ());
                                }
                                eyeloc.setY(highes.getY() + 0.002);
                                break;
                            }
                        } //getting eyeloc as pos where you want to place card
                        eyeloc.setY(eyeloc.getY() + 0.005); //moving eyeloc up a bit, to not clip into anything

                        Location display_location = player.getLocation();
                        display_location.setPitch(event.getPlayer().isSneaking() ? 180 : 0);
                        ItemDisplay display = player.getLocation().getWorld().spawn(display_location, ItemDisplay.class);
                        if (handmeta.getPersistentDataContainer().has(hcard)){
                            handmeta.setCustomModelData(handmeta.getPersistentDataContainer().get(hcard, PersistentDataType.INTEGER));
                            handmeta.getPersistentDataContainer().remove(hcard);
                            handstack.setItemMeta(handmeta);
                        }
                        display.setItemStack(handstack);
                        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                        display.setTransformation(new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(0, 0, 0, 0), new Vector3f(1, 1, 1), new AxisAngle4f(0.7853981634f, 0, 0, 0)));

                        Interaction interaction = player.getLocation().getWorld().spawn(eyeloc, Interaction.class);
                        interaction.addPassenger(display);
                        interaction.setInteractionHeight(0.001f);
                        interaction.setInteractionWidth(0.35f);
                        interaction.setRotation(eyeloc.getYaw(), 0);
                        player.decrementStatistic(Statistic.USE_ITEM, Material.WARPED_FUNGUS_ON_A_STICK);

                    }
                }
                else if (offstack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK)
                        && offmeta.getPersistentDataContainer().has(bundle)
                        && offmeta.getPersistentDataContainer().has(Cbundle)
                        && offmeta.getPersistentDataContainer().has(Bitems)
                        && offmeta.getPersistentDataContainer().has(BIMcount)
                        && offmeta.getPersistentDataContainer().has(BIcount)
                        && offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) > offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)) {
                    // main hand - card
                    // offhand good bundle to place card in
                    String sitems = offmeta.getPersistentDataContainer().get(Bitems,PersistentDataType.STRING);
                    ArrayList<ItemStack> items = getItemsFromBase64(sitems);
                    items.add(handstack);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    String sitem = convertItemsToBase64(items);
                    offmeta.getPersistentDataContainer().set(Bitems,PersistentDataType.STRING,sitem);
                    offmeta.getPersistentDataContainer().set(BIcount,PersistentDataType.INTEGER,offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)+1);
                    List<String> lore = offmeta.getLore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                        lore.add("§r§7При использовании выдает случайную карту");
                        lore.add("§r§7Количество карт в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER),offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER)));
                    } else {
                        lore.set(1, "§r§7Количество карт в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                    }
                    offmeta.setLore(lore);
                    offstack.setItemMeta(offmeta);
                } // card in main, good bundle in off, placing card in bundle
                /*
                else if (offstack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK)
                        && handmeta.getPersistentDataContainer().has(ucard)
                        && offmeta.getPersistentDataContainer().has(bundle)
                        && offmeta.getPersistentDataContainer().has(Ubundle)
                        && offmeta.getPersistentDataContainer().has(Bitems)
                        && offmeta.getPersistentDataContainer().has(BIMcount)
                        && offmeta.getPersistentDataContainer().has(BIcount)
                        && offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) > offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)) {
                    String sitems = offmeta.getPersistentDataContainer().get(Bitems, PersistentDataType.STRING);
                    ArrayList<ItemStack> items = getItemsFromBase64(sitems);
                    items.add(handstack);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    String sitem = convertItemsToBase64(items);
                    offmeta.getPersistentDataContainer().set(Bitems, PersistentDataType.STRING, sitem);
                    offmeta.getPersistentDataContainer().set(BIcount, PersistentDataType.INTEGER, offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER) + 1);
                    List<String> lore = offmeta.getLore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                        lore.add("§r§7При использовании выдает случайную карту игры UNO");
                        lore.add("§r§7Количество карт в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                    } else {
                        lore.set(1, "§r§7Количество карт в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                    }
                    offmeta.setLore(lore);
                    offstack.setItemMeta(offmeta);

                }

                 */
            }

            else if (handmeta.getPersistentDataContainer().has(bundle) && handmeta.getPersistentDataContainer().has(Bitems)
                    && handmeta.getPersistentDataContainer().has(BIcount) && handmeta.getPersistentDataContainer().has(BIMcount)) { //we are holding bundle
                Block block = player.getTargetBlockExact((int) player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(), FluidCollisionMode.NEVER);
                if (block == null || !player.isSneaking()) {
                    do_stuff(handstack,handmeta,player);
                } else {
                    if (player.isSneaking()) {
                        Vector direction = player.getEyeLocation().getDirection();
                        Location eyeloc = player.getEyeLocation();
                        for (double i = 0; i < player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(); i += 0.002) {
                            eyeloc.add(direction.clone().multiply(0.002));
                            if (eyeloc.getBlock().getType() != Material.AIR) {break;}
                            Location highes = null;
                            boolean bbase = false;
                            for (Entity entity : eyeloc.getWorld().getNearbyEntities(eyeloc, 0.02, 0.02, 0.02)) {
                                if (entity instanceof Interaction) {
                                    if (highes == null) {
                                        highes = entity.getLocation();
                                        if (((ItemDisplay) entity.getPassengers().getFirst()).getItemStack() != null && ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta() != null)
                                            bbase = ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta().getPersistentDataContainer().has(base);
                                    }
                                    if (highes.getY() < entity.getLocation().getY()) {
                                        highes = entity.getLocation();
                                        if (((ItemDisplay) entity.getPassengers().getFirst()).getItemStack() != null && ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta() != null)
                                            bbase = ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta().getPersistentDataContainer().has(base);
                                    }
                                }
                            }
                            if (highes != null) {
                                if (bbase) {
                                    eyeloc.setX(highes.getX());
                                    eyeloc.setZ(highes.getZ());
                                }
                                eyeloc.setY(highes.getY() + 0.002);
                                break;
                            }
                        } //getting eyeloc as pos where you want to place card
                        eyeloc.setY(eyeloc.getY() + 0.005); //moving eyeloc up a bit, to not clip into anything

                        Location display_location = player.getLocation();
                        display_location.setPitch(90);
                        ItemDisplay display = player.getLocation().getWorld().spawn(display_location, ItemDisplay.class);
                        display.setItemStack(handstack);
                        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                        display.setTransformation(new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(0, 0, 0, 0), new Vector3f(0.6f, 0.6f, 0.6f), new AxisAngle4f(0.7853981634f, 0, 0, 0)));

                        Interaction interaction = player.getLocation().getWorld().spawn(eyeloc, Interaction.class);
                        interaction.addPassenger(display);
                        interaction.setInteractionHeight(0.001f);
                        interaction.setInteractionWidth(0.45f);
                        interaction.setRotation(eyeloc.getYaw(), 0);
                        player.decrementStatistic(Statistic.USE_ITEM, Material.WARPED_FUNGUS_ON_A_STICK);

                    } else {
                        do_stuff(handstack,handmeta,player);
                    }

                }
            }

            /*
            else if (handstack.getItemMeta().getPersistentDataContainer().has(dice)) {
                Block block = player.getTargetBlockExact((int) player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(), FluidCollisionMode.NEVER);
                if (block != null) {
                    Vector direction = player.getEyeLocation().getDirection();
                    Location eyeloc = player.getEyeLocation();
                    for (double i = 0; i < player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(); i += 0.002) {
                        eyeloc.add(direction.clone().multiply(0.002));
                        if (eyeloc.getBlock().getType() != Material.AIR) {break;}
                        Location highes = null;
                        boolean bbase = false;
                        for (Entity entity : eyeloc.getWorld().getNearbyEntities(eyeloc, 0.02, 0.02, 0.02)) {
                            if (entity instanceof Interaction) {
                                if (highes == null) {
                                    highes = entity.getLocation();
                                    if (((ItemDisplay) entity.getPassengers().getFirst()).getItemStack() != null && ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta() != null)
                                        bbase = ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta().getPersistentDataContainer().has(base);
                                }
                                if (highes.getY() < entity.getLocation().getY()) {
                                    highes = entity.getLocation();
                                    if (((ItemDisplay) entity.getPassengers().getFirst()).getItemStack() != null && ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta() != null)
                                        bbase = ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta().getPersistentDataContainer().has(base);
                                }
                            }
                        }
                        if (highes != null) {
                            if (bbase) {
                                eyeloc.setX(highes.getX());
                                eyeloc.setZ(highes.getZ());
                            }
                            eyeloc.setY(highes.getY() + 0.002);
                            break;
                        }
                    } //getting eyeloc as pos where you want to place card
                    eyeloc.setY(eyeloc.getY() + 0.005); //moving eyeloc up a bit, to not clip into anything

                    //eyeloc.setY(eyeloc.clone().getY() + 0.002); //moving eyeloc up a bit, to not clip into anything

                    System.out.println(eyeloc.serialize()); //TODO prints

                    Interaction interaction = player.getLocation().getWorld().spawn(eyeloc.clone(), Interaction.class);

                    //item setup
                    ItemDisplay display = player.getLocation().getWorld().spawn(player.getLocation().clone(), ItemDisplay.class);
                    handmeta.setCustomModelData(33400+new Random().nextInt(6));
                    handstack.setItemMeta(handmeta);
                    display.setItemStack(handstack);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    display.setRotation(display.getLocation().getYaw(),0);
                    System.out.println(interaction.getUniqueId()); //TODO prints
                    System.out.println(display.getUniqueId());

                    display.setTransformation(new Transformation(new Vector3f(0, 0.15f, 0), new AxisAngle4f(0, 0, 0, 0), new Vector3f(0.4f, 0.4f, 0.4f), new AxisAngle4f(0.7853981634f, 0, 0, 0)));
                    interaction.setInteractionHeight(0.3f);
                    interaction.setInteractionWidth(0.3f);
                    interaction.addPassenger(display);
                    player.decrementStatistic(Statistic.USE_ITEM, Material.WARPED_FUNGUS_ON_A_STICK);

                }
            }
            */ //dice
            else if (handmeta.getPersistentDataContainer().has(board)) {
                Block block1 = player.getTargetBlockExact((int) player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(), FluidCollisionMode.NEVER);
                if (block1 != null) {
                    Location NNcarpetL = getMostBottomLeftCarpet(block1);
                    if (NNcarpetL != null) {
                        NNcarpetL.setPitch(0);
                        NNcarpetL.setYaw((Math.round(player.getLocation().getYaw()/90+3)%4)*90);

                        NNcarpetL.setY(NNcarpetL.getY() + 0.005); //moving location up a bit, to not clip into anything



                        //System.out.println(NNcarpetL.serialize()); //TODO prints

                        Interaction interaction = player.getLocation().getWorld().spawn(NNcarpetL.clone(), Interaction.class);

                        //System.out.println(interaction.getUniqueId()); //TODO prints

                        //item setup
                        ItemDisplay display = player.getLocation().getWorld().spawn(NNcarpetL.clone(), ItemDisplay.class);
                        display.setItemStack(handstack);
                        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

                        //System.out.println(display.getUniqueId()); //TODO prints

                        for (int x = 0;x<10;x++){
                            for (int y = 0; y<10;y++) {
                                Interaction subint = NNcarpetL.getWorld().spawn(NNcarpetL, Interaction.class);
                                subint.setInteractionWidth(0.2f);
                                subint.setInteractionHeight(0.01f);
                                subint.getPersistentDataContainer().set(base, PersistentDataType.BOOLEAN, true);
                                subint.getPersistentDataContainer().set(square,PersistentDataType.BOOLEAN,true);
                                subint.teleport(NNcarpetL.clone().add(-0.9+(x*0.2), 0.09, -0.9+(y*0.2)));
                            }
                        }

                        display.setRotation(display.getLocation().getYaw(), 0);
                        display.setTransformation(new Transformation(new Vector3f(0, 0.06f, 0), new AxisAngle4f(0, 0, 0, 0), new Vector3f(1f, 0.3f, 1f), new AxisAngle4f(0, 0, 0, 0)));
                        interaction.setInteractionHeight(0.07f);
                        interaction.setInteractionWidth(2f); //0.2f
                        interaction.addPassenger(display);
                        player.decrementStatistic(Statistic.USE_ITEM, Material.WARPED_FUNGUS_ON_A_STICK);
                    }
                }
            }

            else if (handmeta.getPersistentDataContainer().has(checker) && offmeta != null && offmeta.getPersistentDataContainer().has(checkerB)
                    && offmeta.getPersistentDataContainer().has(bundle) && offmeta.getPersistentDataContainer().has(Bitems)
                    && offmeta.getPersistentDataContainer().has(BIcount) && offmeta.getPersistentDataContainer().has(BIMcount)
                    && offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) > offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)) {

                String sitems = offmeta.getPersistentDataContainer().get(Bitems,PersistentDataType.STRING);
                ArrayList<ItemStack> items = getItemsFromBase64(sitems);
                if (handmeta.getCustomModelData()%2==1) {
                    handmeta.setCustomModelData(handmeta.getCustomModelData()-1);
                    handstack.setItemMeta(handmeta);
                }
                items.add(handstack);
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                String sitem = convertItemsToBase64(items);
                offmeta.getPersistentDataContainer().set(Bitems,PersistentDataType.STRING,sitem);
                offmeta.getPersistentDataContainer().set(BIcount,PersistentDataType.INTEGER,offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)+1);
                List<String> lore = offmeta.getLore();
                if (lore == null) {
                    lore = new ArrayList<>();
                    lore.add("§r§7Шашки можно ставить только на доску");
                    lore.add("§r§7Количество шашек в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER),offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER)));
                } else {
                    lore.set(1, "§r§7Количество шашек в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                }
                offmeta.setLore(lore);
                offstack.setItemMeta(offmeta);

            }

            else if (handmeta.getPersistentDataContainer().has(chessp) && offmeta != null && offmeta.getPersistentDataContainer().has(chessB)
                    && offmeta.getPersistentDataContainer().has(bundle) && offmeta.getPersistentDataContainer().has(Bitems)
                    && offmeta.getPersistentDataContainer().has(BIcount) && offmeta.getPersistentDataContainer().has(BIMcount)
                    && offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) > offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)) {

                String sitems = offmeta.getPersistentDataContainer().get(Bitems,PersistentDataType.STRING);
                ArrayList<ItemStack> items = getItemsFromBase64(sitems);
                items.add(handstack);
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                String sitem = convertItemsToBase64(items);
                offmeta.getPersistentDataContainer().set(Bitems,PersistentDataType.STRING,sitem);
                offmeta.getPersistentDataContainer().set(BIcount,PersistentDataType.INTEGER,offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)+1);
                List<String> lore = offmeta.getLore();
                if (lore == null) {
                    lore = new ArrayList<>();
                    lore.add("§r§7Шахматные фигуры можно ставить только на доску");
                    lore.add("§r§7Количество фигур в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER),offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER)));
                } else {
                    lore.set(1, "§r§7Количество фигур в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                }
                offmeta.setLore(lore);
                offstack.setItemMeta(offmeta);

            }

            if (handmeta.getPersistentDataContainer().has(domino)) { // we are holding domino
                if (offstack.getType().equals(Material.AIR) || offmeta == null
                        || !offmeta.getPersistentDataContainer().has(bundle)
                        || !offmeta.getPersistentDataContainer().has(BIMcount)
                        || !offmeta.getPersistentDataContainer().has(dominoB)
                        || !offmeta.getPersistentDataContainer().has(BIcount)
                        || offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) <= offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)) {
                    //in the second hand we are NOT holding a correct bundle
                    Block block = player.getTargetBlockExact((int) player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(), FluidCollisionMode.NEVER);
                    if (block != null) {
                        Vector direction = player.getEyeLocation().getDirection();
                        Location eyeloc = player.getEyeLocation();
                        for (double i = 0; i < player.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).getBaseValue(); i += 0.002) {
                            eyeloc.add(direction.clone().multiply(0.002));
                            if (eyeloc.getBlock().getType() != Material.AIR) {break;}
                            Location highes = null;
                            boolean bbase = false;
                            for (Entity entity : eyeloc.getWorld().getNearbyEntities(eyeloc, 0.02, 0.02, 0.02)) {
                                if (entity instanceof Interaction) {
                                    if (highes == null) {
                                        highes = entity.getLocation();
                                        if (((ItemDisplay) entity.getPassengers().getFirst()).getItemStack() != null && ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta() != null)
                                            bbase = ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta().getPersistentDataContainer().has(base);
                                    }
                                    if (highes.getY() < entity.getLocation().getY()) {
                                        highes = entity.getLocation();
                                        if (((ItemDisplay) entity.getPassengers().getFirst()).getItemStack() != null && ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta() != null)
                                            bbase = ((ItemDisplay) entity.getPassengers().getFirst()).getItemStack().getItemMeta().getPersistentDataContainer().has(base);
                                    }
                                }
                            }
                            if (highes != null) {
                                if (bbase) {
                                    eyeloc.setX(highes.getX());
                                    eyeloc.setZ(highes.getZ());
                                }
                                eyeloc.setY(highes.getY() + 0.002);
                                break;
                            }
                        } //getting eyeloc as pos where you want to place card
                        eyeloc.setY(eyeloc.getY() + 0.005); //moving eyeloc up a bit, to not clip into anything
                        eyeloc = moveToGridCenter(eyeloc);
                        Location display_location = player.getLocation();
                        display_location.setPitch(0);
                        display_location.setYaw((Math.round(display_location.getYaw()/90)%4)*90);
                        ItemDisplay display = player.getLocation().getWorld().spawn(display_location, ItemDisplay.class);
                        if (handmeta.getPersistentDataContainer().has(hcard)) {
                            handmeta.setCustomModelData(handmeta.getPersistentDataContainer().get(hcard, PersistentDataType.INTEGER));
                            handmeta.getPersistentDataContainer().remove(hcard);
                            handstack.setItemMeta(handmeta);
                        }
                        display.setItemStack(handstack);
                        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                        display.setTransformation(new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(0, 0, 0, 0), new Vector3f(0.5f, 0.5f, 0.5f), new AxisAngle4f(0.7853981634f, 0, 0, 0)));

                        Interaction interaction = player.getLocation().getWorld().spawn(eyeloc, Interaction.class);
                        interaction.addPassenger(display);
                        interaction.setInteractionHeight(0.001f);
                        interaction.setInteractionWidth(0.15f);
                        interaction.setRotation(display_location.getYaw(), 0);
                        player.decrementStatistic(Statistic.USE_ITEM, Material.WARPED_FUNGUS_ON_A_STICK);

                    }
                }
                else if (offstack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK)
                        && offmeta.getPersistentDataContainer().has(bundle)
                        && offmeta.getPersistentDataContainer().has(dominoB)
                        && offmeta.getPersistentDataContainer().has(Bitems)
                        && offmeta.getPersistentDataContainer().has(BIMcount)
                        && offmeta.getPersistentDataContainer().has(BIcount)
                        && offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) > offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)) {
                    // main hand - card
                    // offhand good bundle to place card in
                    String sitems = offmeta.getPersistentDataContainer().get(Bitems,PersistentDataType.STRING);
                    ArrayList<ItemStack> items = getItemsFromBase64(sitems);
                    items.add(handstack);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    String sitem = convertItemsToBase64(items);
                    offmeta.getPersistentDataContainer().set(Bitems,PersistentDataType.STRING,sitem);
                    offmeta.getPersistentDataContainer().set(BIcount,PersistentDataType.INTEGER,offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)+1);
                    List<String> lore = offmeta.getLore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                        lore.add("§r§7Выдает одну случайную домино");
                        lore.add("§r§7Количество домино в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER),offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER)));
                    } else {
                        lore.set(1, "§r§7Количество домино в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                    }
                    offmeta.setLore(lore);
                    offstack.setItemMeta(offmeta);
                } // domino in main, good bundle in off, placing domino in bundle
                /*
                else if (offstack.getType().equals(Material.WARPED_FUNGUS_ON_A_STICK)
                        && handmeta.getPersistentDataContainer().has(ucard)
                        && offmeta.getPersistentDataContainer().has(bundle)
                        && offmeta.getPersistentDataContainer().has(Ubundle)
                        && offmeta.getPersistentDataContainer().has(Bitems)
                        && offmeta.getPersistentDataContainer().has(BIMcount)
                        && offmeta.getPersistentDataContainer().has(BIcount)
                        && offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) > offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)) {
                    String sitems = offmeta.getPersistentDataContainer().get(Bitems, PersistentDataType.STRING);
                    ArrayList<ItemStack> items = getItemsFromBase64(sitems);
                    items.add(handstack);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    String sitem = convertItemsToBase64(items);
                    offmeta.getPersistentDataContainer().set(Bitems, PersistentDataType.STRING, sitem);
                    offmeta.getPersistentDataContainer().set(BIcount, PersistentDataType.INTEGER, offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER) + 1);
                    List<String> lore = offmeta.getLore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                        lore.add("§r§7При использовании выдает случайную карту игры UNO");
                        lore.add("§r§7Количество карт в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                    } else {
                        lore.set(1, "§r§7Количество карт в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                    }
                    offmeta.setLore(lore);
                    offstack.setItemMeta(offmeta);

                }

                 */
            }

        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event){
        if (event.getRightClicked().getType().equals(EntityType.INTERACTION) && (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WARPED_FUNGUS_ON_A_STICK) || event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))) {
            //we are clicked on interaction with warped fungus, of nothing in main hand

            if (event.getRightClicked().getPersistentDataContainer().has(base)
                    && event.getRightClicked().getPersistentDataContainer().has(square)
                    && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WARPED_FUNGUS_ON_A_STICK)
                    && event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null
                    && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(item)
                    && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(checker)){

                if (event.getRightClicked().getPassengers().isEmpty()) {
                    ItemDisplay display = event.getPlayer().getLocation().getWorld().spawn(event.getPlayer().getLocation(), ItemDisplay.class);
                    display.setRotation((Math.round(display.getLocation().getYaw()/90+3)%4)*90, 0);
                    if (event.getPlayer().isSneaking()){
                        ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
                        ItemMeta meta = stack.getItemMeta();
                        if (meta.getCustomModelData()%2==0)meta.setCustomModelData(meta.getCustomModelData()+1);
                        else meta.setCustomModelData(meta.getCustomModelData()-1);
                        event.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
                    }
                    display.setItemStack(event.getPlayer().getInventory().getItemInMainHand());
                    event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    display.setTransformation(new Transformation(new Vector3f(0, 0.14f, 0), new AxisAngle4f(0, 0, 0, 0), new Vector3f(0.5f, 0.3f, 0.5f), new AxisAngle4f(0, 0, 0, 0)));
                    event.getRightClicked().addPassenger(display);
                }
                //onItemUse(new PlayerStatisticIncrementEvent(event.getPlayer(),Statistic.USE_ITEM,event.getPlayer().getStatistic(Statistic.USE_ITEM,Material.WARPED_FUNGUS_ON_A_STICK),event.getPlayer().getStatistic(Statistic.USE_ITEM,Material.WARPED_FUNGUS_ON_A_STICK)+1,Material.WARPED_FUNGUS_ON_A_STICK));
                return;
            }
            else if (event.getRightClicked().getPersistentDataContainer().has(base) && event.getRightClicked().getPersistentDataContainer().has(square) && !event.getRightClicked().getPassengers().isEmpty()) {
                Interaction interaction2 = (Interaction) event.getRightClicked();
                ItemStack stack = ((ItemDisplay) interaction2.getPassengers().getFirst()).getItemStack();
                event.getPlayer().getInventory().addItem(stack);
                interaction2.getPassengers().getFirst().remove();
                return;
            }

            if (event.getRightClicked().getPersistentDataContainer().has(base)
                    && event.getRightClicked().getPersistentDataContainer().has(square)
                    && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WARPED_FUNGUS_ON_A_STICK)
                    && event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null
                    && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(item)
                    && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(chessp)){

                if (event.getRightClicked().getPassengers().isEmpty()) {
                    ItemDisplay display = event.getPlayer().getLocation().getWorld().spawn(event.getPlayer().getLocation(), ItemDisplay.class);
                    display.setRotation((Math.round(display.getLocation().getYaw()/90)%4)*90, 0);
                    display.setItemStack(event.getPlayer().getInventory().getItemInMainHand());
                    event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    display.setTransformation(new Transformation(new Vector3f(0, display.getItemStack().getItemMeta().getCustomModelData() == 33210 || display.getItemStack().getItemMeta().getCustomModelData() == 33211 ? 0.18f :  0.23f, 0), new AxisAngle4f(0, 0, 0, 0), new Vector3f(0.5f, display.getItemStack().getItemMeta().getCustomModelData() == 33210 || display.getItemStack().getItemMeta().getCustomModelData() == 33211 ?  0.4f : 0.5f, 0.5f), new AxisAngle4f(0, 0, 0, 0)));
                    event.getRightClicked().addPassenger(display);
                }
                //onItemUse(new PlayerStatisticIncrementEvent(event.getPlayer(),Statistic.USE_ITEM,event.getPlayer().getStatistic(Statistic.USE_ITEM,Material.WARPED_FUNGUS_ON_A_STICK),event.getPlayer().getStatistic(Statistic.USE_ITEM,Material.WARPED_FUNGUS_ON_A_STICK)+1,Material.WARPED_FUNGUS_ON_A_STICK));
                return;
            }
            else if (event.getRightClicked().getPersistentDataContainer().has(base) && event.getRightClicked().getPersistentDataContainer().has(square) && !event.getRightClicked().getPassengers().isEmpty()) {
                Interaction interaction2 = (Interaction) event.getRightClicked();
                ItemStack stack = ((ItemDisplay) interaction2.getPassengers().getFirst()).getItemStack();
                event.getPlayer().getInventory().addItem(stack);
                interaction2.getPassengers().getFirst().remove();
                return;
            }


            if (!event.getRightClicked().getPassengers().isEmpty() && event.getRightClicked().getPassengers().size() == 1) {
            if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta() != null
                    && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(bundle)
                    && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(Bitems)
                    && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(Cbundle)
                    && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(BIcount)
                    && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(BIMcount)
                    && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) > event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)) {
                Interaction interaction = (Interaction) event.getRightClicked();
                ItemStack stack = ((ItemDisplay) interaction.getPassengers().getFirst()).getItemStack(); //display stack
                ItemMeta offmeta = event.getPlayer().getInventory().getItemInOffHand().getItemMeta();
                String sitems = offmeta.getPersistentDataContainer().get(Bitems,PersistentDataType.STRING);
                ArrayList<ItemStack> items = getItemsFromBase64(sitems);
                items.add(stack);
                interaction.getPassengers().getFirst().remove();
                interaction.remove();
                String sitem = convertItemsToBase64(items);
                offmeta.getPersistentDataContainer().set(Bitems,PersistentDataType.STRING,sitem);
                offmeta.getPersistentDataContainer().set(BIcount,PersistentDataType.INTEGER,offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)+1);
                List<String> lore = offmeta.getLore();
                if (lore == null) {
                    lore = new ArrayList<>();
                    lore.add("§r§7При использовании выдает случайную карту");
                    lore.add("§r§7Количество карт в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER),offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER)));
                } else {
                    lore.set(1, "§r§7Количество карт в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                }
                offmeta.setLore(lore);
                event.getPlayer().getInventory().getItemInOffHand().setItemMeta(offmeta);
                return;
            }

                if (event.getPlayer().getInventory().getItemInOffHand().getItemMeta() != null
                        && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(bundle)
                        && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(Bitems)
                        && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(dominoB)
                        && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(BIcount)
                        && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(BIMcount)
                        && event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER) > event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)) {
                    Interaction interaction = (Interaction) event.getRightClicked();
                    ItemStack stack = ((ItemDisplay) interaction.getPassengers().getFirst()).getItemStack(); //display stack
                    ItemMeta offmeta = event.getPlayer().getInventory().getItemInOffHand().getItemMeta();
                    String sitems = offmeta.getPersistentDataContainer().get(Bitems,PersistentDataType.STRING);
                    ArrayList<ItemStack> items = getItemsFromBase64(sitems);
                    items.add(stack);
                    interaction.getPassengers().getFirst().remove();
                    interaction.remove();
                    String sitem = convertItemsToBase64(items);
                    offmeta.getPersistentDataContainer().set(Bitems,PersistentDataType.STRING,sitem);
                    offmeta.getPersistentDataContainer().set(BIcount,PersistentDataType.INTEGER,offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER)+1);
                    List<String> lore = offmeta.getLore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                        lore.add("§r§7Выдает одну случайную домино");
                        lore.add("§r§7Количество домино в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER),offmeta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER)));
                    } else {
                        lore.set(1, "§r§7Количество домино в мешочке: §6%s/%s".formatted(offmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), offmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
                    }
                    offmeta.setLore(lore);
                    event.getPlayer().getInventory().getItemInOffHand().setItemMeta(offmeta);
                    return;
                }


                //we are not holding correct bundle

            if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WARPED_FUNGUS_ON_A_STICK)
                    && event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null
                    && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(item) //) {
                    && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(card)) {
                onItemUse(new PlayerStatisticIncrementEvent(event.getPlayer(),Statistic.USE_ITEM,event.getPlayer().getStatistic(Statistic.USE_ITEM,Material.WARPED_FUNGUS_ON_A_STICK),event.getPlayer().getStatistic(Statistic.USE_ITEM,Material.WARPED_FUNGUS_ON_A_STICK)+1,Material.WARPED_FUNGUS_ON_A_STICK));
                return;
            }



            if (event.getPlayer().isSneaking()) {
                Interaction interaction = (Interaction) event.getRightClicked();
                ItemDisplay display = ((ItemDisplay) interaction.getPassengers().getFirst());
                if (display.getItemStack() != null && display.getItemStack().getItemMeta() != null) {
                    if (display.getItemStack().getItemMeta().getPersistentDataContainer().has(rotatable) && !display.getItemStack().getItemMeta().getPersistentDataContainer().has(domino)) {
                        Location loc = display.getLocation();
                        loc.setPitch(loc.getPitch() == 0 ? 180 : 0);
                        Transformation transformation = display.getTransformation();
                        transformation.getTranslation().y = -transformation.getTranslation().y;
                        display.setTransformation(transformation);
                        display.teleport(loc);
                        interaction.addPassenger(display);
                        event.setCancelled(true);
                        return;
                    } else if (display.getItemStack().getItemMeta().getPersistentDataContainer().has(domino)) {
                        Location loc = display.getLocation();
                        loc.setYaw(loc.getYaw()+90);
                        display.teleport(loc);
                        interaction.addPassenger(display);
                        return;
                    }
                }
            }
            Interaction interaction2 = (Interaction) event.getRightClicked();
            ItemStack stack = ((ItemDisplay) interaction2.getPassengers().getFirst()).getItemStack();
            if (stack.getItemMeta().getPersistentDataContainer().has(board)){
                for (Entity entity : interaction2.getLocation().getWorld().getNearbyEntities(interaction2.getBoundingBox().expand(0,0.2,0))){
                    if (entity.equals(interaction2)) continue;
                    for (Entity pasenger : entity.getPassengers()) {
                        if (pasenger.getType().equals(EntityType.ITEM_DISPLAY) && ((ItemDisplay) pasenger).getItemStack()!= null){
                            pasenger.getLocation().getWorld().dropItemNaturally(pasenger.getLocation(),((ItemDisplay) pasenger).getItemStack());
                        }
                    }
                    entity.remove();
                }
                event.getPlayer().getInventory().addItem(stack);
                interaction2.remove();
                return;
            }
            event.getPlayer().getInventory().addItem(stack);
            interaction2.getPassengers().getFirst().remove();
            interaction2.remove();

            }
        }
    }

    public void do_stuff(ItemStack handstack, ItemMeta handmeta, Player player){
        String sitems = handmeta.getPersistentDataContainer().get(Bitems, PersistentDataType.STRING);
        ArrayList<ItemStack> items = getItemsFromBase64(sitems);
        if (!items.isEmpty()) {
            ItemStack toadd;
            if (handmeta.getPersistentDataContainer().has(RObundle)) {
                Random random = new Random();
                int ind = random.nextInt(items.size());
                toadd = items.get(ind);
                items.remove(ind);
            } else {
                toadd = items.getFirst();
                items.removeFirst();
            }
            if (handmeta.getPersistentDataContainer().has(Cbundle) && player.isSneaking()) {
                ItemMeta tometa = toadd.getItemMeta();
                if (tometa != null) {
                    if (!tometa.getPersistentDataContainer().has(hcard)) {
                        tometa.getPersistentDataContainer().set(hcard, PersistentDataType.INTEGER, tometa.getCustomModelData());
                        tometa.setCustomModelData(33075); //hidden card
                        toadd.setItemMeta(tometa);
                    }
                }
            }
            if (!player.getInventory().addItem(toadd).equals(new HashMap<>())) {
                player.getWorld().dropItem(player.getLocation(), toadd);
            }
            handmeta.getPersistentDataContainer().set(Bitems, PersistentDataType.STRING, convertItemsToBase64(items));
            handmeta.getPersistentDataContainer().set(BIcount, PersistentDataType.INTEGER, handmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER) - 1);
            List<String> lore = handmeta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
                lore.add("§r§7При использовании выдает случайную карту");
                lore.add("§r§7Количество карт в мешочке: §6%s/%s".formatted(handmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), handmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
            } else {
                lore.set(1, "§r§7Количество карт в мешочке: §6%s/%s".formatted(handmeta.getPersistentDataContainer().get(BIcount, PersistentDataType.INTEGER), handmeta.getPersistentDataContainer().get(BIMcount, PersistentDataType.INTEGER)));
            }
            handmeta.setLore(lore);
            handstack.setItemMeta(handmeta);
        }
    }

    public Location moveToGridCenter(Location location) {
        // Size of each cell in the grid
        double cellSize = 0.025;

        // Get the coordinates within the block (0.0 to 1.0)
        double localX = location.getX() - location.getBlockX();
        double localZ = location.getZ() - location.getBlockZ();

        // Determine the cell indices within the 10x10 grid
        int cellX = (int) (localX / cellSize);
        int cellZ = (int) (localZ / cellSize);

        // Calculate the center of the cell
        double centerX = cellX * cellSize + (cellSize / 2.0);
        double centerZ = cellZ * cellSize + (cellSize / 2.0);

        // Create a new location at the center of the cell
        Location centerLocation = new Location(
                location.getWorld(),
                location.getBlockX() + centerX,
                location.getY(),
                location.getBlockZ() + centerZ
        );

        return centerLocation;
    }

    public static Location getMostBottomLeftCarpet(Block targetBlock) {

        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            return null;
        }

        // Define the offsets for all four quadrants
        int[][][] allOffsets = {
                { {0, 0}, {0, 1}, {1, 0}, {1, 1} },
                { {-1, 0}, {-1, 1}, {0, 0}, {0, 1} },
                { {0, -1}, {0, 0}, {1, -1}, {1, 0} },
                { {-1, -1}, {-1, 0}, {0, -1}, {0, 0} }
        };

        // Iterate over each set of offsets
        for (int[][] offsets : allOffsets) {
            boolean allCarpets = true;
            Block topRightBlock = null;

            for (int[] offset : offsets) {
                Block block = targetBlock.getRelative(offset[0], 0, offset[1]);
                if (!block.getType().toString().contains("CARPET")) {
                    allCarpets = false;
                    break;
                }

                // Determine the most bottom-left block
                if (topRightBlock == null || (block.getX() > topRightBlock.getX()) ||
                        (block.getX() == topRightBlock.getX() && block.getZ() > topRightBlock.getZ())) {
                    topRightBlock = block;
                }
            }

            if (allCarpets && topRightBlock != null) {
                return topRightBlock.getLocation();
            }
        }

        // If no 2x2 area of carpets is found, return null
        return null;
    }

    /*
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("gimme")) {
                Player player = (Player) sender;


                for (int y = 0; y < 28; y++) {
                    ItemStack stack = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
                    ItemMeta meta = stack.getItemMeta();
                    if (meta != null) {
                        meta.getPersistentDataContainer().set(item, PersistentDataType.BOOLEAN, true);
                        meta.getPersistentDataContainer().set(domino, PersistentDataType.BOOLEAN, true);
                        meta.setCustomModelData(33300+y);
                        meta.setDisplayName("§rДомино");
                    }
                    stack.setItemMeta(meta);
                    player.getInventory().addItem(stack);
                    //player.getWorld().dropItemNaturally(player.getLocation(), stack);
                }



            } else if (command.getName().equals("base64")) {
                Player player = (Player) sender;
                ItemStack stack = player.getInventory().getItemInMainHand();
                String items = convertItemsToBase64(new ArrayList<>(List.of(stack)));
                System.out.println(items);
                player.getInventory().addItem(getItemsFromBase64(items).getFirst());
            } else {
                Player player = (Player) sender;
                ItemStack stack = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
                ItemMeta meta = stack.getItemMeta();
                if (meta != null) {
                    meta.getPersistentDataContainer().set(item, PersistentDataType.BOOLEAN, true);
                    meta.getPersistentDataContainer().set(bundle, PersistentDataType.BOOLEAN, true);
                    meta.getPersistentDataContainer().set(dominoB,PersistentDataType.BOOLEAN,true);
                    meta.getPersistentDataContainer().set(Bitems,PersistentDataType.STRING,"");
                    meta.getPersistentDataContainer().set(BIMcount, PersistentDataType.INTEGER, 28);
                    meta.getPersistentDataContainer().set(BIcount, PersistentDataType.INTEGER, 0);
                    meta.getPersistentDataContainer().set(RObundle,PersistentDataType.BOOLEAN,true);
                    meta.setCustomModelData(33101);
                    List<String> lore = new ArrayList<>();
                    lore.add("§r§7Выдает одну случайную домино");
                    lore.add("§r§7Количество домино в мешочке: §6%s/%s".formatted(meta.getPersistentDataContainer().get(BIcount,PersistentDataType.INTEGER),meta.getPersistentDataContainer().get(BIMcount,PersistentDataType.INTEGER)));
                    meta.setLore(lore);
                    meta.setDisplayName("§rМешочек с домино");
                }
                stack.setItemMeta(meta);
                player.getInventory().addItem(stack);
            }
        }
        return true;
    }

     */

    public static ArrayList<ItemStack> getItemsFromBase64(String baseString) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(baseString));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            int size = dataInput.readInt();
            ArrayList<ItemStack> returning_stacks = new ArrayList<>();
            for (int i = 0; i < size;i++){
                returning_stacks.add((ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return returning_stacks;
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }

    public static String convertItemsToBase64(ArrayList<ItemStack> itemStacks) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(itemStacks.size());
            for (ItemStack stack : itemStacks) {
                dataOutput.writeObject(stack);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception ignored) {}
        return "Errore";
    }

}
