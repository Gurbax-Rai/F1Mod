package net.grodax.f1mod.entity.custom;

import net.grodax.f1mod.block.ModBlocks;
import net.grodax.f1mod.client.KeyBindings;
import net.grodax.f1mod.entity.ModEntities;
import net.grodax.f1mod.item.ModItems;
import net.grodax.f1mod.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

// This class represents a driveable Formula 1 car entity in the world.
// It handles custom vehicle physics, player controls, custom engine sounds,
// and specialized interactions like the Drag Reduction System (DRS).
public class F1CarEntity extends Boat {

    private static final EntityDataAccessor<Boolean> IS_DRS_ACTIVE =
            SynchedEntityData.defineId(F1CarEntity.class, EntityDataSerializers.BOOLEAN);

    // CONFIG
    private static final float BASE_ACCEL = 0.08f;
    private static final float TURN_STRENGTH = 3.5f;

    private float steeringAngle;
    private float currentSpeed = 0f;
    private boolean drsActive = false;
    private float turnAngle = 0f;
    private boolean wasMoving = false;
    private static boolean drsPressedLastTick = false;

    private int soundCooldownTicks = 0;

    // Constructs a new F1CarEntity with a given entity type and world level.
    // Parameters:
    //   type - The entity type representing this car.
    //   level - The world level where the car is being created.
    public F1CarEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
        this.setVariant(Boat.Type.OAK);
    }

    // Constructs a new F1CarEntity at a specific coordinate in the world.
    // Parameters:
    //   level - The world level where the car is being spawned.
    //   x - The x-coordinate for the spawn location.
    //   y - The y-coordinate for the spawn location.
    //   z - The z-coordinate for the spawn location.
    public F1CarEntity(Level level, double x, double y, double z) {
        this(ModEntities.F1_CAR_ENTITY.get(), level);
        this.setPos(x, y, z);
    }

    // Determines the maximum step height the car can drive up without jumping.
    // Returns:
    //   The step height as a float.
    @Override
    public float maxUpStep() {
        return 1.25f;
    }

    // Retrieves the item that is dropped when the car is destroyed.
    // Returns:
    //   The F1 car item.
    @Override
    public Item getDropItem() {
        return ModItems.F1CAR.get();
    }

    // Updates the entity's state and physics each game tick. Handles gravity,
    // water submersion destruction, and delegates player controls if occupied.
    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            BlockPos pos = this.blockPosition();
            FluidState fluid = this.level().getFluidState(pos);
            if (fluid.isSource() || fluid.is(FluidTags.WATER)) {
                this.destroy(this.damageSources().generic());
                return;
            }
        }

        if (!this.onGround()) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x, motion.y - 0.08, motion.z);
        }

        if (this.getDeltaMovement().y > 0) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0, this.getDeltaMovement().z);
        }

        if (this.isVehicle()) {
            Entity passenger = this.getFirstPassenger();

            if (passenger instanceof Player player) {
                controlCar(player);
                playEngineSound();
            }
        } else {
            currentSpeed *= 0.85f;
        }

        if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            float turn = player.xxa;
            this.steeringAngle = turn * 30.0F;
        }
    }

    // Retrieves the current steering angle of the car's wheels.
    // Returns:
    //   The steering angle as a float.
    public float getSteeringAngle() {
        return this.steeringAngle;
    }

    // Calculates and applies movement, acceleration, and turning based on player inputs.
    // Uses the player's forward and strafe inputs to modify the car's internal speed
    // and turn angle. Applies friction and handles DRS speed boosts.
    // Parameters:
    //   player - The player actively controlling the car.
    private void controlCar(Player player) {
        float forward = player.zza;
        float strafe = player.xxa;

        if (forward > 0) {
            currentSpeed += BASE_ACCEL;
        } else if (forward < 0) {
            currentSpeed -= BASE_ACCEL * 0.75f;
        } else {
            if (Math.abs(currentSpeed) > 0.01f) {
                float friction = isOnTrack() ? 0.95f : 0.92f;
                currentSpeed *= friction;
            } else {
                currentSpeed = 0f;
            }
        }

        float max = this.isDrsActive() ? 1.5F : 1.0F;
        if (isOnTrack()) max *= 1.25f;

        currentSpeed = clamp(currentSpeed, -0.3f, max);

        if (Math.abs(strafe) > 0.1f) {
            float invertedStrafe = -strafe;
            float drsTurnModifier = this.isDrsActive() ? 0.5f : 1.0f;
            float turnSpeed = (TURN_STRENGTH * drsTurnModifier) *
                    (1.0f - Math.min(1.0f, Math.abs(currentSpeed) / max));

            turnAngle += invertedStrafe * turnSpeed;
            this.setYRot(this.getYRot() + turnAngle);
            turnAngle *= 0.7f;
        } else {
            turnAngle *= 0.85f;
        }

        Vec3 look = this.getLookAngle();
        Vec3 horizontalLook = new Vec3(look.x, 0, look.z).normalize();
        Vec3 horizontalMovement = horizontalLook.scale(currentSpeed);

        this.setDeltaMovement(horizontalMovement.x, this.getDeltaMovement().y, horizontalMovement.z);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    // Checks if the block directly below the car is a designated track block.
    // Returns:
    //   true if the block below is a track block, false otherwise.
    private boolean isOnTrack() {
        BlockPos below = this.blockPosition().below();
        return this.level().getBlockState(below).is(ModBlocks.TRACK_BLOCK.get());
    }

    // Listens for client tick events to handle the DRS toggle keybinding.
    // Parameters:
    //   event - The client tick event being fired.
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean pressed = KeyBindings.TOGGLE_DRS.isDown();

        if (pressed && !drsPressedLastTick) {
            if (mc.player.getVehicle() instanceof F1CarEntity car) {
                car.toggleDRS();
            }
        }

        drsPressedLastTick = pressed;
    }

    // Toggles the active state of the Drag Reduction System (DRS).
    public void toggleDRS() {
        this.drsActive = !this.drsActive;
    }

    // Checks whether the Drag Reduction System (DRS) is currently active.
    // Returns:
    //   true if DRS is active, false otherwise.
    public boolean isDrsActive() {
        return this.drsActive;
    }

    // Determines if another entity can board the car.
    // Parameters:
    //   passenger - The entity attempting to board.
    // Returns:
    //   true if the car is currently empty, false otherwise.
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    // Updates the position of the rider to match the car's location.
    // Parameters:
    //   passenger - The entity riding the car.
    //   moveFunction - The function handling the movement logic.
    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        passenger.setPos(this.getX(), this.getY(), this.getZ());
    }

    // Handles player interaction when they right-click the car.
    // Parameters:
    //   player - The player interacting with the car.
    //   hand - The hand the player used to interact.
    // Returns:
    //   An InteractionResult indicating the success of the action.
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            player.startRiding(this);
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    // Reads the car's custom properties from a saved NBT tag.
    // Parameters:
    //   tag - The CompoundTag containing the saved data.
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        currentSpeed = tag.getFloat("Speed");
        drsActive = tag.getBoolean("DRS");
        turnAngle = tag.getFloat("TurnAngle");
        wasMoving = tag.getBoolean("WasMoving");
    }

    // Writes the car's custom properties to an NBT tag for saving.
    // Parameters:
    //   tag - The CompoundTag to write the data into.
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Speed", currentSpeed);
        tag.putBoolean("DRS", drsActive);
        tag.putFloat("TurnAngle", turnAngle);
        tag.putBoolean("WasMoving", wasMoving);
    }

    // Checks if the car is currently in water.
    // Returns:
    //   false, as the car's physics override standard water behavior.
    @Override
    public boolean isInWater() {
        return false;
    }

    // Determines if the car can float or operate like a boat in fluid.
    // Parameters:
    //   state - The fluid state being checked.
    // Returns:
    //   false, as the car cannot boat in fluid.
    @Override
    public boolean canBoatInFluid(FluidState state) {
        return false;
    }

    // Overrides standard fall damage checking to disable boat fall handling.
    // Parameters:
    //   y - The distance fallen.
    //   onGround - Whether the car is on the ground.
    //   state - The block state being fallen onto.
    //   pos - The position of the block.
    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    // Disables default block intersection checks.
    @Override
    protected void checkInsideBlocks() {}

    // Disables default stepping sounds.
    // Parameters:
    //   pos - The position of the block.
    //   state - The state of the block.
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {}

    // Retrieves the sound event for paddling.
    // Returns:
    //   null, as the car does not use paddles.
    @Override
    public SoundEvent getPaddleSound() {
        return null;
    }

    // Calculates pitch and volume based on current speed and plays the custom
    // engine sound on a cooldown timer.
    private void playEngineSound() {
        if (soundCooldownTicks > 0) soundCooldownTicks--;

        if (currentSpeed != 0 && soundCooldownTicks <= 0) {
            float pitch = 0.5f + (Math.min(Math.abs(currentSpeed), 1.5f));
            float volume = 0.3f + (Math.min(Math.abs(currentSpeed), 0.6f) * 0.7f);

            this.level().playLocalSound(
                    this.getX(), this.getY(), this.getZ(),
                    ModSounds.ENGINE_SOUND.get(),
                    this.getSoundSource(),
                    volume, pitch, false
            );

            soundCooldownTicks = 15;
        }
    }

    // Registers the synced data parameters for the car, such as the DRS state.
    // Parameters:
    //   builder - The builder used to define the synced data.
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_DRS_ACTIVE, false);
    }

    // Sets the Y rotation (yaw) of the car and refreshes its dimensions to
    // match the new angle.
    // Parameters:
    //   pYRot - The new Y rotation angle.
    @Override
    public void setYRot(float pYRot) {
        super.setYRot(pYRot);
        this.refreshDimensions();
    }

    // Calculates and retrieves the dynamic hitbox dimensions of the car based on its rotation.
    // Parameters:
    //   pPose - The current pose of the entity.
    // Returns:
    //   The dynamically scaled EntityDimensions.
    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        float yaw = this.getYRot();

        float angleRadians = yaw * ((float)Math.PI / 180F);
        float cos = Math.abs((float)Math.cos(angleRadians));
        float sin = Math.abs((float)Math.sin(angleRadians));

        float projectedWidth = (2.5f * sin) + (1.5f * cos);
        float projectedLength = (2.5f * cos) + (1.5f * sin);

        float finalSize = Math.max(projectedWidth, projectedLength);

        return EntityDimensions.scalable(finalSize, 1.0f);
    }

    // Handles the destruction of the car, dropping its item form into the world before discarding it.
    // Parameters:
    //   damageSource - The source of the damage that destroyed the car.
    @Override
    public void destroy(DamageSource damageSource) {
        if (!this.level().isClientSide) {
            this.spawnAtLocation(ModItems.F1CAR.get());
        }
        this.discard();
    }

    // Restricts a value to be within a given minimum and maximum range.
    // Parameters:
    //   val - The original value to clamp.
    //   min - The minimum allowed boundary.
    //   max - The maximum allowed boundary.
    // Returns:
    //   The clamped value.
    private float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}