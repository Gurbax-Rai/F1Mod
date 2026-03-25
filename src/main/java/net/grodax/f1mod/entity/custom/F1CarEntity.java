package net.grodax.f1mod.entity.custom;

import net.grodax.f1mod.block.ModBlocks;
import net.grodax.f1mod.entity.ModEntities;
import net.grodax.f1mod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class F1CarEntity extends Boat {

    // ===============================
    // CONFIG
    // ===============================
    private static final float BASE_ACCEL = 0.04f;
    private static final float MAX_SPEED = 1.2f;
    private static final float DRS_BOOST = 1.8f;

    private float currentSpeed = 0f;
    private boolean drsActive = false;

    public F1CarEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
        this.setVariant(Boat.Type.OAK); // REQUIRED
    }

    public F1CarEntity(Level level, double x, double y, double z) {
        this(ModEntities.F1_CAR_ENTITY.get(), level);
        this.setPos(x, y, z);
    }

    // ===============================
    // DROP ITEM
    // ===============================
    @Override
    public Item getDropItem() {
        return ModItems.F1CAR.get();
    }

    // ===============================
    // MAIN TICK
    // ===============================
    @Override
    public void tick() {
        super.tick();

        if (this.isVehicle()) {
            Entity passenger = this.getFirstPassenger();

            if (passenger instanceof Player player) {
                controlCar(player);
            }
        } else {
            // natural slowdown
            currentSpeed *= 0.95f;
        }
    }

    // ===============================
    // CONTROL SYSTEM
    // ===============================
    private void controlCar(Player player) {
        float forward = player.zza;
        float strafe = player.xxa;

        // ACCELERATION
        if (forward > 0) {
            currentSpeed += BASE_ACCEL;
        } else if (forward < 0) {
            currentSpeed -= BASE_ACCEL * 0.5f;
        } else {
            currentSpeed *= 0.98f; // friction
        }

        // SPEED LIMIT
        float max = drsActive ? MAX_SPEED * DRS_BOOST : MAX_SPEED;

        // Track boost
        if (isOnTrack()) {
            max *= 1.3f;
        }

        currentSpeed = clamp(currentSpeed, -0.4f, max);

        // TURNING (harder at high speed)
        float turnFactor = 1.0f - (Math.abs(currentSpeed) / max);
        float turnSpeed = 3.0f * turnFactor;

        this.setYRot(this.getYRot() + strafe * turnSpeed);

        // MOVEMENT
        Vec3 look = this.getLookAngle();
        Vec3 movement = look.scale(currentSpeed);

        this.setDeltaMovement(movement);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    // ===============================
    // TRACK DETECTION
    // ===============================
    private boolean isOnTrack() {
        BlockPos below = this.blockPosition().below();
        return this.level().getBlockState(below).is(ModBlocks.TRACK_BLOCK.get());
    }

    // ===============================
    // DRS (toggle later via keybind)
    // ===============================
    public void toggleDRS() {
        this.drsActive = !this.drsActive;
    }

    // ===============================
    // RIDING
    // ===============================
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        passenger.setPos(
                this.getX(),
                this.getY() + 0.4,
                this.getZ()
        );
    }

    // ===============================
    // INTERACTION
    // ===============================
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            player.startRiding(this);
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    // ===============================
    // SAVE DATA (for fuel/gears later)
    // ===============================
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        currentSpeed = tag.getFloat("Speed");
        drsActive = tag.getBoolean("DRS");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Speed", currentSpeed);
        tag.putBoolean("DRS", drsActive);
    }

    // ===============================
    // REQUIRED (1.21)
    // ===============================
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    // ===============================
    // UTIL
    // ===============================
    private float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}