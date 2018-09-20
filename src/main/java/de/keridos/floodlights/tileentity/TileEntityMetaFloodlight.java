package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.core.NetworkDataList;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.MathUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.keridos.floodlights.block.BlockPhantomLight.UPDATE;
import static de.keridos.floodlights.util.GeneralUtil.getPosFromPosFacing;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 06.05.2015.
 * This Class
 */
@SuppressWarnings({"WeakerAccess", "NullableProblems"})
public abstract class TileEntityMetaFloodlight extends TileEntityFL implements ITickable {

    protected boolean active;
    protected boolean wasActive;
    protected boolean hasRedstone;
    protected int timeout;
    protected ItemStackHandler inventory;

    protected Block lightBlock = ModBlocks.blockPhantomLight;
    protected int rangeStraight = ConfigHandler.rangeStraightFloodlight;
    protected int rangeCone = ConfigHandler.rangeConeFloodlight;
    protected int currentRange = rangeStraight;
    protected int lightBlockStep = 2;
    protected int floodlightId = new Random().nextInt();

    private AtomicBoolean executorActive = new AtomicBoolean(false);
    private boolean hasLight;

    public TileEntityMetaFloodlight() {
        super();

        inventory = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }
        };
    }

    public void setHasRedstoneSignal(boolean hasSignal) {
        // Light blocks are managed in the update() method
        hasRedstone = hasSignal;
        active = hasRedstone ^ inverted;
        syncData();
    }

    public void toggleInverted() {
        inverted = !inverted;
        active = hasRedstone ^ inverted;
        syncData();
    }

    /**
     * Notifies this machine that a container block has been removed and the tile entity is about to be destroyed.
     */
    public void notifyBlockRemoved() {
        if (world.isRemote)
            return;

        lightSource(true);
    }

    /**
     * Creates new phantom light (depending on the {@link #lightBlock} field) at given position.
     */
    @SuppressWarnings("ConstantConditions")
    protected void createPhantomLight(BlockPos pos) {
        if (world.setBlockState(pos, lightBlock.getDefaultState(), 19)) {
            TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(pos);
            light.addSource(this.pos, floodlightId);
        }
    }

    @Override
    public void update() {
        if (world.isRemote)
            return;

        if (timeout > 0) {
            timeout--;
            return;
        }

        // Energy itself is handled in one of the derived classes
        if (active && hasEnergy() && (wasActive != active || !hasLight)) {
            // Spawn phantom lights
            lightSource(false);
            wasActive = active;
        } else if ((active && !hasEnergy()) || (!active && wasActive)) {
            // A floodlight just run out of energy or was shut down - deactivate it
            lightSource(true);
            timeout = ConfigHandler.timeoutFloodlights + 10;
            wasActive = false;
        }
    }

    /**
     * Returns whether this machine is ready - no active timeouts etc.
     */
    protected boolean isCooledDown() {
        return timeout == 0 && !executorActive.get();
    }

    /**
     * Whether this machine has electric, heat or any other energy type required to operate.
     */
    protected boolean hasEnergy() {
        return false;
    }

    /**
     * Whether this machine can perform any task at the moment. Used in the {@link #update()} method.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean isReady() {
        return timeout == 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.WAS_ACTIVE))
            wasActive = nbtTagCompound.getBoolean(Names.NBT.WAS_ACTIVE);
        if (nbtTagCompound.hasKey(Names.NBT.LIGHT))
            hasLight = nbtTagCompound.getBoolean(Names.NBT.LIGHT);
        if (nbtTagCompound.hasKey(Names.NBT.HAS_REDSTONE))
            setHasRedstoneSignal(nbtTagCompound.getBoolean(Names.NBT.HAS_REDSTONE));
        if (nbtTagCompound.hasKey(Names.NBT.ITEMS))
            inventory.deserializeNBT(nbtTagCompound.getCompoundTag(Names.NBT.ITEMS));
        if (nbtTagCompound.hasKey(Names.NBT.CURRENT_RANGE))
            currentRange = nbtTagCompound.getInteger(Names.NBT.CURRENT_RANGE);
        if (nbtTagCompound.hasKey(Names.NBT.FLOODLIGHT_ID))
            floodlightId = nbtTagCompound.getInteger(Names.NBT.FLOODLIGHT_ID);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(Names.NBT.WAS_ACTIVE, wasActive);
        nbtTagCompound.setBoolean(Names.NBT.LIGHT, hasLight);
        nbtTagCompound.setBoolean(Names.NBT.HAS_REDSTONE, hasRedstone);
        nbtTagCompound.setTag(Names.NBT.ITEMS, inventory.serializeNBT());
        nbtTagCompound.setInteger(Names.NBT.CURRENT_RANGE, currentRange);
        nbtTagCompound.setInteger(Names.NBT.FLOODLIGHT_ID, floodlightId);
        return nbtTagCompound;
    }

    @Override
    public NetworkDataList getSyncData(@Nonnull NetworkDataList data) {
        super.getSyncData(data);
        data.add(active);
        data.add(hasLight);
        return data;
    }

    @Override
    public void applySyncData(ByteBuf buffer) {
        super.applySyncData(buffer);
        active = buffer.readBoolean();
        hasLight = buffer.readBoolean();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory : super.getCapability(capability, facing);
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @SuppressWarnings("ConstantConditions")

    private void lightSource(boolean remove) {
        lightSource(remove, true);
    }

    private void lightSource(boolean remove, boolean wait) {
        if (hasLight == !remove)
            return;

        if (executorActive.get() && wait) {
            // Executor haven't finished executing "!remove" action yet - wait for it to finish.
            new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    if (executorActive.get()) {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else
                        lightSource(remove, false);
                }
            }).start();
        }

        if (mode >= 0 && mode <= 2) {
            hasLight = !remove;
            // Update block appearance
            //world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockFLColorableMachine.ACTIVE, hasLight), 2);
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }

        // Apply light range from config
        updateCurrentRange();

        switch (mode) {
            case LIGHT_MODE_STRAIGHT:
                straightSource(remove);
                break;
            case LIGHT_MODE_NARROW_CONE:
                narrowConeSource(remove);
                break;
            case LIGHT_MODE_WIDE_CONE:
                wideConeSource(remove);
                break;
        }
    }

    /**
     * Returns whether this floowlight emits light.
     */
    public boolean hasLight() {
        return hasLight;
    }

    /**
     * This method is called by the {@link TileEntityMetaFloodlight} class. Don't call it manually.
     */
    protected void straightSource(boolean remove) {
        LightSwitchExecutor executor = new LightSwitchExecutor(remove);
        for (int i = 1; i <= currentRange; i += lightBlockStep) {
            int x = this.pos.getX() + this.orientation.getFrontOffsetX() * i;
            int y = this.pos.getY() + this.orientation.getFrontOffsetY() * i;
            int z = this.pos.getZ() + this.orientation.getFrontOffsetZ() * i;

            BlockPos blockPos = new BlockPos(x, y, z);
            if ((!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock() != blockType) && !remove)
                break;
            executor.add(blockPos);
        }

        executor.execute();
    }

    /**
     * This method is called by the {@link TileEntityMetaFloodlight} class. Don't call it manually.
     */
    protected void wideConeSource(boolean remove) {
        LightSwitchExecutor executor = new LightSwitchExecutor(remove);
        boolean[] failedBeams = new boolean[9];

        if (!remove && world.getBlockState(getPosFromPosFacing(this.pos, this.orientation)).isOpaqueCube()) {
            return;
        }
        for (int j = 0; j <= 16; j++) {
            if (j <= 8) {
                for (int i = 1; i <= currentRange / 4; i += lightBlockStep) {
                    int b = 0;
                    int c = 0;
                    switch (j) {
                        case 0:
                            b += i;
                            break;
                        case 1:
                            b -= i;
                            break;
                        case 2:
                            c += i;
                            break;
                        case 3:
                            c -= i;
                            break;
                        case 4:
                            b += i;
                            c += i;
                            break;
                        case 5:
                            b += i;
                            c -= i;
                            break;
                        case 6:
                            b -= i;
                            c += i;
                            break;
                        case 7:
                            b -= i;
                            c -= i;
                            break;
                    }
                    int[] rotatedCoords = MathUtil.rotate(i, b, c, this.orientation);
                    int x = this.pos.getX() + rotatedCoords[0];
                    int y = this.pos.getY() + rotatedCoords[1];
                    int z = this.pos.getZ() + rotatedCoords[2];
                    executor.add(new BlockPos(x, y, z));
                    if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() && !remove) {
                        if (i < 4) {   //This is for canceling the long rangs beams
                            failedBeams[j] = true;
                        }
                        break;
                    }
                }
            } else if (!failedBeams[j - 9] || remove) { // This is for the inner beams at longer range
                for (int i = 4; i <= rangeCone / 4; i += lightBlockStep) {
                    int b = 0;
                    int c = 0;
                    switch (j) {
                        case 9:
                            b += i / 2;
                            break;
                        case 10:
                            b -= i / 2;
                            break;
                        case 11:
                            c += i / 2;
                            break;
                        case 12:
                            c -= i / 2;
                            break;
                        case 13:
                            b += i / 2;
                            c += i / 2;
                            break;
                        case 14:
                            b += i / 2;
                            c -= i / 2;
                            break;
                        case 15:
                            b -= i / 2;
                            c += i / 2;
                            break;
                        case 16:
                            b -= i / 2;
                            c -= i / 2;
                            break;
                    }
                    int[] rotatedCoords = MathUtil.rotate(i, b, c, this.orientation);
                    int x = this.pos.getX() + rotatedCoords[0];
                    int y = this.pos.getY() + rotatedCoords[1];
                    int z = this.pos.getZ() + rotatedCoords[2];
                    executor.add(new BlockPos(x, y, z));
                    if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() && !remove) {
                        break;
                    }
                }
            }
        }

        executor.execute();
    }

    /**
     * This method is called by the {@link TileEntityMetaFloodlight} class. Don't call it manually.
     */
    protected void narrowConeSource(boolean remove) {
        LightSwitchExecutor executor = new LightSwitchExecutor(remove);

        boolean[] failedBeams = new boolean[9];    // for the additional beam to cancel when the main beams fail.
        for (int j = 0; j <= 16; j++) {
            if (j <= 8) {     // This is the main beams
                for (int i = 1; i <= currentRange; i += lightBlockStep) {
                    // for 1st light:
                    if (i == 1) {
                        int x = this.pos.getX() + this.orientation.getFrontOffsetX();
                        int y = this.pos.getY() + this.orientation.getFrontOffsetY();
                        int z = this.pos.getZ() + this.orientation.getFrontOffsetZ();
                        executor.add(new BlockPos(x, y, z));
                        if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() && !remove) {
                            return;
                        }
                    }
                    int b = 0;
                    int c = 0;
                    switch (j) {
                        case 0:
                            b += i / 2;
                            break;
                        case 1:
                            b -= i / 2;
                            break;
                        case 2:
                            c += i / 2;
                            break;
                        case 3:
                            c -= i / 2;
                            break;
                        case 4:
                            b += i / 2;
                            c += i / 2;
                            break;
                        case 5:
                            b += i / 2;
                            c -= i / 2;
                            break;
                        case 6:
                            b -= i / 2;
                            c += i / 2;
                            break;
                        case 7:
                            b -= i / 2;
                            c -= i / 2;
                            break;
                    }
                    int[] rotatedCoords = MathUtil.rotate(i, b, c, this.orientation); // rotate the coordinate to the correct spot in the real world :)
                    int x = this.pos.getX() + rotatedCoords[0];
                    int y = this.pos.getY() + rotatedCoords[1];
                    int z = this.pos.getZ() + rotatedCoords[2];
                    BlockPos sourcePos = new BlockPos(x, y, z);
                    executor.add(sourcePos);
                    if (world.getBlockState(sourcePos).isOpaqueCube() && !remove) {
                        if (i < 8) {   //This is for canceling the long rangs beams
                            failedBeams[j] = true;
                        }
                        break;
                    }
                }
            } else if (!failedBeams[j - 9] || remove) { // This is for the inner beams at longer range
                for (int i = 8; i <= rangeCone; i += lightBlockStep) {
                    int b = 0;
                    int c = 0;
                    switch (j) {
                        case 9:
                            b += i / 4;
                            break;
                        case 10:
                            b -= i / 4;
                            break;
                        case 11:
                            c += i / 4;
                            break;
                        case 12:
                            c -= i / 4;
                            break;
                        case 13:
                            b += i / 4;
                            c += i / 4;
                            break;
                        case 14:
                            b += i / 4;
                            c -= i / 4;
                            break;
                        case 15:
                            b -= i / 4;
                            c += i / 4;
                            break;
                        case 16:
                            b -= i / 4;
                            c -= i / 4;
                            break;
                    }
                    int[] rotatedCoords = MathUtil.rotate(i, b, c, this.orientation);
                    int x = this.pos.getX() + rotatedCoords[0];
                    int y = this.pos.getY() + rotatedCoords[1];
                    int z = this.pos.getZ() + rotatedCoords[2];
                    BlockPos sourcePos = new BlockPos(x, y, z);
                    executor.add(sourcePos);
                    if (world.getBlockState(sourcePos).isOpaqueCube() && !remove) {
                        break;
                    }
                }
            }
        }

        executor.execute();
    }

    @Override
    public void setMode(int mode) {
        super.setMode(mode);
        updateCurrentRange();
    }

    public void changeMode(EntityPlayer player) {
        if (world.isRemote)
            return;

        String modeString = "";
        if (!active && isCooledDown()) {
            mode = (mode == LIGHT_MODE_WIDE_CONE ? LIGHT_MODE_STRAIGHT : mode + 1);
            updateCurrentRange();

            switch (mode) {
                case LIGHT_MODE_STRAIGHT:
                    modeString = Names.Localizations.STRAIGHT;
                    break;
                case LIGHT_MODE_NARROW_CONE:
                    modeString = Names.Localizations.NARROW_CONE;
                    break;
                case LIGHT_MODE_WIDE_CONE:
                    modeString = Names.Localizations.WIDE_CONE;
                    break;
            }
        } else
            modeString = Names.Localizations.MACHINE_ENABLED_ERROR;

        player.sendMessage(new TextComponentTranslation(Names.Localizations.MODE, safeLocalize(modeString)));
    }

    private void updateCurrentRange() {
        if (mode == LIGHT_MODE_STRAIGHT)
            currentRange = rangeStraight;
        else
            currentRange = rangeCone;
    }

    /**
     * This class handles placing and destroying light blocks ({@link de.keridos.floodlights.block.BlockPhantomLight}).
     *
     * Work is divided into batches - this should prevent from leaving invalid light sources in a world after
     * disabling a floodlight.
     */
    protected class LightSwitchExecutor implements Runnable {

        private static final int BATCH_SIZE = 5;
        private static final int BATCH_DELAY = 150;

        private static final int SOURCE_MODE_DISABLE_UPDATES = 0;
        private static final int SOURCE_MODE_CHANGE = 1;
        private static final int SOURCE_MODE_ENABLE_UPDATES = 2;

        private List<BlockPos> blocks = new ArrayList<>();
        private IThreadListener threadListener;

        private int batches;
        private boolean remove;

        /**
         * Initializes LightSwitchExecutor.
         * @param remove wither light sources should be removed from world.
         */
        public LightSwitchExecutor(boolean remove) {
            this.remove = remove;

            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                threadListener = Minecraft.getMinecraft();
            else
                threadListener = FMLCommonHandler.instance().getMinecraftServerInstance();
        }

        /**
         * Adds new position of light block to be placed or removed.
         */
        public void add(BlockPos pos) {
            blocks.add(pos);
        }

        /**
         * Starts placing or removing previously {@link #add}ed blocks.
         */
        public void execute() {
            batches = (int) Math.ceil((float) blocks.size() / BATCH_SIZE);
            try {
                doExecute();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                executorActive.set(false);
            }
        }

        private void doExecute() {
            executorActive.set(true);

            // 1st step - disable light block updates
            executeMode(SOURCE_MODE_DISABLE_UPDATES, false);

            // 2nd step - modify light blocks
            // 3rd step - enable light block updates (optional)
            new Thread(this).start();
        }

        @Override
        public void run() {
            try {
                executeMode(SOURCE_MODE_CHANGE, true);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            executorActive.set(false);
        }

        private void executeMode(final int mode, boolean async) {
            for (int batch = 0; batch < batches; batch++) {
                final int finalBatch = batch;
                Runnable task = () -> {
                    for (int i = finalBatch * BATCH_SIZE; i < finalBatch * BATCH_SIZE + BATCH_SIZE && i < blocks.size(); i++) {
                        setSource(blocks.get(i), mode);
                    }

                    if (mode == SOURCE_MODE_CHANGE && finalBatch == batches - 1 && !remove)
                        executeMode(SOURCE_MODE_ENABLE_UPDATES, false);
                };

                if (async)
                    threadListener.addScheduledTask(task);
                else
                    task.run();

                if (async && mode == SOURCE_MODE_CHANGE && batch < batches - 1) {
                    try {
                        Thread.sleep(BATCH_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @SuppressWarnings("ConstantConditions")
        private void setSource(BlockPos blockPos, int sourceMode) {
            boolean lightExists = world.getBlockState(blockPos).getBlock() == lightBlock;
            switch (sourceMode) {
                case SOURCE_MODE_DISABLE_UPDATES:
                    if (lightExists)
                        world.setBlockState(blockPos, world.getBlockState(blockPos).withProperty(UPDATE, false), 1);
                    break;
                case SOURCE_MODE_CHANGE:
                    if (remove && lightExists) {
                        // Remove existing phantom light
                        TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(blockPos);
                        light.removeSource(pos);
                    } else if (!remove && lightExists) {
                        // Add this floodlight as a source to the existing phantom light
                        TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(blockPos);
                        light.addSource(pos, floodlightId);
                    } else if (!remove && !lightExists && world.isAirBlock(blockPos)) {
                        // Create new phantom light
                        createPhantomLight(blockPos);
                    }
                    break;
                case SOURCE_MODE_ENABLE_UPDATES:
                    if (lightExists)
                        world.setBlockState(blockPos, world.getBlockState(blockPos).withProperty(UPDATE, true), 1);
                    break;
            }
        }
    }
}
