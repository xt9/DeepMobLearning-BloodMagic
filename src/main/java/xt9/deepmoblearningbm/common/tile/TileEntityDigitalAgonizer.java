package xt9.deepmoblearningbm.common.tile;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.tile.TileAltar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import xt9.deepmoblearning.common.energy.DeepEnergyStorage;
import xt9.deepmoblearning.common.handlers.BaseItemHandler;
import xt9.deepmoblearning.common.handlers.DataModelHandler;
import xt9.deepmoblearning.common.items.ItemDataModel;
import xt9.deepmoblearning.common.util.DataModel;
import xt9.deepmoblearningbm.ModConfig;
import xt9.deepmoblearningbm.common.inventory.CatalystInputHandler;
import xt9.deepmoblearningbm.util.Catalyst;
import xt9.deepmoblearningbm.util.EssenceHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by xt9 on 2018-06-30.
 */
public class TileEntityDigitalAgonizer extends TileEntity implements ITickable {
    public static final int GUI_ID = 1;

    private BaseItemHandler dataModel = new DataModelHandler();
    private BaseItemHandler input = new CatalystInputHandler();
    private DeepEnergyStorage energyCap = new DeepEnergyStorage(100000, 25600 , 0, 0);
    private int catalystOperations = 0;
    private int catalystOperationsMax = 0;
    private int saveTicks = 0;
    private int progress = 0;
    private BlockPos altarPos = BlockPos.fromLong(0);
    private double multiplier = 1.0;
    //private FluidTank tank = new FluidTank(new FluidStack(getEssenceFluid(), 0), 12000);

    @Override
    public void update() {
        saveTicks++;

        if(!world.isRemote) {
            if(catalystOperations == 0) {
                if (hasValidCatalyst()) {
                    consumeCatalyst();
                }
            }

            if(canContinueCraft()) {
                progress++;
                energyCap.voidEnergy(ModConfig.getAgonizerRFCost());
                if(progress % 60 == 0) {
                    fillAltarTank();
                    progress = 0;
                }
            } else if(!hasValidDataModel()) {
                progress = 0;
            } else if(getAltar() == null) {
                progress = 0;
            }

            doStaggeredDiskSave(100);
        }
    }

    private void fillAltarTank() {
        BloodAltar altar = getAltar();
        if(altar != null) {
            altar.fillMainTank(getFillAmount());
        }

        if(catalystOperations > 0) {
            catalystOperations--;
        } else {
            multiplier = 1.0;
        }
    }

    private void consumeCatalyst() {
        Catalyst catalyst = Catalyst.getCatalyst(getCatalystStack());
        if(catalyst != null) {
            catalystOperations = catalyst.getOperations();
            catalystOperationsMax = catalyst.getOperations();
            multiplier = catalyst.getMultiplier();
            getCatalystStack().shrink(1);
        }
    }

    private boolean canContinueCraft() {
        return energyCap.getEnergyStored() > ModConfig.getAgonizerRFCost() && !altarIsFull() && hasValidDataModel();
    }

    public void setAltarPos(BlockPos pos) {
        altarPos = pos;
    }

    public BloodAltar getAltar() {
        if(altarPos != null) {
            TileEntity tile = world.getTileEntity(altarPos);
            if(tile instanceof TileAltar) {
                return (BloodAltar) tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            } else {
                return null;
            }
        }
        return null;
    }

    private boolean altarIsFull() {
        return getAltar() == null || getAltar().getFluidAmount() == getAltar().getCapacity();
    }

    private ItemStack getCatalystStack() {
        return input.getStackInSlot(0);
    }

    private ItemStack getDataModelStack() {
        return dataModel.getStackInSlot(0);
    }

    public boolean hasValidDataModel() {
        return getDataModelStack().getItem() instanceof ItemDataModel && DataModel.getTier(getDataModelStack()) != 0;
    }

    public boolean hasValidCatalyst() {
        return Catalyst.isValidCatalyst(getCatalystStack());
    }

    public int getFillAmount() {
        return EssenceHelper.getFillAmount(getDataModelStack(), getMultiplier());
    }

    public int getCatalystOperations() {
        return catalystOperations;
    }

    public int getCatalystOperationsMax() {
        return catalystOperationsMax;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getProgress() {
        return progress;
    }

    private void doStaggeredDiskSave(int divisor) {
        if(saveTicks % divisor == 0) {
            markDirty();
            saveTicks = 0;
        }
    }

    public void updateState(boolean markDirty) {
        IBlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);
        if(markDirty) {
            markDirty();
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 3, writeToNBT(new NBTTagCompound()));
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("catalystOperations", catalystOperations);
        compound.setInteger("catalystOperationsMax", catalystOperationsMax);
        compound.setInteger("progress", progress);
        compound.setDouble("mutliplier", multiplier);
        compound.setLong("altarPos", altarPos.toLong());
        compound.setTag("dataModel", dataModel.serializeNBT());
        compound.setTag("input", input.serializeNBT());
        energyCap.writeEnergy(compound);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        catalystOperations = compound.hasKey("catalystOperations", Constants.NBT.TAG_INT) ? compound.getInteger("catalystOperations") : 0;
        catalystOperationsMax = compound.hasKey("catalystOperationsMax", Constants.NBT.TAG_INT) ? compound.getInteger("catalystOperationsMax") : 0;
        progress = compound.hasKey("progress", Constants.NBT.TAG_INT) ? compound.getInteger("progress") : 0;
        multiplier = compound.hasKey("mutliplier", Constants.NBT.TAG_DOUBLE) ? compound.getDouble("mutliplier") : 1.0;
        altarPos = compound.hasKey("altarPos", Constants.NBT.TAG_LONG) ? BlockPos.fromLong(compound.getLong("altarPos")) : null;
        dataModel.deserializeNBT(compound.getCompoundTag("dataModel"));
        input.deserializeNBT(compound.getCompoundTag("input"));
        energyCap.readEnergy(compound);
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(dataModel, input));
        } if(capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyCap);
        } else {
            return super.getCapability(capability, facing);
        }
    }
}
