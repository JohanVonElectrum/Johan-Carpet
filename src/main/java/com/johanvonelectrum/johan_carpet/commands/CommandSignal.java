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
                        .executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "value"), false))
                        .then(argument("barrel", BoolArgumentType.bool())
                                .executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "value"), BoolArgumentType.getBool(context, "barrel")))
                        )
                );

        dispatcher.register(literalArgumentBuilder);
    }

    private static int execute(ServerCommandSource source, int value, boolean barrel) throws CommandSyntaxException {
        if (!JohanSettings.commandSignal || source == null)
            return 0;

        ItemStack item;
        NbtCompound tags = new NbtCompound();
        if (value <= 3 && !barrel) {
            item = Items.CAULDRON.getDefaultStack();
            NbtCompound tag = new NbtCompound();
            tag.putString("level", String.valueOf(value));
            tags.put("BlockStateTag", tag);
        } else if (value <= 8 && value != 7 && !barrel) {
            item = Items.COMPOSTER.getDefaultStack();
            NbtCompound tag = new NbtCompound();
            tag.putString("level", String.valueOf(value));
            tags.put("BlockStateTag", tag);
        } else {
            item = Items.BARREL.getDefaultStack();
            NbtList itemsTag = new NbtList();

            for (int slot = 0, count = (int) Math.ceil(27 * (value - 1) / 14D); count > 0; slot++, count -= 64) {
                NbtCompound slotTag = new NbtCompound();
                slotTag.putByte("Slot", (byte) slot);
                slotTag.putString("id", Registry.ITEM.getId(Items.WHITE_SHULKER_BOX).toString());
                slotTag.putByte("Count", (byte) Math.min(64, count));
                itemsTag.add(slotTag);
            }

            NbtCompound tag = new NbtCompound();
            tag.put("Items", itemsTag);
            tags.put("BlockEntityTag", tag);
        }

        BaseText text = new LiteralText("Signal: " + value);
        text.setStyle(text.getStyle().withColor(Formatting.RED));
        item.setNbt(tags);
        item.setCustomName(text);
        source.getPlayer().giveItemStack(item);

        return 1;
    }

}
