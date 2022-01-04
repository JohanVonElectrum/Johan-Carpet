package com.johanvonelectrum.johan_carpet.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandSignal {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = literal("signal")
                .requires((player) -> player.hasPermissionLevel(2) && JohanSettings.commandSignal)
                .then(argument("value", IntegerArgumentType.integer(1, 897))
                        .executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "value")))
                );

        dispatcher.register(literalArgumentBuilder);
    }

    private static int execute(ServerCommandSource source, int signal) throws CommandSyntaxException {
        if (!JohanSettings.commandSignal || source == null)
            return 0;

        ItemStack item = Items.BARREL.getDefaultStack();
        NbtCompound tags = new NbtCompound();
        NbtList itemsTag = new NbtList();

        for (int slot = 0, count = fromSignal((byte) 27, (byte) 1, signal); count > 0; slot++, count -= 64) {
            NbtCompound slotTag = new NbtCompound();
            slotTag.putByte("Slot", (byte) slot);
            slotTag.putString("id", Registry.ITEM.getId(Items.WHITE_SHULKER_BOX).toString());
            slotTag.putByte("Count", (byte) Math.min(64, count));
            itemsTag.add(slotTag);
        }

        NbtCompound tag = new NbtCompound();
        tag.put("Items", itemsTag);
        tags.put("BlockEntityTag", tag);

        BaseText text = new LiteralText("Signal: " + signal);
        text.setStyle(text.getStyle().withColor(Formatting.RED));
        item.setNbt(tags);
        item.setCustomName(text);
        source.getPlayer().giveItemStack(item);

        System.out.println(item.getNbt());

        return 1;
    }

    private static int signalOf(byte slots, byte stackSize, int items) {
        return 14 * items / (slots * stackSize) + (items > 0 ? 1 : 0);
    }

    private static int fromSignal(byte slots, byte stackSize, int signal) {
        int items = (int) Math.ceil((signal - (signal > 0 ? 1 : 0)) * (27 / 14D));
        for (;signalOf(slots, stackSize, items) < signal;items++);
        return items;
    }

}
