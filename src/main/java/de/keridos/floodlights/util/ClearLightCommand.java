package de.keridos.floodlights.util;

import de.keridos.floodlights.handler.lighting.LightHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.List;

/**
 * Created by Keridos on 20.04.2015.
 * This Class
 */
public class ClearLightCommand implements ICommand {
    public ClearLightCommand() {
    }

    @Override
    public String getCommandName() {
        return "fl_clearlights";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "removes all lights in tracker";
    }

    @Override
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] p_71515_2_) {
        if (!sender.getEntityWorld().isRemote) {
            LightHandler.getInstance().removeAllLights();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender.canCommandSenderUseCommand(getRequiredPermissionLevel(sender), this.getCommandName());
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] p_71516_2_) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public int getRequiredPermissionLevel(ICommandSender sender) {
        return 4;
    }
}
