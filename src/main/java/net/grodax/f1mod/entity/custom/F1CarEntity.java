package net.grodax.f1mod.entity.custom;

import net.grodax.f1mod.block.ModBlocks;
import net.grodax.f1mod.entity.ModEntities;
import net.grodax.f1mod.item.ModItems;
import net.minecraft.client.Minecraft;
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
    private static final float BASE_ACCEL = 0.08f;
    private static final float MAX_SPEED = 0.6f;
    private static final float DRS_BOOST = 1.5f;
    private static final float TURN_STRENGTH = 3.5f;
    private static final float FRICTION = 0.92f;
    private static final float STRAIGHT_SPEED_FRICTION = 0.98f;

    private float currentSpeed = 0f;
    private boolean drsActive = false;
    private float turnAngle = 0f;
    private boolean wasMoving = false;

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

                // Update passenger rotation based on car movement
                updatePassengerRotation(player);
            }
        } else {
            // Natural slowdown - more realistic for car
            currentSpeed *= 0.85f;
        }
    }

    // ===============================
    // CONTROL SYSTEM
    // ===============================
    private void controlCar(Player player) {
        float forward = player.zza;
        float strafe = player.xxa;

        // ACCELERATION - more responsive
        if (forward > 0) {
            currentSpeed += BASE_ACCEL;
        } else if (forward < 0) {
            currentSpeed -= BASE_ACCEL * 0.75f;
        } else {
            // Apply friction based on current speed and direction
            if (Math.abs(currentSpeed) > 0.01f) {
                float friction = isOnTrack() ? 0.95f : 0.92f;
                currentSpeed *= friction;
            } else {
                currentSpeed = 0f;
            }
        }

        // SPEED LIMIT
        float max = drsActive ? MAX_SPEED * DRS_BOOST : MAX_SPEED;

        // Track boost
        if (isOnTrack()) {
            max *= 1.25f;
        }

        currentSpeed = clamp(currentSpeed, -0.3f, max);

        // TURNING - fixed direction issue
        boolean isMoving = Math.abs(forward) > 0.1f || Math.abs(strafe) > 0.1f;

        if (Math.abs(strafe) > 0.1f) {
            // Invert strafe for proper turning direction
            float invertedStrafe = -strafe;

            // Calculate turn based on speed - faster at lower speeds
            float turnSpeed = TURN_STRENGTH * (1.0f - Math.min(1.0f, Math.abs(currentSpeed) / max));
            turnAngle += invertedStrafe * turnSpeed;

            // Apply turning to rotation
            this.setYRot(this.getYRot() + turnAngle);

            // Reduce turn angle over time for smoother steering
            turnAngle *= 0.7f;
        } else {
            // Gradually reduce turn angle when not turning
            turnAngle *= 0.85f;
        }

        // MOVEMENT - more car-like physics
        Vec3 look = this.getLookAngle();
        Vec3 movement = look.scale(currentSpeed);

        this.setDeltaMovement(movement);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    // ===============================
    // UPDATE PASSENGER ROTATION
    // ===============================
    private void updatePassengerRotation(Player player) {
//        // If car is moving, make passenger look forward in the direction of travel
//        if (Minecraft.getInstance().options.keyUp.isDown() || Minecraft.getInstance().options.keyLeft.isDown() || Minecraft.getInstance().options.keyRight.isDown() || Minecraft.getInstance().options.keyDown.isDown()) {
//            // Set passenger's body rotation to match car's rotation
//
//            // Keep passenger facing forward relative to car movement
//            player.absRotateTo(this.getYRot(), player.getXRot());
//        } else {
//            // If not moving, let player control their own rotation freely
//            // But still update body rotation to match current car rotation for consistency
//            player.setYBodyRot(this.getYRot());
//        }
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
                this.getY(),
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
        turnAngle = tag.getFloat("TurnAngle");
        wasMoving = tag.getBoolean("WasMoving");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Speed", currentSpeed);
        tag.putBoolean("DRS", drsActive);
        tag.putFloat("TurnAngle", turnAngle);
        tag.putBoolean("WasMoving", wasMoving);
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
